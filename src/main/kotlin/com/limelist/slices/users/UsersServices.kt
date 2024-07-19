package com.limelist.slices.users

import com.limelist.slices.users.services.avatars.AvatarsSharingService
import com.limelist.slices.users.services.internal.UsersRegistrationInternalService
import com.limelist.slices.users.services.userData.UsersDataServiceInterface

data class UsersServices(
    val usersRegistrationService: UsersRegistrationInternalService,
    val usersDataService: UsersDataServiceInterface,
    val avatarsSharingService: AvatarsSharingService
)