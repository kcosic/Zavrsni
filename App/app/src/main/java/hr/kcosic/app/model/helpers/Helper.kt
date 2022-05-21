package hr.kcosic.app.model.helpers

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import hr.kcosic.app.model.enums.ActivityEnum

class Helper {
    companion object{
        fun openActivity(context: Context, activity: ActivityEnum){
            val intent = Intent(context, activity.getClass().java)
            ContextCompat.startActivity(context, intent, null)
        }

        fun showToast(context: Context, text: String){
            Toast.makeText(context, text, )
        }
    }
}