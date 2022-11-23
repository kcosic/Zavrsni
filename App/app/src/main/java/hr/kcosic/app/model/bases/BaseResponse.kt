package hr.kcosic.app.model.bases

import hr.kcosic.app.model.serializer.TimestampSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
open class BaseResponse {
    var Message: String? = null
    var Status:Int? = null
    var IsSuccess:Boolean? = null

    @Serializable(TimestampSerializer::class)
    var Timestamp: Date? = null
}