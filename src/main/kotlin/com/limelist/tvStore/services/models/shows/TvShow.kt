package com.limelist.tvStore.services.models.shows

import com.limelist.tvStore.services.models.AgeLimit
import com.limelist.tvStore.services.models.TvModel

interface TvShow : TvModel {
    val name: String
    val assessment: Float
    val ageLimit:  AgeLimit
    val previewUrl: String?
}