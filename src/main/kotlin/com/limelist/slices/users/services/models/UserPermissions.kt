package com.limelist.slices.users.services.models

import kotlinx.serialization.Serializable

@Serializable
class UserPermissions(
    val flag: Long
) {
    companion object{
        val canChangeUsersPermissions = UserPermissions(0b1)
        val canChangeUsersAccountData = UserPermissions(0b10)

        val canEditChannelsInfo = UserPermissions(0b100)
        val canEditShowsInfo = UserPermissions(0b1000)
        val canEditReleasesInfo = UserPermissions(0b10000)

        val canSendComments = UserPermissions(0b100000)

        val administration = UserPermissions(Long.MAX_VALUE)
        val default = canSendComments
    }
}
