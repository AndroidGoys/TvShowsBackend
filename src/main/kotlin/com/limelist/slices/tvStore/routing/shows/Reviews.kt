package com.limelist.slices.tvStore.routing.shows

import com.limelist.slices.shared.receiveJson
import com.limelist.slices.shared.respondResult
import com.limelist.slices.shared.withAuth
import com.limelist.slices.tvStore.routing.models.AddReviewModel
import com.limelist.slices.tvStore.services.tvShows.reviews.TvShowReviewsService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.server.resources.post

internal fun Route.showReviews(tvReviewsService: TvShowReviewsService) {
    getReviews(tvReviewsService)
    authenticate("access-auth"){
        getUserReview(tvReviewsService)
        addOrUpdateReview(tvReviewsService)
        deleteReview(tvReviewsService)
    }

}

private fun Route.getReviews(tvReviewsService: TvShowReviewsService) {
    get<AllShows.Show.Reviews>{ args ->
        val reviews = tvReviewsService.getAll(
            args.parent.id,
            args.limit,
            args.timeStart,
            args.timeZone
        )
        call.respondResult(reviews)
    }
}

private fun Route.addOrUpdateReview(tvReviewsService: TvShowReviewsService) {
    post<AllShows.Show.Reviews> { args ->
        call.withAuth { user ->
            call.receiveJson<AddReviewModel>{ review ->
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
    tvReviewsService: TvShowReviewsService
) {
    get<AllShows.Show.Reviews.My> { args ->
        call.withAuth { data ->
            val review = tvReviewsService.getUserReview(
                args.show.id,
                data.id,
                args.timeZone
            )

            call.respondResult(review)
        }
    }

}

private fun Route.deleteReview(
    tvReviewsService: TvShowReviewsService
) {
    delete<AllShows.Show.Reviews.My> { args ->
        call.withAuth { user ->
            val result = tvReviewsService.deleteReview(
                args.show.id,
                user.id,
            )

            call.respondResult(result)
        }
    }
}