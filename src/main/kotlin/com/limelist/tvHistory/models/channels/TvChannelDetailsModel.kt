package com.limelist.tvHistory.models.channels

import kotlinx.serialization.Serializable

@Serializable
data class TvChannelDetailsModel(
    override val id: Int,
    override val name: String,
    override val imageUrl: String,
    val description: String,
    val viewUrls: Iterable<String>
) : TvChannel