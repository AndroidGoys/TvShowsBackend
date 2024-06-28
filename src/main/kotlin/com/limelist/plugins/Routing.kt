package com.limelist.plugins

import com.limelist.ApplicationServices
import io.ktor.server.application.*
import io.ktor.server.resources.Resources
import io.ktor.server.routing.*

import com.limelist.tvHistory.routing.useTvHistory;

fun Application.configureRouting(services: ApplicationServices) {
    install(Resources)
    routing {
        route("/api"){
            useTvHistory("/", services.tvHistoryServices)
        }
    }
}