package com.limelist.tvHistory.services.models.releases

import kotlinx.serialization.Serializable

@Serializable
class TvChannelShowRelease(
    override val id: Int,
    val showId: String,
    val showName: String,
    override val description: String,
    override val timeStart: Long,
    override val timeStop: Long
) : TvRelease