package com.limelist.slices.tvStore.routing

import com.limelist.slices.shared.respondJson
import com.limelist.slices.tvStore.services.tvShowServices.TvShowsFilter
import com.limelist.slices.tvStore.services.tvShowServices.TvShowsServiceInterface
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Route.shows(tvShowsService: TvShowsServiceInterface) {
    route("shows"){
        get<AllShows>(){ args ->
            val shows = tvShowsService.getAllShows(
                args.limit,
                args.offset,
                TvShowsFilter(args.name)
            )
            call.respondJson(shows);
        }
        get<AllShows.Show>(){ args ->
            val show = tvShowsService.getShowDetails(args.id)

            if (show == null) {
                call.respond(HttpStatusCode.NotFound);
                return@get
            }

            call.respondJson(
                HttpStatusCode.Found,
                show
            )
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

            call.respondJson(
                channels
            )
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
            val channelsLimit: Int? = null,
            val channelsOffset: Int? = null,
            val releasesLimit: Int? = null,
            val releasesStart: Long? = null,
            val timeZone: Float? = null
        )
    }
}