package com.limelist.slices.users.services

import com.limelist.slices.users.services.models.*
import com.limelist.slices.shared.RequestResult

interface UserCreationInternalService {
    fun createNewUser(registration: RegistrationData): RequestResult<UserData>
}