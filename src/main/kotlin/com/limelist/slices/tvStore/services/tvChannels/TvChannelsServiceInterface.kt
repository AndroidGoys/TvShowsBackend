package com.limelist.slices.tvStore.services.tvChannelServices

import com.limelist.slices.shared.RequestResult
import com.limelist.slices.tvStore.services.models.channels.TvChannelDetailsModel
import com.limelist.slices.tvStore.services.models.channels.TvChannelPreviewModel
import com.limelist.slices.tvStore.services.models.channels.TvChannels
import com.limelist.slices.tvStore.services.models.channels.TvChannelsFilter
import com.limelist.slices.tvStore.services.models.reviews.TvReviews
import com.limelist.slices.tvStore.services.models.releases.TvChannelReleases
import com.limelist.slices.tvStore.services.models.releases.TvChannelShowRelease

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

    suspend fun getReviews(
        channelId: Int,
        limit: Int?,
        timeStart: Long?,
    ) : RequestResult<TvReviews>
}