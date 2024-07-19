package com.limelist.slices.tvStore.dataAccess.sqlite.repositories.favorites

import com.limelist.slices.tvStore.dataAccess.sqlite.repositories.BaseSqliteTvRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.sql.Connection

abstract class BaseFavoritesSqliteRepository(
    connection: Connection,
    mutex: Mutex,
    tableName: String
) : BaseSqliteTvRepository(connection, mutex, tableName) {

    private val addFavoritesStatement = connection.prepareStatement("""
        INSERT OR IGNORE INTO ${tableName}(user_id, show_id)
            VALUES (?,?)
    """)

    open suspend fun addUserFavorites(userId: Int, favoriteId: Int) {
        addFavoritesStatement.run {
            setInt(1, userId)
            setInt(2, favoriteId)
            executeUpdate()
        }
        connection.commit()
    }

    private val deleteFavoritesStatement = connection.prepareStatement("""
        DELETE FROM ${tableName}
        WHERE user_id = ? AND show_id = ?
        
    """)

    open suspend fun removeUserFavorite(userId: Int, favoriteId: Int) {
        deleteFavoritesStatement.run {
            setInt(1, userId)
            setInt(2, favoriteId)
            executeUpdate()
        }

        connection.commit()
    }

}