package hr.kcosic.app.dal

import android.util.Base64
import hr.kcosic.app.model.ResetPassword
import hr.kcosic.app.model.bases.ApiRoutes
import hr.kcosic.app.model.entities.*
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.Duration
import java.util.*


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
    private val client: OkHttpClient =
        OkHttpClient.Builder().writeTimeout(Duration.ZERO).callTimeout(Duration.ZERO)
            .connectTimeout(Duration.ZERO).readTimeout(Duration.ZERO).build()

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


    //region base HTTP methods
    /**
     * Get method
     *
     * @param url URL that GET request will target
     * @param usernamePassword OPTIONAL username and password for header
     */
    private fun get(url: String, usernamePassword: String? = null): Call {
        val request: Request = Request.Builder().url(url).addHeader(
            "Authorization", if (usernamePassword.isNullOrEmpty()) getTokenHeader()
            else "Basic ${
                Base64.encodeToString(
                    usernamePassword.encodeToByteArray(), Base64.NO_WRAP
                )
            }"
        ).addHeader(
            "Authorization-For", Helper.AUTH_FOR_KEY!!
        ).build()
        return client.newCall(request)
    }

    private fun get(url: String, addAuth: Boolean): Call {
        val request = Request.Builder().url(url)
        if (addAuth) request.addHeader("Authorization", getTokenHeader())
        return client.newCall(request.build())
    }


    private inline fun <reified T> post(url: String, value: T, addAuth: Boolean = true): Call {
        val json = Helper.serializeData(value)
        val body: RequestBody = json.toRequestBody(headers)
        val request = Request.Builder().url(url).post(body)

        if (addAuth) {

            request.addHeader("Authorization", getTokenHeader()).addHeader(
                "Authorization-For", Helper.AUTH_FOR_KEY!!
            )
        }

        return client.newCall(request.build())
    }

    private inline fun <reified T> put(url: String, value: T, addAuth: Boolean = true): Call {
        val json = Helper.serializeData(value)
        val body: RequestBody = json.toRequestBody(headers)
        val request = Request.Builder().url(url).put(body)
        if (addAuth) {

            request.addHeader("Authorization", getTokenHeader()).addHeader(
                "Authorization-For", Helper.AUTH_FOR_KEY!!
            )
        }

        return client.newCall(request.build())
    }

    private fun delete(url: String): Call {
        val request: Request =
            Request.Builder().url(url).addHeader("Authorization", getTokenHeader()).addHeader(
                "Authorization-For", Helper.AUTH_FOR_KEY!!
            ).delete().build()
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

    fun retrieveLocationByCoordinatesAndRadius(
        latLng: String, radius: Int, date: Date? = null
    ): Call {
        return get(
            "${ApiRoutes.LOCATION}/Coordinates/${Helper.toBase64(latLng)}/Radius/$radius${
                if (date != null) "?date=${
                    Helper.formatIsoDateTime(
                        date
                    )
                }" else ""
            }"
        )
    }

    fun retrieveLocationByCoordinates(latLng: String): Call {
        return get("${ApiRoutes.LOCATION}/Coordinates/${Helper.toBase64(latLng)}")
    }

    fun discoverLocationByAddress(address: String): Call {
        return get("${ApiRoutes.LOCATION}/Discover/${Helper.toBase64(address)}")
    }

    fun discoverLocationByAddress(address: String, addAuth: Boolean = true): Call {
        return get("${ApiRoutes.LOCATION}/Discover/${Helper.toBase64(address)}", addAuth)
    }

    fun retrieveLocation(locationId: Int, expanded: Boolean = false): Call {
        return get("${ApiRoutes.LOCATION}/${locationId}${if (expanded) "?expanded=true" else ""}")
    }

    fun retrieveLocations(): Call {
        return get(ApiRoutes.LOCATION)
    }

    fun deleteLocation(locationId: Int): Call {
        return delete("${ApiRoutes.LOCATION}/${locationId}")
    }

    fun createLocation(newLocation: Location): Call {
        return post(ApiRoutes.LOCATION, newLocation)
    }

    fun updateLocation(updatedLocation: Location): Call {
        return put(ApiRoutes.LOCATION, updatedLocation)
    }
    //#endregion

    //#region Appointment
    fun retrieveAppointment(appointmentId: Int, expanded: Boolean = false): Call {
        return get("${ApiRoutes.APPOINTMENT}/${appointmentId}${if (expanded) "?expanded=true" else ""}")
    }

    fun retrieveAppointments(): Call {
        return get(ApiRoutes.APPOINTMENT)
    }

    fun deleteAppointment(appointmentId: Int): Call {
        return delete("${ApiRoutes.APPOINTMENT}/${appointmentId}")
    }

    fun createAppointment(newAppointment: Appointment): Call {
        return post(ApiRoutes.APPOINTMENT, newAppointment)
    }

    fun updateAppointment(updatedAppointment: Appointment): Call {
        return put(ApiRoutes.APPOINTMENT, updatedAppointment)
    }

    //#endregion
    //#region Car
    fun retrieveCar(carId: Int, expanded: Boolean = false): Call {
        return get("${ApiRoutes.CAR}/${carId}${if (expanded) "?expanded=true" else ""}")
    }

    fun retrieveCars(): Call {
        return get(ApiRoutes.CAR)
    }

    fun deleteCar(carId: Int): Call {
        return delete("${ApiRoutes.CAR}/${carId}")
    }

    fun createCar(newCar: Car): Call {
        return post(ApiRoutes.CAR, newCar)
    }

    fun updateCar(updatedCar: Car): Call {
        return put("${ApiRoutes.CAR}/${updatedCar.Id}", updatedCar)
    }

    //#endregion

    //#region Request
    fun retrieveRequest(requestId: Int, expanded: Boolean = false): Call {
        return get("${ApiRoutes.REQUEST}/${requestId}${if (expanded) "?expanded=true" else ""}")
    }

    fun retrieveActiveUserRequest(userId: Int, inFuture: Boolean = false): Call {
        return get("${ApiRoutes.REQUEST}/User/${userId}/Active${if (inFuture) "?inFuture=true" else ""}")
    }

    fun retrieveCurrentRequest(): Call {
        return get("${ApiRoutes.REQUEST}/Current")
    }

    fun retrieveUpcomingRequest(): Call {
        return get("${ApiRoutes.REQUEST}/Next")
    }

    fun retrieveRequests(): Call {
        return get(ApiRoutes.REQUEST)
    }

    fun deleteRequest(requestId: Int): Call {
        return delete("${ApiRoutes.REQUEST}/${requestId}")
    }

    fun createRequest(newRequest: hr.kcosic.app.model.entities.Request): Call {
        return post(ApiRoutes.REQUEST, newRequest)
    }

    fun updateRequest(updatedRequest: hr.kcosic.app.model.entities.Request): Call {
        return put("${ApiRoutes.REQUEST}/${updatedRequest.Id}", updatedRequest)
    }

    //#endregion
    //#region Review
    fun retrieveReview(reviewId: Int, expanded: Boolean = false): Call {
        return get("${ApiRoutes.REVIEW}/${reviewId}${if (expanded) "?expanded=true" else ""}")
    }

    fun retrieveReviews(): Call {
        return get(ApiRoutes.REVIEW)
    }

    fun deleteReview(reviewId: Int): Call {
        return delete("${ApiRoutes.REVIEW}/${reviewId}")
    }

    fun createReview(newReview: Review, requestId: Int): Call {
        return post("${ApiRoutes.REVIEW}/Shop/$requestId", newReview)
    }

    fun updateReview(updatedReview: Review): Call {
        return put("${ApiRoutes.REVIEW}/${updatedReview.Id}", updatedReview)
    }

    //#endregion
    //#region Shop
    fun retrieveShop(shopId: Int, expanded: Boolean = false): Call {
        return get("${ApiRoutes.SHOP}/${shopId}${if (expanded) "?expanded=true" else ""}")
    }

    fun retrieveChildShops(parentShopId: Int): Call {
        return get("${ApiRoutes.SHOP}/${parentShopId}/Childs")
    }

    fun retrieveParentShop(childShopId: Int): Call {
        return get("${ApiRoutes.SHOP}/${childShopId}/Parent")
    }

    fun retrieveShopReviews(shopId: Int): Call {
        return get("${ApiRoutes.SHOP}/${shopId}/Reviews")
    }

    fun retrieveShops(): Call {
        return get(ApiRoutes.SHOP)
    }

    fun retrieveShopAvailability(shopId: Int, dateOfRepair: String): Call {
        return get("${ApiRoutes.SHOP}/$shopId/Availability/${Helper.toBase64(dateOfRepair)}")
    }

    fun retrieveShopNotificationData(): Call {
        return get("${ApiRoutes.SHOP}/NotificationData")
    }

    fun retrieveShopRecentReviews(): Call {
        return get("${ApiRoutes.SHOP}/Reviews/Recent")
    }

    fun deleteShop(shopId: Int): Call {
        return delete("${ApiRoutes.SHOP}/${shopId}")
    }

    fun createShop(newShop: Shop): Call {
        return post(ApiRoutes.SHOP, newShop)
    }

    fun updateShop(updatedShop: Shop): Call {
        return put("${ApiRoutes.SHOP}/${updatedShop.Id}", updatedShop)
    }

    //#endregion
    //#region User
    fun retrieveUser(userId: Int, expanded: Boolean = false): Call {
        return get("${ApiRoutes.USER}/${userId}${if (expanded) "?expanded=true" else ""}")
    }

    fun retrieveLatestRequest(): Call {
        return get("${ApiRoutes.USER}/Request/Latest")
    }

    fun retrieveUsers(): Call {
        return get(ApiRoutes.USER)
    }

    fun deleteUser(userId: Int): Call {
        return delete("${ApiRoutes.USER}/${userId}")
    }

    fun createUser(newUser: User): Call {
        return post(ApiRoutes.USER, newUser)
    }

    fun updateUser(updatedUser: User): Call {
        return put("${ApiRoutes.USER}/${updatedUser.Id}", updatedUser)
    }

    fun retrieveUserRequests(): Call {
        return get("${ApiRoutes.USER}/Requests")
    }

    fun resetPassword(userId: Int, oldPassword: String, newPassword: String): Call {
        val data = ResetPassword(
            Helper.toBase64(oldPassword), Helper.toBase64(newPassword)
        )
        return post("${ApiRoutes.USER}/${userId}/ResetPassword", data)
    }


    //#endregion
    // #region Issue
    fun retrieveIssue(issueId: Int, expanded: Boolean = false): Call {
        return get("${ApiRoutes.ISSUE}/${issueId}${if (expanded) "?expanded=true" else ""}")
    }

    fun retrieveIssues(): Call {
        return get(ApiRoutes.ISSUE)
    }

    fun deleteIssue(issueId: Int): Call {
        return delete("${ApiRoutes.ISSUE}/${issueId}")
    }

    fun createIssue(newIssue: Issue): Call {
        return post(ApiRoutes.ISSUE, newIssue)
    }

    fun updateIssue(updatedIssue: Issue): Call {
        return put("${ApiRoutes.ISSUE}/${updatedIssue.Id}", updatedIssue)
    }
    //#endregion
}


