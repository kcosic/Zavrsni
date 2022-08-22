package hr.kcosic.app.dal

import android.util.Base64
import hr.kcosic.app.model.bases.ApiRoutes
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.entities.User
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.SingleResponse
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


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
    private val client: OkHttpClient = OkHttpClient()

    /**
     * Media type for regular
     */
    private val headers: MediaType = "application/json; charset=utf-8".toMediaType()


    /**
     * Gets token from shared preferences
     */
    private fun getTokenHeader(): String {
        val token = Helper.retrieveSharedPreference<String>(PreferenceEnum.TOKEN.getName())
        return Base64.encodeToString("token:${token}".toByteArray(), Base64.NO_WRAP)
    }

    fun login(username: String, password: String): BaseResponse {
        val response = get(ApiRoutes.AUTH, "${username}:${password}")

        return try {
            Helper.deserializeObject(response) as SingleResponse<User>
        } catch(ex: Error){
            Helper.deserializeObject(response) as ErrorResponse
        }
    }


    //region base methods
    /**
     * Get method
     *
     * @param url URL that GET request will target
     * @param usernamePassword OPTIONAL username and password for header
     */
    private fun get(url: String, usernamePassword: String? = null): String {
        val request: Request = Request.Builder()
            .url(url)
            .addHeader(
                "Authorization",
                if(usernamePassword.isNullOrEmpty()) getTokenHeader()
                else Base64.encodeToString(usernamePassword.encodeToByteArray(), Base64.NO_WRAP)
            )
            .build()

        client.newCall(request).execute().use { response -> return response.body!!.string() }
    }


    private inline fun <reified T> post(url: String, value: T): String {
        val body: RequestBody = Helper.serializeData(value).toRequestBody(headers)
        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Authorization", getTokenHeader() )
            .build()
        client.newCall(request).execute().use { response -> return response.body!!.string() }
    }

    private inline fun <reified T> delete(url: String): String {
        val request: Request = Request.Builder()
            .url(url)
            .addHeader("Authorization", getTokenHeader() )
            .delete()
            .build()
        client.newCall(request).execute().use { response -> return response.body!!.string() }
    }
    //endregion

}


