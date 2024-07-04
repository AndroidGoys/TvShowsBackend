package com.limelist.tvHistory.services.tvShowServices

import com.limelist.tvHistory.services.models.channels.TvChannels
import com.limelist.tvHistory.services.models.shows.TvShowChannelModel
import com.limelist.tvHistory.services.models.shows.TvShowDetailsModel
import com.limelist.tvHistory.services.models.shows.TvShowPreviewModel
import com.limelist.tvHistory.services.models.shows.TvShows

interface TvShowsServiceInterface {
    suspend fun getAllShows(limit: Int?, offset: Int?, filter: TvShowsFilter): TvShows<TvShowPreviewModel>
    suspend fun getShowDetails(id: Int): TvShowDetailsModel?
    suspend fun getShowChannels(
        showId: Int,
        channelsLimit: Int?,
        channelsOffset: Int?,
        releasesLimit: Int?,
        releasesTimeStart: Long?,
        timeZone: Float?
    ): TvChannels<TvShowChannelModel>
}