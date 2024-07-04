package com.limelist.tvHistory.services.dataUpdateServices.source.models

import kotlinx.serialization.Serializable

@Serializable
data class Release(
    val channelId: Int,
    val showId: Int,
    val timeStart: Int,
    val timeStop: Int,
    val description: String,
)