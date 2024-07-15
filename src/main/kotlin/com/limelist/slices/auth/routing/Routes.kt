package com.limelist.slices.auth.routing

import com.limelist.slices.auth.AuthServices
import com.limelist.slices.auth.services.TokenIssuanceService
import com.limelist.slices.auth.services.models.RefreshToken
import com.limelist.slices.shared.RequestResult
import com.limelist.slices.shared.receiveJson
import com.limelist.slices.shared.respondWithResult
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.login(service: TokenIssuanceService) {
    post("login") {
        val response = service.login(
            call.receiveJson()
        )

        call.respondWithResult(response)
    }
}

fun Route.register(service: TokenIssuanceService) {
    post("register"){
        val response = service.register(
            call.receiveJson()
        )
        call.respondWithResult(response)
    }
}

fun Route.refresh(service: TokenIssuanceService) {
    post("refresh"){
        val refreshToken = call.receiveJson<RefreshToken>()

        val validationResult = service.validate(refreshToken)

        if (validationResult is RequestResult.FailureResult) {
            call.respondWithResult<Unit>(validationResult)
            return@post
        }

        val token = validationResult.unwrap()

        val response = service.refresh(
            token
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
