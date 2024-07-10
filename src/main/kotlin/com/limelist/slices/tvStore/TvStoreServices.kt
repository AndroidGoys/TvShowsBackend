package com.limelist.slices.tvStore

import com.limelist.slices.shared.DbLifeCycle
import com.limelist.slices.shared.HostedService
import com.limelist.slices.tvStore.services.tvChannelServices.TvChannelsServiceInterface
import com.limelist.slices.tvStore.services.tvShowServices.TvShowsServiceInterface

data class TvStoreServices(
    val tvChannelsService: TvChannelsServiceInterface,
    val tvShowsService: TvShowsServiceInterface,
    val backgroundServices: Iterable<HostedService>,
    val databases: Iterable<DbLifeCycle>
) {
}