package com.limelist.slices.tvStore.services.tvShowServices

import com.limelist.slices.shared.RequestResult
import com.limelist.slices.tvStore.services.models.reviews.TvReviews
import com.limelist.slices.tvStore.services.models.channels.TvChannels
import com.limelist.slices.tvStore.services.models.shows.*

interface TvShowsServiceInterface {
    suspend fun getAllShows(
        limit: Int?,
        offset: Int?,
        filter: TvShowsFilter
    ): RequestResult<TvShows<TvShowPreviewModel>>

    suspend fun getShowDetails(
        id: Int
    ): RequestResult<TvShowDetailsModel>

    suspend fun getShowChannels(
        showId: Int,
        channelsLimit: Int?,
        channelsOffset: Int?,
        releasesLimit: Int?,
        releasesTimeStart: Long?,
        timeZone: Float?
    ): RequestResult<TvChannels<TvShowChannelModel>>

    suspend fun getUserFavorites(
        userId: Int,
        limit: Int?,
        offset: Int?
    ): RequestResult<TvShows<TvShowPreviewModel>>

    suspend fun addToFavorite(
        userId: Int,
        showId: Int,
    ): RequestResult<Unit>

    suspend fun getReviews(
        showId: Int,
        limit: Int?,
        timeStart: Long?,
        timeZone: Float?

    ): RequestResult<TvReviews>

    suspend fun addReview(
        showId: Int,
        userId: Int,
        assessment: Int,
        text: String,
    ): RequestResult<Unit>

}