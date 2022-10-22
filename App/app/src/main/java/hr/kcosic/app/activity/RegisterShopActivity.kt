package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import hr.kcosic.app.R
import hr.kcosic.app.model.listeners.OnNegativeButtonClickListener
import hr.kcosic.app.model.listeners.OnPositiveButtonClickListener
import hr.kcosic.app.model.bases.BaseActivity
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.hideKeyboard
import hr.kcosic.app.model.entities.Location
import hr.kcosic.app.model.entities.Shop
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.ListResponse
import hr.kcosic.app.model.responses.SingleResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.InvalidObjectException

class RegisterShopActivity : BaseActivity(), OnMapReadyCallback {
    private var map: SupportMapFragment? = null
    private lateinit var btnFinishRegistration: Button
    private lateinit var btnBack: Button
    private lateinit var btnLocation: Button
    private lateinit var actvAddress: AutoCompleteTextView
    private lateinit var etVat: EditText
    private lateinit var etShortName: EditText
    private lateinit var etLegalName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRepeatPassword: EditText
    private lateinit var locationDialog: AlertDialog
    private lateinit var progressBarHolder: FrameLayout
    private lateinit var dialogProgressbar: FrameLayout
    private lateinit var googleMap: GoogleMap
    private var mapMarker: Marker? = null
    private var location: Location? = null
    private var isSearching = false

    val handleAddressSearch = debounce<Unit>(1000, coroutineScope) {
        searchAddress(actvAddress.text?.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_shop)
        initializeComponents()
    }

    override fun initializeComponents() {
        btnFinishRegistration = findViewById(R.id.btnFinishRegistration)
        btnLocation = findViewById(R.id.btnLocation)
        btnBack = findViewById(R.id.btnBack)
        etVat = findViewById(R.id.etVat)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etRepeatPassword = findViewById(R.id.etRepeatPassword)
        etShortName = findViewById(R.id.etShortName)
        etLegalName = findViewById(R.id.etLegalName)
        progressBarHolder = findViewById(R.id.progressBarHolder)

        btnLocation.setOnClickListener {
            val dialogView = Helper.inflateView(R.layout.map_dialog)
            actvAddress = dialogView.findViewById(R.id.actvAddress)
            dialogProgressbar = dialogView.findViewById(R.id.progressBarHolder)
            dialogProgressbar.visibility = View.VISIBLE

            actvAddress.addTextChangedListener(object : TextWatcher {
                var textBefore: String? = null

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    textBefore = p0.toString()
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0.toString() != textBefore) handleAddressSearch(Unit)
                }

                override fun afterTextChanged(p0: Editable?) {}

            })

