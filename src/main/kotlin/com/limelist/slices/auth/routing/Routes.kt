package com.limelist.slices.auth.routing

import com.limelist.slices.auth.services.AuthService
import com.limelist.slices.shared.respondJson
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.login(service: AuthService) {
    post("login") {
        val response = service.login(
            call.receive()
        )

        call.respondJson(response)
    }
}

fun Route.signup(service: AuthService) {
    post("signup") {
        val response = service.signup(
            call.receive()
        )
        call.respondJson(response)
    }
}


fun Route.refresh(service: AuthService) {
    post("refresh"){
        val response = service.refresh(
            call.receive()
        )

    }
}

fun Route.updateLoginData(service: AuthService){
    put {

    }
}

fun Route.useAuth(root: String, authService: AuthService){
    route(root){
        refresh(authService)
        login(authService)
        updateLoginData(authService)
    }
}
