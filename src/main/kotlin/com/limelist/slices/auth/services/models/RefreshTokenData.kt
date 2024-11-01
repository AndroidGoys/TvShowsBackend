package com.limelist.slices.auth.services.models

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenData(
    val userId: Int,
    val lastUpdate: Long,
    val expirationTime: Long
)
