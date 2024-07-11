package com.limelist.slices.tvStore.services.models.shows

import com.limelist.slices.tvStore.services.models.AgeLimit
import kotlinx.serialization.Serializable

@Serializable
class TvShowDetailsModel(
    override val id: Int,
    override val name: String,
    override val assessment: Float,
    override val ageLimit: AgeLimit,
    override val previewUrl: String?,
    val frames: List<String>,
    val description: String
) : TvShow