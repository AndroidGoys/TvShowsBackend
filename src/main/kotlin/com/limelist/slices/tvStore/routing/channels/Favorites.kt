package com.limelist.slices.tvStore.routing.channels

import com.limelist.slices.shared.receiveJson
import com.limelist.slices.shared.respondResult
import com.limelist.slices.shared.withAuth
import com.limelist.slices.tvStore.routing.models.OnlyIdModel
import com.limelist.slices.tvStore.routing.shows.AllShows
import com.limelist.slices.tvStore.services.tvChannels.TvChannelsServiceInterface
import com.limelist.slices.tvStore.services.tvShows.TvShowsServiceInterface
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.delete
import io.ktor.server.routing.*

internal fun Route.favoriteChannels(
    tvChannelsService: TvChannelsServiceInterface
) {
    authenticate("access-auth") {
        addFavoriteChannel(tvChannelsService)
        getFavoriteChannels(tvChannelsService)
        removeFavoriteChannel(tvChannelsService)
    }
}


private fun Route.addFavoriteChannel(
    tvChannelsService: TvChannelsServiceInterface
) {
    post<AllChannels.Favorites> {
        call.withAuth { user ->
            call.receiveJson<OnlyIdModel>{ show ->
                val result = tvChannelsService.addToFavorite(
                    user.id,
                    show.id
                )

                call.respondResult(result)
            }
        }
    }
}

private fun Route.getFavoriteChannels(
    tvChannelsService: TvChannelsServiceInterface
) {
    get<AllChannels.Favorites>{ args ->
        call.withAuth { user ->
            call.respondResult(
                tvChannelsService.getUserFavorites(
                    user.id,
                    args.limit,
                    args.offset
                )
            )
        }
    }
}

private fun Route.removeFavoriteChannel(
    tvChannelsService: TvChannelsServiceInterface
) {
    delete<AllChannels.Favorites> {
        call.withAuth { user ->
            call.receiveJson<OnlyIdModel> { show ->
                call.respondResult(
                    tvChannelsService.removeUserFavorite(user.id, show.id)
                )
            }
        }
    }
}