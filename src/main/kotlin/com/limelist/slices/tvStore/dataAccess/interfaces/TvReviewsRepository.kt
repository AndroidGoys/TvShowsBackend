package com.limelist.slices.tvStore.dataAccess.interfaces

import com.limelist.slices.tvStore.services.models.comments.TvReview
import com.limelist.slices.tvStore.services.models.comments.TvReviews

interface TvReviewsRepository {
    suspend fun get(parentId: Int, limit: Int, offset: Int): TvReviews
    suspend fun add(parentId: Int, comment: TvReview)
}