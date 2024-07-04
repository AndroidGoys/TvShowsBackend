package com.limelist.tvHistory.dataAccess.models

import com.limelist.tvHistory.services.models.AgeLimit

public class TvShowCreateModel (
    val id: Int,
    val name: String,
    val ageLimit: Int,
    val previewUrl: String,
    val description: String
)
