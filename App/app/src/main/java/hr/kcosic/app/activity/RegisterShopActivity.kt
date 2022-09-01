package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import hr.kcosic.app.R
import hr.kcosic.app.model.OnNegativeButtonClickListener
import hr.kcosic.app.model.OnPositiveButtonClickListener
import hr.kcosic.app.model.bases.BaseActivity
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.entities.Location
import hr.kcosic.app.model.entities.Shop
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.helpers.Helper
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
    var map: SupportMapFragment? = null
    private lateinit var btnFinishRegistration: Button
    private lateinit var btnBack: Button
    private lateinit var btnLocation: Button
    private lateinit var actvAddress: AutoCompleteTextView
    private lateinit var txtVat: EditText
    private lateinit var txtShortName: EditText
    private lateinit var txtLegalName: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var txtRepeatPassword: EditText
    private lateinit var locationDialog: AlertDialog
    private lateinit var googleMap: GoogleMap
    private var searchJob: Job? = null
    private var coroutineScope: CoroutineScope = CoroutineScope(Job())
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
        txtVat = findViewById(R.id.txtVat)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        txtRepeatPassword = findViewById(R.id.txtRepeatPassword)
        txtShortName = findViewById(R.id.txtShortName)
        txtLegalName = findViewById(R.id.txtLegalName)

        btnLocation.setOnClickListener {
            val dialogView = Helper.inflateView(R.layout.map_dialog)
            actvAddress = dialogView.findViewById(R.id.actvAddress)
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
        val newShop = Shop();
        newShop.Location = location
        newShop.Email = txtEmail.text.toString()
        newShop.LegalName = txtLegalName.text.toString()
        newShop.ShortName = txtShortName.text.toString()
        newShop.Password = txtPassword.text.toString()
        newShop.Vat = txtVat.text.toString()

        apiService.register(newShop).enqueue(object :
            Callback {
            var mainHandler: Handler = Handler(applicationContext.mainLooper)

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleRegisterException(call, e)
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
            handleRegisterError(resp.Message!!)
        }
    }

    fun handleRegisterError(message: String) {
        Helper.showLongToast(this, message)
    }

    fun handleRegisterException(call: Call, e: Exception) {
        call.cancel()
        Helper.showLongToast(this, e.message.toString())
    }

    private fun isFormValid(): Boolean {
        var isValid = true

        when {
            txtVat.text.toString().isEmpty() -> {
                txtVat.setError(getString(R.string.required_value), Helper.getErrorIcon())
                isValid = false
            }
            !Helper.isStringInRange(txtVat.text.toString(), 1, 50) -> {
                txtVat.setError(getString(R.string.length_between_1_50), Helper.getErrorIcon())
                isValid = false
            }

        }

        when {
            txtEmail.text.toString().isEmpty() -> {
                txtEmail.setError(getString(R.string.required_value), Helper.getErrorIcon())
                isValid = false
            }
            !Helper.isStringInRange(txtEmail.text.toString(), 1, 50) -> {
                txtEmail.setError(getString(R.string.length_between_1_50), Helper.getErrorIcon())
                isValid = false
            }

        }

        when {
            txtLegalName.text.toString().isEmpty() -> {
                txtLegalName.setError(getString(R.string.required_value), Helper.getErrorIcon())
                isValid = false
            }
            !Helper.isStringInRange(txtLegalName.text.toString(), 1, 200) -> {
                txtLegalName.setError(
                    getString(R.string.length_between_1_200),
                    Helper.getErrorIcon()
                )
                isValid = false
            }

        }

        when {
            txtShortName.text.toString().isEmpty() -> {
                txtShortName.setError(getString(R.string.required_value), Helper.getErrorIcon())
                isValid = false
            }
            !Helper.isStringInRange(txtShortName.text.toString(), 1, 50) -> {
                txtShortName.setError(
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

        if (txtRepeatPassword.text.isNullOrEmpty()) {
            isValid = false
            txtRepeatPassword.error = getString(R.string.required_value)
        } else if (!Helper.isStringInRange(txtRepeatPassword.text.toString(), 1, 50)) {
            isValid = false
            txtRepeatPassword.error = getString(R.string.length_between_1_50)
        }

        if (txtRepeatPassword.text.toString() != txtPassword.text.toString()) {
            isValid = false
            txtRepeatPassword.error = getString(R.string.passwords_must_match)
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
                    googleMap.setOnMapLongClickListener {
                        apiService.retrieveLocationByCoordinates("${it.latitude}!${it.longitude}")
                            .enqueue(object :
                                Callback {
                                val mainHandler2 = Handler(applicationContext.mainLooper)

                                override fun onFailure(call: Call, e: IOException) {
                                    mainHandler2.post {
                                        handleRetrieveLocationException(call, e)
                                    }
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    mainHandler2.post {
                                        handleRetrieveLocationResponse(response, it)
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
                location = data;
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
            handleRetrieveLocationError(resp.Message!!)
        }
    }

    fun handleRetrieveLocationError(message: String) {
        Helper.showLongToast(this, message)
    }

    fun handleRetrieveLocationException(call: Call, e: Exception) {
        call.cancel()
        Helper.showLongToast(this, e.message.toString())
    }

    fun <T> debounce(
        waitMs: Long = 300L,
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
                        handleDiscoverAddressException(call, e)
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
            handleDiscoverAddressError(resp.Message!!)
        }
    }

    fun handleDiscoverAddressError(message: String) {
        Helper.showLongToast(this, message)
    }

    fun handleDiscoverAddressException(call: Call, e: Exception) {
        call.cancel()
        Helper.showLongToast(this, e.message.toString())
    }

    fun setLocationToMap(location: Location) {
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