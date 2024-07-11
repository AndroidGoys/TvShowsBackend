package com.limelist.slices.shared

class RequestException(
    val response: RequestExceptionResponse
) : Exception() {
}