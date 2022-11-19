package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.kcosic.app.R
import hr.kcosic.app.adapter.TimeAdapter
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ValidatedActivityWithNavigation
import hr.kcosic.app.model.entities.Car
import hr.kcosic.app.model.entities.Location
import hr.kcosic.app.model.entities.Request
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.helpers.IconHelper
import hr.kcosic.app.model.helpers.ValidationHelper
import hr.kcosic.app.model.listeners.OnPositiveButtonClickListener
import hr.kcosic.app.model.listeners.ButtonClickListener
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.ListResponse
import hr.kcosic.app.model.responses.SingleResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.InvalidObjectException


class NewRequestActivity : ValidatedActivityWithNavigation(ActivityEnum.NEW_REQUEST) {

    private lateinit var rvRepairTime: RecyclerView
    private lateinit var ddMyVehicles: Spinner
    private lateinit var btnAddVehicle: Button
    private lateinit var btnNext: Button
    private lateinit var etManufacturer: EditText
    private lateinit var etModel: EditText
    private lateinit var etYear: EditText
    private lateinit var etOdometer: EditText
    private lateinit var dialogProgressbar: FrameLayout
    private lateinit var etIssueDescription: EditText
    private lateinit var timeAdapter: TimeAdapter
    private lateinit var selectedLocation: Location
    private lateinit var newVehicleDialog: Dialog
    private lateinit var dateOfRepair: String

