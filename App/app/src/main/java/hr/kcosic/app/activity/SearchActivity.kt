package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.*
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hr.kcosic.app.R
import hr.kcosic.app.adapter.ReviewsAdapter
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ContextInstance
import hr.kcosic.app.model.bases.ValidatedActivityWithNavigation
import hr.kcosic.app.model.bases.hideKeyboard
import hr.kcosic.app.model.entities.Location
import hr.kcosic.app.model.entities.Shop
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
import java.text.SimpleDateFormat
import java.util.*


class SearchActivity : ValidatedActivityWithNavigation(ActivityEnum.SEARCH), OnMapReadyCallback {
    companion object{
        const val NEW_REQUEST_KEY = "f23789hui39r278h"
    }
    private lateinit var swUseCurrentLocation: SwitchCompat
    private lateinit var actvSearchAddress: AutoCompleteTextView
    private lateinit var etDateOfRepair: EditText

    private lateinit var tvRepairShopName: TextView
    private lateinit var llRepairShopName: LinearLayout
    private lateinit var llSearch: LinearLayout
    private lateinit var pbActvSearch: ProgressBar
    private lateinit var llRepairShopRate: LinearLayout
    private lateinit var tvRepairShopRate: TextView
    private lateinit var cardMap: CardView
    private lateinit var vScroll: ScrollView
    private lateinit var btnSelectLocation: Button
    private lateinit var btnRefreshShops: FloatingActionButton

    private lateinit var googleMap: GoogleMap
    private var map: SupportMapFragment? = null

    private var userMarker: Marker? = null
    private var shopMarkers: MutableList<Marker> = mutableListOf()
    private var locationMarkerMap: MutableMap<String, Location> = mutableMapOf()
    private var selectedLocation: Location? = null
    private val repairCalendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    private var textBefore: String? = null
    private lateinit var rvReviews : RecyclerView
    private lateinit var reviewsAdapter: ReviewsAdapter

    val handleAddressSearch = debounce<Unit>(1000, coroutineScope) {
        mainHandler.post{
            showComponent(pbActvSearch)
            searchAddress(actvSearchAddress.text?.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeComponents()
    }

    override fun initializeComponents() {
        swUseCurrentLocation = findViewById(R.id.swUseCurrentLocation)
        actvSearchAddress = findViewById(R.id.actvSearchAddress)
        etDateOfRepair = findViewById(R.id.etDateOfRepair)
        llRepairShopName = findViewById(R.id.llRepairShopName)
        llRepairShopRate = findViewById(R.id.llRepairShopRate)
        llSearch = findViewById(R.id.llSearch)
        tvRepairShopName = findViewById(R.id.tvRepairShopName)
        pbActvSearch = findViewById(R.id.pbActvSearch)
        tvRepairShopRate = findViewById(R.id.tvRepairShopRate)
        cardMap = findViewById(R.id.cardMap)
        vScroll = findViewById(R.id.vScroll)
        btnSelectLocation = findViewById(R.id.btnSelectLocation)
        btnRefreshShops = findViewById(R.id.btnRefreshShops)
        rvReviews = findViewById(R.id.rvReviews)
        map = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        map?.getMapAsync(this)
        etDateOfRepair.setText(Helper.formatDate(Calendar.getInstance().time))
        hideComponent(llRepairShopRate)
        hideComponent(llRepairShopName)
        hideComponent(btnSelectLocation)
        val date =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                repairCalendar.set(Calendar.YEAR, year)
                repairCalendar.set(Calendar.MONTH, month)
                repairCalendar.set(Calendar.DAY_OF_MONTH, day)
                repairCalendar.set(Calendar.HOUR, 0)
                repairCalendar.set(Calendar.MINUTE, 0)
                repairCalendar.set(Calendar.SECOND, 0)
                repairCalendar.set(Calendar.MILLISECOND, 0)
                updateRepairDateLabel()
            }
        etDateOfRepair.setOnClickListener {
            DatePickerDialog(
                this,
                date,
                repairCalendar.get(Calendar.YEAR),
                repairCalendar.get(Calendar.MONTH),
                repairCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        actvSearchAddress.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textBefore = p0.toString()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString() != textBefore) handleAddressSearch(Unit)
            }

        })

