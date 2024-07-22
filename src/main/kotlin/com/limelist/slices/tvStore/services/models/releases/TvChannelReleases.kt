package com.limelist.slices.tvStore.services.models.releases

import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat

@Serializable
class TvChannelReleases<T>(
    var timeStart: Long,
    val timeStop: Long,
    val totalCount: Int,
    val releases: List<T>
) where T : TvRelease