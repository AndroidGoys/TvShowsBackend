package com.limelist.slices.tvStore

import com.limelist.slices.shared.DbLifeCycle
import com.limelist.slices.shared.HostedService
import com.limelist.slices.tvStore.services.tvChannels.TvChannelsServiceInterface
import com.limelist.slices.tvStore.services.tvChannels.reviews.TvChannelReviewsService
import com.limelist.slices.tvStore.services.tvShows.TvShowsServiceInterface
import com.limelist.slices.tvStore.services.tvShows.reviews.TvShowReviewsService

data class TvStoreServices(
    val tvChannelsService: TvChannelsServiceInterface,
    val tvShowsService: TvShowsServiceInterface,
    val tvChannelReviewsService: TvChannelReviewsService,
    val tvShowReviewsService: TvShowReviewsService,
    val backgroundServices: Iterable<HostedService>,
    val databases: Iterable<DbLifeCycle>
) {
}