        swUseCurrentLocation.setOnCheckedChangeListener { buttonView, isChecked ->
            showSpinner()
            coroutineScope.launch {
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
                    if (actvVisibility) {
                        showComponent(llSearch)
                    } else {
                        hideComponent(llSearch)
                    }
                    buttonView.isEnabled = buttonViewActivated
                    if (latLng != null) {
                        createOrUpdateMarkerFromPosition(latLng, "Your location", true)
                        retrieveLocalShops(latLng)
                    } else if (!actvVisibility) {
                        Helper.showLongToast(
                            ContextInstance.getContext()!!,
                            "Error occurred while trying to retrieve your live location."
                        )
                    } else {
                        hideSpinner()
                    }
                }
            }
        }

        btnRefreshShops.setOnClickListener{
            if(userMarker != null){
                @SuppressLint("SimpleDateFormat")
                val formatter = SimpleDateFormat("dd.MM.yyyy")

                if(etDateOfRepair.text.toString().isNotEmpty()){
                    val dateOfRepair = formatter.parse(etDateOfRepair.text.toString()) ?: null
                    retrieveLocalShops(userMarker!!.position, dateOfRepair)
                }
            }
        }

        btnSelectLocation.setOnClickListener{
            if(selectedLocation != null && etDateOfRepair.text.toString().isNotEmpty()){
                val baggage: MutableMap<String, String> = mutableMapOf()
                baggage["selectedLocation"] = Helper.serializeData(selectedLocation)
                baggage["dateOfRepair"] = etDateOfRepair.text.toString()
                Helper.openActivity(this, ActivityEnum.NEW_REQUEST, baggage, NEW_REQUEST_KEY)
            }
        }

        swUseCurrentLocation.isChecked = Helper.hasLocationPermissions()
    }

    @SuppressLint("ClickableViewAccessibility", "NotifyDataSetChanged")
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.setOnMarkerClickListener {
            val locationMarker = locationMarkerMap
            val location = locationMarker[it.id]
            if (location != null) {
                val shop: Shop? =  if(location.Shops != null && location.Shops!!.size > 0) location.Shops!![0] else null;
                tvRepairShopName.text = shop?.ShortName
                tvRepairShopRate.text = shop?.HourlyRate?.toString()

                if(shop?.Reviews != null && shop.Reviews!!.size > 0){
                    showComponent(rvReviews)

                    reviewsAdapter = ReviewsAdapter(shop.Reviews!!)
                    rvReviews.layoutManager = LinearLayoutManager(this)
                    rvReviews.adapter = reviewsAdapter
                    reviewsAdapter.notifyDataSetChanged()
                }
                else {
                    hideComponent(rvReviews)
                }
                selectedLocation = location
                showComponent(llRepairShopName)
                showComponent(llRepairShopRate)
                showComponent(btnSelectLocation)



            } else {
                hideComponent(rvReviews)
                hideComponent(llRepairShopName)
                hideComponent(llRepairShopRate)
                hideComponent(btnSelectLocation)
                selectedLocation = null
            }

            false
        }
        googleMap.setOnMapClickListener {
            hideComponent(llRepairShopRate)
            hideComponent(rvReviews)
            hideComponent(llRepairShopName)
            hideComponent(btnSelectLocation)
            selectedLocation = null
        }
        googleMap.setOnCameraMoveListener {
            vScroll.requestDisallowInterceptTouchEvent(true)
        }
    }

    private fun searchAddress(address: String?) {
        if (address != null && address.isNotEmpty()) {
            actvSearchAddress.isEnabled = false
            apiService.discoverLocationByAddress(address).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    mainHandler.post {
                        handleApiResponseException(call, e)
                        hideSpinner()
                        hideComponent(pbActvSearch)
                        actvSearchAddress.isEnabled = true
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    mainHandler.post {
                        handleDiscoverAddressResponse(response)
                        hideSpinner()
                        hideComponent(pbActvSearch)
                        actvSearchAddress.isEnabled = true
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
    }

    private fun setLocationToMap(location: Location) {
        if (location.Latitude != null && location.Longitude != null) {
            val latLng = LatLng(location.Latitude!!, location.Longitude!!)
            updateAndRefocusUserMarker(
                latLng,
                "${location.Street} ${location.StreetNumber}, ${location.City}"
            )
            actvSearchAddress.clearFocus()
            hideKeyboard(actvSearchAddress.rootView)
            retrieveLocalShops(location)
        }
    }

    private fun updateAndRefocusUserMarker(latLng: LatLng, title: String) {
        if (userMarker != null) {
            userMarker!!.remove()
            googleMap.clear()

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
            .clickable(false)
            .visible(true)

        googleMap.addCircle(mapCircle)
        val cameraPosition2 = CameraPosition.Builder()
            .target(latLng)
            .zoom(10.5F)
            .build()
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2))
    }

    private fun retrieveLocalShops(location: Location, date: Date? = null) {
        showSpinner()

        apiService.retrieveLocationByCoordinatesAndRadius(
            "${location.Latitude}!${location.Longitude}",
            10000,
            date
        ).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    hideSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleRetrieveLocationByCoordinatesAndRadiusResponse(response)
                    hideSpinner()
                }
            }
        })
    }

    private fun retrieveLocalShops(latLng: LatLng, date: Date? = null) {
        showSpinner()
        apiService.retrieveLocationByCoordinatesAndRadius(
            "${latLng.latitude}!${latLng.longitude}",
            10000,
            date
        ).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    hideSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleRetrieveLocationByCoordinatesAndRadiusResponse(response)
                    hideSpinner()
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

                shopMarkers.forEach {
                    it.remove()
                }
                shopMarkers = mutableListOf()
                locationMarkerMap = mutableMapOf()
                data.filter { location -> location.Latitude != null && location.Longitude != null }
                    .forEach {
                        val marker = createOrUpdateMarkerFromPosition(
                            LatLng(it.Latitude!!, it.Longitude!!),
                            it.toString(),
                            false,

                            )
                        if (marker != null) {
                            locationMarkerMap[marker.id] = it
                        }
                    }

            } catch (e: InvalidObjectException) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    private fun createOrUpdateMarkerFromPosition(
        position: LatLng? = null,
        markerText: String,
        isUserPosition: Boolean = false,
    ): Marker? {
        if (position == null) {
            return null
        }
        var marker: Marker? = null
        if (isUserPosition) {
            updateAndRefocusUserMarker(position, markerText)
        } else {
            val bitmap = AppCompatResources.getDrawable(this, R.drawable.screwdriver_wrench)!!
                .toBitmap(60, 60)
            val paint = Paint()
            val filter: ColorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(this, R.color.tertiary_900),
                PorterDuff.Mode.SRC_IN
            )
            paint.colorFilter = filter
            val canvas = Canvas(bitmap)
            canvas.drawBitmap(bitmap, 0F, 0F, paint)

            val newMarker: Marker? = googleMap.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(markerText)
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
            )
            if (newMarker != null) {
                shopMarkers.add(newMarker)
                marker = newMarker
            }
        }
        return marker
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

    @SuppressLint("SimpleDateFormat")
    private fun updateRepairDateLabel() {
        etDateOfRepair.setText(Helper.formatDate(repairCalendar.time))
    }


}