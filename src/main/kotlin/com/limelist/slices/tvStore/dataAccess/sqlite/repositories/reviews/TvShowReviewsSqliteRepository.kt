package com.limelist.slices.tvStore.dataAccess.sqlite.repositories.reviews

import com.limelist.slices.tvStore.dataAccess.interfaces.TvChannelReviewsRepository
import com.limelist.slices.tvStore.dataAccess.interfaces.TvShowReviewsRepository
import kotlinx.coroutines.sync.Mutex
import java.sql.Connection

class TvShowReviewsSqliteRepository(
    connection: Connection,
    mutex: Mutex
): BaseTvReviewsRepository(connection, mutex, "shows", "show_reviews"),
    TvShowReviewsRepository {
}