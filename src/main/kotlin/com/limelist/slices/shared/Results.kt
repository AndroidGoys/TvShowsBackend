package com.limelist.slices.shared

import io.ktor.http.*

sealed class RequestResult<out T>(
    val statusCode: HttpStatusCode,
){
    class SuccessResult<out T>(
        val data: T,
        statusCode: HttpStatusCode = HttpStatusCode.OK,
    ): RequestResult<T>(statusCode) {
        override fun unwrap(): T {
            return data
        }
    }

    class FailureResult(
        val error: RequestError,
        statusCode: HttpStatusCode = HttpStatusCode.BadRequest,
    ): RequestResult<Nothing>(statusCode) {
        override fun unwrap(): Nothing {
            throw Exception(error.errorMessage)
        }
    }

    abstract fun unwrap() : T
}

