package com.limelist.tvHistory.dataAccess.sqlite.repositories

import com.limelist.tvHistory.models.channels.TvChannelPreviewModel
import kotlinx.coroutines.sync.Mutex
import java.sql.Connection

import com.limelist.tvHistory.dataAccess.interfaces.TvChannelsRepository
import com.limelist.tvHistory.models.channels.TvChannelDetailsModel
import com.limelist.tvHistory.models.channels.TvChannelShows
import kotlinx.coroutines.sync.withLock

class TvChannelsSqliteRepository(
    connection: Connection,
    mutex: Mutex
) : BaseSqliteTvRepository(connection, mutex, channelsTabelName),
    TvChannelsRepository {

        override suspend fun getAllChannels(
            limit: Int,
            offset: Int
        ): List<TvChannelPreviewModel> = mutex.withLock {
            val queryLimit = if (limit > 0) "LIMIT $limit" else "";
            val queryOffset = if (offset > 0) "OFFSET $offset" else "";

            val statement = connection.prepareStatement("""
                SELECT id, name, image_url 
                FROM channels
                ORDER BY id
                $queryLimit
                $queryOffset
            """)

            val set = statement.executeQuery()
            val channels = mutableListOf<TvChannelPreviewModel>()

            while (set.next()){
                val id = set.getInt("id")
                val name = set.getString("name")
                val imageUrl = set.getString("image_url")
                channels.add(TvChannelPreviewModel(
                    id = id,
                    name = name,
                    imageUrl = imageUrl
                ))
            }

            statement.close()
            return@withLock channels.toList()
    }

    override suspend fun getChannelDetails(id: Int): TvChannelDetailsModel? {
        throw NotImplementedError("Изменены модели")

//        return mutex.withLock {
//
//            return@withLock connection.prepareStatement("""
//                    SELECT channels.id, channels.name, channels.description,(
//                        SELECT GROUP_CONCAT(url, ',')
//                        FROM channel_view_urls
//                        WHERE channel_view_urls.channel_id = channels.id
//                    ) AS view_urls
//                    FROM channels
//                    WHERE channels.id = ?
//                """).use { statement ->
//                statement.setInt(1, id)
//                statement.executeQuery().use { resultSet ->
//                    if (resultSet.next()) {
//                        TvChannelFullModel(
//                            id = resultSet.getInt("id"),
//                            name = resultSet.getString("name"),
//                            description = resultSet.getString("description"),
//                            channelViewUrls = resultSet.getString("channel_view_urls")?.split(",")?.toList() ?: emptyList()
//                        )
//                    } else {
//                        null
//                    }
//                }
//            }
//        }
    }

    override suspend fun getChannelShows(channelId: Int): TvChannelShows {
        TODO("Not yet implemented")
    }
}
