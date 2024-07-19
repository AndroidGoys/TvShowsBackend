package com.limelist.slices.tvStore.services.models.reviews

import kotlinx.serialization.Serializable

@Serializable
data class TvReview (
    val userId: Int,
    val assessment: Int,
    val date: Long,
    val text: String
)