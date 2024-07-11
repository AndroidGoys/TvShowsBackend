package com.limelist.slices.tvStore.dataAccess.models.create

class TvChannelCreateModel(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val view_urls: List<String>,
    val tagIds: List<Int>
) {
}