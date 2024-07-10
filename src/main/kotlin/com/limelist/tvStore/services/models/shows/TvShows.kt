package com.limelist.tvStore.services.models.shows

import kotlinx.serialization.Serializable

@Serializable
open class TvShows<out T>(
    val totalCount: Int,
    val shows: List<T>
) where T : TvShow