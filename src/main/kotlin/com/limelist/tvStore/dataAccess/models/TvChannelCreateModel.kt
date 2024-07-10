package com.limelist.tvStore.dataAccess.models

class TvChannelCreateModel(
    val id: Int,
    val name: String,
    val image: String,
    val description: String,
    val view_urls: List<String>
) {
}