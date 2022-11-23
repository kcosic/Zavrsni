package hr.kcosic.app.model.bases

import android.os.Bundle
import hr.kcosic.app.R
import hr.kcosic.app.model.entities.Shop
import hr.kcosic.app.model.entities.User
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.enums.ErrorCodeEnum
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.responses.ErrorResponse

abstract class ValidatedActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validateUser()
    }

    private fun validateUser() {
        try {
            val token = Helper.retrieveSharedPreference<String>(PreferenceEnum.TOKEN)
            val authFor = Helper.retrieveSharedPreference<String>(PreferenceEnum.AUTH_FOR)
            if (token == Helper.NO_VALUE || authFor == Helper.NO_VALUE) {
                Helper.showShortToast(this, getString(R.string.token_error))
                Helper.openActivity(this, ActivityEnum.LOGIN)
            }
        } catch (e: Exception) {
            Helper.showShortToast(this, e.message ?: getString(R.string.unknown_error))
            Helper.openActivity(this, ActivityEnum.LOGIN)
        }
    }

    fun getUser(): User {
        lateinit var user: User
        try {
            user = Helper.deserializeObject(
                Helper.retrieveSharedPreference(PreferenceEnum.USER)
            )
        } catch (e: Exception) {
            Helper.showShortToast(this, e.message ?: getString(R.string.unknown_error))
            logoutUser()
        }
        return user
    }

    fun getShop(): Shop {
        lateinit var shop: Shop
        try {
            shop = Helper.deserializeObject(
                Helper.retrieveSharedPreference(PreferenceEnum.SHOP)
            )
        } catch (e: Exception) {
            Helper.showShortToast(this, e.message ?: getString(R.string.unknown_error))
            logoutShop()
        }
        return shop
    }

    override fun handleApiResponseError(response: ErrorResponse) {
        super.handleApiResponseError(response)
        if (response.ErrorCode == ErrorCodeEnum.InvalidCredentials) {
            logoutUser()
        }
    }

    fun logoutUser() {
        Helper.showLongToast(this, getString(R.string.token_error))
        Helper.deleteSharedPreference(PreferenceEnum.TOKEN)
        Helper.deleteSharedPreference(PreferenceEnum.AUTH_FOR)
        Helper.deleteSharedPreference(PreferenceEnum.USER)
        Helper.openActivity(this, ActivityEnum.LOGIN)
    }

    fun logoutShop() {
        Helper.showLongToast(this, getString(R.string.token_error))
        Helper.deleteSharedPreference(PreferenceEnum.TOKEN)
        Helper.deleteSharedPreference(PreferenceEnum.AUTH_FOR)
        Helper.deleteSharedPreference(PreferenceEnum.SHOP)
        Helper.openActivity(this, ActivityEnum.LOGIN)
    }
}