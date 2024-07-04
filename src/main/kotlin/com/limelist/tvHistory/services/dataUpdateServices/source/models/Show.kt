package com.limelist.tvHistory.services.dataUpdateServices.source.models

import kotlinx.serialization.Serializable

@Serializable
data class Show(
    val id: Int,
    val name: String,
)