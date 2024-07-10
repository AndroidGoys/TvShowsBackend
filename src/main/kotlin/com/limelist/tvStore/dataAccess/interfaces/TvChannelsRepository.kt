package com.limelist.tvStore.dataAccess.interfaces

import com.limelist.tvStore.dataAccess.models.TvChannelCreateModel
import com.limelist.tvStore.services.models.channels.TvChannels
import com.limelist.tvStore.services.models.channels.TvChannelDetailsModel
import com.limelist.tvStore.services.models.channels.TvChannelPreviewModel
import com.limelist.tvStore.services.models.releases.TvChannelReleases
import com.limelist.tvStore.services.models.releases.TvChannelShowRelease

interface TvChannelsRepository : TvRepository {
    suspend fun getAllChannels(limit: Int, offset: Int): TvChannels<TvChannelPreviewModel>
    suspend fun getChannelDetails(id: Int): TvChannelDetailsModel?
    suspend fun searchByName(name:String, limit: Int, offset: Int): TvChannels<TvChannelPreviewModel>
    suspend fun getChannelReleases(channelId: Int, limit: Int, timeStart: Long): TvChannelReleases<TvChannelShowRelease>
    suspend fun updateMany(channels: List<TvChannelCreateModel>)
}