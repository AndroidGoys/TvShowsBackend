package com.limelist

import com.limelist.slices.tvStore.TvStoreConfig
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationConfig(
    val tvStoreConfig: TvStoreConfig,
)