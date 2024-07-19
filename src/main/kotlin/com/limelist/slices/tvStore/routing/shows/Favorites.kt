package com.limelist.slices.tvStore.routing.shows

import com.limelist.slices.shared.receiveJson
import com.limelist.slices.shared.respondResult
import com.limelist.slices.shared.withAuth
import com.limelist.slices.tvStore.routing.models.OnlyIdModel
import com.limelist.slices.tvStore.services.tvShows.TvShowsServiceInterface
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.delete
import io.ktor.server.response.*
import io.ktor.server.routing.*

internal fun Route.favoriteShows(
    tvShowsService: TvShowsServiceInterface
) {
    authenticate("access-auth") {
        addFavoriteShow(tvShowsService)
        getFavoriteShows(tvShowsService)
        removeFavoriteShows(tvShowsService)
    }
}

private fun Route.addFavoriteShow(
    tvShowsService: TvShowsServiceInterface
) {
    post<AllShows.Favorites> {
        call.withAuth { user ->
            call.receiveJson<OnlyIdModel>{ show ->
                val result = tvShowsService.addToFavorite(
                    user.id,
                    show.id
                )

                call.respondResult(result)
            }
        }
    }
}

private fun Route.getFavoriteShows(tvShowsService: TvShowsServiceInterface) {
    get<AllShows.Favorites>{ args ->
        call.withAuth { user ->
            call.respondResult(
                tvShowsService.getUserFavorites(
                    user.id,
                    args.limit,
                    args.offset
                )
            )
        }
    }
}

private fun Route.removeFavoriteShows(tvShowsService: TvShowsServiceInterface) {
    delete<AllShows.Favorites> {
        call.withAuth { user ->
            call.receiveJson<OnlyIdModel> { show ->
                call.respondResult(
                    tvShowsService.removeUserFavorite(user.id, show.id)
                )
            }
        }
    }
}