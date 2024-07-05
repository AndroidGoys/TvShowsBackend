package com.limelist.tvHistory.dataAccess.interfaces

import com.limelist.tvHistory.dataAccess.models.TvShowCreateModel
import com.limelist.tvHistory.services.models.channels.TvChannels
import com.limelist.tvHistory.services.models.shows.TvShows
import com.limelist.tvHistory.services.models.shows.TvShowChannelModel
import com.limelist.tvHistory.services.models.shows.TvShowDetailsModel
import com.limelist.tvHistory.services.models.shows.TvShowPreviewModel

interface TvShowsRepository : TvRepository {
    suspend fun getAllShows(limit: Int, offset: Int): TvShows<TvShowPreviewModel>
    suspend fun getShowDetails(id: Int): TvShowDetailsModel?
    suspend fun searchByName(name: String, limit: Int, offset: Int): TvShows<TvShowPreviewModel>
    suspend fun updateMany(shows: List<TvShowCreateModel>)
    suspend fun getShowChannels(
        showId: Int,
        channelsLimit: Int,
        channelsOffset: Int,
        releasesLimit: Int,
        releasesTimeStart: Long
    ): TvChannels<TvShowChannelModel>
}
