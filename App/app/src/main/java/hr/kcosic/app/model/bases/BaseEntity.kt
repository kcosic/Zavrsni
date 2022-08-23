package hr.kcosic.app.model.bases

import java.io.Serializable
import kotlin.properties.Delegates

open class BaseEntity :Serializable{
    var Id: String? = null
    var DateCreated: String? = null
    var DateModified: String? = null
    var DateDeleted: String? = null
    var IsDeleted: Boolean? = null
}