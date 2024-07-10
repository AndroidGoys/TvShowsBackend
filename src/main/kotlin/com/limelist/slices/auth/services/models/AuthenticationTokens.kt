package com.limelist.slices.auth.services.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationTokens (
    val refreshToken: String,
    val accessToken: String,
)