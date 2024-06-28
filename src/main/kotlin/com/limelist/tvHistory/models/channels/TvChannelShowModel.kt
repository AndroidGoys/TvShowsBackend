package com.limelist.tvHistory.models.channels

import com.limelist.tvHistory.models.AgeLimit
import com.limelist.tvHistory.models.TvTimeSpan
import com.limelist.tvHistory.models.shows.TvShow
import kotlinx.serialization.Serializable

@Serializable
class TvChannelShowModel(
    private val channelId: Int,
    override val id: Int,
    override val name: String,
    override val assessment: Float,
    override val ageLimit: AgeLimit,
    override val previewUrl: String,
    val dates: Iterable<TvTimeSpan>
) : TvShow {
}