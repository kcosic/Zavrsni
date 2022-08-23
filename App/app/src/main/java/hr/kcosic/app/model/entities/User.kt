package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import hr.kcosic.app.model.serializer.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*
import kotlin.properties.Delegates

@Serializable
class User : BaseEntity() {
    var Username: String?=null
    var Password: String?=null
    var Email: String?=null
    var FirstName: String?=null
    var LastName: String?=null
    var Issues: MutableList<Issue>?=null
    var Locations: MutableList<Location>?=null
    var RepairHistories: MutableList<RepairHistory>?=null
    var Requests: MutableList<Request>?=null
    var Reviews: MutableList<Review>?=null
    var Cars: MutableList<Car>?=null
    @Serializable(with = DateSerializer::class)
    var DateOfBirth: Date?=null
    var CarId:Int? = null

}