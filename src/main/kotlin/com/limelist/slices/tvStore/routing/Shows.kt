package com.limelist.slices.tvStore.routing

import com.limelist.slices.shared.respondJson
import com.limelist.slices.shared.respondWithResult
import com.limelist.slices.tvStore.services.tvShowServices.TvShowsFilter
import com.limelist.slices.tvStore.services.tvShowServices.TvShowsServiceInterface
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun Route.shows(tvShowsService: TvShowsServiceInterface) {
    route("shows"){
        get<AllShows>(){ args ->
            val shows = tvShowsService.getAllShows(
                args.limit,
                args.offset,
                TvShowsFilter(args.name)
            )
            call.respondWithResult(shows);
        }
        get<AllShows.Show>(){ args ->
            val show = tvShowsService.getShowDetails(args.id)
            call.respondWithResult(show)
        }

        get<AllShows.Show.Channels>() { args ->
            val channels = tvShowsService.getShowChannels(
                args.show.id,
                args.channelsLimit,
                args.channelsOffset,
                args.releasesLimit,
                args.releasesStart,
                args.timeZone
            )

            call.respondWithResult(channels)
        }

        authenticate("access-auth"){
            get<AllShows.Show.Favorites>{
                val userIdPrincipal = call.principal<UserIdPrincipal>()

                if (userIdPrincipal == null){
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }

                call.respondJson(
                    tvShowsService.getUserFavorites(
                        userIdPrincipal.name.toInt()
                    )
                )
            }

            post<AllShows.Show.Favorites> {

            }
        }
    }
}


@Serializable
@Resource("/")
data class AllShows(
    val limit: Int? = null,
    val offset: Int? = null,
    val name: String? = null
){
    @Serializable
    @Resource("{id}")
    data class Show(
        val id: Int,
    ){
        @Serializable
        @Resource("channels")
        data class Channels(
            val show: Show,
            @SerialName("channels-limit")
            val channelsLimit: Int? = null,
            @SerialName("channels-offset")
            val channelsOffset: Int? = null,
            @SerialName("releases-limit")
            val releasesLimit: Int? = null,
            @SerialName("releases-start")
            val releasesStart: Long? = null,
            @SerialName("time-zone")
            val timeZone: Float? = null
        )

        @Serializable
        @Resource("favorites")
        class Favorites
    }
}