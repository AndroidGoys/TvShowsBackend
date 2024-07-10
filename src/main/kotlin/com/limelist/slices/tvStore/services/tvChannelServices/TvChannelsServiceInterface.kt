package com.limelist.slices.tvStore.services.tvChannelServices

import com.limelist.slices.tvStore.services.models.channels.TvChannelDetailsModel
import com.limelist.slices.tvStore.services.models.channels.TvChannelPreviewModel
import com.limelist.slices.tvStore.services.models.channels.TvChannels
import com.limelist.slices.tvStore.services.models.releases.TvChannelReleases
import com.limelist.slices.tvStore.services.models.releases.TvChannelShowRelease

interface TvChannelsServiceInterface {
    suspend fun getAllChannels(
        limit: Int?,
        offset: Int?,
        filter: TvChannelsFilter
    ): TvChannels<TvChannelPreviewModel>

    suspend fun getChannelDetails(
        id: Int
    ): TvChannelDetailsModel?

    suspend fun getChannelReleases(
        channelId: Int,
        limit: Int?,
        timeStart: Long?,
        timeZone: Float?
    ): TvChannelReleases<TvChannelShowRelease>
}