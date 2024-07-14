package com.limelist.slices.users

import com.limelist.slices.users.services.UserCreationInternalService

data class UserServices(
    val userCreationService: UserCreationInternalService
)