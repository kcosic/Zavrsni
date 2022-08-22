package hr.kcosic.app.model.bases

import hr.kcosic.app.BuildConfig
import java.lang.Error

object ApiRoutes {
    private val BASE_URL: String = if(BuildConfig.DEBUG) "http://localhost:31420" else throw Error("Release url not configured")

    val APPOINTMENTS: String = "${BASE_URL}/appointments"
    val AUTH: String= "${BASE_URL}/auth"
    val CARS: String= "${BASE_URL}/cars"
    val ISSUES: String= "${BASE_URL}/issues"
    val LOCATIONS: String= "${BASE_URL}/locations"
    val REPAIR_HISTORIES: String= "${BASE_URL}/repairHistories"
    val REQUESTS: String= "${BASE_URL}/requests"
    val REVIEWS: String= "${BASE_URL}/reviews"
    val SHOPS: String= "${BASE_URL}/shops"

}