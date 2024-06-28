package com.limelist.tvHistory

import com.limelist.tvHistory.services.TvChannelsService
import com.limelist.tvHistory.services.TvShowsService

data class TvHistoryServices(
    val tvChannelsService: TvChannelsService,
    val tvShowsService: TvShowsService
)