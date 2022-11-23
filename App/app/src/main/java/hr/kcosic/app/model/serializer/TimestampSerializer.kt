package hr.kcosic.app.model.serializer

import android.annotation.SuppressLint
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.*

object TimestampSerializer : KSerializer<Date> {
    override val descriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    @SuppressLint("SimpleDateFormat")
    override fun serialize(encoder: Encoder, value: Date) {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSSSSSX")
        encoder.encodeString(formatter.format(value))
    }

    @SuppressLint("SimpleDateFormat")
    override fun deserialize(decoder: Decoder): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSSSSSX")
        return formatter.parse(decoder.decodeString())!!
    }
}