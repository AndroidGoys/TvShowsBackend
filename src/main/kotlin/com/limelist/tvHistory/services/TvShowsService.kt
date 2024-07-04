package com.limelist.tvHistory.services

import com.limelist.tvHistory.services.models.shows.TvShows
import com.limelist.tvHistory.services.models.shows.TvShowDetailsModel
import com.limelist.tvHistory.dataAccess.interfaces.TvShowsRepository
import com.limelist.tvHistory.services.models.channels.TvChannels
import com.limelist.tvHistory.services.models.shows.TvShowChannelModel
import com.limelist.tvHistory.services.models.shows.TvShowPreviewModel

class TvShowsService(
    private val tvShows: TvShowsRepository
) {
    suspend fun getAllShows(
        limit: Int?,
        timeStart: Long?
    ): TvShows<TvShowPreviewModel> {
        val timeStart = timeStart ?: 0
        val limit = limit ?: -1

        return  tvShows.getAllShows(
            limit,
            timeStart
        )

    }

    suspend fun getShowDetails(
        showId: Int
    ): TvShowDetailsModel? {
        return tvShows.getShowDetails(showId);
    }

    suspend fun getShowChannels(
        showId: Int,
        channelsLimit: Int?,
        channelsOffset: Int?,
        releasesTimeStart: Long?,
        releasesLimit: Int?
    ) : TvChannels<TvShowChannelModel> {
        val channelsLimit = channelsLimit ?: -1
        val channelsOffset = channelsOffset ?: 0
        val releasesLimit = releasesLimit ?: -1
        val releasesTimeStart = releasesTimeStart?: 0

        return tvShows.getShowChannels(
            showId,
            channelsLimit,
            channelsOffset,
            releasesLimit,
            releasesTimeStart
        )
    }
}