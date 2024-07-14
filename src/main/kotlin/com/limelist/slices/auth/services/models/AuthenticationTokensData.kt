package com.limelist.slices.auth.services.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationTokensData(
    val refreshTokenId: String,
    val userId: Int,
)
