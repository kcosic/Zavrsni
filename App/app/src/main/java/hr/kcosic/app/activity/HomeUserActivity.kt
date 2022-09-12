package hr.kcosic.app.activity

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ValidatedActivity
import hr.kcosic.app.model.entities.Token
import hr.kcosic.app.model.enums.ErrorCodeEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.SingleResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class HomeUserActivity : ValidatedActivity(), OnMapReadyCallback {
    private lateinit var homeContentView: ScrollView
    private lateinit var homeNoContentView: LinearLayout
    private lateinit var nextServiceDate: CardView
    private lateinit var nextServiceMap: CardView
    private lateinit var nextServiceDescription: CardView
    private lateinit var nextServiceDetails: CardView
    private lateinit var txtDateOfRepair: TextView
    private var mapServiceLocation: SupportMapFragment? = null
    private lateinit var txtTimeOfRepair: TextView
    private lateinit var txtRepairShopName: TextView
    private lateinit var txtRepairShopVat: TextView
    private lateinit var txtRequestDescription: TextView
    private lateinit var txtEstimatedTime: TextView
    private lateinit var txtEstimatedPrice: TextView
    private lateinit var progressBarHolder: FrameLayout
    private lateinit var googleMap: GoogleMap
    private var data: hr.kcosic.app.model.entities.Request? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_user)
        initializeComponents()
        retrieveActiveUserRequest()
    }

    override fun initializeComponents() {
        homeContentView = findViewById(R.id.homeContentView)
        homeNoContentView = findViewById(R.id.homeNoContentView)
        nextServiceDate = findViewById(R.id.nextServiceDate)
        nextServiceMap = findViewById(R.id.nextServiceMap)
        nextServiceDescription = findViewById(R.id.nextServiceDescription)
        nextServiceDetails = findViewById(R.id.nextServiceDetails)
        txtDateOfRepair = findViewById(R.id.txtDateOfRepair)
        txtTimeOfRepair = findViewById(R.id.txtTimeOfRepair)
        txtRepairShopName = findViewById(R.id.txtRepairShopName)
        txtRepairShopVat = findViewById(R.id.txtRepairShopVat)
        txtRequestDescription = findViewById(R.id.txtRequestDescription)
        txtEstimatedTime = findViewById(R.id.txtEstimatedTime)
        txtEstimatedPrice = findViewById(R.id.txtEstimatedPrice)
        progressBarHolder = findViewById(R.id.progressBarHolder)
    }
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        val shopLocation = data?.Shop?.Location
        val myPosition = LatLng(shopLocation?.Latitude!!, shopLocation.Longitude!!)

        googleMap.addMarker(
            MarkerOptions()
                .position(myPosition)
                .title(shopLocation.toString())
        )
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(myPosition))
    }

    private fun retrieveActiveUserRequest() {
        apiService.retrieveActiveUserRequest(getUser().Id!!).enqueue((object :
            Callback {
            val mainHandler = Handler(applicationContext.mainLooper)

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleRetrieveLocationException(call, e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleRetrieveLocationResponse(response)
                }
            }
        }
        ))
    }

    fun handleRetrieveLocationResponse(response: Response) {

        val resp: BaseResponse = Helper.parseStringResponse<SingleResponse<Token>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            val data = resp.Data as hr.kcosic.app.model.entities.Request

            try {
                mapServiceLocation = supportFragmentManager.findFragmentById(R.id.mapServiceLocation) as? SupportMapFragment
                mapServiceLocation?.getMapAsync(this)
                this.data = data
                populateViewWithData()

                progressBarHolder.visibility = View.GONE
                showContent(true)


            } catch (e: Exception) {
                showContent(false)
                progressBarHolder.visibility = View.GONE
                Helper.showLongToast(this, e.message.toString())
            }

        } else {
            if(resp is ErrorResponse && resp.ErrorCode == ErrorCodeEnum.RecordNotFound){
               showContent(false)
            }
            else {
                handleRetrieveLocationError(resp.Message!!)
            }
            progressBarHolder.visibility = View.GONE

        }
    }

    private fun populateViewWithData() {
        txtDateOfRepair.text = Helper.formatDate(data?.Issues?.get(0)?.DateOfSubmission!!)
        txtDateOfRepair.text = Helper.formatTime(data?.Issues?.get(0)?.DateOfSubmission!!)
        txtRepairShopName.text = data?.Shop?.ShortName
        txtRepairShopVat.text = data?.Shop?.Vat
        txtRequestDescription.text = data?.Issues?.get(0)?.Summary
        txtEstimatedTime.text = Helper.formatDate(data?.EstimatedFinishDate!!)
        txtEstimatedPrice.text = data?.EstimatedPrice?.toString()
    }


    private fun handleRetrieveLocationError(message: String) {
        Helper.showLongToast(this, message)
    }

    fun handleRetrieveLocationException(call: Call, e: Exception) {
        call.cancel()
        Helper.showLongToast(this, e.message.toString())

    }

    private fun showContent(hasContent: Boolean){
        if(hasContent){
            homeContentView.visibility = View.VISIBLE
            homeNoContentView.visibility = View.GONE
        }
        else {
            homeContentView.visibility = View.GONE
            homeNoContentView.visibility = View.VISIBLE
        }

    }

}