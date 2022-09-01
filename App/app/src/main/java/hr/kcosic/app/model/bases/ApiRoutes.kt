package hr.kcosic.app.model.bases

import hr.kcosic.app.BuildConfig
import java.lang.Error

object ApiRoutes {
    val BASE_URL: String = if(BuildConfig.DEBUG) "http://localhost:31420" else throw Error("Release url not configured")

    val APPOINTMENTS: String = "${BASE_URL}/api/Appointments"
    val AUTH: String= "${BASE_URL}/api/Auth"
    val CAR: String= "${BASE_URL}/api/Car"
    val ISSUE: String= "${BASE_URL}/api/Issue"
    val LOCATION: String= "${BASE_URL}/api/Location"
    val REPAIR_HISTORY: String= "${BASE_URL}/api/RepairHistory"
    val REQUEST: String= "${BASE_URL}/api/Request"
    val REVIEW: String= "${BASE_URL}/api/Review"
    val SHOP: String= "${BASE_URL}/api/Shop"

}