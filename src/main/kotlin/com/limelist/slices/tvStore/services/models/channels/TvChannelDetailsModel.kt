package com.limelist.slices.tvStore.services.models.channels

import com.limelist.slices.tvStore.services.models.tags.TvTagPreview
import kotlinx.serialization.Serializable

@Serializable
data class TvChannelDetailsModel(
    override val id: Int,
    override val name: String,
    override val imageUrl: String,
    override val assessment: Float,
    val description: String,
    val tags: List<TvTagPreview>,
    val viewUrls: List<String>
) : TvChannel