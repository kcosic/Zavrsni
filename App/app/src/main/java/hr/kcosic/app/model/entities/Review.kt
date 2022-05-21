package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import kotlin.properties.Delegates

class Review : BaseEntity() {
    var UserId by Delegates.notNull<Int>()
    var ShopId by Delegates.notNull<Int>()
    lateinit var Comment: String
    var Rating by Delegates.notNull<Double>()
    lateinit var Shop: Shop
    lateinit var User: User
}