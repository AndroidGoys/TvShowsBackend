package com.limelist.tvHistory.services.models.shows

import com.limelist.tvHistory.services.models.channels.TvChannel
import com.limelist.tvHistory.services.models.releases.TvChannelReleases
import kotlinx.serialization.Serializable

@Serializable
class TvShowChannelModel(
    private val showId: Int,
    override val id: Int,
    override val name: String,
    override val imageUrl: String,
    val releases: TvChannelReleases
) : TvChannel