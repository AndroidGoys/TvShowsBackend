package com.limelist.tvHistory.services.models.channels

import com.limelist.tvHistory.services.models.TvModel
import kotlinx.serialization.Serializable

interface TvChannel : TvModel {
    val name: String
    val imageUrl: String
    val assessment: Float
}