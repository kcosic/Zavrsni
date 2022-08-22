package hr.kcosic.app.model.bases

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.kcosic.app.dal.ApiService
import hr.kcosic.app.model.entities.User
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.*

open class BaseActivity : AppCompatActivity() {

    val apiService = ApiService.instance()
    val activityScope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextSingleton.initialize(applicationContext)
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.cancel()
    }

    fun getUser(): User {
        return Helper.deserializeObject(
            Helper.retrieveSharedPreference(PreferenceEnum.USER.getName())
        )
    }
}