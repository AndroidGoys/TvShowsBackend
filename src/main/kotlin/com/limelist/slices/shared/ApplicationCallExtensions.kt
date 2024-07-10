package com.limelist.slices.shared

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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

suspend inline fun <reified T> ApplicationCall.respondJson(
    value: T,
) {
    this.respondJson(HttpStatusCode.OK, value)
}