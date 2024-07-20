package com.limelist.slices.tvStore.services.models.reviews

import com.limelist.slices.shared.changeTimeZone
import com.limelist.slices.tvStore.services.models.TimeZoneDependent
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class TvReview (
    val userId: Int,
    val assessment: Int,
    var date: Long,
    val text: String
): TimeZoneDependent {
    override fun changeTimeZone(timeZone: Float) {
        date = date.changeTimeZone(timeZone)
    }
}