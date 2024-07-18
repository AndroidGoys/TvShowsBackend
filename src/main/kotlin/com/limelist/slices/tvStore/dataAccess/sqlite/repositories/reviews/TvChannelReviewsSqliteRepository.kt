package com.limelist.slices.tvStore.dataAccess.sqlite.repositories.reviews

import com.limelist.slices.tvStore.dataAccess.interfaces.TvChannelReviewsRepository
import kotlinx.coroutines.sync.Mutex
import java.sql.Connection

class TvChannelReviewsSqliteRepository(
    connection: Connection,
    mutex: Mutex
): BaseTvReviewsRepository(connection, mutex, "shows", "show_reviews"),
    TvChannelReviewsRepository {
}