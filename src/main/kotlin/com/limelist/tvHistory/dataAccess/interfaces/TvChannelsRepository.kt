package com.limelist.tvHistory.dataAccess.interfaces

import com.limelist.tvHistory.dataAccess.models.TvChannelCreateModel
import com.limelist.tvHistory.services.models.channels.TvChannel
import com.limelist.tvHistory.services.models.channels.TvChannels
import com.limelist.tvHistory.services.models.channels.TvChannelDetailsModel
import com.limelist.tvHistory.services.models.channels.TvChannelPreviewModel
import com.limelist.tvHistory.services.models.releases.TvChannelReleases
import com.limelist.tvHistory.services.models.releases.TvChannelShowRelease
import com.limelist.tvHistory.services.models.shows.TvShowPreviewModel

interface TvChannelsRepository : TvRepository {
    suspend fun getAllChannels(limit: Int, offset: Int): TvChannels<TvChannelPreviewModel>
    suspend fun getChannelDetails(id: Int): TvChannelDetailsModel?
    suspend fun searchByName(name:String, limit: Int, offset: Int): TvChannels<TvChannelPreviewModel>
    suspend fun getChannelReleases(channelId: Int, limit: Int, timeStart: Long): TvChannelReleases<TvChannelShowRelease>
    suspend fun updateMany(channels: List<TvChannelCreateModel>)
}