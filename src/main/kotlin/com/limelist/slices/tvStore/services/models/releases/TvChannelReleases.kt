package com.limelist.slices.tvStore.services.models.releases

import com.limelist.slices.shared.changeTimeZone
import com.limelist.slices.tvStore.services.models.TimeZoneDependent
import kotlinx.serialization.Serializable
import kotlinx.serialization.StringFormat

@Serializable
class TvChannelReleases<T>(
    var timeStart: Long,
    var timeStop: Long,
    val totalCount: Int,
    val releases: List<T>
) : TimeZoneDependent where T : TvRelease {

    override fun changeTimeZone(timeZone: Float) {
        timeStart = timeStart.changeTimeZone(timeZone)
        timeStop = timeStop.changeTimeZone(timeZone)

        releases.forEach {
            it.changeTimeZone(timeZone)
        }
    }
}