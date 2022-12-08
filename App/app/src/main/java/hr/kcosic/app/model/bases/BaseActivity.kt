package hr.kcosic.app.model.bases

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import hr.kcosic.app.R
import hr.kcosic.app.dal.ApiService
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.responses.ErrorResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import okhttp3.Call

abstract class BaseActivity : AppCompatActivity() {

    val apiService = ApiService.instance()
    val coroutineScope: CoroutineScope = CoroutineScope(Job())
    lateinit var mainHandler:Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextInstance.initialize(this)
        mainHandler = Handler(applicationContext.mainLooper)
        if(!Helper.hasLocationPermissions()){
            Helper.getLocationPermission()
        }
    }

    abstract fun initializeComponents()

    open fun handleApiResponseError(response: ErrorResponse){
        Helper.showLongToast(this,response.Message!!)
    }
    open fun handleApiResponseException(call: Call, e: Exception) {
        if(e.message?.contains("Failed to connect to") == true){
            Helper.showLongToast(this, getString(R.string.internet_connection_error))
        }
        else {
            Helper.showLongToast(this, e.message.toString())
        }
        call.cancel()
    }


    protected fun <T : View> showComponent(component: T) {
        component.visibility = View.VISIBLE
    }

    protected fun  <T : View> hideComponent(component: T) {
        component.visibility = View.GONE
    }
}