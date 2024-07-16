package com.limelist.slices.shared.serialization

import com.limelist.slices.shared.RequestError
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class ErrorCodeSerializer() : KSerializer<RequestError.ErrorCode> {
    override val descriptor: SerialDescriptor
        = PrimitiveSerialDescriptor("ErrorCode", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): RequestError.ErrorCode {
        return RequestError.ErrorCode(decoder.decodeInt())
    }

    override fun serialize(encoder: Encoder, value: RequestError.ErrorCode) {
        encoder.encodeInt(value.flag)
    }

}