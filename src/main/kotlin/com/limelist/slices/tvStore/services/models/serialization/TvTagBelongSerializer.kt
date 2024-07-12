package com.limelist.slices.tvStore.services.models.serialization

import com.limelist.slices.tvStore.services.models.tags.TvTagBelong
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class TvTagBelongSerializer : KSerializer<TvTagBelong> {
    override val descriptor: SerialDescriptor
        = PrimitiveSerialDescriptor("TvTagBelong", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): TvTagBelong {
        val flag = decoder.decodeInt()
        return TvTagBelong(flag)
    }

    override fun serialize(encoder: Encoder, value: TvTagBelong) {
        encoder.encodeInt(value.flag)
    }
}