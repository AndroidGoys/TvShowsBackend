package com.limelist.tvHistory.services.models.shows

import com.limelist.tvHistory.services.models.channels.TvChannel
import com.limelist.tvHistory.services.models.releases.TvChannelReleases
import kotlinx.serialization.Serializable

@Serializable
class TvShowChannelModel(
    override val id: Int,
    private val showId: Int,
    override val name: String,
    override val imageUrl: String,
    override val assessment: Float,
    val releases: TvChannelReleases,
) : TvChannel