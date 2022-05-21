package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import java.util.*
import kotlin.properties.Delegates

class User : BaseEntity() {
    lateinit var Username: String
    lateinit var Password: String
    lateinit var Email: String
    lateinit var FirstName: String
    lateinit var LastName: String
    lateinit var DateOfBirth: Date
    var CarId by Delegates.notNull<Int>()
    lateinit var Issues: MutableList<Issue>
    lateinit var Locations: MutableList<Location>
    lateinit var RepairHistories: MutableList<RepairHistory>
    lateinit var Requests: MutableList<Request>
    lateinit var Reviews: MutableList<Review>
    lateinit var Cars: MutableList<Car>
}