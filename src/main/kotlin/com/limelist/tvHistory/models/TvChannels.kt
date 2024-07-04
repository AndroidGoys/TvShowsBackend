package com.limelist.tvHistory.models

import com.limelist.tvHistory.models.shows.TvShowChannelModel
import kotlinx.serialization.Serializable

@Serializable
data class TvChannels(
    val leftAmount: Int,
    val channels: MutableList<TvShowChannelModel>
)