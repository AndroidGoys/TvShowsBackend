package com.limelist.tvHistory.services.models.channels

import kotlinx.serialization.Serializable

@Serializable
data class TvChannelPreviewModel(
    override val id: Int,
    override val name: String,
    override val imageUrl: String,
    override val assessment: Float,
) : TvChannel