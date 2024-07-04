package com.limelist.tvHistory

import com.limelist.shared.HostedService
import com.limelist.tvHistory.services.tvChannelServices.TvChannelsService
import com.limelist.tvHistory.services.tvChannelServices.TvChannelsServiceInterface
import com.limelist.tvHistory.services.tvShowServices.TvShowsService
import com.limelist.tvHistory.services.tvShowServices.TvShowsServiceInterface

data class TvHistoryServices(
    val tvChannelsService: TvChannelsServiceInterface,
    val tvShowsService: TvShowsServiceInterface,
    val backgroundServices: Iterable<HostedService>
) {
}