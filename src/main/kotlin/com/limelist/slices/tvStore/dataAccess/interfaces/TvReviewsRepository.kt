package com.limelist.slices.tvStore.dataAccess.interfaces

import com.limelist.slices.tvStore.services.models.reviews.TvReview
import com.limelist.slices.tvStore.services.models.reviews.TvReviews

interface TvReviewsRepository {
    suspend fun get(parentId: Int, limit: Int, timeStart: Long): TvReviews
    suspend fun update(parentId: Int, review: TvReview)
    suspend fun getForUser(userId: Int, parentId: Int): TvReview?
    suspend fun delete(showId: Int, userId: Int)
    suspend fun getByAssessment(parentId: Int, assessment: Int, limit: Int, timeStart: Long): TvReviews
}