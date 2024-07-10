package com.limelist.tvStore.services.dataUpdateServices

import kotlinx.serialization.Serializable

@Serializable
data class JsonSourceDataUpdateServiceConfig (
    val pathToDataSet: String? = null,
    val reloadDataSet: Boolean = false,
    val apiKey: String? = null,
    val folderId: String? = null,
    val findShowImages: Boolean = false,
)