package com.limelist.tvHistory.models

import com.limelist.tvHistory.models.shows.TvShow
import kotlinx.serialization.Serializable

@Serializable
open class TvShows<T>(
    val shows: Iterable<TvShow>
) where T : TvShow