    private var selectedVehicle: Car? = null
    private var timeOfRepair: String? = null
    private var shopId: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrieveDataFromIntent()
        retrieveAvailability()
        retrieveMyVehicles()
        initializeComponents()
    }


    private fun retrieveDataFromIntent() {
        val stringMap = intent.getStringExtra(SearchActivity.NEW_REQUEST_KEY)
        val map = Helper.deserializeObject<Map<String, String>>(stringMap!!)
        selectedLocation = Helper.deserializeObject(map["selectedLocation"]!!)
        dateOfRepair = map["dateOfRepair"]!!
        shopId = selectedLocation.Shops?.get(0)?.Id!!
    }


    override fun initializeComponents() {
        rvRepairTime = findViewById(R.id.rvRepairTime)
        ddMyVehicles = findViewById(R.id.ddMyVehicles)
        btnAddVehicle = findViewById(R.id.btnNewVehicle)
        btnNext = findViewById(R.id.btnNext)
        etIssueDescription = findViewById(R.id.etIssueDescription)

        etIssueDescription.doOnTextChanged { _, _, _, _ -> validateNextButton() }

        ddMyVehicles.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                validateNextButton()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedVehicle = parent?.getItemAtPosition(position) as Car
                validateNextButton()
            }
        }

        btnAddVehicle.setOnClickListener {
            val dialogView = Helper.inflateView(R.layout.new_vehicle_layout)
            etManufacturer = dialogView.findViewById(R.id.etManufacturer)
            etModel = dialogView.findViewById(R.id.etModel)
            etYear = dialogView.findViewById(R.id.etYear)
            etOdometer = dialogView.findViewById(R.id.etOdometer)
            dialogProgressbar = dialogView.findViewById(R.id.progressBarHolder)
            newVehicleDialog = Helper.showConfirmDialog(
                this,
                "",
                dialogView,
                getString(R.string.add),
                getString(R.string.cancel),
                object : OnPositiveButtonClickListener {
                    override fun onPositiveBtnClick(dialog: DialogInterface?) {
                        if (ValidationHelper.validateCar(etManufacturer, etModel, etYear, etOdometer)) {
                            createVehicle(assignFormToVehicle(), dialog)
                        }
                    }
                }, null, true
            )
        }

        btnNext.setOnClickListener {
            createNewRequest(assignDataToRequest())
        }

    }

    private fun isRequestValid(): Boolean {
        var isValid = true
        when (ddMyVehicles.selectedItem) {
            null -> {
                (ddMyVehicles.selectedView as TextView).setError(
                    getString(R.string.required_value),
                    IconHelper.getErrorIcon()
                )
                isValid = false
            }
        }

        when {
            etIssueDescription.text == null -> {
                etIssueDescription.setError(
                    getString(R.string.required_value),
                    IconHelper.getErrorIcon()
                )
                isValid = false
            }
            !ValidationHelper.isStringInRange(etIssueDescription.text.toString(), 1, 2000) -> {
                etIssueDescription.setError(
                    getString(R.string.length_between_1_2000),
                    IconHelper.getErrorIcon()
                )
                isValid = false
            }

        }

        when (timeOfRepair) {
            null -> {
                isValid = false
            }
        }

        return isValid
    }

    private fun createNewRequest(newRequest: Request) {
        showSpinner()
        apiService.createRequest(newRequest).enqueue(object : Callback {
            val mainHandler = Handler(applicationContext.mainLooper)
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    hideSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleCreateRequestResponse(response)
                    hideSpinner()
                }
            }
        })
    }

    fun handleCreateRequestResponse(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Car>>(response.body!!.string())

        if (resp.IsSuccess!!) {
            Helper.showLongToast(this, getString(R.string.request_submitted))
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    private fun assignDataToRequest(): Request {

        val newRequest = Request()
        newRequest.CarId = selectedVehicle?.Id
        newRequest.RepairDate = Helper.stringToDateTime("$dateOfRepair $timeOfRepair")
        newRequest.RequestDate = java.util.Calendar.getInstance().time
        newRequest.UserId = getUser().Id
        newRequest.ShopId = shopId
        newRequest.IssueDescription = etIssueDescription.text.toString()
        return newRequest
    }


    private fun createVehicle(newVehicle: Car, dialog: DialogInterface?) {
        dialogProgressbar.visibility = View.VISIBLE
        apiService.createCar(newVehicle).enqueue(object : Callback {
            val mainHandler = Handler(applicationContext.mainLooper)
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    dialogProgressbar.visibility = View.GONE

                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleCreateCarResponse(response, dialog)
                    dialogProgressbar.visibility = View.GONE
                }
            }

        })
    }

    fun handleCreateCarResponse(response: Response, dialog: DialogInterface?) {

        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Car>>(response.body!!.string())

        if (resp.IsSuccess!!) {
            Helper.showLongToast(this, getString(R.string.vehicle_created_successfuly))
            retrieveMyVehicles()
            dialog?.dismiss()

        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    private fun assignFormToVehicle(): Car {
        val car = Car()
        car.Manufacturer = etManufacturer.text.toString()
        car.Model = etModel.text.toString()
        car.Odometer = etOdometer.text.toString().toDouble()
        car.Year = etYear.text.toString().toInt()
        return car
    }


    private fun retrieveMyVehicles() {
        showSpinner()
        apiService.retrieveCars().enqueue(object : Callback {
            val mainHandler = Handler(applicationContext.mainLooper)
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    hideSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleRetrieveCarsResponse(response)
                    hideSpinner()
                }
            }
        })
    }

    fun handleRetrieveCarsResponse(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<ListResponse<Car>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is ListResponse<*>) {

            try {
                @Suppress("UNCHECKED_CAST") val data = resp.Data as ArrayList<Car>

                ddMyVehicles.adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, data)


            } catch (e: InvalidObjectException) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    private fun retrieveAvailability() {
        showSpinner()
        //TODO: fix when multiple shops are in  the same location
        apiService.retrieveShopAvailability(shopId!!, dateOfRepair)
            .enqueue(object : Callback {
                val mainHandler = Handler(applicationContext.mainLooper)
                override fun onFailure(call: Call, e: IOException) {
                    mainHandler.post {
                        handleApiResponseException(call, e)
                        hideSpinner()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    mainHandler.post {
                        handleRetrieveAvailabilityResponse(response)
                        hideSpinner()
                    }
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    fun handleRetrieveAvailabilityResponse(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<ListResponse<String>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is ListResponse<*>) {

            try {
                @Suppress("UNCHECKED_CAST") val data = resp.Data as ArrayList<String>
                val radioButtonClickListener = object : ButtonClickListener {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onClick(s: String) {
                        // Notify adapter
                        rvRepairTime.post {
                            timeOfRepair = s
                            timeAdapter.notifyDataSetChanged()
                            validateNextButton()

                        }
                    }
                }
                timeAdapter = TimeAdapter(
                    data,
                    selectedLocation.Shops?.get(0)?.WorkHours!!,
                    radioButtonClickListener
                )
                rvRepairTime.layoutManager = LinearLayoutManager(this)
                rvRepairTime.adapter = timeAdapter
                timeAdapter.notifyDataSetChanged()
            } catch (e: InvalidObjectException) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    private fun validateNextButton() {
        if (isRequestValid()) {
            btnNext.visibility = View.VISIBLE
        } else {
            btnNext.visibility = View.GONE
        }
    }

}