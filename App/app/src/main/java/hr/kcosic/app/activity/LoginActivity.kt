package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.SwitchCompat
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.BaseActivity
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.entities.Token
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.responses.SingleResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.io.InvalidObjectException

class LoginActivity : BaseActivity() {
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var btnRegisterUser: Button
    private lateinit var btnRegisterShop: Button
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText
    private lateinit var registerDialog: AlertDialog
    private lateinit var swLoginAsShop: SwitchCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val token = Helper.retrieveSharedPreference<String>(PreferenceEnum.TOKEN)
        val authFor = Helper.retrieveSharedPreference<String>(PreferenceEnum.AUTH_FOR)
        if (false && token != Helper.NO_VALUE && authFor != Helper.NO_VALUE) {
            Helper.showShortToast(this,getString(R.string.already_logged_in))
            Helper.openActivity(this,if(authFor == "User")ActivityEnum.HOME_USER else ActivityEnum.HOME_USER)
        }
        setContentView(R.layout.activity_login)
        initializeComponents()
    }

    @SuppressLint("SetTextI18n")             //TODO remove, for dev purposes
    override fun initializeComponents() {
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)

        txtEmail.setText("menta")               //TODO: remove, for dev purposes only
        txtPassword.setText("123456")           //TODO: remove, for dev purposes only

        swLoginAsShop = findViewById(R.id.sw_login_as_shop)

        btnRegister.setOnClickListener {
            val dialogView = Helper.inflateView(R.layout.register_dialog)
            btnRegisterUser = dialogView.findViewById(R.id.btnUserRegister)
            btnRegisterShop = dialogView.findViewById(R.id.btnShopRegister)


            btnRegisterUser.setOnClickListener {
                Helper.openActivity(this, ActivityEnum.REGISTER_USER)
                registerDialog.dismiss()
            }
            btnRegisterShop.setOnClickListener {
                Helper.openActivity(this, ActivityEnum.REGISTER_SHOP)
                registerDialog.dismiss()
            }
            registerDialog = Helper.showAlertDialog(this, dialogView)
        }

        btnLogin.setOnClickListener {

            if (txtEmail.text.isEmpty()) {
                txtEmail.setError(getString(R.string.invalid_email), Helper.getErrorIcon())
            } else {
                txtEmail.error = null
            }

            if (txtPassword.text.isEmpty()) {
                txtPassword.setError(getString(R.string.invalid_password), Helper.getErrorIcon())

            } else {
                txtPassword.error = null
            }

            if (txtEmail.text.isNotEmpty() && txtPassword.text.isNotEmpty()) {
                login()
            }

        }
    }

    private fun login() {
        if(swLoginAsShop.isActivated){
            Helper.setAuthKeyToShop()
        }else {
            Helper.setAuthKeyToUser()
        }
        apiService.login(txtEmail.text.toString(), txtPassword.text.toString())
            .enqueue(object :
                Callback {
                var mainHandler: Handler = Handler(applicationContext.mainLooper)

                override fun onFailure(call: Call, e: IOException) {
                    mainHandler.post {
                        handleLoginException(call, e)
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    mainHandler.post {
                        handleLoginSuccess(response)
                    }
                }
            })
    }

    fun handleLoginSuccess(response: Response) {

        val resp: BaseResponse =
            Helper.parseStringResponse<SingleResponse<Token>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            val data = resp.Data as Token

            try {
                Helper.createOrEditSharedPreference(
                    PreferenceEnum.TOKEN,
                    data.TokenValue
                )

                Helper.createOrEditSharedPreference(
                    PreferenceEnum.AUTH_FOR,
                    Helper.AUTH_FOR_KEY
                )
                Helper.createOrEditSharedPreference(
                    if(Helper.AUTH_FOR_KEY == "User")PreferenceEnum.USER else PreferenceEnum.SHOP,
                    if(Helper.AUTH_FOR_KEY == "User")Helper.serializeData(data.User) else Helper.serializeData(data.Shop)
                )
                Helper.openActivity(this, if(Helper.AUTH_FOR_KEY == "User")ActivityEnum.HOME_USER else ActivityEnum.HOME_USER)

            } catch (e: InvalidObjectException) {
                Helper.showLongToast(this, e.message.toString())
            }

        } else {
            handleLoginError(resp.Message!!)
        }
    }

    private fun handleLoginError(message: String) {
        Helper.showLongToast(this, message)
    }

    fun handleLoginException(call: Call, e: Exception) {
        call.cancel()
        Helper.showLongToast(this, e.message.toString())

    }
}