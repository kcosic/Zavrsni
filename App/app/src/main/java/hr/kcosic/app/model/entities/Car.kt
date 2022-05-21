package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import kotlin.properties.Delegates

class Car : BaseEntity() {
    lateinit var Manufacturer: String
    lateinit var Make: String
    lateinit var Model: String
    var Year by Delegates.notNull<Int>()
    var Odometer by Delegates.notNull<Double>()
    lateinit var RepairHistories: MutableList<RepairHistory>
    lateinit var User: User
}