package com.limelist.tvHistory.dataAccess.interfaces

import com.limelist.tvHistory.models.channels.TvChannelDetailsModel
import com.limelist.tvHistory.models.channels.TvChannelPreviewModel
import com.limelist.tvHistory.models.channels.TvChannelShows

interface TvChannelsRepository : TvRepository {
    suspend fun getAllChannels(limit: Int, offset: Int): Iterable<TvChannelPreviewModel>
    suspend fun getChannelDetails(id: Int): TvChannelDetailsModel?
    suspend fun getChannelShows(channelId: Int): TvChannelShows
}