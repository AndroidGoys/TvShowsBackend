package com.limelist.slices.auth.services.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshToken (
    @SerialName("refreshToken")
    val value: String
)