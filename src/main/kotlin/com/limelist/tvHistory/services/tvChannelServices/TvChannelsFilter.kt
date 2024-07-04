package com.limelist.tvHistory.services.tvChannelServices

import kotlinx.serialization.Serializable

@Serializable
data class TvChannelsFilter (
    val name: String?
)