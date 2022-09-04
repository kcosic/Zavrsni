package hr.kcosic.app.model.enums

enum class PreferenceEnum(
    private var Name: String
) {
    TOKEN("Token"),
    USER("User"),
    SHOP("Shop"),
    AUTH_FOR("AuthFor");

    fun getName() : String{
        return Name
    }
}