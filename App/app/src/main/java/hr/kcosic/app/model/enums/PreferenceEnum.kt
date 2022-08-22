package hr.kcosic.app.model.enums

enum class PreferenceEnum(
    private var Name: String
) {
    TOKEN("Token"),
    USER("User");

    fun getName() : String{
        return Name
    }
}