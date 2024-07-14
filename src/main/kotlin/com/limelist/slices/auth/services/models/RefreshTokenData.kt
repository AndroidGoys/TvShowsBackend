package com.limelist.slices.auth.services.models

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenData(
    val refreshTokenId: String,
    val expirationTime: Long
)
