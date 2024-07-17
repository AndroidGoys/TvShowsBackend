package com.limelist.slices.tvStore.routing

import com.limelist.slices.shared.respondJson
import com.limelist.slices.shared.respondWithResult
import com.limelist.slices.tvStore.services.tvChannelServices.TvChannelsFilter
import com.limelist.slices.tvStore.services.tvChannelServices.TvChannelsServiceInterface
import io.ktor.http.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

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
            call.respondWithResult(channels);
        }

        get<AllChannels.Channel>(){ args ->
            val channel = tvChannelsService.getChannelDetails(
                args.id
            )
            call.respondWithResult(channel);
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
            @SerialName("time-start")
            val timeStart: Long? = null,
            @SerialName("time-zone")
            val timeZone: Float? = null
        )
    }
}