package com.limelist.slices.users.services.models

import kotlinx.serialization.Serializable

@Serializable
data class UserDetailsModel(
    val id: Int,
    val email: String,
    val nickname: String,
    val avatarUrl: String?,
    val registrationDateSeconds: Long,
    val permissions: UserPermissions
) {
    fun toPreview(): UserPreviewModel {
        return UserPreviewModel(
            id,
            nickname,
            avatarUrl
        )
    }
}