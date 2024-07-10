package com.limelist.tvStore.dataAccess.sqlite.repositories

import com.limelist.tvStore.services.models.channels.TvChannelPreviewModel
import kotlinx.coroutines.sync.Mutex
import java.sql.Connection

import com.limelist.tvStore.dataAccess.interfaces.TvChannelsRepository
import com.limelist.tvStore.dataAccess.models.TvChannelCreateModel
import com.limelist.tvStore.services.models.AgeLimit
import com.limelist.tvStore.services.models.channels.TvChannels
import com.limelist.tvStore.services.models.channels.TvChannelDetailsModel
import com.limelist.tvStore.services.models.releases.TvChannelReleases
import com.limelist.tvStore.services.models.releases.TvChannelShowRelease
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.cancellation.CancellationException

class TvChannelsSqliteRepository(
    connection: Connection,
    mutex: Mutex
) : BaseSqliteTvRepository(connection, mutex, channelsTabelName),
    TvChannelsRepository {

    private val getAllChannelsStatement = connection.prepareStatement("""
        SELECT 
            channels.id as id, 
            channels.name as name, 
            channels.image_url as image_url,
            AVG(channel_reviews.assessment) as assessment
        FROM channels
        LEFT JOIN channel_reviews
            ON channel_reviews.channel_id = channels.id
            
        GROUP BY channels.id
        ORDER BY channels.id

        LIMIT ?
        OFFSET ?
    """)

    override suspend fun getAllChannels(
        limit: Int,
        offset: Int
    ): TvChannels<TvChannelPreviewModel> = mutex.withLock {
        var set = getAllChannelsStatement.run {
            setInt(1, limit)
            setInt(2, offset)
            return@run executeQuery()
        }

        val channels = buildList<TvChannelPreviewModel> {
            while (set.next()){
                add(TvChannelPreviewModel(
                    set.getInt("id"),
                    set.getString("name"),
                    set.getString("image_url"),
                    set.getFloat("assessment")
                ))
            }
        }

        set = getCountStatement.executeQuery()
        if (!set.next())
            throw UnknownError()

        return@withLock TvChannels(
            set.getInt(1),
            channels
        )
    }


    private val getChannelDetailsStatement = connection.prepareStatement("""
        SELECT 
            channel.*,
            AVG(channel_reviews.assessment) as assessment,
            channel_view_urls.url as view_url
        FROM (
            SELECT channels.* 
            FROM channels 
            WHERE channels.id = ?
        ) as channel
        LEFT JOIN channel_reviews
            ON channel_reviews.channel_id = channel.id
        LEFT JOIN channel_view_urls
            ON channel_view_urls.channel_id = channel.id    
        GROUP BY channel.id
    """)

    override suspend fun getChannelDetails(id: Int): TvChannelDetailsModel? {
        val set = getChannelDetailsStatement.run{
            setInt(1, id)
            executeQuery()
        }

        if (!set.next())
            return null;

        val id = set.getInt("id")
        val name = set.getString("name")
        val imageUrl = set.getString("image_url")
        val description = set.getString("description")
        val assessment = set.getFloat("assessment")

        val viewUrls = mutableListOf<String>()

        do {
            val url: String? = set.getString("view_url")
            if (url != null)
                viewUrls.add(url)
        } while (set.next())

        return TvChannelDetailsModel(
            id,
            name,
            imageUrl,
            assessment,
            description,
            viewUrls,
        )
    }


    val searchByNameStatement = connection.prepareStatement("""
       SELECT 
            channels.id as id, 
            channels.name as name, 
            channels.image_url as image_url,
            AVG(channel_reviews.assessment) as assessment
        FROM ( 
            SELECT channels.* 
            FROM channels
            WHERE name LIKE ?
        ) as channels
        LEFT JOIN channel_reviews
            ON channel_reviews.channel_id = channels.id
             
        GROUP BY channels.id
        ORDER BY channels.id
        
        LIMIT ?
        OFFSET ?
    """)

    override suspend fun searchByName(
        name: String,
        limit: Int,
        offset: Int
    ): TvChannels<TvChannelPreviewModel> = mutex.withLock {
        var set = getAllChannelsStatement.run {
            setString(1, "%$name%")
            setInt(2, limit)
            setInt(3, offset)
            return@run executeQuery()
        }

        val channels = buildList {
            while (set.next()){
                add(TvChannelPreviewModel(
                    set.getInt("id"),
                    set.getString("name"),
                    set.getString("image_url"),
                    set.getFloat("assessment")
                ))
            }
        }

        return@withLock TvChannels(
            -1,
            channels
        )
    }


    private val getChannelReleasesStatement = connection.prepareStatement("""
        SELECT 
            releases.*,
            shows.id as show_id,
            shows.name as show_name,
            shows.assessment as show_assessment,
            shows.age_limit as show_age_limit,
            shows.preview_url as preview_url
        FROM ( 
            SELECT releases.* 
            FROM releases
            WHERE releases.channel_id = ?
                AND time_stop > ?
        ) as  releases
        
        INNER JOIN (
            SELECT 
                shows.id as id, 
                shows.name as name, 
                shows.age_limit as age_limit,
                shows.preview_url as preview_url,
                AVG(show_reviews.assessment) as assessment
            FROM shows
            LEFT JOIN show_reviews
                ON show_reviews.show_id = shows.id
                
            GROUP BY shows.id
            ORDER BY shows.id
        ) as shows
            ON shows.id = releases.show_id
        ORDER BY releases.time_stop
        
        LIMIT ?
    """)

    override suspend fun getChannelReleases(
        channelId: Int,
        limit: Int,
        timeStart: Long
    ): TvChannelReleases<TvChannelShowRelease> = mutex.withLock {

        val set = getChannelReleasesStatement.run{
            setInt(1, channelId)
            setLong(2, timeStart)
            setInt(3, limit)
            return@run executeQuery()
        }

        val releases = buildList {
            while (set.next()){
                add(TvChannelShowRelease(
                    set.getInt("show_id"),
                    set.getString("show_name"),
                    set.getFloat("show_assessment"),
                    AgeLimit.fromInt(set.getInt("show_age_limit")),
                    set.getString("preview_url"),
                    set.getString("description"),
                    set.getLong("time_start"),
                    set.getLong("time_stop"),
                ))
            }
        }
        val timeStart = releases.firstOrNull()?.timeStart ?: 0
        val timeStop =  releases.lastOrNull()?.timeStart ?: 0

        return@withLock TvChannelReleases(
            timeStart,
            timeStop,
            -1,
            releases
        )
    }


    private val updateChannelStatement = connection.prepareStatement("""
        INSERT INTO channels
          (id, name, image_url, description)
        VALUES
          (?, ?, ?, ?)
        ON CONFLICT(id) DO UPDATE
            SET name = EXCLUDED.name, 
                image_url = EXCLUDED.image_url, 
                description = EXCLUDED.description
            
    """)

    private val clearViewUrlsStatement = connection.prepareStatement("""
        DELETE FROM channel_view_urls
    """.trimIndent())

    private val updateChannelViewUrlsStatement = connection.prepareStatement("""
        INSERT INTO channel_view_urls 
            (channel_id, url)
        VALUES 
            (?, ?)
    """)

    override suspend fun updateMany(
        channels: List<TvChannelCreateModel>
    ) = mutex.withLock {
        clearViewUrlsStatement.executeUpdate()
        coroutineScope{
            for (channel in channels) {
                if (!isActive)
                    break

                updateChannelStatement.run {
                    setInt(1, channel.id)
                    setString(2, channel.name)
                    setString(3, channel.image)
                    setString(4, channel.description)
                    executeUpdate()
                }

                for (url in channel.view_urls){
                    updateChannelViewUrlsStatement.run{
                        setInt(1, channel.id)
                        setString(2, url)
                        executeUpdate()
                    }
                }
            }

            connection.commit()
            if (!isActive)
                throw CancellationException("Operation cancelled")
        }
    }

}
