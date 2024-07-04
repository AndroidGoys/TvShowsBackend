package com.limelist.tvHistory.services.models.releases

import kotlinx.serialization.Serializable

@Serializable
data class TvChannelReleases(
    val timeStart: Long,
    val timeStop: Long,
    val totalCount: Int,
    val releases: List<TvChannelShowRelease>
)