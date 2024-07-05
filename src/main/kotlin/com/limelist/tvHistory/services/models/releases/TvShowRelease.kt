package com.limelist.tvHistory.services.models.releases

import kotlinx.serialization.Serializable

@Serializable
data class TvShowRelease(
    override val showId: Int,
    override val description: String,
    override val timeStart: Long,
    override val timeStop: Long
) : TvRelease