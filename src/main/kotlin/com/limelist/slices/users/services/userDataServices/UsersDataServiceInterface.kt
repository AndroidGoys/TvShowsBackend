package com.limelist.slices.users.services.userDataServices

import com.limelist.slices.shared.RequestResult
import com.limelist.slices.users.services.models.UserDetailsModel
import com.limelist.slices.users.services.models.UserPreviewModel

interface UsersDataServiceInterface {
    suspend fun getMe(userId: Int): RequestResult<UserDetailsModel>
    suspend fun findUserById(userId: Int): RequestResult<UserPreviewModel>
}