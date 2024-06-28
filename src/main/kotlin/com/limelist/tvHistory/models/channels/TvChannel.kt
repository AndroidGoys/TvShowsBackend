package com.limelist.tvHistory.models.channels

import com.limelist.tvHistory.models.TvModel
import kotlinx.serialization.Serializable

interface TvChannel : TvModel {
    val name: String
    val imageUrl: String
}