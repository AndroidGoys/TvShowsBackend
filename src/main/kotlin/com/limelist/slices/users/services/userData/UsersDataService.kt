package com.limelist.slices.users.services.userData

import com.limelist.slices.shared.RequestError
import com.limelist.slices.shared.RequestResult
import com.limelist.slices.users.dataAccess.interfaces.UsersRepository
import com.limelist.slices.users.services.internal.models.UserDetailsModel
import com.limelist.slices.users.services.internal.models.UserPreviewModel
import com.limelist.slices.shared.RequestError.ErrorCode.Companion.NotFound
import io.ktor.http.*

class UsersDataService(
    val users: UsersRepository
) : UsersDataServiceInterface {

    private val userNotFoundResult = RequestResult.FailureResult(
        RequestError(NotFound, "user id not found"),
        HttpStatusCode.NotFound
    )

    override suspend fun getMe(
        userId: Int
    ): RequestResult<UserDetailsModel> {
        val user = users.findById(userId)

        if (user == null) {
            return userNotFoundResult
        }

        return RequestResult.SuccessResult(
            user
        )
    }

    override suspend fun findUserById(
        userId: Int
    ): RequestResult<UserPreviewModel> {
        val user = users.findById(userId)
        if (user == null) {
            return userNotFoundResult
        }

        return RequestResult.SuccessResult(
            user.toPreview()
        )
    }
}