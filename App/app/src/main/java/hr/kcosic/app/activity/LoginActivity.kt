package hr.kcosic.app.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.BaseActivity
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.entities.Token
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.responses.SingleResponse
import kotlinx.serialization.json.Json
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        btnRegister.setOnClickListener{
            val dialogView = Helper.inflateView(R.layout.register_dialog)
            btnRegisterUser = dialogView.findViewById(R.id.btnUserRegister)
            btnRegisterShop = dialogView.findViewById(R.id.btnShopRegister)


            btnRegisterUser.setOnClickListener{
                Helper.openActivity(this, ActivityEnum.REGISTER_USER)
                //TODO: close dialog
            }
            btnRegisterShop.setOnClickListener{
                Helper.showShortToast(this, "Navigating to shop reg")
                //TODO: close dialog
            }
            Helper.showAlertDialog(this, dialogView)
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

        val resp: BaseResponse = Helper.parseStringResponse<SingleResponse<Token>>(response.body!!.string())

        if (resp.IsSuccess!! && resp is SingleResponse<*>) {
            val data = resp.Data as Token

            try{
                Helper.createOrEditSharedPreference(
                    PreferenceEnum.TOKEN,
                    data.TokenValue
                )
                Helper.createOrEditSharedPreference(
                    PreferenceEnum.USER,
                    Helper.serializeData(data.User)
                )
                Helper.openActivity(this, ActivityEnum.MAIN)

            } catch(e: InvalidObjectException){
                Helper.showLongToast(this, e.message.toString())
            }

        } else {
            handleLoginError(resp.Message!!)
        }
    }

    fun handleLoginError(message: String) {
        Helper.showLongToast(this, message)
    }

    fun handleLoginException(call: Call, e: Exception) {
        call.cancel()
        Helper.showLongToast(this, e.message.toString())

    }
}