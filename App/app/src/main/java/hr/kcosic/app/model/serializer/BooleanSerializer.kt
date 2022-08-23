package hr.kcosic.app.model.serializer

import hr.kcosic.app.model.enums.ErrorCodeEnum
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object BooleanSerializer : KSerializer<Boolean> {
    override val descriptor =PrimitiveSerialDescriptor("Boolean", PrimitiveKind.BOOLEAN)

    override fun serialize(encoder: Encoder, value: Boolean) = encoder.encodeBoolean(value)

    override fun deserialize(decoder: Decoder): Boolean = decoder.decodeBoolean()
}