package com.limelist.slices.tvStore.services.models.tags

import com.limelist.slices.tvStore.services.EnumFlag
import com.limelist.slices.tvStore.services.models.serialization.TvTagBelongSerializer
import kotlinx.serialization.Serializable


@Serializable(with = TvTagBelongSerializer::class)
class TvTagBelong (
    flag: Int
) : EnumFlag(flag) {
    companion object {
        val belongToChannels = TvTagBelong(0b1)
        val belongToShows = TvTagBelong(0b10)
    }
}
