package hr.kcosic.app.model.entities

import kotlinx.serialization.Serializable
import kotlin.properties.Delegates

@Serializable
class Location {
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
}