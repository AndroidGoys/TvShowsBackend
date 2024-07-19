package com.limelist.slices.users.services.internal

import com.limelist.slices.shared.RequestResult
import com.limelist.slices.users.services.internal.models.RegistrationData
import com.limelist.slices.users.services.internal.models.UserDetailsModel

interface UsersRegistrationInternalService {
    suspend fun createNewUser(registration: RegistrationData): RequestResult<UserDetailsModel>
}