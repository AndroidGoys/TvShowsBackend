package com.limelist.tvHistory.services.models.releases

import kotlinx.serialization.Serializable

@Serializable
data class TvChannelReleases(
    val totalCount: Int,
    val releases: List<TvChannelShowRelease>
)