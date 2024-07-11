package com.limelist.slices.tvStore.services.models.tags

import kotlinx.serialization.Serializable


@Serializable
class TvTagBelong (
    val flag: Int
) {
    companion object {
        val belongToChannels = TvTagBelong(0b1)
        val belongToShows = TvTagBelong(0b10)
    }
}
