package com.limelist.slices.users.services.internal

import com.limelist.slices.shared.RequestError
import com.limelist.slices.shared.RequestError.ErrorCode.Companion.LoginExistsError
import com.limelist.slices.shared.RequestResult
import com.limelist.slices.users.dataAccess.interfaces.UsersRepository
import com.limelist.slices.users.services.models.RegistrationData
import com.limelist.slices.users.services.models.UserDetailsModel
import com.limelist.slices.shared.getCurrentUnixUtc0TimeSeconds
import com.limelist.slices.users.services.models.UserPermissions

class DefaultUsersCreationInternalService(
    val users: UsersRepository
) : UsersCreationInternalService {

    override suspend fun createNewUser(
        registration: RegistrationData
    ): RequestResult<UserDetailsModel> {
        val user = users.findByEmail(registration.email)
        if (user != null) {
            return RequestResult.FailureResult(
                RequestError(
                    LoginExistsError,
                    "A user with this email already exists"
                )
            )
        }

        var userData = UserDetailsModel(
            -1,
            registration.email,
            registration.username,
            null,
            getCurrentUnixUtc0TimeSeconds(),
            UserPermissions.default
        )
        userData = users.add(userData)

        return RequestResult.SuccessResult(
            userData
        )
    }
}