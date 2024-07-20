package com.limelist.slices.tvStore.services.models.releases

import com.limelist.slices.shared.changeTimeZone
import kotlinx.serialization.Serializable

@Serializable
data class TvShowRelease(
    override val description: String,
    override var timeStart: Long,
    override var timeStop: Long
) : TvRelease {
    override fun changeTimeZone(timeZone: Float) {
        timeStart = timeStart.changeTimeZone(timeZone)
        timeStop = timeStop.changeTimeZone(timeZone)
    }
}