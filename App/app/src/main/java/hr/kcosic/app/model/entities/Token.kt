package hr.kcosic.app.model.entities

import kotlinx.serialization.Serializable

@Serializable
class Token {
    var UserId: Int? = null
    var TokenValue: String?= null

    @Serializable
    var User: User? = null
}