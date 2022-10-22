package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import hr.kcosic.app.model.serializer.BooleanSerializer
import hr.kcosic.app.model.serializer.DateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import java.util.*
import kotlin.properties.Delegates

@Serializable
class Appointment : BaseEntity() {
    var ShopId: Int? = null
    @Serializable(with = DateSerializer::class)
    var DateTimeStart: Date? = null
    @Serializable(with = DateSerializer::class)
    var DateTimeEnd: Date? = null
    var Istaken: Boolean? = null
    @Serializable
    var Shop: Shop? = null
}