package hr.kcosic.app.model.entities

import android.widget.RemoteViews
import hr.kcosic.app.model.bases.BaseEntity
import kotlin.properties.Delegates

class Shop : BaseEntity() {
    lateinit var LegalName: String
    lateinit var ShortName: String
    var LocationId by Delegates.notNull<Int>()
    lateinit var Appointments: MutableList<Appointment>
    lateinit var Location: Location
    lateinit var RepairHistories: MutableList<RepairHistory>
    lateinit var Requests: MutableList<Request>
    lateinit var Reviews: MutableList<Review>
}