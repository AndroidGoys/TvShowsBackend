package com.limelist.slices.tvStore.services.tvShows

import com.limelist.slices.shared.RequestError
import com.limelist.slices.shared.RequestResult
import com.limelist.slices.tvStore.dataAccess.interfaces.TvShowsRepository
import com.limelist.slices.tvStore.services.models.channels.TvChannels
import com.limelist.slices.tvStore.services.models.shows.*
import io.ktor.http.*

class TvShowsService(
    private val tvShows: TvShowsRepository
) : TvShowsServiceInterface {

    private val showNotFoundResult = RequestResult.FailureResult(
        RequestError(
            RequestError.ErrorCode.NotFound,
            "Show not found"
        ),
        HttpStatusCode.NotFound
    )

    override suspend fun getAllShows(
        limit: Int?,
        offset: Int?,
        filter: TvShowsFilter
    ): RequestResult<TvShows<TvShowPreviewModel>> {
        val shows =
            if (filter.name != null) {
                tvShows.searchByName(
                    filter.name,
                    limit ?: -1,
                    offset ?: 0,
                )
            } else {
                tvShows.getAllShows(
                    limit ?: -1,
                    offset ?: 0,
                )
            }
        return RequestResult.SuccessResult(
            shows
        )
    }

    override suspend fun getShowDetails(
        id: Int
    ): RequestResult<TvShowDetailsModel> {
        val showDetails = tvShows.getShowDetails(id)

        if (showDetails == null) {
            return showNotFoundResult
        }

        return RequestResult.SuccessResult(
            showDetails
        )
    }

    override suspend fun getShowChannels(
        showId: Int,
        channelsLimit: Int?,
        channelsOffset: Int?,
        releasesLimit: Int?,
        releasesTimeStart: Long?,
        timeZone: Float?
    ): RequestResult<TvChannels<TvShowChannelModel>> {

        if (!tvShows.contains(showId)){
            return showNotFoundResult
        }

        val showChannels = tvShows.getShowChannels(
            showId,
            channelsLimit ?: -1,
            channelsOffset ?: 0,
            releasesLimit ?: -1,
            releasesTimeStart ?: 0,
        )

        return RequestResult.SuccessResult(showChannels)
    }
}