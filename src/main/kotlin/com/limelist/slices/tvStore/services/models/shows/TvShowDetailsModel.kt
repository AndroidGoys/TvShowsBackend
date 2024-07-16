package com.limelist.slices.tvStore.services.models.shows

import com.limelist.slices.tvStore.services.models.AgeLimit
import com.limelist.slices.tvStore.services.models.tags.TvTagPreview
import kotlinx.serialization.Serializable

@Serializable
class TvShowDetailsModel(
    override val id: Int,
    override val name: String,
    override val assessment: Float,
    override val ageLimit: AgeLimit,
    override val previewUrl: String?,
    val frames: List<String>,
    val tags: List<TvTagPreview>,
    val description: String
) : TvShow