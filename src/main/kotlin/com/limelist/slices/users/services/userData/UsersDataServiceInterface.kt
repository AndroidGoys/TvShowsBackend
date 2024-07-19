package com.limelist.slices.users.services.userData

import com.limelist.slices.shared.RequestResult
import com.limelist.slices.users.services.internal.models.UserDetailsModel
import com.limelist.slices.users.services.internal.models.UserPreviewModel

interface UsersDataServiceInterface {
    suspend fun getMe(userId: Int): RequestResult<UserDetailsModel>
    suspend fun findUserById(userId: Int): RequestResult<UserPreviewModel>
}