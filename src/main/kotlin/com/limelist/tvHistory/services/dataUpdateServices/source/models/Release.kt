package com.limelist.tvHistory.services.dataUpdateServices.source.models

import kotlinx.serialization.Serializable

@Serializable
data class Release(
    val channelId: Int,
    val showId: Int,
    val timestart: Int,
    val timestop: Int,
    val description: String,
)