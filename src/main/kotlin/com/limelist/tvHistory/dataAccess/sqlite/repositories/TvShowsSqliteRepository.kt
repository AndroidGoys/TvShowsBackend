package com.limelist.tvHistory.dataAccess.sqlite.repositories

import com.limelist.tvHistory.models.shows.TvShowDetailsModel
import com.limelist.tvHistory.models.shows.TvShowPreviewModel
import kotlinx.coroutines.sync.Mutex
import java.sql.Connection

import com.limelist.tvHistory.dataAccess.interfaces.ТvShowsRepository
import com.limelist.tvHistory.models.AgeLimit
import com.limelist.tvHistory.models.TvChannels
import kotlinx.coroutines.sync.withLock

class TvShowsSqliteRepository(
    connection: Connection,
    mutex: Mutex
) : BaseSqliteTvRepository(connection, mutex, showsTabelName),
    ТvShowsRepository {
    override suspend fun getAllShows(
        limit: Int?,
        timeStart: Long
    ): Iterable<TvShowPreviewModel> = mutex.withLock{
        val queryLimit = if (limit!! > 0) "LIMIT $limit" else "";
        val querytimeStart = if(timeStart > 0) "TIMESTART $timeStart" else "";

        val statement = connection.prepareStatement("""
           SELECT id, name, assessment, ageLimit, previewUrl
           FROM shows
           ORDER BY id
           $queryLimit
           $querytimeStart
        """)
        val set = statement.executeQuery()
        val shows = ArrayList<TvShowPreviewModel>()
        while (set.next()){
            val id = set.getInt("id")
            val name = set.getString("name")
            val assessment = set.getFloat("assessment")
            val ageLimitValue = set.getInt("ageLimit")
            val ageLimit = AgeLimit.fromInt(ageLimitValue)
            val previewUrl = set.getString("previewUrl")
            shows.add(TvShowPreviewModel(id, name, assessment, ageLimit, previewUrl))
        }
        statement.close()
        return@withLock shows.toList()
    }

    override suspend fun getShowDetails(
        id: Int
    ): TvShowDetailsModel? = mutex.withLock{
        val statement = connection.prepareStatement("""
           SELECT id, name, assessment, ageLimit, previewUrl, description
           FROM shows
           WHERE id = ?
        """)
        statement.setInt(1, id)
        val resultSet = statement.executeQuery()

        if (!resultSet.next()) {
            return null
        }
        else{
        val ageLimitValue = resultSet.getInt("ageLimit")
        val ageLimit = AgeLimit.fromInt(ageLimitValue)

        return TvShowDetailsModel(
            id = resultSet.getInt("id"),
            name = resultSet.getString("name"),
            assessment = resultSet.getFloat("assessment"),
            ageLimit = ageLimit,
            previewUrl = resultSet.getString("previewUrl"),
            description = resultSet.getString("description")
        )
        }

    }

    override suspend fun getShowChannels(showId: Int) : TvChannels {
        TODO("Not yet implemented")
    }
}