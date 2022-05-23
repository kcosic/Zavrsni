package hr.kcosic.app.model.helpers

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import hr.kcosic.app.R
import hr.kcosic.app.model.OnPositiveButtonClickListener
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.exceptions.DuplicateKeyException
import java.io.InvalidObjectException
import java.util.*
import kotlin.reflect.typeOf

class Helper {
    companion object {
        const val APP_KEY = "My App"


        fun openActivity(context: Context, activity: ActivityEnum) {
            val intent = Intent(context, activity.getClass().java)
            ContextCompat.startActivity(context, intent, null)
        }

        fun showLongToast(context: Context, text: String) {
            Toast
                .makeText(context, text, Toast.LENGTH_LONG)
                .show()
        }

        fun showShortToast(context: Context, text: String) {
            Toast
                .makeText(context, text, Toast.LENGTH_SHORT)
                .show()
        }

        fun showAlertDialog(context: Context, titleResource: Int, textResource: Int) {
            showAlertDialog(
                context,
                context.getString(titleResource),
                context.getString(textResource)
            )
        }

        fun showAlertDialog(context: Context, title: String, text: String) {
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder
                .setMessage(text)
                .setTitle(title)
                .show()
        }


        fun showConfirmDialog(
            context: Context,
            titleResource: Int,
            textResource: Int,
            positiveButtonTextResource: Int,
            negativeButtonTextResource: Int,
            positiveCallback: OnPositiveButtonClickListener
        ) {
            showConfirmDialog(
                context,
                context.getString(titleResource),
                context.getString(textResource),
                context.getString(positiveButtonTextResource),
                context.getString(negativeButtonTextResource),
                positiveCallback
            )
        }

        /**
         *  EXAMPLE
         *   showConfirmDialog(context, "", "", "", "", object: OnPositiveButtonClickListener{
         *       override fun onPositiveBtnClick(dialog: DialogInterface?) {
         *       TODO("Not yet implemented")
         *      }
         *  })
         */
        fun showConfirmDialog(
            context: Context,
            title: String,
            text: String,
            positiveButtonText: String,
            negativeButtonText: String,
            positiveCallback: OnPositiveButtonClickListener
        ) {
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder
                .setMessage(text)
                .setTitle(title)
                .apply {
                    setPositiveButton(
                        positiveButtonText
                    ) { dialog, _ ->
                        dialog.dismiss()
                        positiveCallback.onPositiveBtnClick(dialog)
                    }
                    setNegativeButton(negativeButtonText) { dialog, _ -> dialog.dismiss() }
                }
        }

        inline fun <reified T > retrieveSharedPreference(context: Context, key: String) : T{
            val sharedPrefs = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)

            if (sharedPrefs.contains(key))
                sharedPrefs.edit().remove(key).apply()

            return when(typeOf<T>()){
                typeOf<Boolean>()   -> sharedPrefs.getBoolean(key, false) as T
                typeOf<String>()    -> sharedPrefs.getString(key, "Value not found") as T
                typeOf<Float>()     -> sharedPrefs.getFloat(key, -1f) as T
                typeOf<Int>()       -> sharedPrefs.getInt(key, -1) as T
                typeOf<Long>()      -> sharedPrefs.getLong(key, -1L) as T
                else                -> throw InvalidObjectException(context.getString(R.string.invalid_pref_type_error))
            }
        }

        inline fun <reified T > createOrEditSharedPreference(context: Context, key: String, value: T){
            val sharedPrefs = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)

            if (sharedPrefs.contains(key))
                sharedPrefs.edit().remove(key).apply()

            when(typeOf<T>()){
                typeOf<Boolean>()   -> sharedPrefs.edit().putBoolean(key, value as Boolean).apply()
                typeOf<String>()    -> sharedPrefs.edit().putString(key, value as String).apply()
                typeOf<Float>()     -> sharedPrefs.edit().putFloat(key, value as Float).apply()
                typeOf<Int>()       -> sharedPrefs.edit().putInt(key, value as Int).apply()
                typeOf<Long>()      -> sharedPrefs.edit().putLong(key, value as Long).apply()
                else                -> throw InvalidObjectException(context.getString(R.string.invalid_pref_type_error))
            }
        }

        fun deleteSharedPreference(context: Context, key: String){
            context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE).edit().remove(key).apply()
        }
    }
}