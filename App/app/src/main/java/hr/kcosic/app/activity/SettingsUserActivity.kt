package hr.kcosic.app.activity

import android.app.Dialog
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import hr.kcosic.app.R
import hr.kcosic.app.adapter.TimeAdapter
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ValidatedActivityWithNavigation
import hr.kcosic.app.model.entities.Car
import hr.kcosic.app.model.entities.Location
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.listeners.OnPositiveButtonClickListener
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.ListResponse
import hr.kcosic.app.model.responses.SingleResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.InvalidObjectException

class SettingsUserActivity : ValidatedActivityWithNavigation(ActivityEnum.SETTINGS_USER) {

    //#region Account
    private lateinit var btnAccountInfoExpand: ImageButton
    private lateinit var btnAccountInfoCollapse: ImageButton
    private lateinit var btnAccountInfoSave: ImageButton
    private lateinit var accountInfoContent: LinearLayout
    private lateinit var etUserName: EditText
    private lateinit var btnEditUserName: ImageButton
    private lateinit var etEmail: EditText
    private lateinit var btnEditEmail: ImageButton
    private lateinit var btnResetPassword: Button
    //#endregion

    //#region Personal
    private lateinit var btnPersonalInfoExpand: ImageButton
    private lateinit var btnPersonalInfoCollapse: ImageButton
    private lateinit var btnPersonalInfoSave: ImageButton
    private lateinit var personalInfoContent: LinearLayout
    private lateinit var etFirstName: EditText
    private lateinit var btnEditFirstName: ImageButton
    private lateinit var etLastName: EditText
    private lateinit var btnEditLastName: ImageButton
    private lateinit var etDob: EditText
    private lateinit var btnEditDob: ImageButton
    //#endregion Personal

    //#region Vehicles
    private lateinit var btnVehiclesExpand: ImageButton
    private lateinit var btnVehiclesCollapse: ImageButton
    private lateinit var vehiclesContent: LinearLayout
    private lateinit var rvVehicles: RecyclerView
    private lateinit var btnNewVehicle: Button
    //#endregion Vehicles

    //#region Vehicle dialog
    private lateinit var etManufacturer: EditText
    private lateinit var etModel: EditText
    private lateinit var etYear: EditText
    private lateinit var etOdometer: EditText
    private lateinit var dialogProgressbar: FrameLayout
    private lateinit var newVehicleDialog: Dialog
    //#endregion Vehicle dialog
    //#region System
    private lateinit var btnSystemExpand: ImageButton
    private lateinit var btnSystemCollapse: ImageButton
    private lateinit var settingsContent: LinearLayout
    private lateinit var btnLogout: Button
    //#endregion System

