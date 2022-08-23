package hr.kcosic.app.model.bases

import hr.kcosic.app.model.serializer.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*
import kotlin.properties.Delegates

@Serializable
open class BaseResponse {
    var Message: String? = null
    var Status:Int? = null
    var IsSuccess:Boolean? = null

    @Serializable(DateSerializer::class)
    var Timestamp: Date? = null
}