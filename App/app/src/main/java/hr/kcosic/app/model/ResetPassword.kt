package hr.kcosic.app.model

import hr.kcosic.app.model.bases.BaseEntity
import hr.kcosic.app.model.entities.Shop
import hr.kcosic.app.model.serializer.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class ResetPassword(
    var OldPassword: String,
    var NewPassword: String
) {

}