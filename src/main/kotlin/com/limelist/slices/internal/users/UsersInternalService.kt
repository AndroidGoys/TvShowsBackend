package com.limelist.slices.internal.users

import com.limelist.slices.internal.users.models.*
import com.limelist.slices.shared.RequestResult

interface UsersInternalService {
    fun createNewUser(registration: RegistrationData): RequestResult<UserData>
}