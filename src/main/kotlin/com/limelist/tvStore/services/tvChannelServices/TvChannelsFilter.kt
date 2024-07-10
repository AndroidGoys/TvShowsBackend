package com.limelist.tvStore.services.tvChannelServices

import kotlinx.serialization.Serializable

@Serializable
data class TvChannelsFilter (
    val name: String?
)