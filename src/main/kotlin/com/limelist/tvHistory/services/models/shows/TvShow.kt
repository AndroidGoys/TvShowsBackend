package com.limelist.tvHistory.services.models.shows

import com.limelist.tvHistory.services.models.AgeLimit
import com.limelist.tvHistory.services.models.TvModel

interface TvShow : TvModel {
    val name: String
    val assessment: Float
    val ageLimit:  AgeLimit
    val previewUrl: String?
}