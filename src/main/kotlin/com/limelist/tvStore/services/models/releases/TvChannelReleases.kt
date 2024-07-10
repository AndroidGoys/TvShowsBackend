package com.limelist.tvStore.services.models.releases

import kotlinx.serialization.Serializable

@Serializable
data class TvChannelReleases<T>(
    val timeStart: Long,
    val timeStop: Long,
    val totalCount: Int,
    val releases: List<T>
)where T : TvRelease