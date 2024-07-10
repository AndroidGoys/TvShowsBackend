package com.limelist.slices.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthConfig (
    val secret: String
)