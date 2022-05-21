package hr.kcosic.app.model.responses

import kotlin.properties.Delegates

class PaginatedResponse<T> : ListResponse<T>() {
    var Page by Delegates.notNull<Int>();
    var PageSize by Delegates.notNull<Int>();
    var Total by Delegates.notNull<Int>();
}