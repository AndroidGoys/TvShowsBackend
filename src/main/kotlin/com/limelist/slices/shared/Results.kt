package com.limelist.slices.shared

import io.ktor.http.*

sealed class RequestResult<out T>(
    val statusCode: HttpStatusCode,
){
    class SuccessResult<out T>(
        val data: T,
        statusCode: HttpStatusCode = HttpStatusCode.OK,
    ): RequestResult<T>(statusCode)

    class ErrorResult(
        val error: RequestError,
        statusCode: HttpStatusCode = HttpStatusCode.BadRequest,
    ): RequestResult<Nothing>(statusCode)


}

