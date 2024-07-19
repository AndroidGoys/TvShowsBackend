package com.limelist.slices.auth.routing

import com.limelist.slices.auth.AuthServices
import com.limelist.slices.auth.services.TokenIssuanceService
import com.limelist.slices.auth.services.models.LoginData
import com.limelist.slices.auth.services.models.RefreshToken
import com.limelist.slices.shared.RequestResult
import com.limelist.slices.shared.receiveJson
import com.limelist.slices.shared.respondResult
import com.limelist.slices.users.services.internal.models.RegistrationData
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Route.login(service: TokenIssuanceService) {
    post("login") {
        call.receiveJson<LoginData> { data ->
            val response = service.login(
                data
            )
            call.respondResult(response)
        }
    }
}

fun Route.register(service: TokenIssuanceService) {
    post("register"){
        call.receiveJson<RegistrationData> { data ->
            val response = service.register(
                data
            )
            call.respondResult(response)
        }
    }
}

fun Route.refresh(service: TokenIssuanceService) {
    post("refresh"){
        call.receiveJson<RefreshToken>{refreshToken ->
            val validationResult = service.validate(refreshToken)

            if (validationResult is RequestResult.FailureResult) {
                call.respondResult<Unit>(validationResult)
                return@post
            }

            val token = validationResult.unwrap()

            val response = service.refresh(
                token
            )

            call.respondResult(response)
        }
    }
}

fun Route.useAuth(root: String, authServices: AuthServices) {
    route(root){
        refresh(authServices.tokenIssuanceService)
        login(authServices.tokenIssuanceService)
        register(authServices.tokenIssuanceService)
    }
}
