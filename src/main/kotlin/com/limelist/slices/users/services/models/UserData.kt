package com.limelist.slices.users.services.models

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val id: Int,
    val email: String,
    val username: String,
    val avatarUrl: String?,
    val registrationDateSeconds: Long,
    val permissions: UserPermissions
)