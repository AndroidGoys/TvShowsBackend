package com.limelist.slices.tvStore.services.models.releases

import com.limelist.slices.tvStore.services.models.TimeZoneDependent

interface TvRelease : TimeZoneDependent {
    val description: String
    val timeStart: Long
    val timeStop: Long
}