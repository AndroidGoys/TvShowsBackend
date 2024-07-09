package com.limelist

import com.limelist.tvHistory.TvHistoryConfig
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationConfig(
    val tvHistoryConfig: TvHistoryConfig,
)