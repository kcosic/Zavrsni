package hr.kcosic.app.model.entities

import hr.kcosic.app.model.bases.BaseEntity
import java.util.*
import kotlin.properties.Delegates

class Issue : BaseEntity() {
    var UserId by Delegates.notNull<Int>()
    lateinit var DateOfSubmission: Date
    lateinit var Summary: String
    lateinit var User: User
}