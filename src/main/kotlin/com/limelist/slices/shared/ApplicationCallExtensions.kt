package com.limelist.slices.shared

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
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

suspend inline fun <reified T> ApplicationCall.respondWithResult(
    result: Result<T>,
){
    result.fold(
        { value -> this.respondJson(value) },
        {exception ->
            if (exception is RequestException) {
                val response = exception.response;
                this.respondJson(
                    HttpStatusCode.fromValue(response.statusCode),
                    exception.response
                )
            } else {
                throw exception
            }
        }
    )
}

suspend inline fun <reified T> ApplicationCall.respondJson(
    value: T,
) {
    this.respondJson(HttpStatusCode.OK, value)
}

@OptIn(ExperimentalSerializationApi::class)
suspend inline fun <reified T> ApplicationCall.receiveJson(): T {
    val stream = this.receiveStream()
    return Json.decodeFromStream(stream)
}