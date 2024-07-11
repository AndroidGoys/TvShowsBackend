package com.limelist.slices.auth.services.models

import kotlinx.serialization.Serializable

@Serializable
class UserPermissions(val flag: Int) {
     companion object {
         val isSuperUser = UserPermissions(0b1)
         val canComment = UserPermissions(0b10)
     }
}
