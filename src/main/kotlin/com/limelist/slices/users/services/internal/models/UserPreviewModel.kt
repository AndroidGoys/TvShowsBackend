package com.limelist.slices.users.services.internal.models

import kotlinx.serialization.Serializable

@Serializable
class UserPreviewModel (
    val id: Int,
    val nickname: String,
    val avatarUrl: String?,
)
