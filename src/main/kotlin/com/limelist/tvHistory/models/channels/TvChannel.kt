package com.limelist.tvHistory.models.channels

import com.limelist.tvHistory.models.TvModel

interface TvChannel : TvModel {
    val name: String
    val imageUrl: String
}