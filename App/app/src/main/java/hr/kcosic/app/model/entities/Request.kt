package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import hr.kcosic.app.model.serializer.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*
import kotlin.properties.Delegates

@Serializable
class Request : BaseEntity() {
    var UserId: Int? = null
    var ShopId: Int? = null
    var CarId: Int? = null
    var Price: Double? = null
    var EstimatedPrice: Double? = null
    var ActualPrice: Double? = null
    @Serializable(with = DateSerializer::class)
    var EstimatedFinishDate: Date? = null
    @Serializable(with = DateSerializer::class)
    var ActualFinishDate: Date? = null
    var Completed: Boolean? = null
    var Accepted: Boolean? = null
    var BillPicture: String? = null
    @Serializable
    var Shop: Shop? = null
    @Serializable
    var User: User? = null
    @Serializable
    var Car: Car? = null
    @Serializable
    var Issues: MutableList<Issue>? = null
}