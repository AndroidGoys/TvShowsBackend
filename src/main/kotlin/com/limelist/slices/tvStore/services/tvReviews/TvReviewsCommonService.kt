package com.limelist.slices.tvStore.services.tvReviews

import com.limelist.slices.shared.RequestError
import com.limelist.slices.shared.RequestResult
import com.limelist.slices.shared.getCurrentUnixUtc0TimeSeconds
import com.limelist.slices.tvStore.dataAccess.interfaces.SingleIdRepository
import com.limelist.slices.tvStore.dataAccess.interfaces.TvReviewsRepository
import com.limelist.slices.tvStore.services.models.reviews.ReviewsDistribution
import com.limelist.slices.tvStore.services.models.reviews.TvReview
import com.limelist.slices.tvStore.services.models.reviews.TvReviews
import com.limelist.slices.tvStore.services.tvChannels.reviews.TvChannelReviewsService
import com.limelist.slices.tvStore.services.tvShows.reviews.TvShowReviewsService
import io.ktor.http.*

class TvReviewsCommonService(
    val parents: SingleIdRepository<Int>,
    val tvReviews: TvReviewsRepository,
    val parentName: String
) : TvShowReviewsService, TvChannelReviewsService {

    val parentNotFoundResult = RequestResult.FailureResult(
        RequestError(
            RequestError.ErrorCode.ParentIdNotFoundError,
            "${parentName} id not found"
        ), HttpStatusCode.NotFound
    )

    val reviewNotFoundResult = RequestResult.FailureResult(
        RequestError(
            RequestError.ErrorCode.ReviewNotFoundError,
            "Review not found"
        ), HttpStatusCode.NotFound
    )


    override suspend fun getAll(
        parentId: Int,
        limit: Int?,
        timeStart: Long?,
        timeZone: Float?,
        assessment: Int?
    ): RequestResult<TvReviews> {
        if (!parents.contains(parentId))
            return parentNotFoundResult

        val limit = limit?: -1
        var timeStart = timeStart ?: 0

        val reviews =
            if (assessment == null)
                tvReviews.get(parentId, limit, timeStart)
            else
                tvReviews.getByAssessment(parentId, assessment, limit, timeStart)

        return RequestResult.SuccessResult(reviews)
    }

    override suspend fun addOrUpdateReview(
        parentId: Int,
        userId: Int,
        assessment: Int,
        text: String
    ): RequestResult<TvReview> {
        val tvReview = TvReview(
            userId,
            assessment,
            getCurrentUnixUtc0TimeSeconds(),
            text
        )

        tvReviews.update(
            parentId,
            tvReview
        )

        return RequestResult.SuccessResult(tvReview)
    }

    override suspend fun getDistribution(
        parentId: Int
    ): RequestResult<ReviewsDistribution>{
        if (!parents.contains(parentId))
            return parentNotFoundResult

        val distribution = tvReviews.getDistribution(parentId)
        return RequestResult.SuccessResult(distribution)
    }

    override suspend fun getUserReview(
        parentId: Int,
        userId: Int,
        timeZone: Float?
    ): RequestResult<TvReview> {
        if (!parents.contains(parentId))
            return parentNotFoundResult

        val tvReview = tvReviews.getForUser(userId, parentId)
        if (tvReview == null) {
            return reviewNotFoundResult
        }

        return RequestResult.SuccessResult(tvReview)
    }

    override suspend fun deleteReview(
        parentId: Int,
        userId: Int
    ): RequestResult<Unit> {
        tvReviews.delete(parentId, userId)
        return RequestResult.SuccessResult(Unit)
    }
}