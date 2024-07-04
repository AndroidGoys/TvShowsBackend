package com.limelist.tvHistory.services.dataUpdateServices.source.models

import kotlinx.serialization.Serializable

@Serializable
data class DataSet(
    val channels: List<Channel>,
    val releases: List<Release>,
    val shows: List<Show>,
)