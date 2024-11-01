package com.limelist.slices.auth.services.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AccessToken(
    @SerialName("accessToken")
    val value: String
)