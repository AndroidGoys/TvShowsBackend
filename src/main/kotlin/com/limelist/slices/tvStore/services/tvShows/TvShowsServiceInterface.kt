package com.limelist.slices.tvStore.services.tvShows

import com.limelist.slices.shared.RequestResult
import com.limelist.slices.tvStore.services.models.channels.TvChannels
import com.limelist.slices.tvStore.services.models.shows.*

interface TvShowsServiceInterface {
    suspend fun getAllShows(
        limit: Int?,
        offset: Int?,
        filter: TvShowsFilter
    ): RequestResult<TvShows<TvShowPreviewModel>>

    suspend fun getShowDetails(
        id: Int
    ): RequestResult<TvShowDetailsModel>

    suspend fun getShowChannels(
        showId: Int,
        channelsLimit: Int?,
        channelsOffset: Int?,
        releasesLimit: Int?,
        releasesTimeStart: Long?,
        timeZone: Float?
    ): RequestResult<TvChannels<TvShowChannelModel>>
}