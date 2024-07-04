package com.limelist.tvHistory.services

import com.limelist.tvHistory.dataAccess.interfaces.TvChannelsRepository
import com.limelist.tvHistory.services.models.channels.TvChannels
import com.limelist.tvHistory.services.models.channels.TvChannelDetailsModel
import com.limelist.tvHistory.services.models.channels.TvChannelPreviewModel
import com.limelist.tvHistory.services.models.releases.TvChannelReleases

class TvChannelsService (
    private val tvChannels: TvChannelsRepository
){
    suspend fun getAllChannels(
        limit: Int?,
        offset: Int?
    ): TvChannels<TvChannelPreviewModel> {
        val limit = limit ?: -1
        val offset = offset ?: 0

        return tvChannels.getAllChannels(limit, offset)
    }

    suspend fun getChannelDetails(
        channelId: Int
    ): TvChannelDetailsModel? {
        return tvChannels.getChannelDetails(channelId)
    }

    suspend fun getChannelReleases(
        channelId: Int,
        timeStart: Long?,
        limit: Int?
    ): TvChannelReleases {
        val timeStart = timeStart ?: 0;
        val limit = limit ?: -1;

        return tvChannels.getChannelReleases(channelId, limit, timeStart)
    }
}