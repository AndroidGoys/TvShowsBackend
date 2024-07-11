package com.limelist.slices.tvStore.dataAccess.models.create

import kotlinx.serialization.Serializable

@Serializable
data class TvReleaseCreateModel(
    val channelId: Int,
    val showId: Int,
    val description: String,
    val timeStart:Long,
    val timeStop:Long
)