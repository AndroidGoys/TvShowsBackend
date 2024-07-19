package com.limelist.slices.tvStore.dataAccess.interfaces

interface TvFavoritesRepository<TFavorites> {
    suspend fun getUserFavorites(userId: Int, limit: Int, offset: Int): TFavorites
    suspend fun addUserFavorites(userId: Int, favoriteId: Int)
    suspend fun removeUserFavorite(userId: Int, favoriteId: Int)
}
