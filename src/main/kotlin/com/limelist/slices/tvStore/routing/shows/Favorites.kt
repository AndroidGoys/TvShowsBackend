package com.limelist.slices.tvStore.routing.shows

import com.limelist.slices.shared.receiveJson
import com.limelist.slices.shared.respondResult
import com.limelist.slices.tvStore.routing.models.OnlyIdModel
import com.limelist.slices.tvStore.services.tvShowServices.TvShowsServiceInterface
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.post

internal fun Route.favoriteShows(
    tvShowsService: TvShowsServiceInterface
) {
    authenticate("access-auth") {
        addFavoriteShow(tvShowsService)
        getFavoriteShows(tvShowsService)
    }
}

private fun Route.addFavoriteShow(
    tvShowsService: TvShowsServiceInterface
) {
    post<AllShows.Favorites> {
        val userIdPrincipal = call.principal<UserIdPrincipal>()

        if (userIdPrincipal == null){
            call.respond(HttpStatusCode.Unauthorized)
            return@post
        }

        val show = call.receiveJson<OnlyIdModel>()

        val result = tvShowsService.addToFavorite(
            userIdPrincipal.name.toInt(),
            show.id
        )

        call.respondResult(result)
    }
}

private fun Route.getFavoriteShows(tvShowsService: TvShowsServiceInterface) {
    get<AllShows.Favorites>{ args ->
        val userIdPrincipal = call.principal<UserIdPrincipal>()

        if (userIdPrincipal == null){
            call.respond(HttpStatusCode.Unauthorized)
            return@get
        }

        call.respondResult(
            tvShowsService.getUserFavorites(
                userIdPrincipal.name.toInt(),
                args.limit,
                args.offset
            )
        )
    }
}