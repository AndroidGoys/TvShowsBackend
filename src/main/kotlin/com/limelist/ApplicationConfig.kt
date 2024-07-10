package com.limelist

import com.limelist.tvStore.TvStoreConfig
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationConfig(
    val tvStoreConfig: TvStoreConfig,
)