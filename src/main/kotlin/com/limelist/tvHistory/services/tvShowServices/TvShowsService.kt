package com.limelist.tvHistory.services.tvShowServices

import com.limelist.tvHistory.services.models.shows.TvShows
import com.limelist.tvHistory.services.models.shows.TvShowDetailsModel
import com.limelist.tvHistory.dataAccess.interfaces.TvShowsRepository
import com.limelist.tvHistory.services.models.channels.TvChannels
import com.limelist.tvHistory.services.models.shows.TvShowChannelModel
import com.limelist.tvHistory.services.models.shows.TvShowPreviewModel

class TvShowsService(
    private val tvShows: TvShowsRepository
) : TvShowsServiceInterface {
//    suspend fun getAllShows(
//        limit: Int?,
//        timeStart: Long?
//    ): TvShows<TvShowPreviewModel> {
//        val timeStart = timeStart ?: 0
//        val limit = limit ?: -1
//
//        return  tvShows.getAllShows(
//            limit,
//            timeStart
//        )
//
//    }

    override suspend fun getAllShows(limit: Int?, offset: Int?, filter: TvShowsFilter): TvShows<TvShowPreviewModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getShowDetails(id: Int): TvShowDetailsModel? {
        TODO("Not yet implemented")
    }

//    suspend fun getShowDetails(
//        showId: Int
//    ): TvShowDetailsModel? {
//        return tvShows.getShowDetails(showId);
//    }

    override suspend fun getShowChannels(
        showId: Int,
        channelsLimit: Int?,
        channelsOffset: Int?,
        releasesLimit: Int?,
        releasesTimeStart: Long?
    ): TvChannels<TvShowChannelModel> {
        TODO("Not yet implemented")
    }

//    suspend fun getShowChannels(
//        showId: Int,
//        channelsLimit: Int?,
//        channelsOffset: Int?,
//        releasesTimeStart: Long?,
//        releasesLimit: Int?
//    ) : TvChannels<TvShowChannelModel> {
//        val channelsLimit = channelsLimit ?: -1
//        val channelsOffset = channelsOffset ?: 0
//        val releasesLimit = releasesLimit ?: -1
//        val releasesTimeStart = releasesTimeStart?: 0
//
//        return tvShows.getShowChannels(
//            showId,
//            channelsLimit,
//            channelsOffset,
//            releasesLimit,
//            releasesTimeStart
//        )
//    }
}