package hr.kcosic.app.model.bases

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.kcosic.app.dal.ApiService
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.responses.ErrorResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import okhttp3.Response

abstract class BaseActivity : AppCompatActivity() {

    val apiService = ApiService.instance()
    val coroutineScope: CoroutineScope = CoroutineScope(Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextInstance.initialize(this)
        if(!Helper.hasLocationPermissions()){
            Helper.getLocationPermission()
        }
    }

    abstract fun initializeComponents()
    open fun handleApiResponseError(response: ErrorResponse){
        Helper.showLongToast(this,response.Message!!)
    }

}