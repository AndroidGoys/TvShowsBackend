package com.limelist.tvHistory.services

import com.limelist.tvHistory.dataAccess.interfaces.TvChannelsRepository
import com.limelist.tvHistory.models.TvChannels
import com.limelist.tvHistory.models.TvShows
import com.limelist.tvHistory.models.channels.TvChannelDetailsModel
import com.limelist.tvHistory.models.channels.TvChannelPreviewModel
import com.limelist.tvHistory.models.channels.TvChannelShows

class TvChannelsService (
    private val tvChannels: TvChannelsRepository
){
    suspend fun getAllChannels(
        limit: Int?,
        offset: Int?
    ):TvChannels {
        val limit = limit ?: 0
        val offset = offset ?: 0

        val channels = tvChannels.getAllChannels(limit, offset)
        val totalCount = tvChannels.count()

        val leftAmount = Math.max(
            totalCount - channels.count() - offset,
            0
        )

        return TvChannels(
            leftAmount,
            channels
        )
    }

    suspend fun getChannelDetails(
        channelId: Int
    ): TvChannelDetailsModel? {
        return tvChannels.getChannelDetails(channelId)
    }

    suspend fun getChannelShows(
        channelId: Int
    ): TvChannelShows {
        return tvChannels.getChannelShows(channelId)
    }
}