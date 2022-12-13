package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.kcosic.app.R
import hr.kcosic.app.adapter.VehiclesAdapter
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ValidatedActivityWithNavigation
import hr.kcosic.app.model.entities.Car
import hr.kcosic.app.model.entities.User
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.helpers.ValidationHelper
import hr.kcosic.app.model.listeners.ButtonClickListener
import hr.kcosic.app.model.listeners.OnPositiveButtonClickListener
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.ListResponse
import hr.kcosic.app.model.responses.SingleResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.InvalidObjectException
import java.util.*

class SettingsUserActivity : ValidatedActivityWithNavigation(ActivityEnum.SETTINGS_USER) {

    //#region Account
    private lateinit var llAccountSection: LinearLayout
    private lateinit var ivAccountInfoExpand: ImageView
    private lateinit var ivAccountInfoCollapse: ImageView
    private lateinit var ivAccountInfoSave: ImageView
    private lateinit var accountInfoContent: LinearLayout
    private lateinit var etUserName: EditText
    private lateinit var btnEditUserName: ImageButton
    private lateinit var etEmail: EditText
    private lateinit var btnEditEmail: ImageButton
    private lateinit var btnResetPassword: Button
    //#endregion

    //#region Personal
    private lateinit var llPersonalInfoSection: LinearLayout
    private lateinit var ivPersonalInfoExpand: ImageView
    private lateinit var ivPersonalInfoCollapse: ImageView
    private lateinit var ivPersonalInfoSave: ImageView
    private lateinit var personalInfoContent: LinearLayout
    private lateinit var etFirstName: EditText
    private lateinit var btnEditFirstName: ImageButton
    private lateinit var etLastName: EditText
    private lateinit var btnEditLastName: ImageButton
    private lateinit var etDob: EditText
    private lateinit var btnEditDob: ImageButton
    //#endregion Personal

    //#region Vehicles
    private lateinit var llVehiclesSection: LinearLayout
    private lateinit var ivVehiclesExpand: ImageView
    private lateinit var ivVehiclesCollapse: ImageView
    private lateinit var vehiclesContent: LinearLayout
    private lateinit var rvVehicles: RecyclerView
    private lateinit var btnNewVehicle: Button
    //#endregion Vehicles

    //#region Vehicle dialog
    private lateinit var etManufacturer: EditText
    private lateinit var etModel: EditText
    private lateinit var etYear: EditText
    private lateinit var etOdometer: EditText
    private lateinit var etLicensePlate: EditText
    private lateinit var dialogProgressbar: FrameLayout
    private lateinit var newVehicleDialog: Dialog

    //#endregion Vehicle dialog
    //#region System
    private lateinit var llSystemSection: LinearLayout
    private lateinit var ivSystemExpand: ImageView
    private lateinit var ivSystemCollapse: ImageView
    private lateinit var systemContent: LinearLayout
    private lateinit var btnLogout: Button

    //#endregion System
    private var userData: User? = null
    private lateinit var vehiclesAdapter: VehiclesAdapter
    private val dobCalendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

