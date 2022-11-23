package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import hr.kcosic.app.model.serializer.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class Appointment : BaseEntity() {
    var ShopId: Int? = null
    @Serializable(with = DateSerializer::class)
    var DateTimeStart: Date? = null
    @Serializable(with = DateSerializer::class)
    var DateTimeEnd: Date? = null
    var IsTaken: Boolean? = null
    @Serializable
    var Shop: Shop? = null
}