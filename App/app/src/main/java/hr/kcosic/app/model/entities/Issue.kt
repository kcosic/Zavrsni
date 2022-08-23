package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import hr.kcosic.app.model.serializer.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*
import kotlin.properties.Delegates

@Serializable
class Issue : BaseEntity() {
    var UserId: Int? = null

    @Serializable(with = DateSerializer::class)
    var DateOfSubmission: Date? = null
    var Summary: String? = null
    @Serializable
    var User: User? = null
}