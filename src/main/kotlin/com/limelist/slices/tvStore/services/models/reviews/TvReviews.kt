package com.limelist.slices.tvStore.services.models.reviews

import kotlinx.serialization.Serializable

@Serializable
data class TvReviews(
    val total: Int,
    val comments: List<TvReview>
)
