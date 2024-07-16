package com.limelist.slices.users.services.userDataServices

import com.limelist.slices.shared.RequestResult
import com.limelist.slices.users.services.models.UserDetailsModel
import com.limelist.slices.users.services.models.UserPreviewModel

class UsersDataService : UsersDataServiceInterface {
    override suspend fun getMe(
        userId: Int
    ): RequestResult<UserDetailsModel> {
        TODO("Not yet implemented")
    }

    override suspend fun findUserById(
        userId: Int
    ): RequestResult<UserPreviewModel> {
        TODO("Not yet implemented")
    }

}