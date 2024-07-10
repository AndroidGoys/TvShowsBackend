package com.limelist.slices.tvStore.services.tvChannelServices

import com.limelist.slices.tvStore.dataAccess.interfaces.TvChannelsRepository
import com.limelist.slices.tvStore.services.models.channels.TvChannels
import com.limelist.slices.tvStore.services.models.channels.TvChannelDetailsModel
import com.limelist.slices.tvStore.services.models.channels.TvChannelPreviewModel
import com.limelist.slices.tvStore.services.models.releases.TvChannelReleases
import com.limelist.slices.shared.normalizeUnixSecondsTime
import com.limelist.slices.tvStore.services.models.releases.TvChannelShowRelease

class TvChannelsService (
    private val tvChannels: TvChannelsRepository
): TvChannelsServiceInterface {

    override suspend fun getAllChannels(
        limit: Int?,
        offset: Int?,
        filter: TvChannelsFilter
    ): TvChannels<TvChannelPreviewModel> {
        if (filter.name != null)
            return  tvChannels.searchByName(
                filter.name,
                limit?: -1,
                offset?: 0,
            )

        return tvChannels.getAllChannels(
            limit?: -1,
            offset?: 0,
        )
    }

    override suspend fun getChannelDetails(
        id: Int
    ): TvChannelDetailsModel? {
        return tvChannels.getChannelDetails(id)
    }

    override suspend fun getChannelReleases(
        channelId: Int,
        limit: Int?,
        timeStart: Long?,
        timeZone: Float?
    ): TvChannelReleases<TvChannelShowRelease> {
        val normalizedTimeStart = timeStart?.normalizeUnixSecondsTime(
            timeZone ?: 0.0f
        )

        return tvChannels.getChannelReleases(
            channelId,
            limit?: -1,
            normalizedTimeStart ?: 0,
        )
    }
}