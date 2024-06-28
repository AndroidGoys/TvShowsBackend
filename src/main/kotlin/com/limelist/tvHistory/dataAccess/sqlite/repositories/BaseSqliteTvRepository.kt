package com.limelist.tvHistory.dataAccess.sqlite.repositories
import com.limelist.tvHistory.dataAccess.interfaces.TvRepository
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

    override suspend fun count(): Int = mutex.withLock {
        val statement = connection.createStatement();
        return statement.executeQuery("""
           SELECT COUNT(*) FROM $tableName;
        """).use { resultSet ->
            resultSet.getInt(0)
       }
    }


    companion object {
        val channelsTabelName = "channels"
        val showsTabelName = "shows"

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
                CREATE TABLE IF NOT EXISTS show_dates (
                    id SERIAL PRIMARY KEY,
                    show_id INTEGER REFERENCES shows,
                    channel_id INTEGER REFERENCES channels,
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
