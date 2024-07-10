package com.limelist.slices.tvStore.routing

import io.ktor.server.routing.*

import com.limelist.slices.tvStore.TvStoreServices

fun Route.useTvHistory(rootRoute: String, services: TvStoreServices) {
    route(rootRoute) {
        channels(services.tvChannelsService)
        shows(services.tvShowsService)
    }
}