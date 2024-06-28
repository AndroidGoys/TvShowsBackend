package com.limelist.tvHistory.dataAccess.sqlite.repositories

import com.limelist.tvHistory.models.shows.TvShowDetailsModel
import com.limelist.tvHistory.models.shows.TvShowPreviewModel
import kotlinx.coroutines.sync.Mutex
import java.sql.Connection

import com.limelist.tvHistory.dataAccess.interfaces.ТvShowsRepository
import com.limelist.tvHistory.models.TvChannels
import com.limelist.tvHistory.models.shows.TvShowChannelModel

class TvShowsSqliteRepository(
    connection: Connection,
    mutex: Mutex
) : BaseSqliteTvRepository(connection, mutex, showsTabelName),
    ТvShowsRepository {
    override suspend fun getAllShows(limit: Int?, timeStart: Long): Iterable<TvShowPreviewModel> {
        throw NotImplementedError("Изменены модели")


        //        return mutex.withLock {
//            val statement = connection.prepareStatement(
//                """
//              SELECT id, name, description
//              FROM shows
//              LIMIT ?
//              OFFSET ?
//           """
//            ).apply {
//                setInt(1, limit ?: Int.MAX_VALUE)
//                setInt(2, offset)
//            }
//            statement.executeQuery().use { resultSet ->
//                buildList {
//                    while (resultSet.next()){
//                        add(
//                            TvShowModel(
//                                id = resultSet.getInt("id"),
//                                name = resultSet.getString("name")
//                            )
//                        )
//                    }
//                }
//            }
//        }
    }

    override suspend fun getShowDetails(id: Int): TvShowDetailsModel? {
        throw NotImplementedError("Изменены модели")

//        return mutex.withLock {
//            return connection.prepareStatement(
//                """
//                SELECT shows.id, shows.name, shows.description, shows.prview_url,
//                (
//                    SELECT GROUP_CONCAT(date, ',')
//                    FROM show_dates
//                    WHERE show_dates.show_id = shows.id
//                ) AS dates,
//                (
//                    SELECT GROUP_CONCAT(channel_id, ',')
//                    FROM show_dates
//                    WHERE show_dates.show_id = shows.id
//                ) AS channel_ids,
//                (
//                    SELECT GROUP_CONCAT(time_start, ',')
//                    FROM show_dates
//                    WHERE show_dates.show_id = shows.id
//                ) AS time_starts,
//                (
//                    SELECT GROUP_CONCAT(time_stop, ',')
//                    FROM show_dates
//                    WHERE show_dates.show_id = shows.id
//                ) AS time_stops
//                FROM shows
//                WHERE shows.id = ?
//            """
//            ).apply {
//                setInt(1, id)
//            }.executeQuery().use { resultSet ->
//                if (resultSet.next()) {
//                    TvShowFullModel(
//                        id = resultSet.getInt("id"),
//                        name = resultSet.getString("name"),
//                        description = resultSet.getString("description"),
//                        previewUrl = resultSet.getString("preview_url"),
//                    )
//                } else {
//                    null
//                }
//            }
//        }
//
    }

    override suspend fun getShowChannels(showId: Int) : TvChannels {
        TODO("Not yet implemented")
    }
}