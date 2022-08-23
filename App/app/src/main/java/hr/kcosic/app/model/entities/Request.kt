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
    var Price: Double? = null
    var EstimatedPrice: Double? = null
    var ActualPrice: Double? = null

    @Serializable(with = DateSerializer::class)
    var EstimatedFinishDate: Date? = null

    @Serializable(with = DateSerializer::class)
    var ActualFinishDate: Date? = null

    var BillPicture: String? = null
    var Shop: Shop? = null
    var User: User? = null
}