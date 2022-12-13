package hr.kcosic.app.model.entities

import kotlinx.serialization.Serializable

@Suppress("PropertyName")
@Serializable
class Token {
    var UserId: Int? = null
    var ShopId: Int? = null
    var TokenValue: String? = null

    @Serializable
    var User: User? = null

    @Serializable
    var Shop: Shop? = null
}