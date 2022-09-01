package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import kotlinx.serialization.Serializable
import kotlin.properties.Delegates

@Serializable
class Location : BaseEntity() {
    var UserId: Int? = null
    var Longitude: Double? = null
    var Latitude: Double? = null
    var Street: String? = null
    var StreetNumber: String? = null
    var City: String? = null
    var County: String? = null
    var Country: String? = null

    @Serializable
    var Shops: MutableList<Shop>? = null

    @Serializable
    var User: User? = null

    override fun toString(): String {
        val street = if (Street.isNullOrEmpty()) "" else "$Street"
        val streetNumber = if (StreetNumber.isNullOrEmpty()) "" else " $StreetNumber"
        val city = if (City.isNullOrEmpty()) "" else ", $City"
        val country = if (Country.isNullOrEmpty()) "" else ", $Country"

        return "$street$streetNumber$city$country"
    }
}