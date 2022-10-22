package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.BaseActivity
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.entities.Token
import hr.kcosic.app.model.entities.User
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.responses.ErrorResponse
import hr.kcosic.app.model.responses.SingleResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.InvalidObjectException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.*


class RegisterUserActivity : BaseActivity() {

    private lateinit var btnFinishRegistration: Button
    private lateinit var btnBack: Button
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etDob: EditText
    private lateinit var etPassword: EditText
    private lateinit var etRepeatPassword: EditText
    private lateinit var progressBarHolder: FrameLayout
    private val dobCalendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        initializeComponents()
    }

    @SuppressLint("SetTextI18n")
    override fun initializeComponents() {
        btnFinishRegistration = findViewById(R.id.btnFinishRegistration)
        btnBack = findViewById(R.id.btnBack)
        etDob = findViewById(R.id.etDob)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etRepeatPassword = findViewById(R.id.etRepeatPassword)
        etFirstName = findViewById(R.id.etFirstName)
        etUsername = findViewById(R.id.etUserName)
        etLastName = findViewById(R.id.etLastName)
        progressBarHolder = findViewById(R.id.progressBarHolder)


        //TODO remove, for development only
        etFirstName.setText("Testko")
        etLastName.setText("Testic")
        etDob.setText("09.08.1992")
        etUsername.setText("Testko1")
        etEmail.setText("test1@mail.com")
        etPassword.setText("test1")
        etRepeatPassword.setText("test1")


        btnBack.setOnClickListener {
            Helper.openActivity(this, ActivityEnum.LOGIN)
        }

        btnFinishRegistration.setOnClickListener {
            if (isFormValid()) {
                register()
            }
        }

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
    }

    private fun isFormValid(): Boolean {
        var valid = true
        if (etFirstName.text.isNullOrEmpty()) {
            valid = false
            etFirstName.error = getString(R.string.required_value)
        } else if (!Helper.isStringInRange(etLastName.text.toString(), 1, 50)) {
            valid = false
            etFirstName.error = getString(R.string.length_between_1_50)
        }

        if (etUsername.text.isNullOrEmpty()) {
            valid = false
            etUsername.error = getString(R.string.required_value)
        } else if (!Helper.isStringInRange(etUsername.text.toString(), 5, 50)) {
            valid = false
            etUsername.error = getString(R.string.length_between_5_50)
        }

        if (etDob.text.isNullOrEmpty()) {
            valid = false
            etDob.error = getString(R.string.required_value)
        } else if (!Helper.isStringInRange(etDob.text.toString(), 1, 50)) {
            valid = false
            etDob.error = getString(R.string.length_between_1_50)
        }

        if (etEmail.text.isNullOrEmpty()) {
            valid = false
            etEmail.error = getString(R.string.required_value)
        }

        if (etLastName.text.isNullOrEmpty()) {
            valid = false
            etLastName.error = getString(R.string.required_value)
        } else if (!Helper.isStringInRange(etLastName.text.toString(), 1, 50)) {
            valid = false
            etLastName.error = getString(R.string.length_between_1_50)
        }

        if (etPassword.text.isNullOrEmpty()) {
            valid = false
            etPassword.error = getString(R.string.required_value)
        } else if (!Helper.isStringInRange(etPassword.text.toString(), 1, 50)) {
            valid = false
            etPassword.error = getString(R.string.length_between_1_50)
        }

        if (etRepeatPassword.text.isNullOrEmpty()) {
            valid = false
            etRepeatPassword.error = getString(R.string.required_value)
        } else if (!Helper.isStringInRange(etRepeatPassword.text.toString(), 1, 50)) {
            valid = false
            etRepeatPassword.error = getString(R.string.length_between_1_50)
        }
        if (etRepeatPassword.text.toString() != etPassword.text.toString()) {
            valid = false
            etRepeatPassword.error = getString(R.string.passwords_must_match)
        }

        return valid
    }

    @SuppressLint("SimpleDateFormat")
    private fun register() {
        progressBarHolder.visibility = View.VISIBLE

        val formatter = SimpleDateFormat("dd.MM.yyyy")

        val newUser = User()
        newUser.FirstName = etFirstName.text.toString()
        newUser.LastName = etLastName.text.toString()
        newUser.Email = etEmail.text.toString()
        newUser.Password = etPassword.text.toString()
        newUser.Username = etUsername.text.toString()
        newUser.DateOfBirth = formatter.parse(etDob.text.toString())

        apiService.register(newUser).enqueue(object :
            Callback {
            var mainHandler: Handler = Handler(applicationContext.mainLooper)

            override fun onFailure(call: Call, e: IOException) {
                mainHandler.post {
                    handleApiResponseException(call, e)
                    progressBarHolder.visibility = View.GONE

                }
            }

            override fun onResponse(call: Call, response: Response) {
                mainHandler.post {
                    handleRegisterSuccess(response)
                    progressBarHolder.visibility = View.GONE
                }
            }
        })
    }

    fun handleRegisterSuccess(response: Response) {
        val resp: BaseResponse = Helper.parseStringResponse<SingleResponse<String>>(response.body!!.string())
        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            Helper.openActivity(this, ActivityEnum.LOGIN)
        } else {
            handleApiResponseError(resp as ErrorResponse)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateLabel() {
        etDob.setText(Helper.formatDate(dobCalendar.time))
    }
}