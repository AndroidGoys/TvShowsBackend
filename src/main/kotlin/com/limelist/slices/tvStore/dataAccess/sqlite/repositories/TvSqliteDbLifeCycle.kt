package com.limelist.slices.tvStore.dataAccess.sqlite.repositories

import com.limelist.slices.shared.DbLifeCycle
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.sql.Connection

class TvSqliteDbLifeCycle(
    val connection: Connection,
    val mutex: Mutex
) : DbLifeCycle {
    override suspend fun start() {
    }

    override suspend fun stop() = mutex.withLock {
        connection.close()
    }
}