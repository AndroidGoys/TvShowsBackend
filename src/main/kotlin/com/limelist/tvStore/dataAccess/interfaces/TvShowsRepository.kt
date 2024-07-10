package com.limelist.tvStore.dataAccess.interfaces

import com.limelist.tvStore.dataAccess.models.TvShowCreateModel
import com.limelist.tvStore.services.models.channels.TvChannels
import com.limelist.tvStore.services.models.shows.TvShows
import com.limelist.tvStore.services.models.shows.TvShowChannelModel
import com.limelist.tvStore.services.models.shows.TvShowDetailsModel
import com.limelist.tvStore.services.models.shows.TvShowPreviewModel

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

    suspend fun getWithoutImageShows(limit: Int, offset: Int): TvShows<TvShowDetailsModel>
}
