package hr.kcosic.app.model.entities

import kotlin.properties.Delegates

class Location {
    var UserId by Delegates.notNull<Int>()
    lateinit var Street: String
    lateinit var StreetNumber: String
    lateinit var City: String
    lateinit var County: String
    lateinit var Country: String
    var Longitude by Delegates.notNull<Double>()
    var Latitude by Delegates.notNull<Double>()
    lateinit var Shops: MutableList<Shop>
    lateinit var User: User
}