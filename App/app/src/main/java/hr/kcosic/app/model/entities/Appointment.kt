package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import hr.kcosic.app.model.serializer.DateSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import java.util.*
import kotlin.properties.Delegates

@Serializable
class Appointment : BaseEntity() {
    var ShopId:Int? = null
    @Serializable(with = DateSerializer::class)
    var Date: Date? = null
    var Istaken by Delegates.notNull<Boolean>()
    var Shop: Shop? = null
}