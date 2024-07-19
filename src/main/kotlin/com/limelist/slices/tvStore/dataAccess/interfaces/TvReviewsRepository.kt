package com.limelist.slices.tvStore.dataAccess.interfaces

import com.limelist.slices.tvStore.services.models.reviews.TvReview
import com.limelist.slices.tvStore.services.models.reviews.TvReviews

interface TvReviewsRepository {
    suspend fun get(parentId: Int, limit: Int, timeStart: Long): TvReviews
    suspend fun add(parentId: Int, comment: TvReview)
}