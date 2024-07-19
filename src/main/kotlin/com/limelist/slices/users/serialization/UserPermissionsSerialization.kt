package com.limelist.slices.users.serialization

import com.limelist.slices.users.services.internal.models.UserPermissions
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class UserPermissionsSerialization : KSerializer<UserPermissions> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("UserPermissions", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): UserPermissions {
        return UserPermissions(decoder.decodeLong())
    }

    override fun serialize(encoder: Encoder, value: UserPermissions) {
        encoder.encodeLong(value.flag)
    }


}