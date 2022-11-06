package hr.kcosic.app.model.helpers

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks.await
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ContextInstance
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.enums.ErrorCodeEnum
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.listeners.OnNegativeButtonClickListener
import hr.kcosic.app.model.listeners.OnPositiveButtonClickListener
import hr.kcosic.app.model.responses.ErrorResponse
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InvalidObjectException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.typeOf


class Helper {
    @OptIn(ExperimentalSerializationApi::class)
    companion object {
        const val APP_KEY = "My App"
        const val NO_VALUE = "not_found"
        var AUTH_FOR_KEY: String? = null
        val json = Json { explicitNulls = true }

        fun openActivity(
            context: Activity,
            activity: ActivityEnum,
            baggage: MutableMap<String, String>? = null,
            baggageTag: String? = null
        ) {
            val intent = Intent(context, activity.getClass().java)
            intent.putExtra(baggageTag, serializeData(baggage))
            ContextCompat.startActivity(context, intent, null)
            context.overridePendingTransition(
                androidx.appcompat.R.anim.abc_fade_in,
                androidx.appcompat.R.anim.abc_fade_out
            );
        }

        fun openActivity(context: Context, activity: ActivityEnum) {
            val intent = Intent(context, activity.getClass().java)
            ContextCompat.startActivity(context, intent, null)
        }

        fun setAuthKeyToUser() {
            AUTH_FOR_KEY = "User"
        }

        fun setAuthKeyToShop() {
            AUTH_FOR_KEY = "Shop"
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
                    text,
                    Toast.LENGTH_SHORT
                )
                .show()
        }

        fun showAlertDialog(context: Context, titleResource: Int, textResource: Int): AlertDialog {
            return showAlertDialog(
                context,
                context.getString(titleResource),
                context.getString(textResource)
            )
        }

        fun showAlertDialog(context: Context, title: String, text: String): AlertDialog {
            val dialogBuilder = AlertDialog.Builder(context)
            return dialogBuilder
                .setMessage(text)
                .setTitle(title)
                .show()
        }


