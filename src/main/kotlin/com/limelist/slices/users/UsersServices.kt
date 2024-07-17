package com.limelist.slices.users

import com.limelist.slices.users.services.internal.UsersRegistrationInternalService
import com.limelist.slices.users.services.userDataServices.UsersDataServiceInterface

data class UsersServices(
    val usersRegistrationService: UsersRegistrationInternalService,
    val usersDataService: UsersDataServiceInterface
)