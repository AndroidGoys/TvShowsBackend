package com.limelist.tvHistory.services

import com.limelist.tvHistory.models.TvShows
import com.limelist.tvHistory.models.shows.TvShowDetailsModel
import com.limelist.tvHistory.dataAccess.interfaces.ТvShowsRepository
import com.limelist.tvHistory.models.TvChannels
import com.limelist.tvHistory.models.shows.TvShowChannelModel
import com.limelist.tvHistory.models.shows.TvShowPreviewModel

class TvShowsService(
    private val tvShows: ТvShowsRepository
) {
    suspend fun getAllShows(
        limit: Int?,
        timeStart: Long?
    ): TvShows<TvShowPreviewModel> {
        val timeStart = timeStart ?: 0
        val limit = limit ?: 0

        val shows = tvShows.getAllShows(limit, timeStart)
        val totalCount = tvShows.count();

        return TvShows(
            shows
        )
    }

    suspend fun getShowDetails(
        showId: Int
    ): TvShowDetailsModel? {
        return tvShows.getShowDetails(showId);
    }

    suspend fun getShowChannels(
        showId: Int
    ) : TvChannels<TvShowChannelModel> {
        return tvShows.getShowChannels(showId)
    }
}