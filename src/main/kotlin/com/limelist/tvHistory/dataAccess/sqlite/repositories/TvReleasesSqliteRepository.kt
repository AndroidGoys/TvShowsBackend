package com.limelist.tvHistory.dataAccess.sqlite.repositories

import com.limelist.tvHistory.dataAccess.interfaces.TvReleasesRepository
import com.limelist.tvHistory.dataAccess.models.TvReleaseDataModel
import kotlinx.coroutines.sync.Mutex
import java.sql.Connection

class TvReleasesSqliteRepository(
    connection: Connection,
    mutex: Mutex
):
    BaseSqliteTvRepository(connection, mutex, releasesTableName),
    TvReleasesRepository {

    override suspend fun updateMany(releases: List<TvReleaseDataModel>) {
        TODO("Not yet implemented")
    }
}