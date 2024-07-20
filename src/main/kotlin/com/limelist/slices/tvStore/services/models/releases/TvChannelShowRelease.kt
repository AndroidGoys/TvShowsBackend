package com.limelist.slices.tvStore.services.models.releases

import com.limelist.slices.shared.changeTimeZone
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
    override var timeStart: Long,
    override var timeStop: Long
) : TvRelease {
    override fun changeTimeZone(timeZone: Float) {
        timeStop = timeStop.changeTimeZone(timeZone)
        timeStart = timeStart.changeTimeZone(timeZone)
    }
}