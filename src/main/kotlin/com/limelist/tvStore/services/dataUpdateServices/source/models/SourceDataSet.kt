package com.limelist.tvStore.services.dataUpdateServices.source.models

import kotlinx.serialization.Serializable

@Serializable
data class SourceDataSet(
    val channels: List<SourceChannel>,
    val releases: List<SourceRelease>,
    val shows: List<SourceShow>,
)