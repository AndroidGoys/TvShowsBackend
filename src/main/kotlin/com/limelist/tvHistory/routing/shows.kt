package com.limelist.tvHistory.routing

import com.limelist.tvHistory.services.TvShowsService
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Route.shows(tvShowsService: TvShowsService) {
    route("shows"){
        get<AllShows>(){ args ->
            val shows = tvShowsService.getAllShows(
                args.limit,
                args.timeStart,
            )
            call.respond(shows);
        }
        get<AllShows.Show>(){ args ->
            val show = tvShowsService.getShowDetails(args.id)

            if (show != null) {
                call.respond(
                    HttpStatusCode.Found,
                    show
                )
                return@get
            }

            call.respond(HttpStatusCode.NotFound);
        }

    }
}


@Serializable
@Resource("/")
data class AllShows(
    val limit: Int? = null,
    val timeStart: Long? = null
){
    @Resource("{id}")
    data class Show(
        val id: Int,
    )
}