package com.limelist.slices.users

import com.limelist.slices.users.services.userDataServices.UsersDataServiceInterface

data class UsersServices(
    val usersDataService: UsersDataServiceInterface
)