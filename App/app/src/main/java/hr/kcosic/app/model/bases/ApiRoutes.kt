package hr.kcosic.app.model.bases

import hr.kcosic.app.BuildConfig
import java.lang.Error

object ApiRoutes {
    const val BASE_URL: String = "https://zavrsniapi.azurewebsites.net" //"http://localhost:31420"

    const val APPOINTMENT: String = "${BASE_URL}/api/Appointment"
    const val AUTH: String= "${BASE_URL}/api/Auth"
    const val CAR: String= "${BASE_URL}/api/Car"
    const val LOCATION: String= "${BASE_URL}/api/Location"
    const val REQUEST: String= "${BASE_URL}/api/Request"
    const val REVIEW: String= "${BASE_URL}/api/Review"
    const val SHOP: String= "${BASE_URL}/api/Shop"
    const val USER: String= "${BASE_URL}/api/User"

}