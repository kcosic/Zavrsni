package hr.kcosic.app.model.responses

import hr.kcosic.app.model.bases.BaseResponse
import kotlinx.serialization.Serializable
import kotlin.properties.Delegates

@Serializable
class SingleResponse<T : Any> : BaseResponse() {
    var Data by Delegates.notNull<T>()
}