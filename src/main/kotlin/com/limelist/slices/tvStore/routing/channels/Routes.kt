package com.limelist.slices.tvStore.routing.channels

import com.limelist.slices.shared.respondJson
import com.limelist.slices.shared.respondResult
import com.limelist.slices.tvStore.TvStoreServices
import com.limelist.slices.tvStore.services.models.channels.TvChannelsFilter
import com.limelist.slices.tvStore.services.tvChannels.TvChannelsServiceInterface
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

fun Route.channels(
    tvStoreServices: TvStoreServices
) {
    route("channels"){
        getAll(tvStoreServices.tvChannelsService)
        getChannel(tvStoreServices.tvChannelsService)
        getChannelReleases(tvStoreServices.tvChannelsService)
        favoriteChannels(tvStoreServices.favoriteChannelsService)
        channelReviews(tvStoreServices.tvChannelReviewsService)
    }
}

private fun Route.getAll(tvChannelsService: TvChannelsServiceInterface) {
    get<AllChannels>(){ args ->
        val channels = tvChannelsService.getAllChannels(
            args.limit,
            args.offset,
            TvChannelsFilter(args.name)
        )
        call.respondResult(channels);
    }
}

private fun Route.getChannel(tvChannelsService: TvChannelsServiceInterface) {
    get<AllChannels.Channel>(){ args ->
        val channel = tvChannelsService.getChannelDetails(
            args.id
        )
        call.respondResult(channel);
    }
}

private fun Route.getChannelReleases(tvChannelsService: TvChannelsServiceInterface) {
    get<AllChannels.Channel.Releases>{ args ->
        val releases = tvChannelsService.getChannelReleases(
            args.channel.id,
            args.limit,
            args.timeStart,
            args.timeZone
        )
        call.respondResult(releases)
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

        @Serializable
        @Resource("reviews")
        class Reviews (
            val parent: Channel,
            val limit: Int? = null,
            @SerialName("time-start")
            val timeStart: Long? = null,
            @SerialName("time-zone")
            val timeZone: Float? = null,
            val assessment: Int? = null
        ) {

            @Serializable
            @Resource("reviews/@my")
            class My (
                val parent: Channel,
                @SerialName("time-zone")
                val timeZone: Float? = null
            )
        }
    }

    @Serializable
    @Resource("favorites")
    class Favorites (
        val limit: Int? = null,
        val offset: Int? = null
    )
}