            map = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
            map?.getMapAsync(this)
            locationDialog = Helper.showConfirmDialog(
                this,
                "",
                dialogView,
                getString(R.string.confirm),
                getString(R.string.cancel),
                object : OnPositiveButtonClickListener {
                    override fun onPositiveBtnClick(dialog: DialogInterface?) {
                        val f = supportFragmentManager.findFragmentById(R.id.map)
                        if (f != null) supportFragmentManager.beginTransaction().remove(f).commit()
                    }
                },
                object : OnNegativeButtonClickListener {
                    override fun onNegativeBtnClick(dialog: DialogInterface?) {
                        location = null
                        val f = supportFragmentManager.findFragmentById(R.id.map)
                        if (f != null) supportFragmentManager.beginTransaction().remove(f).commit()
                    }
                })
        }

        btnBack.setOnClickListener {
            Helper.openActivity(this, ActivityEnum.LOGIN)
        }

        btnFinishRegistration.setOnClickListener {
            if (isFormValid()) {
                register()
            }
        }
    }

    private fun register() {
        val newShop = Shop()
        newShop.Location = location
        newShop.Email = etEmail.text.toString()
        newShop.LegalName = etLegalName.text.toString()
        newShop.ShortName = etShortName.text.toString()
        newShop.Password = etPassword.text.toString()
        newShop.Vat = etVat.text.toString()

        apiService.register(newShop).enqueue(object :
            Callback {
            var mainHandler: Handler = Handler(applicationContext.mainLooper)

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleRegisterSuccess(response)
                }
            }
        })
    }

    fun handleRegisterSuccess(response: Response) {
        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<String>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            Helper.openActivity(this, ActivityEnum.LOGIN)
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }


    private fun isFormValid(): Boolean {
        var isValid = true

        when {
            etVat.text.toString().isEmpty() -> {
                etVat.setError(getString(R.string.required_value), Helper.getErrorIcon())
                isValid = false
            }
            !Helper.isStringInRange(etVat.text.toString(), 1, 50) -> {
                etVat.setError(getString(R.string.length_between_1_50), Helper.getErrorIcon())
                isValid = false
            }

        }

        when {
            etEmail.text.toString().isEmpty() -> {
                etEmail.setError(getString(R.string.required_value), Helper.getErrorIcon())
                isValid = false
            }
            !Helper.isStringInRange(etEmail.text.toString(), 1, 50) -> {
                etEmail.setError(getString(R.string.length_between_1_50), Helper.getErrorIcon())
                isValid = false
            }

        }

        when {
            etLegalName.text.toString().isEmpty() -> {
                etLegalName.setError(getString(R.string.required_value), Helper.getErrorIcon())
                isValid = false
            }
            !Helper.isStringInRange(etLegalName.text.toString(), 1, 200) -> {
                etLegalName.setError(
                    getString(R.string.length_between_1_200),
                    Helper.getErrorIcon()
                )
                isValid = false
            }

        }

        when {
            etShortName.text.toString().isEmpty() -> {
                etShortName.setError(getString(R.string.required_value), Helper.getErrorIcon())
                isValid = false
            }
            !Helper.isStringInRange(etShortName.text.toString(), 1, 50) -> {
                etShortName.setError(
                    getString(R.string.length_between_1_50),
                    Helper.getErrorIcon()
                )
                isValid = false
            }

        }

        if (location == null) {
            Helper.showLongToast(this, getString(R.string.location_required))
            isValid = false
        }

        if (etRepeatPassword.text.isNullOrEmpty()) {
            isValid = false
            etRepeatPassword.error = getString(R.string.required_value)
        } else if (!Helper.isStringInRange(etRepeatPassword.text.toString(), 1, 50)) {
            isValid = false
            etRepeatPassword.error = getString(R.string.length_between_1_50)
        }

        if (etRepeatPassword.text.toString() != etPassword.text.toString()) {
            isValid = false
            etRepeatPassword.error = getString(R.string.passwords_must_match)
        }

        return isValid
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        coroutineScope.launch {
            val mainHandler = Handler(applicationContext.mainLooper)

            val myPosition = Helper.retrievePhoneLocation()
            if (myPosition != null) {
                mainHandler.post {
                    mapMarker = googleMap.addMarker(
                        MarkerOptions()
                            .position(myPosition)
                            .title("Your location")
                    )
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(myPosition))
                    dialogProgressbar.visibility = View.GONE

                    googleMap.setOnMapLongClickListener {
                        dialogProgressbar.visibility = View.VISIBLE

                        apiService.retrieveLocationByCoordinates("${it.latitude}!${it.longitude}")
                            .enqueue(object :
                                Callback {
                                val mainHandler2 = Handler(applicationContext.mainLooper)

                                override fun onFailure(call: Call, e: IOException) {
                                    mainHandler2.post {
                                        handleApiResponseException(call, e)
                                        dialogProgressbar.visibility = View.GONE

                                    }
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    mainHandler2.post {
                                        handleRetrieveLocationResponse(response, it)
                                        dialogProgressbar.visibility = View.GONE
                                    }
                                }
                            }
                            )
                    }
                }
            }
        }
    }

    fun handleRetrieveLocationResponse(response: Response, latLng: LatLng) {
        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Location>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            try {
                val data = resp.Data as Location

                mapMarker!!.remove()
                location = data
                mapMarker = googleMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("${location!!.Street} ${location!!.StreetNumber}, ${location!!.City}")
                )
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            } catch (e: InvalidObjectException) {
                Helper.showLongToast(this, e.message.toString())
            }

        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
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

    private fun searchAddress(address: String?) {
        if (address != null && address.isNotEmpty() && !isSearching) {
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

                actvAddress.setAdapter(
                    ArrayAdapter(
                        this,
                        com.google.android.material.R.layout.mtrl_auto_complete_simple_item,
                        data
                    )
                )

                actvAddress.setOnItemClickListener { adapterView, _, index, _ ->
                    val location = adapterView.adapter.getItem(index) as Location
                    actvAddress.dismissDropDown()
                    setLocationToMap(location)
                }
                actvAddress.showDropDown()

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
            mapMarker!!.remove()
            mapMarker = googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("${location.Street} ${location.StreetNumber}, ${location.City}")
            )
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            actvAddress.clearFocus()
            hideKeyboard(actvAddress.rootView)
            this.location = location
        } else {
            this.location = null
        }
    }
}