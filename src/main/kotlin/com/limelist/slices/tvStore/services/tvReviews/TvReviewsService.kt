package com.limelist.slices.tvStore.services.tvReviews

import com.limelist.slices.shared.RequestResult
import com.limelist.slices.tvStore.services.models.reviews.ReviewsDistribution
import com.limelist.slices.tvStore.services.models.reviews.TvReview
import com.limelist.slices.tvStore.services.models.reviews.TvReviews

interface TvReviewsService{
    suspend fun getAll(
        parentId: Int,
        limit: Int?,
        timeStart: Long?,
        timeZone: Float?,
        assessment: Int?,
    ): RequestResult<TvReviews>

    suspend fun addOrUpdateReview(
        parentId: Int,
        userId: Int,
        assessment: Int,
        text: String,
    ): RequestResult<TvReview>

    suspend fun getUserReview(
        parentId: Int,
        userId: Int,
        timeZone: Float?
    ): RequestResult<TvReview>


    suspend fun deleteReview(
        parentId: Int,
        userId: Int
    ): RequestResult<Unit>

    suspend fun reviewsDistribution(parentId: Int): RequestResult<ReviewsDistribution>
}