    //#region Init
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeComponents()
    }

    override fun initializeComponents() {
        initializeAccountSection()
        initializePersonalInfoSection()
        initializeVehiclesSection()
        initializeSystemSection()
    }

    private fun initializeSystemSection() {
        btnSystemExpand = findViewById(R.id.btnSystemExpand)
        btnSystemCollapse = findViewById(R.id.btnSystemCollapse)
        settingsContent = findViewById(R.id.settingsContent)
        btnLogout = findViewById(R.id.btnLogout)

        btnSystemExpand.setOnClickListener {
            btnSystemExpand.visibility = View.GONE
            btnSystemCollapse.visibility = View.VISIBLE
            settingsContent.visibility = View.VISIBLE
        }

        btnSystemCollapse.setOnClickListener {
            btnSystemExpand.visibility = View.VISIBLE
            btnSystemCollapse.visibility = View.GONE
            settingsContent.visibility = View.GONE
        }

        btnLogout.setOnClickListener {
            clearUserData()
            redirectToLogin()
        }
    }

    private fun initializeVehiclesSection() {
        btnVehiclesExpand = findViewById(R.id.btnVehiclesExpand)
        btnVehiclesCollapse = findViewById(R.id.btnVehiclesCollapse)
        vehiclesContent = findViewById(R.id.vehiclesContent)
        rvVehicles = findViewById(R.id.rvVehicles)
        btnNewVehicle = findViewById(R.id.btnNewVehicle)

        btnVehiclesExpand.setOnClickListener {
            btnVehiclesExpand.visibility = View.GONE
            btnVehiclesCollapse.visibility = View.VISIBLE
            vehiclesContent.visibility = View.VISIBLE
        }

        btnVehiclesCollapse.setOnClickListener {
            btnVehiclesExpand.visibility = View.VISIBLE
            btnVehiclesCollapse.visibility = View.GONE
            vehiclesContent.visibility = View.GONE
        }
        btnNewVehicle.setOnClickListener {
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
                        if (isFormValid()) {
                            createVehicle(assignFormToVehicle(), dialog)
                        }
                    }
                }, null, true
            )
        }
    }

    private fun initializePersonalInfoSection() {
        btnPersonalInfoExpand = findViewById(R.id.btnPersonalInfoExpand)
        btnPersonalInfoCollapse = findViewById(R.id.btnPersonalInfoCollapse)
        btnPersonalInfoSave = findViewById(R.id.btnPersonalInfoSave)
        personalInfoContent = findViewById(R.id.personalInfoContent)
        etFirstName = findViewById(R.id.etFirstName)
        btnEditFirstName = findViewById(R.id.btnEditFirstName)
        etLastName = findViewById(R.id.etLastName)
        btnEditLastName = findViewById(R.id.btnEditLastName)
        etDob = findViewById(R.id.etDob)
        btnEditDob = findViewById(R.id.btnEditDob)

        btnPersonalInfoExpand.setOnClickListener {
            btnPersonalInfoExpand.visibility = View.GONE
            btnPersonalInfoCollapse.visibility = View.VISIBLE
            personalInfoContent.visibility = View.VISIBLE
        }

        btnPersonalInfoCollapse.setOnClickListener {
            btnPersonalInfoExpand.visibility = View.VISIBLE
            btnPersonalInfoCollapse.visibility = View.GONE
            personalInfoContent.visibility = View.GONE
        }
        btnPersonalInfoSave.setOnClickListener {
            savePersonalInfo()
            btnPersonalInfoExpand.visibility = View.GONE
            btnPersonalInfoCollapse.visibility = View.VISIBLE
            personalInfoContent.visibility = View.VISIBLE
        }

        btnEditFirstName.setOnClickListener {
            etFirstName.isEnabled = true
            btnEditFirstName.visibility = View.INVISIBLE
            if (btnPersonalInfoCollapse.visibility != View.GONE) {
                btnPersonalInfoCollapse.visibility = View.GONE
            }
        }
        btnEditLastName.setOnClickListener {
            etLastName.isEnabled = true
            btnEditLastName.visibility = View.INVISIBLE
            if (btnPersonalInfoCollapse.visibility != View.GONE) {
                btnPersonalInfoCollapse.visibility = View.GONE
            }
        }
        btnEditDob.setOnClickListener {
            etDob.isEnabled = true
            btnEditDob.visibility = View.INVISIBLE
            if (btnPersonalInfoCollapse.visibility != View.GONE) {
                btnPersonalInfoCollapse.visibility = View.GONE
            }
        }
    }

    private fun initializeAccountSection() {
        btnAccountInfoExpand = findViewById(R.id.btnAccountInfoExpand)
        btnAccountInfoCollapse = findViewById(R.id.btnAccountInfoCollapse)
        btnAccountInfoSave = findViewById(R.id.btnAccountInfoSave)
        accountInfoContent = findViewById(R.id.accountInfoContent)
        etUserName = findViewById(R.id.etUserName)
        btnEditUserName = findViewById(R.id.btnEditUserName)
        etEmail = findViewById(R.id.etEmail)
        btnEditEmail = findViewById(R.id.btnEditEmail)
        btnResetPassword = findViewById(R.id.btnResetPassword)

        btnAccountInfoExpand.setOnClickListener {
            btnAccountInfoExpand.visibility = View.GONE
            btnAccountInfoCollapse.visibility = View.VISIBLE
            accountInfoContent.visibility = View.VISIBLE
        }

        btnAccountInfoCollapse.setOnClickListener {
            btnAccountInfoExpand.visibility = View.VISIBLE
            btnAccountInfoCollapse.visibility = View.GONE
            accountInfoContent.visibility = View.GONE
        }

        btnAccountInfoSave.setOnClickListener {
            saveAccountInfo()
            btnAccountInfoExpand.visibility = View.GONE
            btnAccountInfoCollapse.visibility = View.VISIBLE
            accountInfoContent.visibility = View.VISIBLE
        }

        btnEditUserName.setOnClickListener {
            etUserName.isEnabled = true
            btnEditUserName.visibility = View.INVISIBLE
            if (btnAccountInfoCollapse.visibility != View.GONE) {
                btnAccountInfoCollapse.visibility = View.GONE
            }
        }

        btnEditEmail.setOnClickListener {
            etEmail.isEnabled = true
            btnEditEmail.visibility = View.INVISIBLE
            if (btnAccountInfoCollapse.visibility != View.GONE) {
                btnAccountInfoCollapse.visibility = View.GONE
            }
        }

        btnResetPassword.setOnClickListener {
            //toggle reset password dialog
        }
    }

    //#endregion Init
    private fun redirectToLogin() {
        Helper.openActivity(this, ActivityEnum.LOGIN)
    }

    private fun clearUserData() {
        Helper.deleteSharedPreference(PreferenceEnum.USER)
        Helper.deleteSharedPreference(PreferenceEnum.TOKEN)
        Helper.deleteSharedPreference(PreferenceEnum.AUTH_FOR)
    }

    private fun savePersonalInfo() {


    }

    private fun saveAccountInfo() {


    }

    //#region Vehicles


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

    private fun isFormValid(): Boolean {
        var isValid = true
        when {
            etManufacturer.text.toString().isEmpty() -> {
                etManufacturer.setError(getString(R.string.required_value), Helper.getErrorIcon())
                isValid = false
            }
            !Helper.isStringInRange(etManufacturer.text.toString(), 1, 50) -> {
                etManufacturer.setError(
                    getString(R.string.length_between_1_50),
                    Helper.getErrorIcon()
                )
                isValid = false
            }
        }
        when {
            etModel.text.toString().isEmpty() -> {
                etModel.setError(getString(R.string.required_value), Helper.getErrorIcon())
                isValid = false
            }
            !Helper.isStringInRange(etModel.text.toString(), 1, 50) -> {
                etModel.setError(getString(R.string.length_between_1_50), Helper.getErrorIcon())
                isValid = false
            }
        }
        when {
            etYear.text.toString().isEmpty() -> {
                etYear.setError(getString(R.string.required_value), Helper.getErrorIcon())
                isValid = false
            }
            !Helper.isValueInRange(
                etYear.text.toString().toInt(),
                1960,
                Calendar.getInstance().get(Calendar.YEAR)
            ) -> {
                etYear.setError(getString(R.string.value_between_1960_today), Helper.getErrorIcon())
                isValid = false
            }
        }
        when {
            etOdometer.text.toString().isEmpty() -> {
                etOdometer.setError(getString(R.string.required_value), Helper.getErrorIcon())
                isValid = false
            }
        }
        when {
            etModel.text.toString().isEmpty() -> {
                etModel.setError(getString(R.string.required_value), Helper.getErrorIcon())
                isValid = false
            }
            !Helper.isStringInRange(etModel.text.toString(), 1, 50) -> {
                etModel.setError(getString(R.string.length_between_1_50), Helper.getErrorIcon())
                isValid = false
            }
        }
        return isValid
    }
    private fun retrieveMyVehicles() {
        progressBarHolder.visibility = View.VISIBLE
        apiService.retrieveCars().enqueue(object : Callback {
            val mainHandler = Handler(applicationContext.mainLooper)
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    progressBarHolder.visibility = View.GONE
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleRetrieveCarsResponse(response)
                    progressBarHolder.visibility = View.GONE
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

                /*ddMyVehicles.adapter =
                    ArrayAdapter(this, android.R.layout.simple_spinner_item, data)*/


            } catch (e: InvalidObjectException) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    //#endregion Vehicles
}