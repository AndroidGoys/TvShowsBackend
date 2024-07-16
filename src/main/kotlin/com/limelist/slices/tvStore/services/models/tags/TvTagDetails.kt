package com.limelist.slices.tvStore.services.models.tags

import kotlinx.serialization.Serializable

@Serializable
data class TvTagDetails (
    val id: Int,
    val name: String,
    val belong: TvTagBelong
)