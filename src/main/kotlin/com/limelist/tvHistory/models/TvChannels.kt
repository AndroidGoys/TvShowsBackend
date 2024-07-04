package com.limelist.tvHistory.models

import com.limelist.tvHistory.models.channels.TvChannelPreviewModel
import kotlinx.serialization.Serializable

@Serializable
data class TvChannels(
    val leftAmount: Int,
    val channels: Iterable<TvChannelPreviewModel>
)