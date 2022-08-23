package hr.kcosic.app.activity

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.BaseActivity
import hr.kcosic.app.model.bases.BaseResponse
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.responses.SingleResponse
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class LoginActivity : BaseActivity() {
    private lateinit var btnLogin: Button
    private lateinit var txtEmail: EditText
    private lateinit var txtPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initComponents()
    }

    private fun initComponents() {
        btnLogin = findViewById(R.id.btnLogin)
        txtEmail = findViewById(R.id.txtEmail)
        txtPassword = findViewById(R.id.txtPassword)

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

                override fun onFailure(call: Call, e: IOException){
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

        val resp: BaseResponse = Helper.parseStringResponse(response.body!!.string())

        if (resp.IsSuccess!!) {
            Helper.createOrEditSharedPreference(
                PreferenceEnum.USER.toString(),
                (resp as SingleResponse<*>).Data
            )
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