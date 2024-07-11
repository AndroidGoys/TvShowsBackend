package com.limelist.slices.auth.routing.models

import com.limelist.slices.auth.services.models.LoginData
import kotlinx.serialization.Serializable

@Serializable
data class UpdateLoginDataBody (
    val newData: LoginData,
    val oldData: LoginData
)