package com.limelist.slices.users.services

import com.limelist.slices.shared.RequestResult
import com.limelist.slices.users.services.models.RegistrationData
import com.limelist.slices.users.services.models.UserData

class DefaultUsersInternalService : UserCreationInternalService {
    override fun createNewUser(registration: RegistrationData): RequestResult<UserData> {
        TODO("Not yet implemented")
    }
}