package hr.kcosic.app.model.responses

import kotlinx.serialization.Serializable

@Serializable
class PaginatedResponse<T> : ListResponse<T>() {
    var Page: Int? = null
    var PageSize: Int? = null
    var Total: Int? = null
}