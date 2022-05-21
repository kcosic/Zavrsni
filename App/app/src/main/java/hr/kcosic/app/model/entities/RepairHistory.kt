package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import java.util.*
import kotlin.properties.Delegates

class RepairHistory : BaseEntity() {
    var UserId by Delegates.notNull<Int>()
    lateinit var DateOfRepair: Date
    var CarId by Delegates.notNull<Int>()
    var ShopId by Delegates.notNull<Int>()
    lateinit var Shop: Shop
    lateinit var User: User
    lateinit var Car: Car
}