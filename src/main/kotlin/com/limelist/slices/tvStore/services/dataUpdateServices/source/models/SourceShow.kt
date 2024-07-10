package com.limelist.slices.tvStore.services.dataUpdateServices.source.models

import kotlinx.serialization.Serializable

@Serializable
data class SourceShow(
    val id: Int,
    val name: String,
)