package com.limelist.slices.tvStore.dataAccess.models.create

data class TvTagCreateModel(
    val name: String,
    val belong: Int //0b01 - каналам 0b10 - шоу
)