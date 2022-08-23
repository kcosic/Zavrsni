package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import kotlinx.serialization.Serializable
import kotlin.properties.Delegates

@Serializable
class Car : BaseEntity() {
    var Manufacturer: String? = null
    var Make: String? = null
    var Model: String? = null
    var Year: Int? = null
    var Odometer: Double? = null
    @Serializable
    var RepairHistories: MutableList<RepairHistory>? = null
    @Serializable
    var User: User? = null
}