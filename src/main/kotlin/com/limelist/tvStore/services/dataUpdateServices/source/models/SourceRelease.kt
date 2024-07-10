package com.limelist.tvStore.services.dataUpdateServices.source.models

import kotlinx.serialization.Serializable

@Serializable
data class SourceRelease(
    val channelId: Int,
    val showId: Int,
    val timestart: Long,
    val timestop: Long,
    val description: String,
)