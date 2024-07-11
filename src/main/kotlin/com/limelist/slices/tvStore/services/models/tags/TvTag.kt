package com.limelist.slices.tvStore.services.models.tags

import kotlinx.serialization.Serializable

@Serializable
data class TvTag (
    val id: Int,
    val name: String,
    val belong: TvTagBelong
)