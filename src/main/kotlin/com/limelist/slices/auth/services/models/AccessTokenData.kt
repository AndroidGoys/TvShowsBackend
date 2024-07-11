package com.limelist.slices.auth.services.models

import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenData (
    val userId: Int,
    val permissions: UserPermissions
)