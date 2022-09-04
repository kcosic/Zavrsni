package hr.kcosic.app.dal

import android.util.Base64
import hr.kcosic.app.model.bases.ApiRoutes
import hr.kcosic.app.model.entities.Shop
import hr.kcosic.app.model.entities.User
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.Duration


class ApiService private constructor() {


    companion object {
        private var apiService: ApiService? = null

        fun instance(): ApiService {
            if (apiService == null) {
                apiService = ApiService()
            }
            return apiService!!
        }
    }


    /**
     * Instance of Http Client
     */
    private val client: OkHttpClient = OkHttpClient.Builder()
        .writeTimeout(Duration.ZERO)
        .callTimeout(Duration.ZERO)
        .connectTimeout(Duration.ZERO)
        .readTimeout(Duration.ZERO)
        .build()

    /**
     * Media type for regular
     */
    private val headers: MediaType = "application/json; charset=utf-8".toMediaType()


    /**
     * Gets token from shared preferences
     */
    private fun getTokenHeader(): String {
        val token = Helper.retrieveSharedPreference<String>(PreferenceEnum.TOKEN)
        return "Basic ${Base64.encodeToString("token:${token}".toByteArray(), Base64.NO_WRAP)}"
    }


    //region base methods
    /**
     * Get method
     *
     * @param url URL that GET request will target
     * @param usernamePassword OPTIONAL username and password for header
     */
    private fun get(url: String, usernamePassword: String? = null): Call {
        val request: Request = Request.Builder()
            .url(url)
            .addHeader(
                "Authorization",
                if (usernamePassword.isNullOrEmpty()) getTokenHeader()
                else "Basic ${
                    Base64.encodeToString(
                        usernamePassword.encodeToByteArray(),
                        Base64.NO_WRAP
                    )
                }"
            ).addHeader(
                "Authorization-For",
                Helper.AUTH_FOR_KEY!!
            )
            .build()
        return client.newCall(request)
    }


    private inline fun <reified T> post(url: String, value: T, addAuth: Boolean = true): Call {
        val data = value;
        val json = Helper.serializeData(value)
        val body: RequestBody = json.toRequestBody(headers)
        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader(
                "Authorization-For",
                Helper.AUTH_FOR_KEY!!
            )
        if (addAuth) request.addHeader("Authorization", getTokenHeader())

        return client.newCall(request.build())
    }

    private inline fun <reified T> delete(url: String): Call {
        val request: Request = Request.Builder()
            .url(url)
            .addHeader("Authorization", getTokenHeader())
            .addHeader(
                "Authorization-For",
                Helper.AUTH_FOR_KEY!!
            )
            .delete()
            .build()
        return client.newCall(request)
    }
    //endregion

    //region Auth
    fun login(username: String, password: String): Call {
        return get("${ApiRoutes.BASE_URL}/Login", "${username}:${password}")
    }

    fun register(newUser: User): Call {
        return post("${ApiRoutes.BASE_URL}/Register/User", newUser, false)
    }

    fun register(newShop: Shop): Call {
        return post("${ApiRoutes.BASE_URL}/Register/Shop", newShop, false)
    }

    //endregion

    //#region Location

    fun retrieveLocationByCoordinates(latLng: String): Call {
        return get("${ApiRoutes.LOCATION}/Coordinates/${Helper.toBase64(latLng)}")
    }

    fun discoverLocationByAddress(address: String): Call {
        return get("${ApiRoutes.LOCATION}/Discover/${Helper.toBase64(address)}")

    }
    //#endregion
}


