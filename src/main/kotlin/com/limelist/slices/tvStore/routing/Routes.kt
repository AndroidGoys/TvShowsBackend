package com.limelist.slices.tvStore.routing

import io.ktor.server.routing.*

import com.limelist.slices.tvStore.TvStoreServices
import com.limelist.slices.tvStore.routing.shows.shows

fun Route.useTvHistory(rootRoute: String, services: TvStoreServices) {
    route(rootRoute) {
        channels(services)
        shows(services)
    }
}