package com.limelist.slices.tvStore.dataAccess.sqlite.repositories.favorites

import com.limelist.slices.tvStore.dataAccess.interfaces.TvFavoritesRepository
import com.limelist.slices.tvStore.dataAccess.sqlite.repositories.BaseSqliteTvRepository
import com.limelist.slices.tvStore.services.models.shows.TvShowPreviewModel
import com.limelist.slices.tvStore.services.models.shows.TvShows
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.sql.Connection

class FavoriteTvShowsSqliteRepository(
    connection: Connection,
    mutex: Mutex
) : BaseFavoritesSqliteRepository(connection, mutex, "favorite_shows"),
    TvFavoritesRepository<TvShows<TvShowPreviewModel>> {

    val getFavoritesStatement = connection.prepareStatement("""
        SELECT 
            shows.*,
            AVG(show_reviews.assessment) as assessment
        FROM ( 
            SELECT shows.* 
                FROM (    
                    SELECT * 
                        FROM favorite_shows
                    WHERE user_id = ?
                    ORDER BY show_id
                    LIMIT ?
                    OFFSET ?
                ) as favorites
            INNER JOIN shows
                ON shows.id == favorites.show_id
        ) as shows
        LEFT JOIN show_reviews
            ON show_reviews.parent_id = shows.id
        GROUP BY shows.id
    """)

    override suspend fun getUserFavorites(
        userId: Int,
        limit: Int,
        offset: Int
    ): TvShows<TvShowPreviewModel> = mutex.withLock {
        var set = getFavoritesStatement.run {
            setInt(1, userId)
            setInt(2, limit)
            setInt(3, offset)
            return@run executeQuery()
        }

        val shows = parseShowPreviews(set)

        return TvShows(
            -1,
            shows
        )
    }
}