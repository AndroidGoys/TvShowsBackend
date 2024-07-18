package com.limelist.slices.tvStore.routing.models

import kotlinx.serialization.Serializable

@Serializable
data class AddReviewModel(
    val assessment: Int,
    val text: String
)