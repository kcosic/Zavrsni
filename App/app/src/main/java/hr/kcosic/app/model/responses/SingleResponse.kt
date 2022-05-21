package hr.kcosic.app.model.responses

import hr.kcosic.app.model.bases.BaseResponse
import kotlin.properties.Delegates

class SingleResponse<T : Any> : BaseResponse() {
    var Data by Delegates.notNull<T>()
}