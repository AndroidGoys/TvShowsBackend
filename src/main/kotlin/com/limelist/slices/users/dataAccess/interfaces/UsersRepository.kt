package com.limelist.slices.users.dataAccess.interfaces

import com.limelist.slices.users.services.internal.models.UserDetailsModel

interface UsersRepository {
    suspend fun add(data: UserDetailsModel) : UserDetailsModel
    suspend fun findByEmail(email: String): UserDetailsModel?
    suspend fun findById(userId: Int): UserDetailsModel?
    suspend fun setAvatarRoute(userId:Int, avatarRoute: String)
}