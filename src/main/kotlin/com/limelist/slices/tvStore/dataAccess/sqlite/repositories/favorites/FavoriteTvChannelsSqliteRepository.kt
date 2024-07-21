package com.limelist.slices.tvStore.dataAccess.sqlite.repositories.favorites

import com.limelist.slices.tvStore.dataAccess.interfaces.TvFavoritesRepository
import com.limelist.slices.tvStore.dataAccess.sqlite.repositories.BaseSqliteTvRepository
import com.limelist.slices.tvStore.services.models.channels.TvChannelPreviewModel
import com.limelist.slices.tvStore.services.models.channels.TvChannels
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.sql.Connection

class FavoriteTvChannelsSqliteRepository(
    connection: Connection,
    mutex: Mutex
) : BaseFavoritesSqliteRepository(connection, mutex, "favorite_channels"),
    TvFavoritesRepository<TvChannels<TvChannelPreviewModel>> {

    val getFavoritesStatement = connection.prepareStatement("""
        SELECT 
            channels.*,
            AVG(channel_reviews.assessment) as assessment
        FROM (
            SELECT channels.*
                FROM (
                    SELECT *
                        FROM favorite_channels
                    WHERE user_id = ?
                    ORDER BY favorite_id
                    
                    LIMIT ?
                    OFFSET ?
                ) as favorites
            INNER JOIN channels
                ON channels.id = favorite_id
        ) as channels
        LEFT JOIN channel_reviews
            ON channel_reviews.parent_id = channels.id
            
        GROUP BY channels.id
        ORDER BY channels.id
        
    """)

    private val getTotalCountStatement = connection.prepareStatement("""
        SELECT COUNT(*)
            FROM favorite_channels
        WHERE user_id = ?
    """)
    override suspend fun getUserFavorites(
        userId: Int,
        limit: Int,
        offset: Int
    ): TvChannels<TvChannelPreviewModel> = mutex.withLock{
        var set = getFavoritesStatement.run {
            setInt(1, userId)
            setInt(2, limit)
            setInt(3, offset)
            return@run executeQuery()
        }

        val channels = parseChannelPreviews(set)

        getTotalCountStatement
            .setInt(1, userId)


        return@withLock TvChannels(
            getParsedCount(getTotalCountStatement),
            channels
        )
    }
}