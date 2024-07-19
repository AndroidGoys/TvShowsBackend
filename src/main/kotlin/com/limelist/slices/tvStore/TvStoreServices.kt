package com.limelist.slices.tvStore

import com.limelist.slices.shared.DbLifeCycle
import com.limelist.slices.shared.HostedService
import com.limelist.slices.tvStore.services.favorites.FavoriteService
import com.limelist.slices.tvStore.services.models.channels.TvChannelPreviewModel
import com.limelist.slices.tvStore.services.models.channels.TvChannels
import com.limelist.slices.tvStore.services.models.shows.TvShowPreviewModel
import com.limelist.slices.tvStore.services.models.shows.TvShows
import com.limelist.slices.tvStore.services.tvChannels.TvChannelsServiceInterface
import com.limelist.slices.tvStore.services.tvChannels.reviews.TvChannelReviewsService
import com.limelist.slices.tvStore.services.tvShows.TvShowsServiceInterface
import com.limelist.slices.tvStore.services.tvShows.reviews.TvShowReviewsService

data class TvStoreServices(
    val tvChannelsService: TvChannelsServiceInterface,
    val tvShowsService: TvShowsServiceInterface,
    val tvChannelReviewsService: TvChannelReviewsService,
    val tvShowReviewsService: TvShowReviewsService,
    val favoriteShowsService: FavoriteService<TvShows<TvShowPreviewModel>>,
    val favoriteChannelsService: FavoriteService<TvChannels<TvChannelPreviewModel>>,
    val backgroundServices: Iterable<HostedService>,
    val databases: Iterable<DbLifeCycle>
) {
}