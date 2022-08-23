package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import kotlinx.serialization.Serializable
import kotlin.properties.Delegates

@Serializable
class Review : BaseEntity() {
    var UserId: Int? = null
    var ShopId: Int? = null
    var Rating: Double? = null
    var Comment: String? = null
    var Shop: Shop? = null
    var User: User? = null
}