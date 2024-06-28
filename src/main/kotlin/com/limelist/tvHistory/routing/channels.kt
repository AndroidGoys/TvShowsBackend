package com.limelist.tvHistory.routing

import com.limelist.shared.respondJson
import com.limelist.tvHistory.services.TvChannelsService
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

fun Route.channels(tvChannelsService: TvChannelsService) {
    route("channels"){
        get<AllChannels>(){ args ->
            val channels = tvChannelsService.getAllChannels(
                args.limit,
                args.offset,
            )
            call.respondJson(channels);
        }

        get<AllChannels.Channel>(){ args ->
            val channel = tvChannelsService.getChannelDetails(
                args.id
            )

            if (channel != null) {
                call.respondJson(
                    HttpStatusCode.Found,
                    channel
                );
                return@get
            }

            call.respond(HttpStatusCode.NotFound);
        }

    }
}

@Serializable
@Resource("/")
data class AllChannels(
    val limit: Int? = null,
    val offset: Int? = null,
    val playlistLimit: Int? = null
){
    @Resource("{id}")
    data class Channel(
        val id: Int,
    )
}