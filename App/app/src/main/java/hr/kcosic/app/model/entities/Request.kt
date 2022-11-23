package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import hr.kcosic.app.model.serializer.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class Request : BaseEntity() {
    var UserId: Int? = null
    var ShopId: Int? = null
    var CarId: Int? = null
    var Price: Double? = null
    var EstimatedPrice: Double? = null
    var EstimatedRepairHours: Int? = null
    @Serializable(with = DateSerializer::class)
    var FinishDate: Date? = null
    @Serializable(with = DateSerializer::class)
    var RepairDate: Date? = null
    @Serializable(with = DateSerializer::class)
    var RequestDate: Date? = null
    var Completed: Boolean? = null
    var UserAccepted: Boolean? = null
    @Serializable(with = DateSerializer::class)
    var UserAcceptedDate: Date? = null
    var ShopAccepted: Boolean? = null
    @Serializable(with = DateSerializer::class)
    var ShopAcceptedDate: Date? = null
    var BillPicture: String? = null
    var IssueDescription: String? = null
    @Serializable
    var Shop: Shop? = null
    @Serializable
    var User: User? = null
    @Serializable
    var Car: Car? = null

}