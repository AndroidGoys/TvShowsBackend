package com.limelist.tvStore.services.models.shows

import com.limelist.tvStore.services.models.channels.TvChannel
import com.limelist.tvStore.services.models.releases.TvChannelReleases
import com.limelist.tvStore.services.models.releases.TvShowRelease
import kotlinx.serialization.Serializable

@Serializable
class TvShowChannelModel(
    override val id: Int,
    override val name: String,
    override val imageUrl: String,
    override val assessment: Float,
    val releases: TvChannelReleases<TvShowRelease>,
) : TvChannel