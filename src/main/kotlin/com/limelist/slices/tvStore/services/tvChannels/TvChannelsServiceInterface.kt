package com.limelist.slices.tvStore.services.tvChannels

import com.limelist.slices.shared.RequestResult
import com.limelist.slices.tvStore.services.models.channels.TvChannelDetailsModel
import com.limelist.slices.tvStore.services.models.channels.TvChannelPreviewModel
import com.limelist.slices.tvStore.services.models.channels.TvChannels
import com.limelist.slices.tvStore.services.models.channels.TvChannelsFilter
import com.limelist.slices.tvStore.services.models.reviews.TvReviews
import com.limelist.slices.tvStore.services.models.releases.TvChannelReleases
import com.limelist.slices.tvStore.services.models.releases.TvChannelShowRelease
import com.limelist.slices.tvStore.services.models.shows.TvShowPreviewModel
import com.limelist.slices.tvStore.services.models.shows.TvShows

interface TvChannelsServiceInterface {
    suspend fun getAllChannels(
        limit: Int?,
        offset: Int?,
        filter: TvChannelsFilter
    ): RequestResult<TvChannels<TvChannelPreviewModel>>

    suspend fun getChannelDetails(
        id: Int
    ): RequestResult<TvChannelDetailsModel>

    suspend fun getChannelReleases(
        channelId: Int,
        limit: Int?,
        timeStart: Long?,
        timeZone: Float?
    ): RequestResult<TvChannelReleases<TvChannelShowRelease>>

    suspend fun getUserFavorites(
        userId: Int,
        limit: Int?,
        offset: Int?
    ): RequestResult<TvChannels<TvChannelPreviewModel>>

    suspend fun addToFavorite(
        userId: Int,
        showId: Int,
    ): RequestResult<Unit>

    suspend fun removeUserFavorite(
        userId: Int, showId: Int
    ): RequestResult<Unit>
}