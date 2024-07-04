package com.limelist.tvHistory.services.dataUpdateServices.source.models

import kotlinx.serialization.Serializable

@Serializable
data class Channel(
    val id: Int,
    val address: String,
    val name: String,
    val image: String,
    val description: String
)