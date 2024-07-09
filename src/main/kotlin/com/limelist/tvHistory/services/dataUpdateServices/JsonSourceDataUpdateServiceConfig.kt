package com.limelist.tvHistory.services.dataUpdateServices

import kotlinx.serialization.Serializable

@Serializable
data class JsonSourceDataUpdateServiceConfig (
    val apiKey: String? = null,
    val folderId: String? = null,
    val findShowImages: Boolean = false,
    val reloadDataSet: Boolean = false,
)