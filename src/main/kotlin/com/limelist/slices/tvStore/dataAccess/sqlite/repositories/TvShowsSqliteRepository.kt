package com.limelist.slices.tvStore.dataAccess.sqlite.repositories

import com.limelist.slices.tvStore.services.models.shows.TvShowDetailsModel
import com.limelist.slices.tvStore.services.models.shows.TvShowPreviewModel
import kotlinx.coroutines.sync.Mutex
import java.sql.Connection

import com.limelist.slices.tvStore.dataAccess.interfaces.TvShowsRepository
import com.limelist.slices.tvStore.dataAccess.models.create.TvShowChannelsDataModel
import com.limelist.slices.tvStore.dataAccess.models.create.TvShowCreateModel
import com.limelist.slices.tvStore.services.models.AgeLimit
import com.limelist.slices.tvStore.services.models.channels.TvChannels
import com.limelist.slices.tvStore.services.models.releases.TvChannelReleases
import com.limelist.slices.tvStore.services.models.releases.TvShowRelease
import com.limelist.slices.tvStore.services.models.shows.TvShows
import com.limelist.slices.tvStore.services.models.shows.TvShowChannelModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.cancellation.CancellationException

class TvShowsSqliteRepository(
    connection: Connection,
    mutex: Mutex
) : BaseSqliteTvRepository(connection, mutex, "shows"),
    TvShowsRepository {

    private val getAllShowsStatement = connection.prepareStatement("""
        SELECT 
            shows.id as id, 
            shows.name as name, 
            shows.age_limit as age_limit,
            shows.preview_url as preview_url,
            AVG(show_reviews.assessment) as assessment
        FROM shows
        LEFT JOIN show_reviews
            ON show_reviews.parent_id = shows.id
            
        GROUP BY shows.id
        ORDER BY shows.id

        LIMIT ?
        OFFSET ?
    """)

    override suspend fun getAllShows(
        limit: Int,
        offset: Int
    ): TvShows<TvShowPreviewModel> = mutex.withLock {
        var set = getAllShowsStatement.run {
            setInt(1, limit)
            setInt(2, offset)
            return@run executeQuery()
        }

        val shows = parseShowPreviews(set)

        set = getCountStatement.executeQuery()
        if (!set.next())
            throw UnknownError()

        return@withLock TvShows(
            set.getInt(1),
            shows
        )
    }


    private val getShowDetailsStatement = connection.prepareStatement("""
        SELECT shows.*, show_frames.url as frame
        FROM (
            SELECT 
                show.*,
                AVG(show_reviews.assessment) as assessment
            FROM (
                SELECT shows.* 
                FROM shows 
                WHERE shows.id = ?
            ) as show
            LEFT JOIN show_reviews
                ON show_reviews.parent_id = show.id
            GROUP BY show.id
        ) as shows 
        LEFT JOIN show_frames
        ON shows.id == show_frames.show_id
        
    """)


    private val getShowTags = connection.prepareStatement("""
        SELECT tags.*
            FROM show_tags
        INNER JOIN tags
            ON tags.id = show_tags.tag_id
        WHERE show_tags.show_id = ?
    """)

    override suspend fun getShowDetails(
        id: Int
    ): TvShowDetailsModel? = mutex.withLock {
        var set = getShowDetailsStatement.run{
            setInt(1, id)
            return@run executeQuery()
        }

        if (!set.next())
            return null;

        val id = set.getInt("id")
        val name = set.getString("name")
        val assessment = set.getFloat("assessment")
        val ageLimit = AgeLimit.fromInt(set.getInt("age_limit"))
        val previewUrl = set.getString("preview_url")
        val description = set.getString("description")

        val frames = buildList{
            do {
                val frame = set.getString("frame")
                if (frame != null)
                    add(frame)
            } while (set.next())
        }

        set = getShowTags.run{
            setInt(1, id)
            executeQuery()
        }

        val tags = buildList{
            while (set.next()){
                add(parseTag(set))
            }
        }

        return TvShowDetailsModel(
            id,
            name,
            assessment,
            ageLimit,
            previewUrl,
            frames,
            tags,
            description
        )
    }


    val searchByNameStatement = connection.prepareStatement("""
        SELECT 
            shows.id as id, 
            shows.name as name, 
            shows.preview_url as preview_url,
            shows.age_limit as age_limit,
            AVG(show_reviews.assessment) as assessment
        FROM ( 
            SELECT shows.* 
            FROM shows
            WHERE name LIKE ?
        ) as shows
        LEFT JOIN show_reviews
            ON show_reviews.parent_id = shows.id
             
        GROUP BY shows.id
        ORDER BY shows.id
        
        LIMIT ?
        OFFSET ?
    """)

    override suspend fun searchByName(
        name: String,
        limit: Int,
        offset: Int
    ): TvShows<TvShowPreviewModel> = mutex.withLock {
        val set = searchByNameStatement.run {
            setString(1, "%${name}%")
            setInt(2, limit)
            setInt(3, offset)
            return@run executeQuery()
        }

        val shows = parseShowPreviews(set)

        return TvShows(
            -1,
            shows
        )
    }

    private val getShowChannelsStatement = connection.prepareStatement("""
        SELECT 
            channels.id as channel_id,
            channels.name as channel_name,
            channels.assessment as channel_assessment,
            channels.image_url as channel_image_url,
            releases.id as release_id,
            releases.description as release_description,
            releases.time_start as release_start,
            releases.time_stop as release_stop
            
        FROM (
            SELECT releases.*
                FROM releases
                WHERE show_id = ? AND releases.time_stop  > ?    
            LImit ?
        ) as releases
        LEFT JOIN (
            SELECT channels.*
                FROM (
                    SELECT 
                        channels.id as id, 
                        channels.name as name, 
                        channels.image_url as image_url,
                        AVG(channel_reviews.assessment) as assessment
                    FROM channels
                    LEFT JOIN channel_reviews
                        ON channel_reviews.parent_id = channels.id
                        
                    GROUP BY channels.id
                    ORDER BY channels.id
                ) as channels
                INNER JOIN (
                    SELECT releases.*
                        FROM releases
                    WHERE show_id = ? AND releases.time_stop  > ?
                    LIMIT ?
                ) as releases
                    ON releases.channel_id = channels.id
                    
            GROUP BY channels.id
            ORDER BY channels.id
            
            LIMIT ?
            OFFSET ?
        ) as channels
            ON releases.channel_id = channels.id
        ORDER BY channels.id, releases.time_stop
    """)

    override suspend fun getShowChannels(
        showId: Int,
        channelsLimit: Int,
        channelsOffset: Int,
        releasesLimit: Int,
        releasesTimeStart: Long
    ): TvChannels<TvShowChannelModel>  = mutex.withLock{
        val set = getShowChannelsStatement.run {
            setInt(1, showId)
            setLong(2, releasesTimeStart)
            setInt(3, releasesLimit)

            setInt(4, showId)
            setLong(5, releasesTimeStart)
            setInt(6, releasesLimit)

            setInt(7, channelsLimit)
            setInt(8, channelsOffset)
            return@run executeQuery()
        }

        val dataModels = buildList{
            while (set.next()){
                add(TvShowChannelsDataModel(set))
            }
        }

        if (dataModels.isEmpty()){
            return TvChannels(
                0,
                listOf()
            )
        }

        val channels = buildList {
            var lastModel = dataModels.first()
            var releases = mutableListOf<TvShowRelease>()

            for (dataModel in dataModels){
                if (dataModel.channelId != lastModel.channelId){

                    val tvReleases = TvChannelReleases(
                        releases.first().timeStart,
                        releases.last().timeStop,
                        -1,
                        releases
                    )

                    add(TvShowChannelModel(
                        lastModel.channelId,
                        lastModel.channelName,
                        lastModel.channelImageUrl,
                        lastModel.channelAssessment,
                        tvReleases
                    ))

                    releases = mutableListOf()
                    lastModel = dataModel
                }

                releases.add(TvShowRelease(
                    dataModel.releaseDescription,
                    dataModel.releaseStart,
                    dataModel.releaseStop
                ))
            }

            val tvReleases = TvChannelReleases(
                releases.first().timeStart,
                releases.last().timeStop,
                -1,
                releases
            )

            add(TvShowChannelModel(
                lastModel.channelId,
                lastModel.channelName,
                lastModel.channelImageUrl,
                lastModel.channelAssessment,
                tvReleases
            ))
        }

        return TvChannels(-1, channels)
    }

    private val getWithoutImageShows = connection.prepareStatement(
        """
        SELECT 
            shows.*,
            AVG(show_reviews.assessment) as assessment
        FROM ( 
            SELECT *
                FROM shows      
                WHERE shows.preview_url IS NULL
        ) as shows
        
        LEFT JOIN show_reviews
            ON show_reviews.parent_id = shows.id
            
        GROUP BY shows.id
        ORDER BY shows.id

        LIMIT ?
        OFFSET ?
    """
    )

    override suspend fun getWithoutImageShows(
        limit: Int,
        offset: Int
    ): TvShows<TvShowDetailsModel> = mutex.withLock {
        val set = getWithoutImageShows.run {
            setInt(1, limit)
            setInt(2, offset)
            return@run executeQuery()
        }

        val shows = buildList{
            while (set.next()){
                add(
                    TvShowDetailsModel(
                        set.getInt("id"),
                        set.getString("name"),
                        set.getFloat("assessment"),
                        AgeLimit.fromInt(set.getInt("age_limit")),
                        set.getString("preview_url"),
                        listOf(),
                        listOf(),
                        set.getString("description"),
                    )
                )
            }
        }

        return@withLock TvShows(
            -1,
            shows
        )
    }

    val getFavoritesStatement = connection.prepareStatement("""
        SELECT 
            shows.*,
            AVG(show_reviews.assessment) as assessment
        FROM ( 
            SELECT shows.* 
                FROM (    
                    SELECT * 
                        FROM favorite_shows
                    WHERE user_id = ?
                    ORDER BY show_id
                    LIMIT ?
                    OFFSET ?
                ) as favorites
            INNER JOIN shows
                ON shows.id == favorites.show_id
        ) as shows
        LEFT JOIN show_reviews
            ON show_reviews.parent_id = shows.id
        GROUP BY shows.id
    """)
    override suspend fun getUserFavorites(
        userId: Int, 
        limit: Int,
        offset: Int
    ): TvShows<TvShowPreviewModel> = mutex.withLock {
        var set = getFavoritesStatement.run {
            setInt(1, userId)
            setInt(2, limit)
            setInt(3, offset)
            return@run executeQuery()
        }

        val shows = parseShowPreviews(set)

        return TvShows(
            -1,
            shows
        )
    }

    val addFavoritesStatement = connection.prepareStatement("""
        INSERT OR IGNORE INTO favorite_shows(user_id, show_id)
            VALUES (?,?)
    """)

    override suspend fun addUserFavorites(
        userId: Int,
        showId: Int
    ): Unit = mutex.withLock {
        addFavoritesStatement.run {
            setInt(1, userId)
            setInt(2, showId)
            executeUpdate()
        }
    }

    private val updateShowsStatementWithPreview = connection.prepareStatement("""
        INSERT INTO shows
            (id, name, age_limit, preview_url, description)
        VALUES
          (?, ?, ?, ?, ?)
        ON CONFLICT(id) DO UPDATE
            SET name = EXCLUDED.name, 
                age_limit = EXCLUDED.age_limit,
                preview_url = EXCLUDED.preview_url, 
                description = EXCLUDED.description
    """)

    private val updateShowsStatementWithoutPreview = connection.prepareStatement("""
        INSERT INTO shows
            (id, name, age_limit, description)
        VALUES
          (?, ?, ?, ?)
        ON CONFLICT(id) DO UPDATE
            SET name = EXCLUDED.name, 
                age_limit = EXCLUDED.age_limit,
                description = EXCLUDED.description
    """)

    private val addShowImage = connection.prepareStatement("""
        INSERT INTO show_frames
            (show_id, url)
        VALUES 
            (?, ?)
    """)

    override suspend fun updateMany(shows: List<TvShowCreateModel>) {
        coroutineScope {
            for (show in shows) {
                if (!isActive)
                    break

                val statement =
                    if (show.previewUrl != null)
                        updateShowsStatementWithPreview
                    else
                        updateShowsStatementWithoutPreview

                 statement.run {
                    setInt(1, show.id)
                    setString(2, show.name)
                    setInt(3, show.ageLimit)
                    if (show.previewUrl != null ) {
                        setString(4, show.previewUrl)
                        setString(5, show.description)
                    }
                    else{
                        setString(4, show.description)
                    }
                    executeUpdate()
                }

                show.images?.forEach { image ->
                    addShowImage.run {
                        setInt(1, show.id)
                        setString(2, image.url)
                        executeUpdate()
                    }
                }
            }
            connection.commit()
            if (!isActive)
                throw CancellationException()
        }
    }


    val containsStatement = connection.prepareStatement("""
        SELECT * FROM $tableName
        WHERE id = ?
        LIMIT 1
    """)

    override suspend fun contains(id: Int): Boolean {
        val set = containsStatement.run {
            setInt(1, id)
            return@run executeQuery()
        }
        return set.next()
    }

}