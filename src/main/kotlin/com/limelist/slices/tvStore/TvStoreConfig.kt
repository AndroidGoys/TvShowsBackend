package com.limelist.slices.tvStore

import com.limelist.slices.tvStore.services.dataUpdateServices.JsonSourceDataUpdateServiceConfig
import kotlinx.serialization.Serializable

@Serializable
data class TvStoreConfig(
    val dataUpdateConfig: JsonSourceDataUpdateServiceConfig,
)