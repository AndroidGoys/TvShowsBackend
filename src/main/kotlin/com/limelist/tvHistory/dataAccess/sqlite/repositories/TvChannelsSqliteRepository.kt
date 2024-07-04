package com.limelist.tvHistory.dataAccess.sqlite.repositories

import com.limelist.tvHistory.services.models.channels.TvChannelPreviewModel
import kotlinx.coroutines.sync.Mutex
import java.sql.Connection

import com.limelist.tvHistory.dataAccess.interfaces.TvChannelsRepository
import com.limelist.tvHistory.services.models.channels.TvChannels
import com.limelist.tvHistory.services.models.channels.TvChannelDetailsModel
import com.limelist.tvHistory.services.models.releases.TvChannelReleases

class TvChannelsSqliteRepository(
    connection: Connection,
    mutex: Mutex
) : BaseSqliteTvRepository(connection, mutex, channelsTabelName),
    TvChannelsRepository {

    //        override suspend fun getAllChannels(
//            limit: Int,
//            offset: Int
//        ): List<TvChannelPreviewModel> = mutex.withLock {
//            val queryLimit = if (limit > 0) "LIMIT $limit" else "";
//            val queryOffset = if (offset > 0) "OFFSET $offset" else "";
//
//            val statement = connection.prepareStatement("""
//                SELECT id, name, image_url
//                FROM channels
//                ORDER BY id
//                $queryLimit
//                $queryOffset
//            """)
//
//            val set = statement.executeQuery()
//            val channels = mutableListOf<TvChannelPreviewModel>()
//
//            while (set.next()){
//                val id = set.getInt("id")
//                val name = set.getString("name")
//                val imageUrl = set.getString("image_url")
//                channels.add(TvChannelPreviewModel(
//                    id = id,
//                    name = name,
//                    imageUrl = imageUrl
//                ))
//            }
//
//            statement.close()
//            return@withLock channels.toList()
//    }
//
//    override suspend fun getChannelDetails(
//        id: Int
//    ): TvChannelDetailsModel? = mutex.withLock {
//        val statement = connection.prepareStatement("""
//            SELECT
//                channels.id as id,
//                channels.name as name,
//                channels.image_url as image_url,
//                channels.description as description,
//                channel_view_urls.url as view_url
//            FROM channels
//            LEFT JOIN channel_view_urls
//            ON channels.id = channel_view_urls.channel_id
//            WHERE channels.id == $id;
//        """)
//
//        val set = statement.executeQuery();
//
//        if (!set.next())
//            return null;
//
//        val id = set.getInt("id")
//        val name = set.getString("name")
//        val imageUrl = set.getString("image_url")
//        val desc = set.getString("description")
//        val viewUrls = mutableListOf(set.getString("view_url"))
//
//        while(set.next()) {
//            viewUrls.add(set.getString("url"))
//        }
//
//        statement.close()
//        return TvChannelDetailsModel(
//            id = id,
//            name = name,
//            imageUrl = imageUrl,
//            description = desc,
//            viewUrls = viewUrls
//        )
//    }
//
//    override suspend fun getChannelShows(channelId: Int): TvChannelShows {
//        val statement = connection.prepareStatement("""
//            SELECT
//                shows.*,
//                show_dates.time_start as time_start,
//                show_dates.time_end as time_end
//
//            FROM channels
//            LEFT JOIN show_dates
//                ON channels.id = show_dates.channel_id
//            LEFT JOIN shows
//                ON shows.id = show_dates.show_id
//            WHERE channels.id = $channelId
//            ORDER BY show_dates.time_start
//        """)
//
//        TODO("Not yet implemented")
//    }
    override suspend fun getAllChannels(limit: Int, offset: Int): TvChannels<TvChannelPreviewModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getChannelDetails(id: Int): TvChannelDetailsModel? {
        TODO("Not yet implemented")
    }

    override suspend fun searchByName(name: String, limit: Int, offset: Int): TvChannels<TvChannelPreviewModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getChannelReleases(channelId: Int, limit: Int, timeStart: Long): TvChannelReleases {
        TODO("Not yet implemented")
    }

    override suspend fun updateMany(channels: List<TvChannelDetailsModel>) {
        TODO("Not yet implemented")
    }

}
