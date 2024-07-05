package com.limelist.tvHistory.services.tvChannelServices

import com.limelist.tvHistory.services.models.channels.TvChannelDetailsModel
import com.limelist.tvHistory.services.models.channels.TvChannelPreviewModel
import com.limelist.tvHistory.services.models.channels.TvChannels
import com.limelist.tvHistory.services.models.releases.TvChannelReleases
import com.limelist.tvHistory.services.models.releases.TvChannelShowRelease

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