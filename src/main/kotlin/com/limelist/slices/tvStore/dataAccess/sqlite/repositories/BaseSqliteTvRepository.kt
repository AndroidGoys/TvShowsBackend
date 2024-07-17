package com.limelist.slices.tvStore.dataAccess.sqlite.repositories
import com.limelist.slices.tvStore.dataAccess.interfaces.TvRepository
import com.limelist.slices.tvStore.services.models.tags.TvTagPreview
import io.ktor.server.plugins.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.sql.Connection
import java.sql.ResultSet

abstract class BaseSqliteTvRepository(
    protected val connection: Connection,
    protected val mutex: Mutex,
    protected val tableName: String
): TvRepository {
    init {
        connection.autoCommit = false
        val statement = connection.createStatement();

        statement.execute("""
            PRAGMA case_sensitive_like=OFF;
        """.trimIndent())

        statement.execute("""
            CREATE TABLE IF NOT EXISTS channels (
                id INTEGER PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                image_url TEXT NOT NULL,
                description TEXT NOT NULL
            );
        """)

        statement.execute("""
            CREATE TABLE IF NOT EXISTS shows (
                id INTEGER PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                age_limit INTEGER,
                description TEXT NOT NULL,  
                preview_url TEXT DEFAULT NULL
            );
        """)

        statement.execute("""
            CREATE TABLE IF NOT EXISTS tags (
                id INTEGER PRIMARY KEY  ,
                name VARCHAR(50) NOT NULL,
                belong INTEGER
            );
        """)

        statement.execute("""
            CREATE TABLE IF NOT EXISTS channel_tags(
                channel_id INTEGER REFERENCES channels,
                tag_id INTEGER REFERENCES tags,
                CONSTRAINT id PRIMARY KEY (channel_id, tag_id)
            );
        """)

        statement.execute("""
            CREATE TABLE IF NOT EXISTS show_tags(
                show_id INTEGER REFERENCES shows,
                tag_id INTEGER REFERENCES tags,
                CONSTRAINT id PRIMARY KEY (show_id, tag_id)
            );
        """)

        statement.execute("""
            CREATE TABLE IF NOT EXISTS show_reviews (
                user_id INTEGER PRIMARY KEY,
                show_id INTEGER REFERENCES shows,
                date INTEGER,  
                comment TEXT,
                assessment INTEGER
            );
        """)

        statement.execute("""
            CREATE TABLE IF NOT EXISTS channel_reviews (
                user_id INTEGER PRIMARY KEY,
                channel_id INTEGER REFERENCES shows,
                date INTEGER,  
                comment TEXT,
                assessment INTEGER
            );
        """)

        statement.execute("""
            CREATE TABLE IF NOT EXISTS releases (
                id INTEGER PRIMARY KEY,
                show_id INTEGER REFERENCES shows,
                channel_id INTEGER REFERENCES channels,
                description TEXT NOT NULL,
                time_start REAL,
                time_stop REAL
            );
        """)


        statement.execute("""
            CREATE TABLE IF NOT EXISTS channel_view_urls (
                id INTEGER PRIMARY KEY,
                channel_id INTEGER REFERENCES channels,
                url TEXT NOT NULL
            );
        """)


        statement.execute("""
            CREATE TABLE IF NOT EXISTS show_frames (
                id INTEGER PRIMARY KEY,
                show_id INTEGER REFERENCES shows,
                url TEXT NOT NULL
            );
        """)
        statement.close()

        connection.commit()
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


    val containsStatement = connection.prepareStatement("""
        SELECT * FROM $tableName
        WHERE id = ?
        LIMIT 1
    """)

    override suspend fun contains(id: Int): Boolean {
        val set = containsStatement.run {
            setInt(1, id)
            return@run executeQuery()
        }
        return set.next()
    }

    protected fun parseTag(
        set: ResultSet
    ): TvTagPreview {
        return TvTagPreview(
            set.getInt("id"),
            set.getString("name"),
        )
    }


}
