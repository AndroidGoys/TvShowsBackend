package com.limelist.slices.users.services

import com.limelist.slices.shared.RequestError
import com.limelist.slices.shared.RequestResult
import com.limelist.slices.users.dataAccess.interfaces.UsersRepository
import com.limelist.slices.users.services.models.RegistrationData
import com.limelist.slices.users.services.models.UserData
import com.limelist.slices.shared.RequestError.ErrorCode.LoginExistsError
import com.limelist.slices.shared.getCurrentUnixUtc0TimeSeconds
import com.limelist.slices.users.services.models.UserPermissions

class DefaultUsersInternalService(
    val users: UsersRepository
) : UserCreationInternalService {

    override suspend fun createNewUser(
        registration: RegistrationData
    ): RequestResult<UserData> {
        val user = users.findByEmail(registration.email)
        if (user != null) {
            return RequestResult.ErrorResult(
                RequestError(
                    LoginExistsError,
                    "A user with this email already exists"
                )
            )
        }

        var userData = UserData(
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