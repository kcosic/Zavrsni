package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import kotlinx.serialization.Serializable

@Serializable
class Review : BaseEntity() {
    var UserId: Int? = null
    var ShopId: Int? = null
    var Rating: Double? = null
    var Comment: String? = null
    @Serializable
    var Shop: Shop? = null
    @Serializable
    var User: User? = null
}