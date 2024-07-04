package com.limelist.tvHistory.dataAccess.interfaces

import com.limelist.tvHistory.models.TvChannels
import com.limelist.tvHistory.models.shows.TvShowDetailsModel
import com.limelist.tvHistory.models.shows.TvShowPreviewModel

interface TvShowsRepository : TvRepository {
    suspend fun getAllShows(limit: Int?, timeStart: Long): Iterable<TvShowPreviewModel>
    suspend fun getShowDetails(id: Int): TvShowDetailsModel?
    suspend fun getShowChannels(showId: Int) : TvChannels
}