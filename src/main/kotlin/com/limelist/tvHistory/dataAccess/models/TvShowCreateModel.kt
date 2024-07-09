package com.limelist.tvHistory.dataAccess.models

import com.limelist.tvHistory.services.dataUpdateServices.yandex.api.YandexImage

public class TvShowCreateModel(
    val id: Int,
    val name: String,
    val ageLimit: Int,
    val previewUrl: String?,
    val images: List<YandexImage>?,
    val description: String?
)
