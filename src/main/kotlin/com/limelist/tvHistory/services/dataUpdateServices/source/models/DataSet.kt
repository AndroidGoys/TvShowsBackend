package com.limelist.tvHistory.services.dataUpdateServices.source.models

import kotlinx.serialization.Serializable

@Serializable
data class DataSet(
    val channels: Iterable<Channel>,
    val releases: Iterable<Release>,
    val shows: Iterable<Show>,
)