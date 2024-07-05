package com.limelist.tvHistory.services.models.channels

import kotlinx.serialization.Serializable

@Serializable
data class TvChannelDetailsModel(
    override val id: Int,
    override val name: String,
    override val imageUrl: String,
    override val assessment: Float,
    val description: String,
    val viewUrls: List<String>
) : TvChannel