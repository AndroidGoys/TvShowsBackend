package com.limelist.slices.tvStore.services.models.releases

import com.limelist.slices.tvStore.services.models.AgeLimit
import kotlinx.serialization.Serializable

@Serializable
class TvChannelShowRelease(
    val showId: Int,
    val showName: String,
    val showAssessment: Float,
    val showAgeLimit: AgeLimit,
    val previewUrl: String?,
    override val description: String,
    override val timeStart: Long,
    override val timeStop: Long
) : TvRelease