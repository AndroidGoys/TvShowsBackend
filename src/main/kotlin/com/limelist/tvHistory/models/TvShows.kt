package com.limelist.tvHistory.models

import com.limelist.tvHistory.models.shows.TvShow
import com.limelist.tvHistory.models.shows.TvShowPreviewModel
import kotlinx.serialization.Serializable

@Serializable
open class TvShows(
    val shows: Iterable<TvShowPreviewModel>
)