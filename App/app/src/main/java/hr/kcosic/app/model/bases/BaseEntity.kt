package hr.kcosic.app.model.bases

import hr.kcosic.app.model.serializer.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*


@Serializable
open class BaseEntity{
    var Id: Int? = null
    @Serializable(DateSerializer::class)
    var DateCreated: Date? = null
    @Serializable(DateSerializer::class)
    var DateModified: Date? = null
    @Serializable(DateSerializer::class)
    var DateDeleted: Date? = null
    var Deleted: Boolean? = null
}