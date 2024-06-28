package com.limelist.tvHistory.models

import com.limelist.tvHistory.models.channels.TvChannel
import com.limelist.tvHistory.models.channels.TvChannelPreviewModel
import kotlinx.serialization.Serializable

@Serializable
data class TvChannels<T>(
    val leftAmount: Int,
    val channels: Iterable<T>
) where T : TvChannel