package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.bases.ValidatedActivityWithNavigation
import hr.kcosic.app.model.entities.Shop
import hr.kcosic.app.model.entities.User
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.helpers.ValidationHelper
import hr.kcosic.app.model.listeners.OnPositiveButtonClickListener
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.SingleResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.InvalidObjectException

class SettingsShopActivity : ValidatedActivityWithNavigation(ActivityEnum.SETTINGS_SHOP) {

    //#region Account
    private lateinit var llAccountSection: LinearLayout
    private lateinit var ivAccountInfoExpand: ImageView
    private lateinit var ivAccountInfoCollapse: ImageView
    private lateinit var ivAccountInfoSave: ImageView
    private lateinit var accountInfoContent: LinearLayout
    private lateinit var etEmail: EditText
    private lateinit var btnEditEmail: ImageButton
    private lateinit var btnResetPassword: Button
    //#endregion

    //#region Shop Info
    private lateinit var llShopInfoSection: LinearLayout
    private lateinit var ivShopInfoExpand: ImageView
    private lateinit var ivShopInfoCollapse: ImageView
    private lateinit var ivShopInfoSave: ImageView
    private lateinit var llShopInfoContent: LinearLayout
    private lateinit var etLegalName: EditText
    private lateinit var btnEditLegalName: ImageButton
    private lateinit var etShortName: EditText
    private lateinit var btnEditShortName: ImageButton
    private lateinit var etVat: EditText
    private lateinit var btnEditVat: ImageButton
    //#endregion Shop Info

    //#region Shop Settings
    private lateinit var llShopSettingsSection: LinearLayout
    private lateinit var ivShopSettingsExpand: ImageView
    private lateinit var ivShopSettingsCollapse: ImageView
    private lateinit var ivShopSettingsSave: ImageView
    private lateinit var llShopSettingsContent: LinearLayout
    private lateinit var etCapacity: EditText
    private lateinit var btnEditCapacity: ImageButton
    private lateinit var etHourlyRate: EditText
    private lateinit var btnEditHourlyRate: ImageButton
    private lateinit var etWorkHoursStart: EditText
    private lateinit var etWorkHoursEnd: EditText
    private lateinit var btnEditWorkHours: ImageButton
    private lateinit var fabMonday: FloatingActionButton
    private lateinit var fabTuesday: FloatingActionButton
    private lateinit var fabWednesday: FloatingActionButton
    private lateinit var fabThursday: FloatingActionButton
    private lateinit var fabFriday: FloatingActionButton
    private lateinit var fabSaturday: FloatingActionButton
    private lateinit var fabSunday: FloatingActionButton

    //#endregion Shop Settings

    //#region System
    private lateinit var llSystemSection: LinearLayout
    private lateinit var ivSystemExpand: ImageView
    private lateinit var ivSystemCollapse: ImageView
    private lateinit var systemContent: LinearLayout
    private lateinit var btnLogout: Button

    //#endregion System
    private var shopData: Shop? = null
    private var monday: Boolean = false
    private var tuesday: Boolean = false
    private var wednesday: Boolean = false
    private var thursday: Boolean = false
    private var friday: Boolean = false
    private var saturday: Boolean = false
    private var sunday: Boolean = false

