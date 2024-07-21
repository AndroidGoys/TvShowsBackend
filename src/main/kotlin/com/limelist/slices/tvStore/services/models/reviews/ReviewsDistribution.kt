package com.limelist.slices.tvStore.services.models.reviews

import kotlinx.serialization.Serializable


@Serializable
data class ReviewsDistribution(
    val distribution: Map<Int, Int>
)
