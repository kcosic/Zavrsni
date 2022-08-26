package hr.kcosic.app.model.bases

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hr.kcosic.app.dal.ApiService
import hr.kcosic.app.model.entities.User
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper

abstract class BaseActivity : AppCompatActivity() {

    val apiService = ApiService.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ContextInstance.initialize(this)
    }

    abstract fun initializeComponents()
}