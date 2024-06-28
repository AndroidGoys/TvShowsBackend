package com.limelist.tvHistory.models.shows

import com.limelist.tvHistory.models.TvTimeSpan
import com.limelist.tvHistory.models.channels.TvChannel
import kotlinx.serialization.Serializable

@Serializable
class TvShowChannelModel(
    private val showId: Int,
    override val id: Int,
    override val name: String,
    override val imageUrl: String,
    val dates: Iterable<TvTimeSpan>
) : TvChannel