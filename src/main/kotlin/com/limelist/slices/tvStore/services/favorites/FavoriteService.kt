package com.limelist.slices.tvStore.services.favorites

import com.limelist.slices.shared.RequestResult

interface FavoriteService<TFavorites> {
    suspend fun getAll(
        userId: Int,
        limit: Int?,
        offset: Int?
    ): RequestResult<TFavorites>

    suspend fun add(
        userId: Int,
        favoriteId: Int,
    ): RequestResult<Unit>

    suspend fun remove(
        userId: Int,
        favoriteId: Int
    ): RequestResult<Unit>
}