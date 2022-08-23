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
    var Shop: Shop? = null
    var User: User? = null
    var Car: Car? = null

}