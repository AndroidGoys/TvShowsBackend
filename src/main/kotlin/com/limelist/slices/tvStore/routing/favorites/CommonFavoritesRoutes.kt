package com.limelist.slices.tvStore.routing.favorites

import com.limelist.slices.shared.receiveJson
import com.limelist.slices.shared.respondResult
import com.limelist.slices.shared.withAuth
import com.limelist.slices.tvStore.routing.channels.AllChannels
import com.limelist.slices.tvStore.routing.models.OnlyIdModel
import com.limelist.slices.tvStore.services.favorites.FavoriteService
import com.limelist.slices.tvStore.services.models.channels.TvChannelPreviewModel
import com.limelist.slices.tvStore.services.models.channels.TvChannels
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.routing.*


internal inline fun <reified T> Route.useFavorite(
    favorites: FavoriteService<T>
) {
    authenticate("access-auth") {
        addFavorite(favorites)
        getFavorite(favorites)
        removeFavorite(favorites)
    }
}


private fun <T> Route.addFavorite(
    favorites: FavoriteService<T>
) {
    post("/favorites") {
        call.withAuth { user ->
            call.receiveJson<OnlyIdModel>{ parent ->
                val result = favorites.add(
                    user.id,
                    parent.id
                )

                call.respondResult(result)
            }
        }
    }
}

@Resource("/favorites")
data class Favorites(
    val limit: Int? = null,
    val offset: Int? = null
)

private inline fun <reified T> Route.getFavorite(
    favorites: FavoriteService<T>
) {
    get<Favorites>{ args ->
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

private fun <T> Route.removeFavorite(
    favorites: FavoriteService<T>
) {
    delete("/favorites") {
        call.withAuth { user ->
            call.receiveJson<OnlyIdModel> { parent ->
                call.respondResult(
                    favorites.remove(user.id, parent.id)
                )
            }
        }
    }
}
