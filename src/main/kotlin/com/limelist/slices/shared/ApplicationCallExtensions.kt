package com.limelist.slices.shared

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

suspend inline fun <reified T> ApplicationCall.respondJson(
    statusCode: HttpStatusCode,
    value: T,
) {
    val str = Json.encodeToString(value)
    this.respondText(
        str,
        ContentType("application", "json"),
        statusCode
    )
}

suspend inline fun <reified T> ApplicationCall.respondResult(
    result: RequestResult<T>,
){
    when(result) {
        is RequestResult.SuccessResult<T>
            -> if (result.data is Unit)
                    this.respond(result.statusCode)
                else
                    this.respondJson(result.statusCode, result.data)

        is RequestResult.FailureResult
            -> this.respondJson(result.statusCode, result.error)
    }
}

suspend inline fun <reified T> ApplicationCall.respondJson(
    value: T,
) {
    this.respondJson(HttpStatusCode.OK, value)
}

@OptIn(ExperimentalSerializationApi::class)
suspend inline fun <reified T> ApplicationCall.receiveJson(
    block: (T) -> Unit
) {
    val stream = this.receiveStream()
    try {
        val result = Json.decodeFromStream<T>(stream)
        block(result)
    }
    catch (e: SerializationException) {
        respondJson(HttpStatusCode.BadRequest)
    }
}


@OptIn(ExperimentalSerializationApi::class)
suspend inline fun ApplicationCall.withAuth(
    block: (UserAuthData) -> Unit
) {
    val userIdPrincipal = principal<UserIdPrincipal>()

    if (userIdPrincipal == null){
        respond(HttpStatusCode.Unauthorized)
        return
    }

    block(
        UserAuthData(
            userIdPrincipal.name.toInt()
        )
    )
}


