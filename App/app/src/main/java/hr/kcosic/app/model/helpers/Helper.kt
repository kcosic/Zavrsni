package hr.kcosic.app.model.helpers

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import hr.kcosic.app.R
import hr.kcosic.app.model.OnPositiveButtonClickListener
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ContextInstance
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.responses.ErrorResponse
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.InvalidObjectException
import java.util.*
import kotlin.reflect.typeOf


class Helper {
    @OptIn(ExperimentalSerializationApi::class)
    companion object {
        const val APP_KEY = "My App"
        const val NO_VALUE = "not_found"
        val json = Json { explicitNulls = true }

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


        fun showAlertDialog(context: Context,view: View) {
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder
                .setView(view)
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

        inline fun <reified T> retrieveSharedPreference(key: PreferenceEnum): T {
            val sharedPrefs =
                ContextInstance.getContext()!!.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)

            return when (typeOf<T>()) {
                typeOf<Boolean>() -> sharedPrefs.getBoolean(key.getName(), false) as T
                typeOf<String>() -> sharedPrefs.getString(key.getName(), NO_VALUE) as T
                typeOf<Float>() -> sharedPrefs.getFloat(key.getName(), -1f) as T
                typeOf<Int>() -> sharedPrefs.getInt(key.getName(), -1) as T
                typeOf<Long>() -> sharedPrefs.getLong(key.getName(), -1L) as T
                else -> throw InvalidObjectException(
                    ContextInstance.getContext()!!.getString(R.string.invalid_pref_type_error)
                )
            }
        }

        inline fun <reified T> createOrEditSharedPreference(
            key: PreferenceEnum,
            value: T
        ) {
            val sharedPrefs =
                ContextInstance.getContext()!!.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)

            if (sharedPrefs.contains(key.getName())) sharedPrefs.edit().remove(key.getName()).apply()

            when (T::class) {
                Boolean::class -> {
                    sharedPrefs.edit().putBoolean(key.getName(), value as Boolean).apply()
                }
                String::class  -> {
                    sharedPrefs.edit().putString(key.getName(), value as String).apply()
                }
                Float::class  -> {
                    sharedPrefs.edit().putFloat(key.getName(), value as Float).apply()
                }
                Int::class  -> {
                    sharedPrefs.edit().putInt(key.getName(), value as Int).apply()
                }
                Long::class  -> {
                    sharedPrefs.edit().putLong(key.getName(), value as Long).apply()
                }
                else -> {
                    throw InvalidObjectException(
                        ContextInstance.getContext()!!.getString(R.string.invalid_pref_type_error) + ": " +T::class
                    )
                }
            }
        }

        fun deleteSharedPreference(key: PreferenceEnum) {
            ContextInstance.getContext()!!.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE)
                .edit().remove(key.getName()).apply()
        }


        inline fun <reified T> serializeData(value: T): String {
            return json.encodeToString(value)
        }

        inline fun <reified T : Any> deserializeObject(value: String): T {
            return json.decodeFromString(value.trimIndent())
        }

        inline fun <reified T : List<Any>> deserializeArray(value: String): List<T> {
            return json.decodeFromString(value.trimIndent())
        }

        @SuppressLint("PrivateResource")
        fun getErrorIcon(): Drawable {
            val icon = AppCompatResources.getDrawable(
                ContextInstance.getContext()!!,
                com.google.android.material.R.drawable.mtrl_ic_error
            )!!
            DrawableCompat.setTint(icon, Color.parseColor(getColor(R.color.danger_500)))
            icon.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
            return icon
        }

        fun getColor(color: Int): String {
            //noinspection ResourceType
            return ContextInstance.getContext()!!.resources.getString(color)
        }

        inline fun <reified T : BaseResponse> parseStringResponse(response: String): BaseResponse {
            return try {
                deserializeObject<T>(response)
            } catch (ex: Exception) {
                deserializeObject<ErrorResponse>(response)
            }
        }

        fun inflateView(viewId: Int, viewGroup: ViewGroup? = null): View {
            val inflater = LayoutInflater.from(ContextInstance.getContext())
            return inflater.inflate(viewId, viewGroup);
        }

        fun isStringInRange(text: String, minimum: Int, maximum: Int): Boolean {
            return text.length in minimum..maximum
        }

    }
}