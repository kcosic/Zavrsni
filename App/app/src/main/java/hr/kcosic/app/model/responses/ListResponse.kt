package hr.kcosic.app.model.responses

import hr.kcosic.app.model.bases.BaseResponse

open class ListResponse<T> :BaseResponse() {
    lateinit var Data: MutableList<T>
}