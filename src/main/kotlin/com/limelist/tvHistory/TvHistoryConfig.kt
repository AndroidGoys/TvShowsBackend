package com.limelist.tvHistory

import com.limelist.tvHistory.services.dataUpdateServices.JsonSourceDataUpdateServiceConfig
import kotlinx.serialization.Serializable

@Serializable
data class TvHistoryConfig(
    val dataUpdateConfig: JsonSourceDataUpdateServiceConfig,
)