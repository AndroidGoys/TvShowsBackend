package com.limelist.slices.tvStore.routing.channels

import com.limelist.slices.shared.receiveJson
import com.limelist.slices.shared.respondResult
import com.limelist.slices.shared.withAuth
import com.limelist.slices.tvStore.routing.models.AddReviewModel
import com.limelist.slices.tvStore.services.tvChannels.reviews.TvChannelReviewsService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.routing.*


internal fun Route.channelReviews(tvReviewsService: TvChannelReviewsService) {
    getReviews(tvReviewsService)
    authenticate("access-auth"){
        getUserReview(tvReviewsService)
        addOrUpdateReview(tvReviewsService)
        deleteReview(tvReviewsService)
    }

}

private fun Route.getReviews(
    tvReviewsService: TvChannelReviewsService
) {
    get<AllChannels.Channel.Reviews>{ args ->
        val reviews = tvReviewsService.getAll(
            args.parent.id,
            args.limit,
            args.timeStart,
            args.timeZone
        )
        call.respondResult(reviews)
    }
}

private fun Route.addOrUpdateReview(
    tvReviewsService: TvChannelReviewsService
) {
    post<AllChannels.Channel.Reviews> { args ->
        call.withAuth { user ->
            val review = call.receiveJson<AddReviewModel>{ review ->
                call.respondResult(
                    tvReviewsService.addOrUpdateReview(
                        args.parent.id,
                        user.id,
                        review.assessment,
                        review.text
                    )
                )
            }
        }
    }
}

private fun Route.getUserReview(
    tvReviewsService: TvChannelReviewsService
) {
    get<AllChannels.Channel.Reviews.My> { args ->
        call.withAuth { data ->
            val review = tvReviewsService.getUserReview(
                args.parent.id,
                data.id,
                args.timeZone
            )

            call.respondResult(review)
        }
    }

}

private fun Route.deleteReview(
    tvReviewsService: TvChannelReviewsService
) {
    delete<AllChannels.Channel.Reviews.My> { args ->
        call.withAuth { user ->
            val result = tvReviewsService.deleteReview(
                args.parent.id,
                user.id,
            )

            call.respondResult(result)
        }
    }
}