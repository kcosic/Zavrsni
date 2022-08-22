package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import java.util.*
import kotlin.properties.Delegates

class Appointment : BaseEntity() {
    var ShopId by Delegates.notNull<Int>()
    lateinit var Date: Date
    var Istaken by Delegates.notNull<Boolean>()
    lateinit var Shop: Shop
}