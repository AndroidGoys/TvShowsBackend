package com.limelist.slices.auth.services.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginData (
    val login: String,
    val password: String
)