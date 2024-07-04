package com.limelist.tvHistory.dataAccess.sqlite.repositories

import com.limelist.tvHistory.services.models.shows.TvShowDetailsModel
import com.limelist.tvHistory.services.models.shows.TvShowPreviewModel
import kotlinx.coroutines.sync.Mutex
import java.sql.Connection

import com.limelist.tvHistory.dataAccess.interfaces.TvShowsRepository
import com.limelist.tvHistory.services.models.channels.TvChannels
import com.limelist.tvHistory.services.models.shows.TvShows
import com.limelist.tvHistory.services.models.shows.TvShowChannelModel

class TvShowsSqliteRepository(
    connection: Connection,
    mutex: Mutex
) : BaseSqliteTvRepository(connection, mutex, showsTabelName),
    TvShowsRepository {
    //    override suspend fun getAllShows(
//        limit: Int?,
//        timeStart: Long
//    ): Iterable<TvShowPreviewModel> = mutex.withLock{
//        val queryLimit = if (limit!! > 0) "LIMIT $limit" else "";
//        val querytimeStart = if(timeStart > 0) "TIMESTART $timeStart" else "";
//
//        val statement = connection.prepareStatement("""
//           SELECT id, name, assessment, ageLimit, previewUrl
//           FROM shows
//           ORDER BY id
//           $queryLimit
//           $querytimeStart
//        """)
//        val set = statement.executeQuery()
//        val shows = ArrayList<TvShowPreviewModel>()
//        while (set.next()){
//            val id = set.getInt("id")
//            val name = set.getString("name")
//            val assessment = set.getFloat("assessment")
//            val ageLimitValue = set.getInt("ageLimit")
//            val ageLimit = AgeLimit.fromInt(ageLimitValue)
//            val previewUrl = set.getString("previewUrl")
//            shows.add(TvShowPreviewModel(id, name, assessment, ageLimit, previewUrl))
//        }
//        statement.close()
//        return@withLock shows.toList()
//    }
//
//    override suspend fun getShowDetails(
//        id: Int
//    ): TvShowDetailsModel? = mutex.withLock{
//        val statement = connection.prepareStatement("""
//           SELECT id, name, assessment, ageLimit, previewUrl, description
//           FROM shows
//           WHERE id = ?
//        """)
//        statement.setInt(1, id)
//        val resultSet = statement.executeQuery()
//
//        if (!resultSet.next()) {
//            return null
//        }
//        else{
//        val ageLimitValue = resultSet.getInt("ageLimit")
//        val ageLimit = AgeLimit.fromInt(ageLimitValue)
//
//        return TvShowDetailsModel(
//            id = resultSet.getInt("id"),
//            name = resultSet.getString("name"),
//            assessment = resultSet.getFloat("assessment"),
//            ageLimit = ageLimit,
//            previewUrl = resultSet.getString("previewUrl"),
//            description = resultSet.getString("description")
//        )
//        }
//
//    }
//
//    override suspend fun getShowChannels(
//        showId: Int
//    ) : Iterable<TvShowChannelModel> = mutex.withLock{
//        val statement = connection.prepareStatement("""
//            SELECT channels.id, channels.name, channels.imageUrl, show_dates.time_start
//            FROM show_dates
//            JOIN channels ON show_dates.show_channel_id = channels.id
//            WHERE show_dates.show_id = ?
//        """)
//        statement.setInt(1, showId)
//
//        val set = statement.executeQuery()
//        val channels = mutableListOf<TvShowChannelModel>()
//
//        while (set.next()){
//            val channelId = set.getInt("id")
//            val channelName = set.getString("name")
//            val channelImageUrl = set.getString("imageUrl")
//            val timeStart = set.getLong("time_start")
//
//            channels.add(
//                TvShowChannelModel(
//                    showId = showId,
//                    id = channelId,
//                    name = channelName,
//                    imageUrl = channelImageUrl,
//                    dates = listOf(TvTimeSpan(timeStart.toInt(), 0))
//                )
//            )
//        }
//        statement.close()
//        return channels
//    }
    override suspend fun getAllShows(limit: Int, timeStart: Long): TvShows<TvShowPreviewModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getShowDetails(id: Int): TvShowDetailsModel? {
        TODO("Not yet implemented")
    }

    override suspend fun getShowChannels(
        showId: Int,
        channelsLimit: Int,
        channelsOffset: Int,
        releasesLimit: Int,
        releasesTimeStart: Long
    ): TvChannels<TvShowChannelModel> {
        TODO("Not yet implemented")
    }

    override suspend fun updateMany(shows: List<TvShowDetailsModel>) {
        TODO("Not yet implemented")
    }
}