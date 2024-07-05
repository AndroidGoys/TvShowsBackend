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
    init{
        initialize(connection)
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


    companion object {
        val channelsTabelName = "channels"
        val showsTabelName = "shows"
        val releasesTableName = "releases"

        fun initialize(connection: Connection) {
            val statement = connection.createStatement();

            statement.execute("""
                CREATE TABLE IF NOT EXISTS $channelsTabelName (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(50) NOT NULL,
                    image_url TEXT NOT NULL,
                    description TEXT NOT NULL
                );
            """)

            statement.execute("""
                CREATE TABLE IF NOT EXISTS $showsTabelName (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(50) NOT NULL,
                    age_limit INTEGER,
                    description TEXT NOT NULL,  
                    preview_url TEXT NOT NULL
                );
            """)

            statement.execute("""
                CREATE TABLE IF NOT EXISTS show_reviews (
                    id SERIAL PRIMARY KEY,
                    show_id INTEGER REFERENCES tv_shows,
                    date INTEGER,  
                    comment TEXT,
                    assessment INTEGER
                );
            """)

            statement.execute("""
                CREATE TABLE IF NOT EXISTS channel_reviews (
                    id SERIAL PRIMARY KEY,
                    channel_id INTEGER REFERENCES shows,
                    date INTEGER,  
                    comment TEXT,
                    assessment INTEGER
                );
            """)

            statement.execute("""
                CREATE TABLE IF NOT EXISTS $releasesTableName (
                    id SERIAL PRIMARY KEY,
                    show_id INTEGER REFERENCES shows,
                    channel_id INTEGER REFERENCES channels,
                    description TEXT NOT NULL,
                    time_start REAL,
                    time_stop REAL
                );
            """)


            statement.execute("""
                CREATE TABLE IF NOT EXISTS channel_view_urls (
                    id SERIAL PRIMARY KEY,
                    channel_id INTEGER REFERENCES channels,
                    url TEXT NOT NULL
                );
            """)
            statement.close()
        }
    }
}