    //#region Init
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeComponents()
        retrieveShop()
    }

    override fun initializeComponents() {
        initializeAccountSection()
        initializeShopInfoSection()
        initializeShopSettingsSection()
        initializeSystemSection()
    }

    private fun initializeShopSettingsSection() {
        llShopSettingsSection = findViewById(R.id.llShopSettingsSection)
        ivShopSettingsExpand = findViewById(R.id.ivShopSettingsExpand)
        ivShopSettingsCollapse = findViewById(R.id.ivShopSettingsCollapse)
        ivShopSettingsSave = findViewById(R.id.ivShopSettingsSave)
        llShopSettingsContent = findViewById(R.id.llShopSettingsContent)
        etCapacity = findViewById(R.id.etCapacity)
        btnEditCapacity = findViewById(R.id.btnEditCapacity)
        etHourlyRate = findViewById(R.id.etHourlyRate)
        btnEditHourlyRate = findViewById(R.id.btnEditHourlyRate)
        etWorkHoursStart = findViewById(R.id.etWorkHoursStart)
        etWorkHoursEnd = findViewById(R.id.etWorkHoursEnd)
        btnEditWorkHours = findViewById(R.id.btnEditWorkHours)
        fabMonday = findViewById(R.id.fabMonday)
        fabTuesday = findViewById(R.id.fabTuesday)
        fabWednesday = findViewById(R.id.fabWednesday)
        fabThursday = findViewById(R.id.fabThursday)
        fabFriday = findViewById(R.id.fabFriday)
        fabSaturday = findViewById(R.id.fabSaturday)
        fabSunday = findViewById(R.id.fabSunday)

        val startTimeListener =
            TimePickerDialog.OnTimeSetListener { _, hourOfDay: Int, minute: Int ->
                shopData!!.WorkHours =
                    "${joinHourMinute(hourOfDay, minute)}:${shopData!!.WorkHours!!.split('-')[1]}"
                updateLabel(etWorkHoursStart, shopData!!.WorkHours!!)
            }
        etWorkHoursStart.setOnClickListener {
            TimePickerDialog(
                this,
                startTimeListener,
                getStartHour(shopData!!.WorkHours),
                getStartMinute(shopData!!.WorkHours),
                true
            ).show()
        }

        etWorkHoursStart.setOnFocusChangeListener { _, inFocus ->
            if (inFocus) {
                TimePickerDialog(
                    this,
                    startTimeListener,
                    getStartHour(shopData!!.WorkHours),
                    getStartMinute(shopData!!.WorkHours),
                    true
                ).show()
            }
        }

        val endTimeListener =
            TimePickerDialog.OnTimeSetListener { _, hourOfDay: Int, minute: Int ->
                shopData!!.WorkHours =
                    "${shopData!!.WorkHours!!.split('-')[0]}:${joinHourMinute(hourOfDay, minute)}"
                updateLabel(etWorkHoursStart, shopData!!.WorkHours!!)
            }
        etWorkHoursEnd.setOnClickListener {
            TimePickerDialog(
                this,
                endTimeListener,
                getEndHour(shopData!!.WorkHours),
                getEndMinute(shopData!!.WorkHours),
                true
            ).show()
        }

        etWorkHoursEnd.setOnFocusChangeListener { _, inFocus ->
            if (inFocus) {
                TimePickerDialog(
                    this,
                    endTimeListener,
                    getEndHour(shopData!!.WorkHours),
                    getEndMinute(shopData!!.WorkHours),
                    true
                ).show()
            }
        }
    }

    private fun reloadWeekdayFabs(){
        fabMonday = FloatingActionButton(this, null, if(monday) R.style.primary_button else R.style.white_button)
        fabTuesday = FloatingActionButton(this, null, if(tuesday) R.style.primary_button else R.style.white_button)
        fabWednesday = FloatingActionButton(this, null, if(wednesday) R.style.primary_button else R.style.white_button)
        fabThursday = FloatingActionButton(this, null, if(thursday) R.style.primary_button else R.style.white_button)
        fabFriday = FloatingActionButton(this, null, if(friday) R.style.primary_button else R.style.white_button)
        fabSaturday = FloatingActionButton(this, null, if(saturday) R.style.primary_button else R.style.white_button)
        fabSunday = FloatingActionButton(this, null, if(sunday) R.style.primary_button else R.style.white_button)
        setFabListeners()
    }

    private fun setFabListeners(){
        fabMonday.setOnClickListener {
            monday = !monday
            fabMonday = FloatingActionButton(this, null, if(monday) R.style.primary_button else R.style.white_button)
            setFabListeners()
        }
        fabTuesday.setOnClickListener {
            tuesday = !tuesday
            fabTuesday = FloatingActionButton(this, null, if(tuesday) R.style.primary_button else R.style.white_button)
            setFabListeners()
        }
        fabWednesday.setOnClickListener {
            wednesday = !wednesday
            fabWednesday = FloatingActionButton(this, null, if(wednesday) R.style.primary_button else R.style.white_button)
            setFabListeners()
        }
        fabThursday.setOnClickListener {
            thursday = !thursday
            fabThursday = FloatingActionButton(this, null, if(thursday) R.style.primary_button else R.style.white_button)
            setFabListeners()
        }
        fabFriday.setOnClickListener {
            friday = !friday
            fabFriday = FloatingActionButton(this, null, if(friday) R.style.primary_button else R.style.white_button)
            setFabListeners()
        }
        fabSaturday.setOnClickListener {
            saturday = !saturday
            fabSaturday = FloatingActionButton(this, null, if(saturday) R.style.primary_button else R.style.white_button)
            setFabListeners()
        }
        fabSunday.setOnClickListener {
            sunday = !sunday
            fabSunday = FloatingActionButton(this, null, if(sunday) R.style.primary_button else R.style.white_button)
            setFabListeners()
        }
    }

    private fun joinHourMinute(hourOfDay: Int, minute: Int): String {
        return "${
            if (hourOfDay < 10) "0$hourOfDay"
            else hourOfDay
        }:${
            if (minute < 10) "0$minute"
            else minute
        }"
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


    private fun initializeShopInfoSection() {
        llShopInfoSection = findViewById(R.id.llShopInfoSection)
        ivShopInfoExpand = findViewById(R.id.ivShopInfoExpand)
        ivShopInfoCollapse = findViewById(R.id.ivShopInfoCollapse)
        ivShopInfoSave = findViewById(R.id.ivShopInfoSave)
        llShopInfoContent = findViewById(R.id.llShopInfoContent)
        etLegalName = findViewById(R.id.etLegalName)
        btnEditLegalName = findViewById(R.id.btnEditLegalName)
        etShortName = findViewById(R.id.etShortName)
        btnEditShortName = findViewById(R.id.btnEditShortName)
        etVat = findViewById(R.id.etVat)
        btnEditVat = findViewById(R.id.btnEditVat)


        llShopInfoSection.setOnClickListener {
            toggleHeader(
                llShopInfoContent,
                ivShopInfoExpand,
                ivShopInfoCollapse,
                ivShopInfoSave
            ) { saveShopInfo() }

        }

        btnEditLegalName.setOnClickListener {
            enableEditField(
                etLegalName,
                btnEditLegalName,
                ivShopInfoSave,
                ivShopInfoCollapse
            )

        }
        btnEditShortName.setOnClickListener {
            enableEditField(etShortName, btnEditShortName, ivShopInfoSave, ivShopInfoCollapse)

        }
        btnEditVat.setOnClickListener {
            enableEditField(etVat, btnEditVat, ivShopInfoSave, ivShopInfoCollapse)

        }
    }

    private fun initializeAccountSection() {
        llAccountSection = findViewById(R.id.llAccountSection)
        ivAccountInfoExpand = findViewById(R.id.ivAccountInfoExpand)
        ivAccountInfoCollapse = findViewById(R.id.ivAccountInfoCollapse)
        ivAccountInfoSave = findViewById(R.id.ivAccountInfoSave)
        accountInfoContent = findViewById(R.id.accountInfoContent)
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

            val newConfirmDialog = Helper.showConfirmDialog(
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

    private fun getStartHour(workHours: String?): Int {
        return try {
            val rawHourMinute = workHours!!.split('-')[0]
            val rawHour = rawHourMinute.split(':')[0]
            rawHour.toInt()
        } catch (e: Exception) {
            8
        }
    }

    private fun getStartMinute(workHours: String?): Int {
        return try {
            val rawHourMinute = workHours!!.split('-')[0]
            val rawMinute = rawHourMinute.split(':')[1]
            rawMinute.toInt()
        } catch (e: Exception) {
            0
        }
    }

    private fun getEndHour(workHours: String?): Int {
        return try {
            val rawHourMinute = workHours!!.split('-')[1]
            val rawHour = rawHourMinute.split(':')[0]
            rawHour.toInt()
        } catch (e: Exception) {
            16
        }
    }

    private fun getEndMinute(workHours: String?): Int {
        return try {
            val rawHourMinute = workHours!!.split('-')[1]
            val rawMinute = rawHourMinute.split(':')[1]
            rawMinute.toInt()
        } catch (e: Exception) {
            0
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
            ivSave != null && ivSave.visibility == View.VISIBLE -> {
                saveFn()
                hideComponent(ivSave)
                hideComponent(ivExpand)
                showComponent(ivCollapse)
            }
            ivExpand.visibility == View.VISIBLE -> {
                hideComponent(ivExpand)
                showComponent(ivCollapse)
                showComponent(llContent)
            }
            ivCollapse.visibility == View.VISIBLE -> {
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
        if (ivSave.visibility != View.VISIBLE) {
            hideComponent(ivCollapse)
            showComponent(ivSave)
        }
    }

    //#endregion Init

    //#region Retrieve User
    private fun retrieveShop() {
        showSpinner()
        apiService.retrieveShop(getShop().Id!!).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    hideSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleRetrieveShopResponse(response)
                    hideSpinner()
                }
            }
        })
    }

    fun handleRetrieveShopResponse(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Shop>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {

            try {
                shopData = resp.Data as Shop
                Helper.createOrEditSharedPreference(
                    PreferenceEnum.SHOP,
                    Helper.serializeData(shopData)
                )

                populateAccountSection(shopData!!)
                populateShopInformationSection(shopData!!)
                populateShopSettingsSection(shopData!!)

            } catch (e: InvalidObjectException) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    private fun populateShopSettingsSection(shop: Shop) {
        etCapacity.setText(if(shop.CarCapacity != null) shop.CarCapacity!!.toString() else "1" )
        etHourlyRate.setText(if(shop.HourlyRate != null) shop.HourlyRate!!.toString() else "1" )
        etWorkHoursStart.setText(if(shop.WorkHours != null) shop.WorkHours!!.split('-')[0] else "" )
        etWorkHoursEnd.setText(if(shop.WorkHours != null) shop.WorkHours!!.split('-')[1] else "" )
        if(shop.WorkDays != null){
            sunday = shop.WorkDays!![0] == '1'
            monday = shop.WorkDays!![1] == '1'
            tuesday = shop.WorkDays!![2] == '1'
            wednesday = shop.WorkDays!![3] == '1'
            thursday = shop.WorkDays!![4] == '1'
            friday = shop.WorkDays!![5] == '1'
            saturday = shop.WorkDays!![6] == '1'
        }
        else {
            sunday = false
            monday = false
            tuesday = false
            wednesday = false
            thursday = false
            friday = false
            saturday = false
        }
        reloadWeekdayFabs()
    }

    private fun populateShopInformationSection(shop: Shop) {
        etLegalName.setText(shop.LegalName)
        etShortName.setText(shop.ShortName)
        etVat.setText(shop.Vat)
    }

    private fun populateAccountSection(shop: Shop) {
        etEmail.setText(shop.Email)
    }

    //#region Update User
    private fun updateShop(shop: Shop) {
        showSpinner()
        apiService.updateShop(shop).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    hideSpinner()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleUpdateShopResponse(response)
                    hideSpinner()
                }
            }
        })
    }

    fun handleUpdateShopResponse(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Shop>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {

            try {
                retrieveShop()
            } catch (e: InvalidObjectException) {
                Helper.showLongToast(this, e.message.toString())
            }
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateLabel(editText: EditText, value: String) {
        editText.setText(value)
    }
    //#endregion Update User

    //#endregion Retrieve User


    private fun saveShopInfo() {
        val shop = getShop()
        if (etLegalName.isEnabled) {
            shop.LegalName = etLegalName.text.toString()
            etLegalName.isEnabled = false
            showComponent(btnEditLegalName)
        }

        if (etShortName.isEnabled) {
            shop.ShortName = etShortName.text.toString()
            etShortName.isEnabled = false
            showComponent(btnEditShortName)

        }

        if (etVat.isEnabled) {
            shop.Vat = etVat.text.toString()
            etVat.isEnabled = false
            showComponent(btnEditVat)
        }

        updateShop(shop)
    }

    private fun saveAccountInfo() {

        val shop = getShop()

        if (etEmail.isEnabled) {
            shop.Email = etEmail.text.toString()
            etEmail.isEnabled = false
            showComponent(btnEditEmail)
        }

        updateShop(shop)
    }


}