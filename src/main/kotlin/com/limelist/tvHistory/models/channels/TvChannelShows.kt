package com.limelist.tvHistory.models.channels

import com.limelist.tvHistory.models.TvShows
import com.limelist.tvHistory.models.shows.TvShow
import com.limelist.tvHistory.models.shows.TvShowChannelModel
import kotlinx.serialization.Serializable

@Serializable
data class TvChannelShows(
    val timeSpanCount: Int,
    val shows: Iterable<TvChannelShowModel>
)