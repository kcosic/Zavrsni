package hr.kcosic.app.model.enums

enum class PreferenceEnum(
    private var Name: String
) {
    TOKEN("Token");

    fun getName() : String{
        return Name;
    }
}