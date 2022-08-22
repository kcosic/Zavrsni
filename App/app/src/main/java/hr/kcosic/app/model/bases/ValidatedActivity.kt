package hr.kcosic.app.model.bases

import android.os.Bundle
import hr.kcosic.app.R
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import java.lang.Exception

open class ValidatedActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validateUser()
    }

    fun validateUser() {
        try {
            val token = Helper.retrieveSharedPreference<String>(PreferenceEnum.TOKEN.getName())
            if (token == Helper.NO_VALUE) {
                Helper.showShortToast(getString(R.string.token_error))
                Helper.openActivity(this,ActivityEnum.LOGIN)
            }
        } catch (e: Exception) {
            Helper.showShortToast(e.message ?: getString(R.string.unknown_error))
            Helper.openActivity(this,ActivityEnum.LOGIN)
        }
    }
}