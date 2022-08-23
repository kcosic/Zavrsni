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
import hr.kcosic.app.R
import hr.kcosic.app.model.OnPositiveButtonClickListener
import hr.kcosic.app.model.bases.BaseEntity
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ContextSingleton
import hr.kcosic.app.model.entities.User
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.exceptions.DuplicateKeyException
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.SingleResponse
import java.io.InvalidObjectException
import java.util.*
import kotlin.reflect.typeOf
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import okhttp3.Response

class Helper {
    companion object {
        const val APP_KEY = "My App"
        const val NO_VALUE = "not_found"


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
                .makeText(
                    context,
                    text, Toast.LENGTH_SHORT
                )
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

        inline fun <reified T> retrieveSharedPreference(key: String): T {
            val sharedPrefs =
                ContextSingleton.getContext()!!.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)

            return when (typeOf<T>()) {
                typeOf<Boolean>() -> sharedPrefs.getBoolean(key, false) as T
                typeOf<String>() -> sharedPrefs.getString(key, NO_VALUE) as T
                typeOf<Float>() -> sharedPrefs.getFloat(key, -1f) as T
                typeOf<Int>() -> sharedPrefs.getInt(key, -1) as T
                typeOf<Long>() -> sharedPrefs.getLong(key, -1L) as T
                else -> throw InvalidObjectException(
                    ContextSingleton.getContext()!!.getString(R.string.invalid_pref_type_error)
                )
            }
        }

        inline fun <reified T> createOrEditSharedPreference(
            key: String,
            value: T
        ) {
            val sharedPrefs =
                ContextSingleton.getContext()!!.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)

            if (sharedPrefs.contains(key))
                sharedPrefs.edit().remove(key).apply()

            when (typeOf<T>()) {
                typeOf<Boolean>() -> sharedPrefs.edit().putBoolean(key, value as Boolean).apply()
                typeOf<String>() -> sharedPrefs.edit().putString(key, value as String).apply()
                typeOf<Float>() -> sharedPrefs.edit().putFloat(key, value as Float).apply()
                typeOf<Int>() -> sharedPrefs.edit().putInt(key, value as Int).apply()
                typeOf<Long>() -> sharedPrefs.edit().putLong(key, value as Long).apply()
                else -> throw InvalidObjectException(
                    ContextSingleton.getContext()!!.getString(R.string.invalid_pref_type_error)
                )
            }
        }

        fun deleteSharedPreference(key: String) {
            ContextSingleton.getContext()!!.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)
                .edit().remove(key).apply()
        }

        inline fun <reified T> serializeData(value: T): String {
            return Json.encodeToString(value)
        }

        inline fun <reified T : Any> deserializeObject(value: String): T {
            return Json.decodeFromString(value.trimIndent())
        }

        inline fun <reified T : List<Any>> deserializeArray(value: String): List<T> {
            return Json.decodeFromString(value.trimIndent())
        }

        @SuppressLint("PrivateResource")
        fun getErrorIcon(): Drawable {
            val icon = AppCompatResources.getDrawable(
                ContextSingleton.getContext()!!,
                com.google.android.material.R.drawable.mtrl_ic_error
            )!!
            DrawableCompat.setTint(icon, Color.parseColor(getColor(R.color.danger_500)))
            icon.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
            return icon
        }

        fun getColor(color: Int): String {
            //noinspection ResourceType
            return ContextSingleton.getContext()!!.resources.getString(color)
        }

        fun parseStringResponse(response: String): BaseResponse {
            return try {
                deserializeObject<SingleResponse<User>>(response)
            } catch (ex: Exception) {
                deserializeObject<ErrorResponse>(response)
            }
        }

    }
}