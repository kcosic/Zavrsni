package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import java.util.*
import kotlin.properties.Delegates

class Request : BaseEntity() {
    var UserId by Delegates.notNull<Int>()
    var ShopId by Delegates.notNull<Int>()
    var Price by Delegates.notNull<Double>()
    lateinit var EstimatedFinishDate: Date
    lateinit var ActualFinishDate: Date
    var EstimatedPrice by Delegates.notNull<Double>()
    var ActualPrice by Delegates.notNull<Double>()
    lateinit var BillPicture: String
    lateinit var Shop: Shop
    lateinit var User: User
}