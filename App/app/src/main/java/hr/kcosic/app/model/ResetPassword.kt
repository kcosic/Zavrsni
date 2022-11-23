package hr.kcosic.app.model

import kotlinx.serialization.Serializable

@Serializable
class ResetPassword(
    var OldPassword: String,
    var NewPassword: String
)