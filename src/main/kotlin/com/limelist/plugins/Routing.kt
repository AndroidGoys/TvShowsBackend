package com.limelist.plugins

import com.limelist.ApplicationServices
import com.limelist.slices.auth.routing.useAuth
import io.ktor.server.application.*
import io.ktor.server.resources.Resources
import io.ktor.server.routing.*

import com.limelist.slices.tvStore.routing.useTvHistory;
import com.limelist.slices.users.routing.useUsers

fun Application.configureRouting(services: ApplicationServices) {
    install(Resources)
    routing {
        route("/api"){
            useTvHistory("/", services.tvStoreServices)
            useAuth("/auth", services.authServices)
            useUsers("/", services.userServices)
        }
    }
}