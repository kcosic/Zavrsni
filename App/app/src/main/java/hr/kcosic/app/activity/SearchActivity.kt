package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Parcel
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ContextInstance
import hr.kcosic.app.model.bases.ValidatedActivityWithNavigation
import hr.kcosic.app.model.bases.hideKeyboard
import hr.kcosic.app.model.entities.Location
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.ListResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.InvalidObjectException


class SearchActivity : ValidatedActivityWithNavigation(ActivityEnum.SEARCH), OnMapReadyCallback {
    private lateinit var swUseCurrentLocation: SwitchCompat
    private lateinit var actvSearchAddress: AutoCompleteTextView
    private lateinit var txtDateOfRepair: EditText
    private lateinit var cardMap: CardView
    private lateinit var vScroll: ScrollView
    private lateinit var btnSelectLocation: Button


    private lateinit var googleMap: GoogleMap
    private var map: SupportMapFragment? = null

    private var userMarker: Marker? = null
    private var shopMarkers: MutableList<Marker> = mutableListOf()
    private var userPosition: LatLng? = null
    private var locationMarkerMap: MutableMap<Marker, Location> = mutableMapOf()
    private var selectedLocation: Location? = null

    val handleAddressSearch = debounce<Unit>(1000, coroutineScope) {
        searchAddress(actvSearchAddress.text?.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeComponents()
    }

    override fun initializeComponents() {
        swUseCurrentLocation = findViewById(R.id.swUseCurrentLocation)
        actvSearchAddress = findViewById(R.id.actvSearchAddress)
        txtDateOfRepair = findViewById(R.id.txtDateOfRepair)
        cardMap = findViewById(R.id.cardMap)
        vScroll = findViewById(R.id.vScroll)
        btnSelectLocation = findViewById(R.id.btnSelectLocation)
        map = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        map?.getMapAsync(this)

        btnSelectLocation.isEnabled = false

        actvSearchAddress.addTextChangedListener(object : TextWatcher {
            var textBefore: String? = null

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textBefore = p0.toString()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString() != textBefore) handleAddressSearch(Unit)
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        swUseCurrentLocation.setOnCheckedChangeListener { buttonView, isChecked ->
            coroutineScope.launch {
                val mainHandler = Handler(applicationContext.mainLooper)
                val actvVisibility: Boolean
                var buttonViewActivated = true
                var latLng: LatLng? = null
                if (isChecked && Helper.hasLocationPermissions()) {
                    actvVisibility = false
                    latLng = Helper.retrievePhoneLocation()
                } else if (isChecked && !Helper.hasLocationPermissions()) {
                    Helper.getLocationPermission()
                    if (Helper.hasLocationPermissions()) {
                        actvVisibility = false
                        latLng = Helper.retrievePhoneLocation()
                    } else {
                        actvVisibility = true
                        buttonViewActivated = false
                    }
                } else {
                    actvVisibility = true
                }

                mainHandler.post {
                    actvSearchAddress.visibility = if (actvVisibility) View.VISIBLE else View.GONE
                    buttonView.isEnabled = buttonViewActivated
                    if(latLng != null){
                        createOrUpdateMarkerFromPosition(latLng, "Your location", true)
                        retrieveLocalShops(latLng)
                    }
                    else if (!actvVisibility) {
                        Helper.showLongToast(ContextInstance.getContext()!!,"Error occurred while trying to retrieve your live location.")
                    }


                }
            }

        }

        swUseCurrentLocation.isEnabled = Helper.hasLocationPermissions()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.setOnMarkerClickListener {
            val location = locationMarkerMap[it]
            if(location != null){
                btnSelectLocation.isEnabled = true
                selectedLocation = location
            } else {
                btnSelectLocation.isEnabled = false
                selectedLocation = null
            }

            false
        }
        googleMap.setOnCameraMoveListener {
            vScroll.requestDisallowInterceptTouchEvent(true)
        }
    }

    private fun searchAddress(address: String?) {
        if (address != null && address.isNotEmpty()) {
            apiService.discoverLocationByAddress(address).enqueue(object : Callback {
                val mainHandler = Handler(applicationContext.mainLooper)

                override fun onFailure(call: Call, e: IOException) {
                    mainHandler.post {
                        handleApiResponseException(call, e)
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    mainHandler.post {
                        handleDiscoverAddressResponse(response)
                    }
                }
            })
        }
    }

    @SuppressLint("PrivateResource")
    fun handleDiscoverAddressResponse(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<ListResponse<Location>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is ListResponse<*>) {

            try {
                @Suppress("UNCHECKED_CAST")
                val data = resp.Data as ArrayList<Location>

                actvSearchAddress.setAdapter(
                    ArrayAdapter(
                        this,
                        com.google.android.material.R.layout.mtrl_auto_complete_simple_item,
                        data
                    )
                )

                //set clicked location
                actvSearchAddress.setOnItemClickListener { adapterView, _, index, _ ->
                    val location = adapterView.adapter.getItem(index) as Location
                    actvSearchAddress.dismissDropDown()
                    setLocationToMap(location)
                }
                actvSearchAddress.showDropDown()

            } catch (e: InvalidObjectException) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
        progressBarHolder.visibility = View.GONE

    }

    fun handleApiResponseException(call: Call, e: Exception) {
        call.cancel()
        Helper.showLongToast(this, e.message.toString())
        progressBarHolder.visibility = View.VISIBLE

    }

    private fun setLocationToMap(location: Location) {
        if (location.Latitude != null && location.Longitude != null) {
            val latLng = LatLng(location.Latitude!!, location.Longitude!!)
            updateAndRefocusUserMarker(latLng, "${location.Street} ${location.StreetNumber}, ${location.City}")
            actvSearchAddress.clearFocus()
            hideKeyboard(actvSearchAddress.rootView)
            retrieveLocalShops(location)
        }
    }

    private fun updateAndRefocusUserMarker(latLng: LatLng, title: String) {
        if(userMarker != null){
            userMarker!!.remove()
        }
        userMarker = googleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(title)
        )
        val mapCircle = CircleOptions()
            .center(latLng)
            .strokeColor(R.color.tertiary_100)
            .radius(10000.0)
            .clickable(true)
            .visible(true)
        googleMap.addCircle(mapCircle)
        val cameraPosition2 = CameraPosition.Builder()
            .target(latLng)
            .zoom(10.5F)
            .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2 ))
    }

