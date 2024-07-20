package com.limelist.slices.tvStore.services.models.reviews

import com.limelist.slices.tvStore.services.models.TimeZoneDependent
import kotlinx.serialization.Serializable

@Serializable
data class TvReviews(
    val total: Int,
    val comments: List<TvReview>
) : TimeZoneDependent {
    override fun changeTimeZone(timeZone: Float) {
        comments.forEach { it.changeTimeZone(timeZone) }
    }
}
