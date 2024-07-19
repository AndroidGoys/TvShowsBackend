package com.limelist.slices.tvStore.services.tvChannels

import com.limelist.slices.shared.RequestError
import com.limelist.slices.shared.RequestResult
import com.limelist.slices.tvStore.dataAccess.interfaces.TvChannelsRepository
import com.limelist.slices.tvStore.services.models.channels.TvChannels
import com.limelist.slices.tvStore.services.models.channels.TvChannelDetailsModel
import com.limelist.slices.tvStore.services.models.channels.TvChannelPreviewModel
import com.limelist.slices.tvStore.services.models.releases.TvChannelReleases
import com.limelist.slices.shared.normalizeUnixSecondsTime
import com.limelist.slices.tvStore.dataAccess.sqlite.repositories.reviews.TvChannelReviewsSqliteRepository
import com.limelist.slices.tvStore.services.models.channels.TvChannelsFilter
import com.limelist.slices.tvStore.services.models.reviews.TvReviews
import com.limelist.slices.tvStore.services.models.releases.TvChannelShowRelease
import io.ktor.http.*

class TvChannelsService(
    private val tvChannels: TvChannelsRepository
): TvChannelsServiceInterface {

    val channelNotFoundResult = RequestResult.FailureResult(
        RequestError(
            RequestError.ErrorCode.NotFound,
            "Channel not found"
        ),
        HttpStatusCode.NotFound
    )

    override suspend fun getAllChannels(
        limit: Int?,
        offset: Int?,
        filter: TvChannelsFilter
    ): RequestResult<TvChannels<TvChannelPreviewModel>> {
        val channels =
            if (filter.name != null) {
                tvChannels.searchByName(
                    filter.name,
                    limit ?: -1,
                    offset ?: 0,
                )
            } else {
                tvChannels.getAllChannels(
                    limit ?: -1,
                    offset ?: 0,
                )
            }

        return RequestResult.SuccessResult(channels)
    }

    override suspend fun getChannelDetails(
        id: Int
    ): RequestResult<TvChannelDetailsModel> {
        val channel = tvChannels.getChannelDetails(id)

        if (channel == null){
            return channelNotFoundResult
        }

        return RequestResult.SuccessResult(channel)
    }

    override suspend fun getChannelReleases(
        channelId: Int,
        limit: Int?,
        timeStart: Long?,
        timeZone: Float?
    ): RequestResult<TvChannelReleases<TvChannelShowRelease>> {
        val normalizedTimeStart = timeStart?.normalizeUnixSecondsTime(
            timeZone ?: 0.0f
        )

        if(tvChannels.contains(channelId)) {
            return channelNotFoundResult
        }

        val releases = tvChannels.getChannelReleases(
            channelId,
            limit?: -1,
            normalizedTimeStart ?: 0,
        )

        return RequestResult.SuccessResult(releases)
    }

    override suspend fun getReviews(channelId: Int, limit: Int?, timeStart: Long?): RequestResult<TvReviews> {
        TODO("Not yet implemented")
    }
}