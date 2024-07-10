package com.limelist.tvStore

import com.limelist.tvStore.services.dataUpdateServices.JsonSourceDataUpdateServiceConfig
import kotlinx.serialization.Serializable

@Serializable
data class TvStoreConfig(
    val dataUpdateConfig: JsonSourceDataUpdateServiceConfig,
)