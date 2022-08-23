package hr.kcosic.app.model.bases

import hr.kcosic.app.BuildConfig
import java.lang.Error

object ApiRoutes {
    private val BASE_URL: String = if(BuildConfig.DEBUG) "http://localhost:31420" else throw Error("Release url not configured")

    val APPOINTMENTS: String = "${BASE_URL}/api/appointments"
    val AUTH: String= "${BASE_URL}/api/Auth"
    val CARS: String= "${BASE_URL}/api/Cars"
    val ISSUES: String= "${BASE_URL}/api/Issues"
    val LOCATIONS: String= "${BASE_URL}/api/Locations"
    val REPAIR_HISTORIES: String= "${BASE_URL}/api/RepairHistories"
    val REQUESTS: String= "${BASE_URL}/api/Requests"
    val REVIEWS: String= "${BASE_URL}/api/Reviews"
    val SHOPS: String= "${BASE_URL}/api/Shops"

}