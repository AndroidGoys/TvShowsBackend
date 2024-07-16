package com.limelist.slices.tvStore.services.models.tags

import kotlinx.serialization.Serializable

@Serializable
data class TvTagPreview(
    val id: Int,
    val name: String,
)