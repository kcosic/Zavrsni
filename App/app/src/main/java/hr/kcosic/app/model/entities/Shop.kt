package hr.kcosic.app.model.entities

import android.widget.RemoteViews
import hr.kcosic.app.model.bases.BaseEntity
import kotlinx.serialization.Serializable
import kotlin.properties.Delegates

@Serializable
class Shop : BaseEntity() {
    var LocationId: Int? = null
    var LegalName: String? = null
    var ShortName: String? = null
    @Serializable
    var Appointments: MutableList<Appointment>? = null
    @Serializable
    var Location: Location? = null
    @Serializable
    var RepairHistories: MutableList<RepairHistory>? = null
    @Serializable
    var Requests: MutableList<Request>? = null
    @Serializable
    var Reviews: MutableList<Review>? = null
}