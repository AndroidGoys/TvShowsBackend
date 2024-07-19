package com.limelist.slices.tvStore.services.favorites

import com.limelist.slices.shared.RequestError
import com.limelist.slices.shared.RequestResult
import com.limelist.slices.tvStore.dataAccess.interfaces.SingleIdRepository
import com.limelist.slices.tvStore.dataAccess.interfaces.TvFavoritesRepository
import io.ktor.http.*

class CommonFavoriteService<TFavorites>(
    val favorites: TvFavoritesRepository<TFavorites>,
    val parents: SingleIdRepository<Int>,
    val favoriteName: String
) : FavoriteService<TFavorites> {
    private val favoriteNotFoundResult = RequestResult.FailureResult(
        RequestError(
            RequestError.ErrorCode.NotFound,
            "$favoriteName not found"
        ),
        HttpStatusCode.NotFound
    )

    override suspend fun getAll(userId: Int, limit: Int?, offset: Int?): RequestResult<TFavorites> {
        val limit = limit ?: -1
        val offset = offset ?: 0

        val favorites = favorites.getUserFavorites(userId, limit, offset)
        return RequestResult.SuccessResult(favorites)
    }

    override suspend fun add(userId: Int, favoriteId: Int): RequestResult<Unit> {
        if (!parents.contains(favoriteId))
            return favoriteNotFoundResult

        favorites.addUserFavorites(userId, favoriteId)
        return RequestResult.SuccessResult(Unit)
    }

    override suspend fun remove(userId: Int, favoriteId: Int): RequestResult<Unit> {
        if (!parents.contains(favoriteId))
            return favoriteNotFoundResult

        favorites.removeUserFavorite(userId, favoriteId)
        return RequestResult.SuccessResult(Unit)
    }
}