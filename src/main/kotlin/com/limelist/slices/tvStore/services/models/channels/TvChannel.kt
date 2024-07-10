package com.limelist.slices.tvStore.services.models.channels

import com.limelist.slices.tvStore.services.models.TvModel

interface TvChannel : TvModel {
    val name: String
    val imageUrl: String
    val assessment: Float
}