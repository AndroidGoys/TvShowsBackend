package com.limelist.tvHistory.models.channels

import com.limelist.tvHistory.models.TvShows
import com.limelist.tvHistory.models.shows.TvShow

class TvChannelShows(
    val timeSpanCount: Int,
    shows: Iterable<TvShow>
) : TvShows<TvChannelShowModel>(shows)