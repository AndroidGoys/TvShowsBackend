package com.limelist.slices.tvStore.routing.shows

import com.limelist.slices.shared.respondResult
import com.limelist.slices.tvStore.TvStoreServices
import com.limelist.slices.tvStore.services.models.shows.TvShowsFilter
import com.limelist.slices.tvStore.services.tvShows.TvShowsServiceInterface
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun Route.shows(tvStoreServices: TvStoreServices) {
    route("shows"){
        getAll(tvStoreServices.tvShowsService)
        getShow(tvStoreServices.tvShowsService)
        getShowChannels(tvStoreServices.tvShowsService)
        favoriteShows(tvStoreServices.favoriteShowsService)
        showReviews(tvStoreServices.tvShowReviewsService)
    }
}

private fun Route.getShow(tvShowsService: TvShowsServiceInterface) {
    get<AllShows.Show>(){ args ->
        val show = tvShowsService.getShowDetails(args.id)
        call.respondResult(show)
    }
}

private fun Route.getShowChannels(tvShowsService: TvShowsServiceInterface) {
    get<AllShows.Show.Channels>() { args ->
        val channels = tvShowsService.getShowChannels(
            args.show.id,
            args.channelsLimit,
            args.channelsOffset,
            args.releasesLimit,
            args.releasesStart,
            args.timeZone
        )

        call.respondResult(channels)
    }
}

private fun Route.getAll(tvShowsService: TvShowsServiceInterface) {
    get<AllShows>(){ args ->
        val shows = tvShowsService.getAllShows(
            args.limit,
            args.offset,
            TvShowsFilter(args.name)
        )
        call.respondResult(shows);
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
    @Resource("favorites")
    class Favorites (
        val limit: Int? = null,
        val offset: Int? = null
    )

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
        @Resource("reviews")
        data class Reviews (
            val parent: Show,
            val limit: Int? = null,
            @SerialName("time-start")
            val timeStart: Long? = null,
            @SerialName("time-zone")
            val timeZone: Float? = null,
        ) {
            @Serializable
            @Resource("@my")
            class My (
                val parent: Reviews,
                @SerialName("time-zone")
                val timeZone: Float? = null
            ) {
                val show get() = parent.parent
            }
        }
    }
}