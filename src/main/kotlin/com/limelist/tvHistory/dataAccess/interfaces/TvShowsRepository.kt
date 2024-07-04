package com.limelist.tvHistory.dataAccess.interfaces

import com.limelist.tvHistory.services.models.channels.TvChannels
import com.limelist.tvHistory.services.models.shows.TvShows
import com.limelist.tvHistory.services.models.shows.TvShowChannelModel
import com.limelist.tvHistory.services.models.shows.TvShowDetailsModel
import com.limelist.tvHistory.services.models.shows.TvShowPreviewModel

interface TvShowsRepository : TvRepository {
    suspend fun getAllShows(limit: Int, timeStart: Long): TvShows<TvShowPreviewModel>
    suspend fun getShowDetails(id: Int): TvShowDetailsModel?
    suspend fun updateMany(shows: List<TvShowDetailsModel>)
    suspend fun getShowChannels(
        showId: Int,
        channelsLimit: Int,
        channelsOffset: Int,
        releasesLimit: Int,
        releasesTimeStart: Long
    ): TvChannels<TvShowChannelModel>
}
