package com.limelist.tvStore.routing

import io.ktor.server.routing.*

import com.limelist.tvStore.TvStoreServices

fun Route.useTvHistory(rootRoute: String, services: TvStoreServices) {
    route(rootRoute) {
        channels(services.tvChannelsService)
        shows(services.tvShowsService)
    }
}