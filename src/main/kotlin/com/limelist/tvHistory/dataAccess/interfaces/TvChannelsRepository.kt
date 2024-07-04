package com.limelist.tvHistory.dataAccess.interfaces

import com.limelist.tvHistory.services.models.channels.TvChannels
import com.limelist.tvHistory.services.models.channels.TvChannelDetailsModel
import com.limelist.tvHistory.services.models.channels.TvChannelPreviewModel
import com.limelist.tvHistory.services.models.releases.TvChannelReleases

interface TvChannelsRepository : TvRepository {
    suspend fun getAllChannels(limit: Int, offset: Int): TvChannels<TvChannelPreviewModel>
    suspend fun getChannelDetails(id: Int): TvChannelDetailsModel?
    suspend fun getChannelReleases(channelId: Int, limit: Int, timeStart: Long): TvChannelReleases
    suspend fun updateMany(channels: List<TvChannelDetailsModel>)
}