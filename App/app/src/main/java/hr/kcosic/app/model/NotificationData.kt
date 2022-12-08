package hr.kcosic.app.model

@kotlinx.serialization.Serializable
class NotificationData {
    var NewRequests: MutableList<Int>? = null
    var UpdatedRequests: MutableList<Int>? = null
}