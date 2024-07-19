package com.limelist.slices.tvStore.routing.channels

import com.limelist.slices.shared.receiveJson
import com.limelist.slices.shared.respondResult
import com.limelist.slices.shared.withAuth
import com.limelist.slices.tvStore.routing.models.OnlyIdModel
import com.limelist.slices.tvStore.services.favorites.FavoriteService
import com.limelist.slices.tvStore.services.models.channels.TvChannelPreviewModel
import com.limelist.slices.tvStore.services.models.channels.TvChannels
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.delete
import io.ktor.server.routing.*

internal fun Route.favoriteChannels(
    favorites: FavoriteService<TvChannels<TvChannelPreviewModel>>
) {
    authenticate("access-auth") {
        addFavoriteChannel(favorites)
        getFavoriteChannels(favorites)
        removeFavoriteChannel(favorites)
    }
}


private fun Route.addFavoriteChannel(
    favorites: FavoriteService<TvChannels<TvChannelPreviewModel>>
) {
    post<AllChannels.Favorites> {
        call.withAuth { user ->
            call.receiveJson<OnlyIdModel>{ show ->
                val result = favorites.add(
                    user.id,
                    show.id
                )

                call.respondResult(result)
            }
        }
    }
}

private fun Route.getFavoriteChannels(
    favorites: FavoriteService<TvChannels<TvChannelPreviewModel>>
) {
    get<AllChannels.Favorites>{ args ->
        call.withAuth { user ->
            call.respondResult(
                favorites.getAll(
                    user.id,
                    args.limit,
                    args.offset
                )
            )
        }
    }
}

private fun Route.removeFavoriteChannel(
    favorites: FavoriteService<TvChannels<TvChannelPreviewModel>>
) {
    delete<AllChannels.Favorites> {
        call.withAuth { user ->
            call.receiveJson<OnlyIdModel> { show ->
                call.respondResult(
                    favorites.remove(user.id, show.id)
                )
            }
        }
    }
}