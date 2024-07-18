package com.limelist.slices.tvStore.routing

import com.limelist.slices.shared.receiveJson
import com.limelist.slices.shared.respondResult
import com.limelist.slices.tvStore.routing.models.AddReviewModel
import com.limelist.slices.tvStore.routing.models.OnlyIdModel
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
            call.respondResult(shows);
        }
        get<AllShows.Show>(){ args ->
            val show = tvShowsService.getShowDetails(args.id)
            call.respondResult(show)
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

            call.respondResult(channels)
        }

        get<AllShows.Show.Reviews>{ args ->
            val reviews = tvShowsService.getReviews(
                args.parent.id,
                args.limit,
                args.timeStart,
                args.timeZone
            )
            call.respondResult(reviews)
        }

        authenticate("access-auth"){
            post<AllShows.Show.Reviews> { args ->
                val userIdPrincipal = call.principal<UserIdPrincipal>()

                if (userIdPrincipal == null){
                    call.respond(HttpStatusCode.Unauthorized)
                    return@post
                }

                val review = call.receiveJson<AddReviewModel>()
                call.respondResult(
                    tvShowsService.addReview(
                        args.parent.id,
                        userIdPrincipal.name.toInt(),
                        review.assessment,
                        review.text
                    )
                )

            }

            get<AllShows.Favorites>{ args ->
                val userIdPrincipal = call.principal<UserIdPrincipal>()

                if (userIdPrincipal == null){
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }

                call.respondResult(
                    tvShowsService.getUserFavorites(
                        userIdPrincipal.name.toInt(),
                        args.limit,
                        args.offset
                    )
                )
            }

            post<AllShows.Favorites> {
                val userIdPrincipal = call.principal<UserIdPrincipal>()

                if (userIdPrincipal == null){
                    call.respond(HttpStatusCode.Unauthorized)
                    return@post
                }

                val show = call.receiveJson<OnlyIdModel>()

                val result = tvShowsService.addToFavorite(
                    userIdPrincipal.name.toInt(),
                    show.id
                )

                call.respondResult(result)
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
        class Reviews (
            val parent: Show,
            val limit: Int? = null,
            val timeStart: Long? = null,
            val timeZone: Float? = null
        )
    }
}