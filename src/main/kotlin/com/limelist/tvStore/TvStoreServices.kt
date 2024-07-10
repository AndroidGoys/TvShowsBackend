package com.limelist.tvStore

import com.limelist.shared.DbLifeCycle
import com.limelist.shared.HostedService
import com.limelist.tvStore.services.tvChannelServices.TvChannelsServiceInterface
import com.limelist.tvStore.services.tvShowServices.TvShowsServiceInterface

data class TvStoreServices(
    val tvChannelsService: TvChannelsServiceInterface,
    val tvShowsService: TvShowsServiceInterface,
    val backgroundServices: Iterable<HostedService>,
    val databases: Iterable<DbLifeCycle>
) {
}