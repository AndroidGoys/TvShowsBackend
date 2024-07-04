package com.limelist.tvHistory.services

import com.limelist.tvHistory.models.TvShows
import com.limelist.tvHistory.models.shows.TvShowDetailsModel
import com.limelist.tvHistory.dataAccess.interfaces.TvShowsRepository
import com.limelist.tvHistory.models.TvChannels

class TvShowsService(
    private val tvShows: TvShowsRepository
) {
    suspend fun getAllShows(
        limit: Int?,
        timeStart: Long?
    ): TvShows {
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
    ) : TvChannels {
        return tvShows.getShowChannels(showId)
    }
}