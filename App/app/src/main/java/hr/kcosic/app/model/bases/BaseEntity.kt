package hr.kcosic.app.model.bases

import java.io.Serializable
import kotlin.properties.Delegates

open class BaseEntity :Serializable{
    lateinit var Id: String
    lateinit var DateCreated: String
    lateinit var DateModified: String
    lateinit var DateDeleted: String
    var IsDeleted by Delegates.notNull<Boolean>()
}