package com.limelist.tvHistory.services.tvShowServices

import com.limelist.shared.normalizeUnixSecondsTime
import com.limelist.tvHistory.services.models.shows.TvShows
import com.limelist.tvHistory.services.models.shows.TvShowDetailsModel
import com.limelist.tvHistory.dataAccess.interfaces.TvShowsRepository
import com.limelist.tvHistory.services.models.channels.TvChannels
import com.limelist.tvHistory.services.models.shows.TvShowChannelModel
import com.limelist.tvHistory.services.models.shows.TvShowPreviewModel

class TvShowsService(
    private val tvShows: TvShowsRepository
) : TvShowsServiceInterface {

    override suspend fun getAllShows(
        limit: Int?,
        offset: Int?,
        filter: TvShowsFilter
    ): TvShows<TvShowPreviewModel> {
        if (filter.name != null)
            return tvShows.searchByName(
                filter.name,
                limit ?: -1,
                offset ?: 0,
            )

        return tvShows.getAllShows(
            limit ?: -1,
            offset ?: 0,
        )
    }

    override suspend fun getShowDetails(
        id: Int
    ): TvShowDetailsModel? {
        return tvShows.getShowDetails(id)
    }

    override suspend fun getShowChannels(
        showId: Int,
        channelsLimit: Int?,
        channelsOffset: Int?,
        releasesLimit: Int?,
        releasesTimeStart: Long?,
        timeZone: Float?
    ): TvChannels<TvShowChannelModel> {
        val normalizedReleasesTimeStart = releasesTimeStart
            ?.normalizeUnixSecondsTime(
                timeZone?: 0.0f
            )

        return tvShows.getShowChannels(
            showId,
            channelsLimit ?: -1,
            channelsOffset ?: 0,
            releasesLimit ?: -1,
            normalizedReleasesTimeStart ?: 0,
        )
    }

}