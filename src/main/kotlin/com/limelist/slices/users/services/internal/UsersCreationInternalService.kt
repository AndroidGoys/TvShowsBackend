package com.limelist.slices.users.services.internal

import com.limelist.slices.users.services.models.*
import com.limelist.slices.shared.RequestResult

interface UsersCreationInternalService {
    suspend fun createNewUser(registration: RegistrationData): RequestResult<UserDetailsModel>
}