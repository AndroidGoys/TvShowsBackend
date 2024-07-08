package com.limelist.tvHistory.dataAccess.sqlite.repositories
import com.limelist.tvHistory.dataAccess.interfaces.TvRepository
import io.ktor.server.plugins.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.sql.Connection

abstract class BaseSqliteTvRepository(
    protected val connection: Connection,
    protected val mutex: Mutex,
    protected val tableName: String
): TvRepository {
    companion object {
        val channelsTabelName = "channels"
        val showsTabelName = "shows"
        val releasesTableName = "releases"
    }

    val getCountStatement = connection.prepareStatement("""
           SELECT COUNT(*) as count FROM $tableName;
    """);

    override suspend fun count(): Int = mutex.withLock {
        val set = getCountStatement.executeQuery();

        if (!set.next())
            throw NotFoundException();

        return@withLock set.getInt(1);
    }

}