    fun retrieveLocalShops(location:Location){
        progressBarHolder.visibility = View.VISIBLE

        apiService.retrieveLocationByCoordinatesAndRadius(
            "${location.Latitude}!${location.Longitude}",
            10000
        ).enqueue(object : Callback {
            val mainHandler = Handler(applicationContext.mainLooper)
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post{
                    handleApiResponseException(call, e);
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post{
                    handleRetrieveLocationByCoordinatesAndRadiusResponse(response);
                }
            }

        })
    }

    fun retrieveLocalShops(latLng: LatLng){
        progressBarHolder.visibility = View.VISIBLE
        apiService.retrieveLocationByCoordinatesAndRadius(
            "${latLng.latitude}!${latLng.longitude}",
            10000
        ).enqueue(object : Callback {
            val mainHandler = Handler(applicationContext.mainLooper)
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post{
                    handleApiResponseException(call, e);
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post{
                    handleRetrieveLocationByCoordinatesAndRadiusResponse(response);
                }
            }

        })
    }

    fun handleRetrieveLocationByCoordinatesAndRadiusResponse(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<ListResponse<Location>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is ListResponse<*>) {

            try {
                @Suppress("UNCHECKED_CAST")
                val data = resp.Data as ArrayList<Location>

                shopMarkers = mutableListOf()
                locationMarkerMap = mutableMapOf()
                data.filter { location -> location.Latitude != null && location.Longitude != null }
                    .forEach {
                        val marker = createOrUpdateMarkerFromPosition(
                            LatLng(it.Latitude!!, it.Longitude!!),
                            it.toString(),
                            false,

                        )
                        if(marker != null){
                            locationMarkerMap[marker] = it
                        }
                    }
            } catch (e: InvalidObjectException) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
        progressBarHolder.visibility = View.GONE

    }

    private fun createOrUpdateMarkerFromPosition(
        position: LatLng? = null,
        markerText: String,
        isUserPosition: Boolean = false,
        shopMarker: Marker? = null,

        ) : Marker? {
        if (position == null) {
            return null;
        }
        var marker : Marker? = null;
        if (isUserPosition) {
            updateAndRefocusUserMarker(position, markerText)
        } else {
            val newMarker: Marker? = googleMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(markerText)
                    .icon(BitmapDescriptorFactory.fromBitmap(AppCompatResources.getDrawable(this, R.drawable.screwdriver_wrench)!!.toBitmap(60,60)))
            )
            if (newMarker != null) {
                shopMarkers.add(newMarker)
                marker = newMarker;
            }
        }
        return marker;
    }

    private fun <T> debounce(
        @Suppress("SameParameterValue") waitMs: Long,
        coroutineScope: CoroutineScope,
        destinationFunction: (T) -> Unit
    ): (T) -> Unit {
        var debounceJob: Job? = null
        return { param: T ->
            debounceJob?.cancel()
            debounceJob = coroutineScope.launch {
                delay(waitMs)
                destinationFunction(param)
            }
        }
    }

}