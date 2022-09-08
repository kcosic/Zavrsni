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
    private lateinit var txtFirstName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtUsername: EditText
    private lateinit var txtDob: EditText
    private lateinit var txtPassword: EditText
    private lateinit var txtRepeatPassword: EditText
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
        txtDob = findViewById(R.id.txtDob)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)
        txtRepeatPassword = findViewById(R.id.txtRepeatPassword)
        txtFirstName = findViewById(R.id.txtFirstName)
        txtUsername = findViewById(R.id.txtUserName)
        txtLastName = findViewById(R.id.txtLastName)
        progressBarHolder = findViewById(R.id.progressBarHolder)


        //TODO remove, for development only
        txtFirstName.setText("Testko")
        txtLastName.setText("Testic")
        txtDob.setText("09.08.1992")
        txtUsername.setText("Testko1")
        txtEmail.setText("test1@mail.com")
        txtPassword.setText("test1")
        txtRepeatPassword.setText("test1")


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
        txtDob.setOnClickListener {
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
        if (txtFirstName.text.isNullOrEmpty()) {
            valid = false
            txtFirstName.error = getString(R.string.required_value)
        } else if (!Helper.isStringInRange(txtLastName.text.toString(), 1, 50)) {
            valid = false
            txtFirstName.error = getString(R.string.length_between_1_50)
        }

        if (txtUsername.text.isNullOrEmpty()) {
            valid = false
            txtUsername.error = getString(R.string.required_value)
        } else if (!Helper.isStringInRange(txtUsername.text.toString(), 5, 50)) {
            valid = false
            txtUsername.error = getString(R.string.length_between_5_50)
        }

        if (txtDob.text.isNullOrEmpty()) {
            valid = false
            txtDob.error = getString(R.string.required_value)
        } else if (!Helper.isStringInRange(txtDob.text.toString(), 1, 50)) {
            valid = false
            txtDob.error = getString(R.string.length_between_1_50)
        }

        if (txtEmail.text.isNullOrEmpty()) {
            valid = false
            txtEmail.error = getString(R.string.required_value)
        }

        if (txtLastName.text.isNullOrEmpty()) {
            valid = false
            txtLastName.error = getString(R.string.required_value)
        } else if (!Helper.isStringInRange(txtLastName.text.toString(), 1, 50)) {
            valid = false
            txtLastName.error = getString(R.string.length_between_1_50)
        }

        if (txtPassword.text.isNullOrEmpty()) {
            valid = false
            txtPassword.error = getString(R.string.required_value)
        } else if (!Helper.isStringInRange(txtPassword.text.toString(), 1, 50)) {
            valid = false
            txtPassword.error = getString(R.string.length_between_1_50)
        }

        if (txtRepeatPassword.text.isNullOrEmpty()) {
            valid = false
            txtRepeatPassword.error = getString(R.string.required_value)
        } else if (!Helper.isStringInRange(txtRepeatPassword.text.toString(), 1, 50)) {
            valid = false
            txtRepeatPassword.error = getString(R.string.length_between_1_50)
        }
        if (txtRepeatPassword.text.toString() != txtPassword.text.toString()) {
            valid = false
            txtRepeatPassword.error = getString(R.string.passwords_must_match)
        }

        return valid
    }

    @SuppressLint("SimpleDateFormat")
    private fun register() {
        progressBarHolder.visibility = View.VISIBLE

        val formatter = SimpleDateFormat("dd.MM.yyyy")

        val newUser = User()
        newUser.FirstName = txtFirstName.text.toString()
        newUser.LastName = txtLastName.text.toString()
        newUser.Email = txtEmail.text.toString()
        newUser.Password = txtPassword.text.toString()
        newUser.Username = txtUsername.text.toString()
        newUser.DateOfBirth = formatter.parse(txtDob.text.toString())

        apiService.register(newUser).enqueue(object :
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
        val resp: BaseResponse = Helper.parseStringResponse<SingleResponse<String>>(response.body!!.string())
        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            progressBarHolder.visibility = View.GONE
            Helper.openActivity(this, ActivityEnum.LOGIN)
        } else {
            progressBarHolder.visibility = View.GONE
            handleRegisterError(resp.Message!!)
        }
    }

    fun handleRegisterError(message: String) {
        Helper.showLongToast(this, message)
        progressBarHolder.visibility = View.GONE

    }

    fun handleRegisterException(call: Call, e: Exception) {
        call.cancel()
        Helper.showLongToast(this, e.message.toString())
        progressBarHolder.visibility = View.GONE

    }

    @SuppressLint("SimpleDateFormat")
    private fun updateLabel() {
        val myFormat = "dd.MM.yyyy"
        val dateFormat = SimpleDateFormat(myFormat)
        txtDob.setText(dateFormat.format(dobCalendar.time))
    }
}