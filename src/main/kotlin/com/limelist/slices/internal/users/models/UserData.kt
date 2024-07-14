package com.limelist.slices.internal.users.models

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val id: Int,
    val email: String,
    val username: String,
    val avatarUrl: String?,
    val registrationData: Long,
    val permissions: UserPermissions
)