    //#region Init
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeComponents()
        retrieveUser()
        retrieveMyVehicles()
    }

    override fun initializeComponents() {
        initializeAccountSection()
        initializePersonalInfoSection()
        initializeVehiclesSection()
        initializeSystemSection()
    }

    private fun initializeSystemSection() {
        llSystemSection = findViewById(R.id.llSystemSection)
        ivSystemExpand = findViewById(R.id.ivSystemExpand)
        ivSystemCollapse = findViewById(R.id.ivSystemCollapse)
        systemContent = findViewById(R.id.systemContent)
        btnLogout = findViewById(R.id.btnLogout)

        llSystemSection.setOnClickListener {
            toggleHeader(systemContent, ivSystemExpand, ivSystemCollapse) {}
        }

        btnLogout.setOnClickListener {
            logoutUser()
        }
    }

    private fun initializeVehiclesSection() {
        llVehiclesSection = findViewById(R.id.llVehiclesSection)
        ivVehiclesExpand = findViewById(R.id.ivVehiclesExpand)
        ivVehiclesCollapse = findViewById(R.id.ivVehiclesCollapse)
        vehiclesContent = findViewById(R.id.vehiclesContent)
        rvVehicles = findViewById(R.id.rvVehicles)
        btnNewVehicle = findViewById(R.id.btnNewVehicle)

        llVehiclesSection.setOnClickListener {
            toggleHeader(vehiclesContent, ivVehiclesExpand, ivVehiclesCollapse) {}
        }

        btnNewVehicle.setOnClickListener {
            val dialogView = Helper.inflateView(R.layout.new_vehicle_layout)
            val etManufacturer: EditText = dialogView.findViewById(R.id.etManufacturer)
            val etModel: EditText = dialogView.findViewById(R.id.etModel)
            val etYear: EditText = dialogView.findViewById(R.id.etYear)
            val etOdometer: EditText = dialogView.findViewById(R.id.etOdometer)
            val etLicensePlate: EditText = dialogView.findViewById(R.id.etLicensePlate)
            val dialogProgressbar: ProgressBar = dialogView.findViewById(R.id.progressBarHolder)
            Helper.showConfirmDialog(
                this,
                "",
                dialogView,
                getString(R.string.add),
                getString(R.string.cancel),
                object : OnPositiveButtonClickListener {
                    override fun onPositiveBtnClick(dialog: DialogInterface?) {
                        if (ValidationHelper.validateCar(
                                etManufacturer,
                                etModel,
                                etYear,
                                etOdometer,
                                etLicensePlate
                            )
                        ) {
                            showComponent(dialogProgressbar)
                            createVehicle(assignFormToVehicle(), dialog)
                        }
                    }
                }, null, true
            )
        }
    }

    private fun initializePersonalInfoSection() {
        llPersonalInfoSection = findViewById(R.id.llPersonalInfoSection)
        ivPersonalInfoExpand = findViewById(R.id.ivPersonalInfoExpand)
        ivPersonalInfoCollapse = findViewById(R.id.ivPersonalInfoCollapse)
        ivPersonalInfoSave = findViewById(R.id.ivPersonalInfoSave)
        personalInfoContent = findViewById(R.id.personalInfoContent)
        etFirstName = findViewById(R.id.etFirstName)
        btnEditFirstName = findViewById(R.id.btnEditFirstName)
        etLastName = findViewById(R.id.etLastName)
        btnEditLastName = findViewById(R.id.btnEditLastName)
        etDob = findViewById(R.id.etDob)
        btnEditDob = findViewById(R.id.btnEditDob)

        val date =
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                dobCalendar.set(Calendar.YEAR, year)
                dobCalendar.set(Calendar.MONTH, month)
                dobCalendar.set(Calendar.DAY_OF_MONTH, day)
                dobCalendar.set(Calendar.HOUR, 0)
                dobCalendar.set(Calendar.MINUTE, 0)
                dobCalendar.set(Calendar.SECOND, 0)
                dobCalendar.set(Calendar.MILLISECOND, 0)
                updateLabel()
            }
        etDob.setOnClickListener {
            DatePickerDialog(
                this,
                date,
                dobCalendar.get(Calendar.YEAR),
                dobCalendar.get(Calendar.MONTH),
                dobCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        etDob.setOnFocusChangeListener { _, inFocus ->
            if (inFocus) {
                DatePickerDialog(
                    this,
                    date,
                    dobCalendar.get(Calendar.YEAR),
                    dobCalendar.get(Calendar.MONTH),
                    dobCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

        }

        llPersonalInfoSection.setOnClickListener {
            toggleHeader(
                personalInfoContent,
                ivPersonalInfoExpand,
                ivPersonalInfoCollapse,
                ivPersonalInfoSave
            ) { savePersonalInfo() }

        }

        btnEditFirstName.setOnClickListener {
            enableEditField(
                etFirstName,
                btnEditFirstName,
                ivPersonalInfoSave,
                ivPersonalInfoCollapse
            )

        }
        btnEditLastName.setOnClickListener {
            enableEditField(etLastName, btnEditLastName, ivPersonalInfoSave, ivPersonalInfoCollapse)

        }
        btnEditDob.setOnClickListener {
            enableEditField(etDob, btnEditDob, ivPersonalInfoSave, ivPersonalInfoCollapse)

        }
    }

    private fun initializeAccountSection() {
        llAccountSection = findViewById(R.id.llAccountSection)
        ivAccountInfoExpand = findViewById(R.id.ivAccountInfoExpand)
        ivAccountInfoCollapse = findViewById(R.id.ivAccountInfoCollapse)
        ivAccountInfoSave = findViewById(R.id.ivAccountInfoSave)
        accountInfoContent = findViewById(R.id.accountInfoContent)
        etUserName = findViewById(R.id.etUserName)
        btnEditUserName = findViewById(R.id.btnEditUserName)
        etEmail = findViewById(R.id.etEmail)
        btnEditEmail = findViewById(R.id.btnEditEmail)
        btnResetPassword = findViewById(R.id.btnResetPassword)


        llAccountSection.setOnClickListener {
            toggleHeader(
                accountInfoContent,
                ivAccountInfoExpand,
                ivAccountInfoCollapse,
                ivAccountInfoSave
            ) { saveAccountInfo() }
        }

        btnEditUserName.setOnClickListener {
            enableEditField(etUserName, btnEditUserName, ivAccountInfoSave, ivAccountInfoCollapse)
        }

        btnEditEmail.setOnClickListener {
            enableEditField(etEmail, btnEditEmail, ivAccountInfoSave, ivAccountInfoCollapse)
        }

        btnResetPassword.setOnClickListener {
            val passwordDialogView = Helper.inflateView(R.layout.password_reset_layout)
            val etOldPassword: EditText = passwordDialogView.findViewById(R.id.etOldPassword)
            val etNewPassword: EditText = passwordDialogView.findViewById(R.id.etNewPassword)
            val etNewPasswordRepeat: EditText =
                passwordDialogView.findViewById(R.id.etNewPasswordRepeat)
            val passwordDialogProgressBar: FrameLayout =
                passwordDialogView.findViewById(R.id.progressBarHolder)

            newVehicleDialog = Helper.showConfirmDialog(
                this,
                "",
                passwordDialogView,
                getString(R.string.save),
                getString(R.string.cancel),
                object : OnPositiveButtonClickListener {
                    override fun onPositiveBtnClick(dialog: DialogInterface?) {
                        if (ValidationHelper.validateResetPassword(
                                etOldPassword,
                                etNewPassword,
                                etNewPasswordRepeat
                            )
                        ) {
                            resetPassword(
                                etOldPassword.text.toString(),
                                etNewPassword.text.toString(),
                                dialog,
                                passwordDialogProgressBar
                            )
                        }
                    }
                }, null, true
            )
        }
    }

    private fun resetPassword(
        oldPassword: String,
        newPassword: String,
        dialog: DialogInterface?,
        progressBar: FrameLayout
    ) {
        apiService.resetPassword(getUser().Id!!, oldPassword, newPassword)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    mainHandler.post {
                        handleApiResponseException(call, e)
                        hideComponent(progressBar)
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    mainHandler.post {
                        handleResetPasswordResponse(response, dialog)
                        hideComponent(progressBar)
                    }
                }
            })
    }

    fun handleResetPasswordResponse(response: Response, dialog: DialogInterface?) {

        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<User>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {

            try {
                Helper.showLongToast(this, "Password Successfully Changed")
                dialog!!.dismiss()
            } catch (e: InvalidObjectException) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    private fun toggleHeader(
        llContent: LinearLayout,
        ivExpand: ImageView,
        ivCollapse: ImageView,
        ivSave: ImageView? = null,
        saveFn: () -> Unit
    ) {
        when {
            ivSave != null && ivSave.visibility == VISIBLE -> {
                saveFn()
                hideComponent(ivSave)
                hideComponent(ivExpand)
                showComponent(ivCollapse)
            }
            ivExpand.visibility == VISIBLE -> {
                hideComponent(ivExpand)
                showComponent(ivCollapse)
                showComponent(llContent)
            }
            ivCollapse.visibility == VISIBLE -> {
                showComponent(ivExpand)
                hideComponent(ivCollapse)
                hideComponent(llContent)
            }
        }
    }

    private fun enableEditField(
        field: EditText,
        btnField: ImageButton,
        ivSave: ImageView,
        ivCollapse: ImageView
    ) {
        field.isEnabled = true
        btnField.visibility = View.INVISIBLE
        if (ivSave.visibility != VISIBLE) {
            hideComponent(ivCollapse)
            showComponent(ivSave)
        }
    }

    //#endregion Init

    //#region Retrieve User
    private fun retrieveUser() {
        showSpinner()
        apiService.retrieveUser(getUser().Id!!).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    hideSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleRetrieveUserResponse(response)
                    hideSpinner()
                }
            }
        })
    }

    fun handleRetrieveUserResponse(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<User>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {

            try {
                userData = resp.Data as User
                Helper.createOrEditSharedPreference(
                    PreferenceEnum.USER,
                    Helper.serializeData(userData)
                )

                populateAccountSection(userData!!)
                populatePersonalInformationSection(userData!!)

            } catch (e: InvalidObjectException) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    private fun populatePersonalInformationSection(user: User) {
        etFirstName.setText(user.FirstName)
        etLastName.setText(user.LastName)
        etDob.setText(Helper.formatDate(user.DateOfBirth!!))
    }

    private fun populateAccountSection(user: User) {
        etEmail.setText(user.Email)
        etUserName.setText(user.Username)
    }

    //#region Update User
    private fun updateUser(user: User) {
        showSpinner()
        apiService.updateUser(user).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    hideSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleUpdateUserResponse(response)
                    hideSpinner()
                }
            }
        })
    }

    fun handleUpdateUserResponse(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<User>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {

            try {
                retrieveUser()
            } catch (e: InvalidObjectException) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateLabel() {
        etDob.setText(Helper.formatDate(dobCalendar.time))
    }
    //#endregion Update User

    //#endregion Retrieve User

    //#region Retrieve Vehicles
    private fun retrieveMyVehicles() {
        showSpinner()
        apiService.retrieveCars().enqueue(object : Callback {
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

    @SuppressLint("NotifyDataSetChanged")
    fun handleRetrieveCarsResponse(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<ListResponse<Car>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is ListResponse<*>) {

            try {
                @Suppress("UNCHECKED_CAST") val data = resp.Data as ArrayList<Car>
                val editButtonClick = object : ButtonClickListener {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onClick(o: Car) {
                        rvVehicles.post {
                            editVehicleDialog(o)
                        }
                    }
                }

                val deleteButtonClick = object : ButtonClickListener {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun onClick(o: Car) {
                        rvVehicles.post {
                            deleteVehicle(o)
                        }
                    }
                }
                vehiclesAdapter = VehiclesAdapter(
                    data,
                    editButtonClick,
                    deleteButtonClick
                )

                rvVehicles.layoutManager = LinearLayoutManager(this)
                rvVehicles.adapter = vehiclesAdapter
                vehiclesAdapter.notifyDataSetChanged()


            } catch (e: InvalidObjectException) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    //#region Delete Vehicle
    private fun deleteVehicle(vehicle: Car) {
        showSpinner()
        apiService.deleteCar(vehicle.Id!!).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    hideSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    hideSpinner()
                    handleDeleteVehicleResponse(response)
                }
            }
        })
    }

    fun handleDeleteVehicleResponse(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Car>>(response.body!!.string())

        if (resp.IsSuccess!!) {
            Helper.showLongToast(this, getString(R.string.vehicle_deleted_successfuly))
            retrieveMyVehicles()
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }
    //#endregion Delete Vehicle

    //#region Edit Vehicle
    private fun editVehicleDialog(vehicle: Car) {
        val dialogView = Helper.inflateView(R.layout.new_vehicle_layout)
        etManufacturer = dialogView.findViewById(R.id.etManufacturer)
        etModel = dialogView.findViewById(R.id.etModel)
        etYear = dialogView.findViewById(R.id.etYear)
        etOdometer = dialogView.findViewById(R.id.etOdometer)
        etLicensePlate = dialogView.findViewById(R.id.etLicensePlate)
        dialogProgressbar = dialogView.findViewById(R.id.progressBarHolder)

        etManufacturer.setText(vehicle.Manufacturer)
        etModel.setText(vehicle.Model)
        etYear.setText(vehicle.Year.toString())
        etOdometer.setText(vehicle.Odometer.toString())
        etLicensePlate.setText(vehicle.LicensePlate.toString())

        newVehicleDialog = Helper.showConfirmDialog(
            this,
            "",
            dialogView,
            getString(R.string.save),
            getString(R.string.cancel),
            object : OnPositiveButtonClickListener {
                override fun onPositiveBtnClick(dialog: DialogInterface?) {
                    if (ValidationHelper.validateCar(etManufacturer, etModel, etYear, etOdometer, etLicensePlate)) {
                        editVehicle(assignFormToVehicle(vehicle), dialog)
                    }
                }
            }, null, true
        )
    }


    private fun editVehicle(vehicle: Car, dialog: DialogInterface?) {
        showSpinner()
        apiService.updateCar(vehicle).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    hideSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    hideSpinner()
                    handleEditVehicleResponse(response, dialog)
                }
            }
        })
    }

    fun handleEditVehicleResponse(response: Response, dialog: DialogInterface?) {

        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Car>>(response.body!!.string())

        if (resp.IsSuccess!!) {
            Helper.showLongToast(this, getString(R.string.vehicle_edited_successfuly))
            retrieveMyVehicles()
            dialog!!.dismiss()
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }
    //#endregion Edit Vehicle


    //#endregion Retrieve Vehicles

    private fun savePersonalInfo() {
        val user = getUser()
        if (etFirstName.isEnabled) {
            user.FirstName = etFirstName.text.toString()
            etFirstName.isEnabled = false
            showComponent(btnEditFirstName)
        }

        if (etLastName.isEnabled) {
            user.LastName = etLastName.text.toString()
            etLastName.isEnabled = false
            showComponent(btnEditLastName)

        }

        if (etDob.isEnabled) {
            user.DateOfBirth = Helper.stringToDate(etDob.text.toString())
            etDob.isEnabled = false
            showComponent(btnEditDob)
        }

        updateUser(user)
    }

    private fun saveAccountInfo() {

        val user = getUser()
        if (etUserName.isEnabled) {
            user.Username = etUserName.text.toString()
            etUserName.isEnabled = false
            showComponent(btnEditUserName)
        }

        if (etEmail.isEnabled) {
            user.Email = etEmail.text.toString()
            etEmail.isEnabled = false
            showComponent(btnEditEmail)
        }

        updateUser(user)
    }

    //#region Vehicles
    private fun createVehicle(newVehicle: Car, dialog: DialogInterface?) {
        showComponent(dialogProgressbar)
        apiService.createCar(newVehicle).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    hideComponent(dialogProgressbar)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleCreateCarResponse(response, dialog)
                    hideComponent(dialogProgressbar)
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

    private fun assignFormToVehicle(existingCar: Car? = null): Car {
        return if (existingCar != null) {
            existingCar.Manufacturer = etManufacturer.text.toString()
            existingCar.Model = etModel.text.toString()
            existingCar.Odometer = etOdometer.text.toString().toDouble()
            existingCar.Year = etYear.text.toString().toInt()
            existingCar.LicensePlate = etLicensePlate.text.toString()
            existingCar
        } else {
            val car = Car()
            car.Manufacturer = etManufacturer.text.toString()
            car.Model = etModel.text.toString()
            car.Odometer = etOdometer.text.toString().toDouble()
            car.Year = etYear.text.toString().toInt()
            car.LicensePlate = etLicensePlate.text.toString()
            car
        }

    }


    //#endregion Vehicles
}