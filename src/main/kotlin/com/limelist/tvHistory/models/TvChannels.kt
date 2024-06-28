package com.limelist.tvHistory.models

import com.limelist.tvHistory.models.channels.TvChannel
import com.limelist.tvHistory.models.channels.TvChannelPreviewModel
import kotlinx.serialization.Serializable
import kotlin.collections.List

@Serializable
data class TvChannels(
    val leftAmount: Int,
    val channels: List<TvChannelPreviewModel>
)