package com.limelist.slices.tvStore.dataAccess.sqlite.repositories

import com.limelist.slices.tvStore.dataAccess.interfaces.TvReleasesRepository
import com.limelist.slices.tvStore.dataAccess.models.create.TvReleaseCreateModel
import io.ktor.server.plugins.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.sql.Connection

class TvReleasesSqliteRepository(
    connection: Connection,
    mutex: Mutex
):
    BaseSqliteTvRepository(connection, mutex, "releases"),
    TvReleasesRepository {

    private val updateReleasesStatement = connection.prepareStatement("""
        INSERT INTO releases
          (show_id, channel_id, description, time_start, time_stop)
        VALUES
          (?, ?, ?, ?, ?)
        ON CONFLICT(show_id, channel_id, time_start) DO UPDATE
            SET show_id = EXCLUDED.show_id, 
                channel_id = EXCLUDED.channel_id, 
                description = EXCLUDED.description,
                time_start = EXCLUDED.time_start,
                time_stop = EXCLUDED.time_stop
    """)

    override suspend fun updateMany(
        releases: List<TvReleaseCreateModel>
    ) = mutex.withLock {
        coroutineScope {
            for (release in releases) {
                if (!isActive)
                    break

                updateReleasesStatement.run {
                    setInt(1, release.showId)
                    setInt(2, release.channelId)
                    setString(3, release.description)
                    setLong(4, release.timeStart)
                    setLong(5, release.timeStop)
                    executeUpdate()
                }
            }
            connection.commit()
            if (!isActive)
                throw CancellationException();
        }
    }

}