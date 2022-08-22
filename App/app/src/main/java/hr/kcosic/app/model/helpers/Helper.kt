package hr.kcosic.app.model.helpers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonJson
import hr.kcosic.app.R
import hr.kcosic.app.model.OnPositiveButtonClickListener
import hr.kcosic.app.model.bases.BaseEntity
import hr.kcosic.app.model.bases.ContextSingleton
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.exceptions.DuplicateKeyException
import java.io.InvalidObjectException
import java.util.*
import kotlin.reflect.typeOf

class Helper {
    companion object {
        const val APP_KEY = "My App"
        const val NO_VALUE = "not_found"


        fun openActivity(context: Context,activity: ActivityEnum) {
            val intent = Intent(context, activity.getClass().java)
            ContextCompat.startActivity(context, intent, null)
        }

        fun showLongToast(text: String) {
            Toast
                .makeText(ContextSingleton.getContext()!!, text, Toast.LENGTH_LONG)
                .show()
        }

        fun showShortToast( text: String) {
            Toast
                .makeText(ContextSingleton.getContext()!!, text, Toast.LENGTH_SHORT)
                .show()
        }

        fun showAlertDialog(titleResource: Int, textResource: Int) {
            showAlertDialog(
                ContextSingleton.getContext()!!.getString(titleResource),
                ContextSingleton.getContext()!!.getString(textResource)
            )
        }

        fun showAlertDialog( title: String, text: String) {
            val dialogBuilder = AlertDialog.Builder(ContextSingleton.getContext()!!)
            dialogBuilder
                .setMessage(text)
                .setTitle(title)
                .show()
        }


        fun showConfirmDialog(
            titleResource: Int,
            textResource: Int,
            positiveButtonTextResource: Int,
            negativeButtonTextResource: Int,
            positiveCallback: OnPositiveButtonClickListener
        ) {
            showConfirmDialog(
                        ContextSingleton.getContext()!!.getString(titleResource),
                ContextSingleton.getContext()!!.getString(textResource),
                ContextSingleton.getContext()!!.getString(positiveButtonTextResource),
                ContextSingleton.getContext()!!.getString(negativeButtonTextResource),
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
            title: String,
            text: String,
            positiveButtonText: String,
            negativeButtonText: String,
            positiveCallback: OnPositiveButtonClickListener
        ) {
            val dialogBuilder = AlertDialog.Builder(ContextSingleton.getContext()!!)
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

        inline fun <reified T> retrieveSharedPreference(key: String): T {
            val sharedPrefs = ContextSingleton.getContext()!!.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)

            return when (typeOf<T>()) {
                typeOf<Boolean>() -> sharedPrefs.getBoolean(key, false) as T
                typeOf<String>() -> sharedPrefs.getString(key, NO_VALUE) as T
                typeOf<Float>() -> sharedPrefs.getFloat(key, -1f) as T
                typeOf<Int>() -> sharedPrefs.getInt(key, -1) as T
                typeOf<Long>() -> sharedPrefs.getLong(key, -1L) as T
                else -> throw InvalidObjectException(ContextSingleton.getContext()!!.getString(R.string.invalid_pref_type_error))
            }
        }

        inline fun <reified T> createOrEditSharedPreference(
            key: String,
            value: T
        ) {
            val sharedPrefs = ContextSingleton.getContext()!!.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)

            if (sharedPrefs.contains(key))
                sharedPrefs.edit().remove(key).apply()

            when (typeOf<T>()) {
                typeOf<Boolean>() -> sharedPrefs.edit().putBoolean(key, value as Boolean).apply()
                typeOf<String>() -> sharedPrefs.edit().putString(key, value as String).apply()
                typeOf<Float>() -> sharedPrefs.edit().putFloat(key, value as Float).apply()
                typeOf<Int>() -> sharedPrefs.edit().putInt(key, value as Int).apply()
                typeOf<Long>() -> sharedPrefs.edit().putLong(key, value as Long).apply()
                else -> throw InvalidObjectException(ContextSingleton.getContext()!!.getString(R.string.invalid_pref_type_error))
            }
        }

        fun deleteSharedPreference(key: String) {
            ContextSingleton.getContext()!!.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE).edit().remove(key).apply()
        }

        inline fun <reified T> serializeData(value: T): String {

            return Klaxon().toJsonString(value)
        }

        inline fun <reified T : Any> deserializeObject(value: String): T {
            return Klaxon().parse<T>(value)!!
        }

        inline fun <reified T : List<Any>> deserializeArray(value: String): List<T> {
            return Klaxon().parseArray<T>(value)!!
        }

        @SuppressLint("PrivateResource")
        fun getErrorIcon(): Drawable {
            val icon = AppCompatResources.getDrawable(ContextSingleton.getContext()!!, com.google.android.material.R.drawable.mtrl_ic_error)!!
            DrawableCompat.setTint(icon, Color.parseColor(getColor(R.color.danger_500)))
            icon.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
            return icon
        }

        fun getColor(color: Int): String{
            //noinspection ResourceType
            return ContextSingleton.getContext()!!.resources.getString(color)
        }

    }
}