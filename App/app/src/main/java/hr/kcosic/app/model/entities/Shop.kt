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
    var Appointments: MutableList<Appointment>? = null
    var Location: Location? = null
    var RepairHistories: MutableList<RepairHistory>? = null
    var Requests: MutableList<Request>? = null
    var Reviews: MutableList<Review>? = null
}