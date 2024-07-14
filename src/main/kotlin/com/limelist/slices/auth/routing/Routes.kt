package com.limelist.slices.auth.routing

import com.limelist.slices.auth.AuthServices
import com.limelist.slices.auth.services.TokenIssuanceService
import com.limelist.slices.shared.receiveJson
import com.limelist.slices.shared.respondJson
import com.limelist.slices.shared.respondWithResult
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.login(service: TokenIssuanceService) {
    post("login") {
        val response = service.login(
            call.receive()
        )

        call.respondWithResult(response)
    }
}

fun Route.register(service: TokenIssuanceService) {
    post("register"){
        val response = service.refresh(
            call.receiveJson()
        )

        call.respondWithResult(response)
    }
}

fun Route.refresh(service: TokenIssuanceService) {
    post("refresh"){
        val response = service.refresh(
            call.receiveJson()
        )

        call.respondWithResult(response)
    }
}

fun Route.useAuth(root: String, authServices: AuthServices) {
    route(root){
        refresh(authServices.tokenIssuanceService)
        login(authServices.tokenIssuanceService)
        register(authServices.tokenIssuanceService)
    }
}