        fun showAlertDialog(context: Context, view: View): AlertDialog {
            val dialogBuilder = AlertDialog.Builder(context)
            return dialogBuilder
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
        ): AlertDialog {
            return showConfirmDialog(
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
            positiveCallback: OnPositiveButtonClickListener,
            customDialogDismiss: Boolean = false
        ): AlertDialog {
            val dialogBuilder = AlertDialog.Builder(context)
            return dialogBuilder
                .setMessage(text)
                .setTitle(title)
                .apply {
                    setPositiveButton(
                        positiveButtonText
                    ) { dialog, _ ->
                        if (!customDialogDismiss) {
                            dialog.dismiss()
                        }
                        positiveCallback.onPositiveBtnClick(dialog)
                    }
                    setNegativeButton(negativeButtonText) { dialog, _ -> dialog.dismiss() }
                }
                .show()
        }

        fun showConfirmDialog(
            context: Context,
            title: String,
            view: View,
            positiveButtonText: String,
            negativeButtonText: String,
            positiveCallback: OnPositiveButtonClickListener,
            negativeCallback: OnNegativeButtonClickListener? = null,
            customDialogDismiss: Boolean = false

        ): AlertDialog {
            val dialogBuilder = AlertDialog.Builder(context)
            return dialogBuilder
                .setView(view)
                .setTitle(title)
                .apply {
                    setPositiveButton(
                        positiveButtonText
                    ) { dialog, _ ->
                        if (!customDialogDismiss) {
                            dialog.dismiss()
                        }
                        positiveCallback.onPositiveBtnClick(dialog)
                    }
                    setNegativeButton(negativeButtonText) { dialog, _ ->
                        if (!customDialogDismiss) {
                            dialog.dismiss()
                        }
                        negativeCallback?.onNegativeBtnClick(dialog)
                    }
                }
                .show()
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

            if (sharedPrefs.contains(key.getName())) sharedPrefs.edit().remove(key.getName())
                .apply()

            when (T::class) {
                Boolean::class -> {
                    sharedPrefs.edit().putBoolean(key.getName(), value as Boolean).apply()
                }
                String::class -> {
                    sharedPrefs.edit().putString(key.getName(), value as String).apply()
                }
                Float::class -> {
                    sharedPrefs.edit().putFloat(key.getName(), value as Float).apply()
                }
                Int::class -> {
                    sharedPrefs.edit().putInt(key.getName(), value as Int).apply()
                }
                Long::class -> {
                    sharedPrefs.edit().putLong(key.getName(), value as Long).apply()
                }
                else -> {
                    throw InvalidObjectException(
                        ContextInstance.getContext()!!
                            .getString(R.string.invalid_pref_type_error) + ": " + T::class
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

        @SuppressLint("PrivateResource")
        fun getMenuIcon(): VectorDrawable {
            val icon = AppCompatResources.getDrawable(
                ContextInstance.getContext()!!,
                R.drawable.ellipsis_vertical
            )!! as VectorDrawable
            DrawableCompat.setTint(icon, Color.parseColor(getColor(R.color.primary_500)))
            icon.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
            return icon
        }


        /**
         * Gets a Bitmap from provided Vector Drawable image
         *
         * @param vd VectorDrawable
         * @return Bitmap
         */
        fun createBitmapFromVectorDrawable(vd: Drawable, width: Int?, height: Int?): Bitmap? {
            return try {
                val bitmap: Bitmap = Bitmap.createBitmap(
                    vd.intrinsicWidth,
                    vd.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bitmap)
                vd.setBounds(0, 0, width ?: canvas.width, height ?: canvas.height)
                vd.draw(canvas)
                bitmap
            } catch (e: OutOfMemoryError) {
                return null
            }
        }

        /**
         * Loads vector drawable and apply tint color on it.
         */
        fun loadVectorDrawableWithTintColor(
            @DrawableRes vdRes: Int,
            @ColorRes clrRes: Int
        ): Drawable {
            val drawable = ContextCompat.getDrawable(ContextInstance.getContext()!!, vdRes)
            DrawableCompat.setTint(
                drawable!!,
                ContextInstance.getContext()!!.resources.getColor(clrRes)
            )
            return drawable
        }

        /**
         * Converts given vector drawable to Bitmap drawable
         */
        fun convertVectorDrawableToBitmapDrawable(vd: Drawable, width: Int?, height: Int?): BitmapDrawable {
            //it is safe to create empty bitmap drawable from null source
            return BitmapDrawable(createBitmapFromVectorDrawable(vd, width, height))
        }

        /**
         * Loads vector drawable , aplys tint on it and returns a wrapped bitmap drawable.
         * Bitmap drawable can be resized using setBounds method (unlike the VectorDrawable)
         * @param context Requires view context !
         */
        fun loadVectorDrawableWithTint(
            @DrawableRes vectorDrawableRes: Int, @ColorRes colorRes: Int, width: Int? = null, height: Int? = null
        ): Drawable {
            val vd: Drawable = loadVectorDrawableWithTintColor(
                vectorDrawableRes,
                colorRes
            )
            val bitmapDrawable: BitmapDrawable =
                convertVectorDrawableToBitmapDrawable(vd, width, height)
            val tint = ContextCompat.getColorStateList(ContextInstance.getContext()!!, colorRes)
            val wrappedDrawable = DrawableCompat.wrap(bitmapDrawable)
            DrawableCompat.setTintList(wrappedDrawable, tint)
            return wrappedDrawable
        }


        fun getColor(color: Int): String {
            //noinspection ResourceType
            return ContextInstance.getContext()!!.resources.getString(color)
        }

        inline fun <reified T : BaseResponse> parseStringResponse(response: String): BaseResponse {
            return try {
                deserializeObject<T>(response)
            } catch (ex: Exception) {
                try {
                    deserializeObject<ErrorResponse>(response)
                } catch (ex2: Exception) {
                    val err = ErrorResponse()
                    err.ErrorCode = ErrorCodeEnum.UnexpectedError
                    err.IsSuccess = false
                    err.Message = "Response parse error: ${ex2.message}"
                    return err
                }
            }
        }

        fun inflateView(viewId: Int, viewGroup: ViewGroup? = null): View {
            val inflater = LayoutInflater.from(ContextInstance.getContext())
            return inflater.inflate(viewId, viewGroup);
        }

        fun isStringInRange(text: String, minimum: Int, maximum: Int): Boolean {
            return text.length in minimum..maximum
        }

        fun hasLocationPermissions(): Boolean {
            if (ActivityCompat.checkSelfPermission(
                    ContextInstance.getContext()!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    ContextInstance.getContext()!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
            return true
        }

        fun getLocationPermission() {
            val arrPerm = ArrayList<String>()

            arrPerm.add(Manifest.permission.ACCESS_FINE_LOCATION)

            arrPerm.add(Manifest.permission.ACCESS_COARSE_LOCATION)

            if (arrPerm.isNotEmpty()) {
                var permissions: Array<String?>? = arrayOfNulls(arrPerm.size)
                permissions = arrPerm.toArray(permissions!!)
                ActivityCompat.requestPermissions(
                    ContextInstance.getContext()!! as Activity,
                    permissions,
                    1
                )
            }
        }

        @SuppressLint("MissingPermission")
        fun retrievePhoneLocation(): LatLng? {
            @SuppressWarnings("VisibleForTests")
            val locationProvider = FusedLocationProviderClient(ContextInstance.getContext()!!)
            if (hasLocationPermissions()) {
                getLocationPermission()
            }


            val locationTask =
                locationProvider.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            val location = await(locationTask)

            return LatLng(location.latitude, location.longitude)

        }

        fun toBase64(text: String): String {
            return Base64.getEncoder().encodeToString(text.toByteArray())
        }

        fun fromBase64(text: String): String {
            return String(Base64.getDecoder().decode(text.toByteArray()))
        }

        @SuppressLint("SimpleDateFormat")
        fun formatDate(date: Date): String {
            val myFormat = "dd.MM.yyyy"
            val dateFormat = SimpleDateFormat(myFormat)
            return dateFormat.format(date)
        }

        @SuppressLint("SimpleDateFormat")
        fun formatDateTime(date: Date): String {
            val myFormat = "dd.MM.yyyy HH:mm"
            val dateFormat = SimpleDateFormat(myFormat)
            return dateFormat.format(date)
        }

        @SuppressLint("SimpleDateFormat")
        fun formatIsoDateTime(date: Date): String {
            val myFormat = "yyyy-MM-dd'T'HH:mm:ss"
            val dateFormat = SimpleDateFormat(myFormat)
            return dateFormat.format(date)
        }

        @SuppressLint("SimpleDateFormat")
        fun isoStringToDateTime(dateString: String): Date {
            val myFormat = "yyyy-MM-dd'T'HH:mm:ss"
            val dateFormat = SimpleDateFormat(myFormat)
            return dateFormat.parse(dateString)!!
        }

        @SuppressLint("SimpleDateFormat")
        fun stringToDateTime(dateString: String): Date {
            val myFormat = "dd.MM.yyyy HH:mm"
            val dateFormat = SimpleDateFormat(myFormat)
            return dateFormat.parse(dateString)!!
        }

        @SuppressLint("SimpleDateFormat")
        fun formatTime(date: Date): String {
            val myFormat = "HH:mm"
            val dateFormat = SimpleDateFormat(myFormat)
            return dateFormat.format(date)
        }

        fun isValueInRange(value: Int, start: Int, end: Int): Boolean {
            return value in start..end;

        }

        fun isValueInRange(value: Double, start: Double, end: Double): Boolean {
            return value in start..end;

        }

    }
}