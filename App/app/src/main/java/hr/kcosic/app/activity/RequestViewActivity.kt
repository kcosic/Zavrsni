package hr.kcosic.app.activity

import android.os.Bundle
import android.os.Handler
import android.widget.CheckBox
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ValidatedActivityWithNavigation
import hr.kcosic.app.model.entities.Request
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.SingleResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.*

class RequestViewActivity : ValidatedActivityWithNavigation(ActivityEnum.REQUEST_VIEW),
    OnMapReadyCallback {

    private lateinit var tvDateOfRepair: TextView
    private lateinit var tvTimeOfRepair: TextView
    private lateinit var tvRepairShopName: TextView
    private lateinit var tvRepairShopVat: TextView
    private lateinit var tvRequestDescription: TextView
    private lateinit var tvEstimatedTime: TextView
    private lateinit var tvEstimatedPrice: TextView
    private lateinit var tvVehicle: TextView
    private lateinit var tvDateOfFinish: TextView
    private lateinit var tvActualPrice: TextView
    private lateinit var tvUserConsent: CheckBox
    private lateinit var tvShopConsent: CheckBox
    private lateinit var tvCompleted: CheckBox
    private lateinit var googleMap: GoogleMap
    private var request: Request? = null
    private var requestId: Int? = null
    private var mapServiceLocation: SupportMapFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeComponents()
        retrieveDataFromIntent()
        retrieveRequest()
    }

    override fun initializeComponents() {

        tvDateOfRepair = findViewById(R.id.tvDateOfRepair)
        tvTimeOfRepair = findViewById(R.id.tvTimeOfRepair)
        tvRepairShopName = findViewById(R.id.tvRepairShopName)
        tvRepairShopVat = findViewById(R.id.tvRepairShopVat)
        tvRequestDescription = findViewById(R.id.tvRequestDescription)
        tvEstimatedTime = findViewById(R.id.tvEstimatedTime)
        tvEstimatedPrice = findViewById(R.id.tvEstimatedPrice)
        tvUserConsent = findViewById(R.id.tvUserConsent)
        tvShopConsent = findViewById(R.id.tvShopConsent)
        tvCompleted = findViewById(R.id.tvCompleted)
        tvVehicle = findViewById(R.id.tvVehicle)
        tvDateOfFinish = findViewById(R.id.tvDateOfFinish)
        tvActualPrice = findViewById(R.id.tvActualPrice)
    }

    private fun retrieveDataFromIntent() {
        val stringMap = intent.getStringExtra(RequestListActivity.REQUEST_VIEW_KEY)
        val map = Helper.deserializeObject<Map<String, String>>(stringMap!!)
        requestId = map["requestId"]!!.toInt()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val shopLocation = request?.Shop?.Location
        val myPosition = LatLng(shopLocation?.Latitude!!, shopLocation.Longitude!!)

        googleMap.addMarker(
            MarkerOptions()
                .position(myPosition)
                .title(shopLocation.toString())
        )
        val cameraPosition2 = CameraPosition.Builder()
            .target(myPosition)
            .zoom(10.5F)
            .build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2))
    }

    private fun retrieveRequest() {
        showSpinner()
        apiService.retrieveRequest(requestId!!, true).enqueue((object :
            Callback {
            

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    hideSpinner()

                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleRetrieveRequestResponse(response)
                    hideSpinner()
                }
            }
        }
     ))
    }

    fun handleRetrieveRequestResponse(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Request>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            try {
                this.request = resp.Data as Request
                mapServiceLocation =
                    supportFragmentManager.findFragmentById(R.id.mapServiceLocation) as? SupportMapFragment
                mapServiceLocation?.getMapAsync(this)
                populateViewWithData()
            } catch (e: Exception) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)

        }
    }

    private fun populateViewWithData() {
        tvDateOfRepair.text = Helper.formatDate(request?.RequestDate!!)
        tvTimeOfRepair.text = Helper.formatTime(request?.RequestDate!!)
        tvRepairShopName.text = request?.Shop?.ShortName
        tvRepairShopVat.text = request?.Shop?.Vat
        tvRequestDescription.text = request?.IssueDescription
        tvEstimatedTime.text = if(request?.EstimatedRepairHours != null) request?.EstimatedRepairHours.toString() else "N/A"
        tvEstimatedPrice.text = request?.EstimatedPrice?.toString()
        tvVehicle.text = request?.Car!!.toString()
        tvActualPrice.text = if(request?.Price != null) request?.Price!!.toString() else "N/A"
        tvDateOfFinish.text = if(request?.FinishDate != null) Helper.formatDate(request?.FinishDate!!) else "N/A"
        tvShopConsent.text = Helper.formatDate(request?.ShopAcceptedDate!!)
        tvShopConsent.setCompoundDrawablesWithIntrinsicBounds(
            when (request?.ShopAccepted) {
                true -> R.drawable.check_green
                false -> R.drawable.xmark_red
                else -> R.drawable.minus_gray
            }
            ,0,0,0)

        tvUserConsent.text = Helper.formatDate(request?.UserAcceptedDate!!)
        tvUserConsent.setCompoundDrawablesWithIntrinsicBounds(
            when (request?.UserAccepted) {
                true -> R.drawable.check_green
                false -> R.drawable.xmark_red
                else -> R.drawable.minus_gray
        }, 0,0,0)

        tvCompleted.text = Helper.formatDate(request?.FinishDate!!)
        tvCompleted.setCompoundDrawablesWithIntrinsicBounds(
            when (request?.Completed) {
                true -> R.drawable.check_green
                false -> R.drawable.xmark_red
                else -> R.drawable.minus_gray
        }, 0,0,0)

    }
}