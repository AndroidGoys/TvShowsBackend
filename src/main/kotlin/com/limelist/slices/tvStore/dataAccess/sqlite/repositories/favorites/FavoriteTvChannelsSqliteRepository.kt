package com.limelist.slices.tvStore.dataAccess.sqlite.repositories.favorites

import com.limelist.slices.tvStore.dataAccess.interfaces.TvFavoritesRepository
import com.limelist.slices.tvStore.dataAccess.sqlite.repositories.BaseSqliteTvRepository
import com.limelist.slices.tvStore.services.models.channels.TvChannelPreviewModel
import com.limelist.slices.tvStore.services.models.channels.TvChannels
import kotlinx.coroutines.sync.Mutex
import java.sql.Connection

class FavoriteTvChannelsSqliteRepository(
    connection: Connection,
    mutex: Mutex
) : BaseFavoritesSqliteRepository(connection, mutex, "favorite_shows"),
    TvFavoritesRepository<TvChannels<TvChannelPreviewModel>> {
    override suspend fun getUserFavorites(userId: Int, limit: Int, offset: Int): TvChannels<TvChannelPreviewModel> {
        TODO("Not yet implemented")
    }
}