package com.limelist.tvHistory.services.models.releases

interface TvRelease {
    val id: Int
    val description: String
    val timeStart: Long
    val timeStop: Long
}