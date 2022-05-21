package hr.kcosic.app.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hr.kcosic.app.R
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        validateUser()
    }

    fun validateUser(){
        try {
            if (getSharedPreferences(PreferenceEnum.TOKEN.getName(), Context.MODE_PRIVATE) == null){
                Helper.openActivity(this, ActivityEnum.MAIN)
            }
            Helper.openActivity(this, ActivityEnum.MAIN)
        }catch (e: Exception){
            Helper.showToast
            Helper.openActivity(this, ActivityEnum.MAIN)

        }
    }
}