package hr.kcosic.app.model.serializer

import android.annotation.SuppressLint
import hr.kcosic.app.model.enums.ErrorCodeEnum
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.*

object ErrorCodeSerializer : KSerializer<ErrorCodeEnum> {
    override val descriptor = PrimitiveSerialDescriptor("ErrorCodeEnum", PrimitiveKind.INT)

    @SuppressLint("SimpleDateFormat")
    override fun serialize(encoder: Encoder, value: ErrorCodeEnum) = encoder.encodeInt(value.code)

    @SuppressLint("SimpleDateFormat")
    override fun deserialize(decoder: Decoder): ErrorCodeEnum {
        val wantedCode = decoder.decodeInt();

        val errorCode = ErrorCodeEnum.values().toList().find { errorCodeEnum -> errorCodeEnum.code == wantedCode}
        return errorCode!!;
    }
}