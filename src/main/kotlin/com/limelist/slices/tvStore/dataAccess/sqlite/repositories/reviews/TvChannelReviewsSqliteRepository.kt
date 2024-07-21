package com.limelist.slices.tvStore.dataAccess.sqlite.repositories.reviews

import com.limelist.slices.tvStore.dataAccess.interfaces.TvChannelReviewsRepository
import com.limelist.slices.tvStore.services.models.reviews.ReviewsDistribution
import com.limelist.slices.tvStore.services.models.reviews.TvReview
import kotlinx.coroutines.sync.Mutex
import java.sql.Connection

class TvChannelReviewsSqliteRepository(
    connection: Connection,
    mutex: Mutex
): BaseTvReviewsRepository(connection, mutex, "channels", "channel_reviews"),
    TvChannelReviewsRepository {
}