package com.limelist.slices.internal.users.models

import kotlinx.serialization.Serializable

@Serializable
data class RegistrationData(
    val username: String,
    val email: String,
    val password: String
)
