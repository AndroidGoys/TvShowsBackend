package com.limelist.tvHistory.services.models.channels

import kotlinx.serialization.Serializable

@Serializable
data class TvChannels<out T>(
    val totalCount: Int,
    val channels: List<T>
) where T: TvChannel