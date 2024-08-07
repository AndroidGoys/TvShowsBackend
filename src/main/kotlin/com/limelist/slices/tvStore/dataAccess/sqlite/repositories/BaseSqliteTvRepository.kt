package com.limelist.slices.tvStore.dataAccess.sqlite.repositories
import com.limelist.slices.tvStore.dataAccess.interfaces.TvRepository
import com.limelist.slices.tvStore.services.models.AgeLimit
import com.limelist.slices.tvStore.services.models.channels.TvChannelPreviewModel
import com.limelist.slices.tvStore.services.models.reviews.TvReview
import com.limelist.slices.tvStore.services.models.shows.TvShowPreviewModel
import com.limelist.slices.tvStore.services.models.tags.TvTagPreview
import io.ktor.server.plugins.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

abstract class BaseSqliteTvRepository(
    protected val connection: Connection,
    protected val mutex: Mutex,
    protected val tableName: String,
): TvRepository {
    init {
        connection.autoCommit = false
        val statement = connection.createStatement();

        statement.execute("""
            CREATE TABLE IF NOT EXISTS channels (
                id INTEGER PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                lower_name VARCHAR(50) NOT NULL,
                image_url TEXT NOT NULL,
                description TEXT NOT NULL
            );
        """)

        statement.execute("""
            CREATE TABLE IF NOT EXISTS shows (
                id INTEGER PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                lower_name VARCHAR(50) NOT NULL,
                age_limit INTEGER,
                description TEXT NOT NULL,  
                preview_url TEXT DEFAULT NULL
            );
        """)

        statement.execute("""
            CREATE TABLE IF NOT EXISTS tags (
                id INTEGER PRIMARY KEY  ,
                name VARCHAR(50) NOT NULL,
                lower_name VARCHAR(50) NOT NULL,
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
                user_id INTEGER,
                parent_id INTEGER REFERENCES shows,
                date REAL,  
                text TEXT,
                assessment INTEGER,
                CONSTRAINT id PRIMARY KEY (user_id, parent_id)
            );
        """)

        statement.execute("""
            CREATE TABLE IF NOT EXISTS channel_reviews (
                user_id INTEGER,
                parent_id INTEGER REFERENCES shows,
                date REAL,  
                text TEXT,
                assessment INTEGER,
                CONSTRAINT id PRIMARY KEY (user_id, parent_id)
            );
        """)

        statement.execute("""
            CREATE TABLE IF NOT EXISTS releases (
                show_id INTEGER REFERENCES shows,
                channel_id INTEGER REFERENCES channels,
                description TEXT NOT NULL,
                time_start REAL,
                time_stop REAL,
                CONSTRAINT id PRIMARY KEY (show_id, channel_id, time_start)
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

        statement.execute("""
            CREATE TABLE IF NOT EXISTS favorite_shows(
                user_id INTEGER,
                favorite_id INTEGER REFERENCES shows,
                CONSTRAINT id PRIMARY KEY (favorite_id, user_id)
            )
        """)

        statement.execute("""
            CREATE TABLE IF NOT EXISTS favorite_channels(
                user_id INTEGER,
                favorite_id INTEGER REFERENCES channels,
                CONSTRAINT id PRIMARY KEY (favorite_id, user_id)
            )
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

    protected fun parseTag(
        set: ResultSet
    ): TvTagPreview {
        return TvTagPreview(
            set.getInt("id"),
            set.getString("name"),
        )
    }

    protected fun parseShowPreviews(
        set: ResultSet
    ) = buildList{
        while (set.next()){
            add(
                TvShowPreviewModel(
                    set.getInt("id"),
                    set.getString("name"),
                    set.getFloat("assessment"),
                    AgeLimit.fromInt(set.getInt("age_limit")),
                    set.getString("preview_url"),
                )
            )
        }
    }

    protected fun parseReviews(
        set: ResultSet
    ) = buildList {
        while (set.next()){
            add(
                TvReview(
                    set.getInt("user_id"),
                    set.getInt("assessment"),
                    set.getLong("date"),
                    set.getString("text")
                )
            )
        }
    }

    protected fun parseChannelPreviews(
        set: ResultSet
    ) = buildList {
        while (set.next()){
            add(
                TvChannelPreviewModel(
                    set.getInt("id"),
                    set.getString("name"),
                    set.getString("image_url"),
                    set.getFloat("assessment")
                )
            )
        }
    }

    protected fun getParsedCount(
        statement: PreparedStatement
    ): Int{
        val set = statement.executeQuery()

        if (!set.next())
            throw UnknownError()

        return set.getInt(1)
    }
}
