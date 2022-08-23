package hr.kcosic.app.model.responses

import kotlinx.serialization.Serializable
import kotlin.properties.Delegates

@Serializable
class PaginatedResponse<T> : ListResponse<T>() {
    var Page by Delegates.notNull<Int>()
    var PageSize by Delegates.notNull<Int>()
    var Total by Delegates.notNull<Int>()
}