package com.limelist.slices.auth.routing

import com.limelist.slices.auth.services.AuthService
import com.limelist.slices.shared.receiveJson
import com.limelist.slices.shared.respondJson
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.login(service: AuthService) {
    post("login") {
        val response = service.login(
            call.receive()
        )

        call.respondJson(response)
    }
}

//fun Route.signup(service: AuthService) {
//    post("signup") {
//        val response = service.signup(
//            call.receiveJson()
//
//        )
//        call.respondJson(response)
//    }
//}


fun Route.refresh(service: AuthService) {
    post("refresh"){
        val response = service.refresh(
            call.receiveJson()
        )
    }
}

//fun Route.updateLoginData(service: AuthService){
//    post("update") {
//        val body = call.receiveJson<UpdateLoginDataBody>()
//        val response = service.updateLoginData(
//            body.oldData,
//            body.newData
//        )
//        call.respondWithResult(response)
//    }
//}

fun Route.useAuth(root: String, authService: AuthService){
    route(root){
        refresh(authService)
        login(authService)
//        updateLoginData(authService)
    }
}
