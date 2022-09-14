package hr.kcosic.app.model.bases

import android.os.Bundle
import hr.kcosic.app.R
import hr.kcosic.app.model.entities.User
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.enums.ErrorCodeEnum
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.responses.ErrorResponse
import java.lang.Exception

abstract class ValidatedActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validateUser()
    }

    fun validateUser() {
        try {
            val token = Helper.retrieveSharedPreference<String>(PreferenceEnum.TOKEN)
            val authFor = Helper.retrieveSharedPreference<String>(PreferenceEnum.AUTH_FOR)
            if (token == Helper.NO_VALUE || authFor == Helper.NO_VALUE) {
                Helper.showShortToast(this,getString(R.string.token_error))
                Helper.openActivity(this,ActivityEnum.LOGIN)
            }
        } catch (e: Exception) {
            Helper.showShortToast(this,e.message ?: getString(R.string.unknown_error))
            Helper.openActivity(this,ActivityEnum.LOGIN)
        }
    }

    fun getUser(): User {
        return Helper.deserializeObject(
            Helper.retrieveSharedPreference(PreferenceEnum.USER)
        )
    }
    fun getShop(): User {
        return Helper.deserializeObject(
            Helper.retrieveSharedPreference(PreferenceEnum.SHOP)
        )
    }

    override fun handleApiResponseError(response: ErrorResponse){
        super.handleApiResponseError(response)
        if(response.ErrorCode == ErrorCodeEnum.InvalidCredentials){
            logoutUser()
        }
    }

    fun logoutUser(){
        Helper.showLongToast(this,getString(R.string.token_error))
        Helper.deleteSharedPreference(PreferenceEnum.TOKEN)
        Helper.deleteSharedPreference(PreferenceEnum.AUTH_FOR)
        Helper.openActivity(this,ActivityEnum.LOGIN)
    }
}