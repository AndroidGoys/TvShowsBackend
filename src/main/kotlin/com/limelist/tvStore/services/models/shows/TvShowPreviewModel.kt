package com.limelist.tvStore.services.models.shows

import com.limelist.tvStore.services.models.AgeLimit
import kotlinx.serialization.Serializable

@Serializable
class TvShowPreviewModel(
    override val id: Int,
    override val name: String,
    override val assessment: Float,
    override val ageLimit: AgeLimit,
    override val previewUrl: String?
) : TvShow