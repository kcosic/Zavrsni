package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import hr.kcosic.app.model.serializer.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*
import kotlin.properties.Delegates

@Serializable
class User : BaseEntity() {
    var Username: String? = null
    var Password: String? = null
    var Email: String? = null
    var FirstName: String? = null
    var LastName: String? = null

    @Serializable
    var Tokens: MutableList<Token>? = null

    @Serializable
    var Issues: MutableList<Issue>? = null
    @Serializable
    var Locations: MutableList<Location>? = null
    @Serializable
    var RepairHistories: MutableList<RepairHistory>? = null
    @Serializable
    var Requests: MutableList<Request>? = null
    @Serializable
    var Reviews: MutableList<Review>? = null
    @Serializable
    var Cars: MutableList<Car>? = null

    @Serializable(with = DateSerializer::class)
    var DateOfBirth: Date? = null

}