package com.limelist.slices.shared

import kotlinx.serialization.Serializable

@Serializable
data class RequestExceptionResponse (
    val statusCode: Int,
    val message: String
)
