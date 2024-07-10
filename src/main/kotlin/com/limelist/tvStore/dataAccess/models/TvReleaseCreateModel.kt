package com.limelist.tvStore.dataAccess.models

import kotlinx.serialization.Serializable

@Serializable
data class TvReleaseCreateModel(
    val channelId: Int,
    val showId: Int,
    val description: String,
    val timeStart:Long,
    val timeStop:Long
)