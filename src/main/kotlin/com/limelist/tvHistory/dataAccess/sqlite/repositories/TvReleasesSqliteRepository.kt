package com.limelist.tvHistory.dataAccess.sqlite.repositories

import com.limelist.tvHistory.dataAccess.interfaces.TvReleasesRepository
import com.limelist.tvHistory.dataAccess.models.TvReleaseCreateModel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.sql.Connection

class TvReleasesSqliteRepository(
    connection: Connection,
    mutex: Mutex
):
    BaseSqliteTvRepository(connection, mutex, releasesTableName),
    TvReleasesRepository {

    private val updateReleasesStatement = connection.prepareStatement("""
        INSERT INTO releases
          (id, show_id, channel_id, description, time_start, time_stop)
        VALUES
          (?, ?, ?, ?, ?, ?)
        ON CONFLICT(id) DO UPDATE
            SET show_id = EXCLUDED.show_id, 
                channel_id = EXCLUDED.channel_id, 
                description = EXCLUDED.description,
                time_start = EXCLUDED.time_start,
                time_stop = EXCLUDED.time_stop
    """)

    private val clearTableStatement = connection.prepareStatement("""
        DELETE FROM releases
    """)

    override suspend fun updateMany(
        releases: List<TvReleaseCreateModel>
    ) = mutex.withLock {

        clearTableStatement.executeUpdate()
        var count = 0;

        for (release in releases) {
            updateReleasesStatement.run {
                count++;
                setInt(1, count)
                setInt(2, release.showId)
                setInt(3, release.channelId)
                setString(4, release.description)
                setLong(5, release.timeStart)
                setLong(6, release.timeStop)
                executeUpdate()
            }
        }

    }
}