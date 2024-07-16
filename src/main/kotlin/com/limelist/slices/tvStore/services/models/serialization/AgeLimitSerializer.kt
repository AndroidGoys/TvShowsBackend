package com.limelist.slices.tvStore.services.models.serialization

import com.limelist.slices.tvStore.services.models.AgeLimit
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class AgeLimitSerializer : KSerializer<AgeLimit> {
    override val descriptor: SerialDescriptor
        = PrimitiveSerialDescriptor("AgeLimit", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): AgeLimit {
        val flag = decoder.decodeInt()
        return AgeLimit(flag)
    }

    override fun serialize(encoder: Encoder, value: AgeLimit) {
        encoder.encodeInt(value.flag)
    }
}