package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import kotlinx.serialization.Serializable

@Suppress("PropertyName")
@Serializable
class Issue : BaseEntity() {
    var RequestId: Int? = null
    var Price: Double? = null
    var Description: String? = null
    var Accepted: Boolean? = null

    @Serializable
    var Request: Request? = null
}