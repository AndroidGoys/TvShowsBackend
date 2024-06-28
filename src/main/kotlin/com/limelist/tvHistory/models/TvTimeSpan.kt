package com.limelist.tvHistory.models

import kotlinx.serialization.Serializable

@Serializable
class TvTimeSpan (
    val timeStart: Int,
    val timeEnd: Int
)