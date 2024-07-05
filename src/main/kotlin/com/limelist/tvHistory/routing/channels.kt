package com.limelist.tvHistory.routing

import com.limelist.shared.respondJson
import com.limelist.tvHistory.services.tvChannelServices.TvChannelsFilter
import com.limelist.tvHistory.services.tvChannelServices.TvChannelsService
import com.limelist.tvHistory.services.tvChannelServices.TvChannelsServiceInterface
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Route.channels(
    tvChannelsService: TvChannelsServiceInterface
) {
    route("channels"){
        get<AllChannels>(){ args ->
            val channels = tvChannelsService.getAllChannels(
                args.limit,
                args.offset,
                TvChannelsFilter(args.name)
            )
            call.respondJson(channels);
        }

        get<AllChannels.Channel>(){ args ->
            val channel = tvChannelsService.getChannelDetails(
                args.id
            )

            if (channel == null) {
                call.respond(HttpStatusCode.NotFound);
                return@get
            }

            call.respondJson(
                HttpStatusCode.Found,
                channel
            );
        }

        get<AllChannels.Channel.Releases>{ args ->
            val releases = tvChannelsService.getChannelReleases(
                args.channel.id,
                args.limit,
                args.timeStart,
                args.timeZone
            )
            call.respondJson(releases)
        }
    }
}

@Serializable
@Resource("/")
data class AllChannels(
    val limit: Int? = null,
    val offset: Int? = null,
    val name: String? = null
){
    @Serializable
    @Resource("{id}")
    data class Channel(
        val id: Int,
    ){
        @Serializable
        @Resource("releases")
        data class Releases(
            val channel: Channel,
            val limit: Int? = null,
            val timeStart: Long? = null,
            val timeZone: Float? = null
        )
    }
}