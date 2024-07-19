package com.limelist.slices.tvStore.routing.shows

import com.limelist.slices.shared.receiveJson
import com.limelist.slices.shared.respondResult
import com.limelist.slices.tvStore.routing.models.AddReviewModel
import com.limelist.slices.tvStore.services.tvShowServices.TvShowsServiceInterface
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.post

internal fun Route.showReviews(tvShowsService: TvShowsServiceInterface) {
    getReviews(tvShowsService)
    addReview(tvShowsService)
}

private fun Route.getReviews(tvShowsService: TvShowsServiceInterface) {
    get<AllShows.Show.Reviews>{ args ->
        val reviews = tvShowsService.getReviews(
            args.parent.id,
            args.limit,
            args.timeStart,
            args.timeZone
        )
        call.respondResult(reviews)
    }
}

private fun Route.addReview(tvShowsService: TvShowsServiceInterface) {
    post<AllShows.Show.Reviews> { args ->
        val userIdPrincipal = call.principal<UserIdPrincipal>()

        if (userIdPrincipal == null){
            call.respond(HttpStatusCode.Unauthorized)
            return@post
        }

        val review = call.receiveJson<AddReviewModel>()
        call.respondResult(
            tvShowsService.addReview(
                args.parent.id,
                userIdPrincipal.name.toInt(),
                review.assessment,
                review.text
            )
        )
    }
}

