package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import kotlinx.serialization.Serializable
import kotlin.properties.Delegates

@Serializable
class Car : BaseEntity() {
    var Manufacturer: String? = null
    var Model: String? = null
    var Year: Int? = null
    var Odometer: Double? = null
    var UserId: Int? = null
    @Serializable
    var Requests: MutableList<Request>? = null
    @Serializable
    var User: User? = null

    override fun toString(): String {
        return "$Manufacturer-$Model-$Year"
    }
}