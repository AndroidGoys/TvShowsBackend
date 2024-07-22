package com.limelist.slices.tvStore.services.models.reviews

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TvReview (
    val userId: Int,
    val assessment: Int,
    var date: Long,
    val text: String
)