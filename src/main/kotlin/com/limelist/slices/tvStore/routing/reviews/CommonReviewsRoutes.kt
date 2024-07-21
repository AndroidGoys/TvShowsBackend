package com.limelist.slices.tvStore.routing.reviews

import com.limelist.slices.shared.receiveJson
import com.limelist.slices.shared.respondResult
import com.limelist.slices.shared.withAuth
import com.limelist.slices.tvStore.routing.models.AddReviewModel
import com.limelist.slices.tvStore.services.tvReviews.TvReviewsService

import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.routing.*

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


internal fun Route.useReviews(tvReviewsService: TvReviewsService) {
    getReviews(tvReviewsService)
    authenticate("access-auth"){
        getUserReview(tvReviewsService)
        addOrUpdateReview(tvReviewsService)
        deleteReview(tvReviewsService)
    }

}

private fun Route.getReviews(tvReviewsService: TvReviewsService) {
    get<Parent.Reviews>{ args ->
        val reviews = tvReviewsService.getAll(
            args.parent.id,
            args.limit,
            args.timeStart,
            args.timeZone,
            args.assessment
        )
        call.respondResult(reviews)
    }
}

private fun Route.addOrUpdateReview(tvReviewsService: TvReviewsService) {
    post<Parent.Reviews> { args ->
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
    tvReviewsService: TvReviewsService
) {
    get<Parent.Reviews.My> { args ->
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
    tvReviewsService: TvReviewsService
) {
    delete<Parent.Reviews.My> { args ->
        call.withAuth { user ->
            val result = tvReviewsService.deleteReview(
                args.parent.id,
                user.id,
            )

            call.respondResult(result)
        }
    }
}

@Serializable
@Resource("/{id}")
data class Parent(
    val id: Int
) {
    @Resource("/reviews")
    data class Reviews (
        val parent: Parent,
        val limit: Int? = null,
        @SerialName("time-start")
        val timeStart: Long? = null,
        @SerialName("time-zone")
        val timeZone: Float? = null,
        val assessment: Int? = null
    ) {
        @Resource("/reviews/@my")
        data class My (
            val parent: Parent,
            @SerialName("time-zone")
            val timeZone: Float? = null
        )
    }
}
