package com.limelist.tvHistory.routing

import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

import com.limelist.tvHistory.TvHistoryServices

fun Route.useTvHistory(rootRoute: String, services: TvHistoryServices) {
    route(rootRoute) {
        channels(services.tvChannelsService)
        shows(services.tvShowsService)
    }
}