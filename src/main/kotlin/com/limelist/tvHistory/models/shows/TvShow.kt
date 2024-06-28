package com.limelist.tvHistory.models.shows

import com.limelist.tvHistory.models.AgeLimit
import com.limelist.tvHistory.models.TvModel

interface TvShow : TvModel {
    val name: String
    val assessment: Float
    val ageLimit:  AgeLimit
    val previewUrl: String
}