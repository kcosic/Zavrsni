package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import hr.kcosic.app.model.serializer.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*
import kotlin.properties.Delegates

@Serializable
class RepairHistory : BaseEntity() {
    var UserId: Int? = null
    var CarId: Int? = null
    var ShopId: Int? = null

    @Serializable(with = DateSerializer::class)
    var DateOfRepair: Date? = null
    @Serializable
    var Shop: Shop? = null
    @Serializable
    var User: User? = null
    @Serializable
    var Car: Car? = null

}