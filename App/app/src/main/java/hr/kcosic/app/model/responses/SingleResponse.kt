package hr.kcosic.app.model.responses

import hr.kcosic.app.model.bases.BaseResponse
import kotlinx.serialization.Serializable

@Serializable
class SingleResponse<T : Any> : BaseResponse() {
    var Data: T? = null
}