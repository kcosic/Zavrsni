package hr.kcosic.app.model.bases

import java.util.*
import kotlin.properties.Delegates

open class BaseResponse {
    lateinit var Message: String
    var Status by Delegates.notNull<Int>()
    var IsSuccess by Delegates.notNull<Boolean>()
    lateinit var TimeStamp: Date
}