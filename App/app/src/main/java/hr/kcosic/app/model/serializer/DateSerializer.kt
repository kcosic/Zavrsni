package hr.kcosic.app.model.serializer

import android.annotation.SuppressLint
import hr.kcosic.app.model.helpers.Helper
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.*

object DateSerializer : KSerializer<Date> {
    override val descriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    @SuppressLint("SimpleDateFormat")
    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(Helper.formatIsoDateTime(value))
    }

    @SuppressLint("SimpleDateFormat")
    override fun deserialize(decoder: Decoder): Date {
        return Helper.isoStringToDateTime(decoder.decodeString());
    }
}