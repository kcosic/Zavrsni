package hr.kcosic.app.model.responses

import hr.kcosic.app.model.bases.BaseResponse
import kotlinx.serialization.Serializable

@Serializable
open class ListResponse<T> :BaseResponse() {
    lateinit var Data: MutableList<T>
}