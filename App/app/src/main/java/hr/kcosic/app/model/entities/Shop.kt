package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import kotlinx.serialization.Serializable

@Suppress("PropertyName")
@Serializable
class Shop : BaseEntity() {
    var LocationId: Int? = null
    var ParentShopId: Int? = null
    var CarCapacity: Int? = null
    var LegalName: String? = null
    var ShortName: String? = null
    var Vat: String? = null
    var Email: String? = null
    var Password: String? = null
    var WorkHours: String? = null
    var WorkDays: String? = null
    var HourlyRate: Double? = null

    @Serializable
    var Appointments: MutableList<Appointment>? = null

    @Serializable
    var Location: Location? = null

    @Serializable
    var Requests: MutableList<Request>? = null

    @Serializable
    var Reviews: MutableList<Review>? = null

    @Serializable
    var ChildShops: MutableList<Shop>? = null

    @Serializable
    var ParentShop: Shop? = null

    @Serializable
    var Tokens: MutableList<Token>? = null
}