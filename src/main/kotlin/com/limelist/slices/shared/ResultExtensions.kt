package com.limelist.slices.shared

fun <T> Result<T>.requestFailure(
    response: RequestExceptionResponse
): Result<T> {
    return Result.failure(
        RequestException(response)
    )
}