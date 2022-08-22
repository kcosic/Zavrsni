package hr.kcosic.app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.BaseActivity
import hr.kcosic.app.model.entities.User
import hr.kcosic.app.model.enums.PreferenceEnum
import hr.kcosic.app.model.helpers.Helper
import hr.kcosic.app.model.responses.SingleResponse
import kotlinx.coroutines.launch

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
        activityScope.launch {
            val response = apiService.login(txtEmail.text.toString(), txtPassword.text.toString())
            if(response.IsSuccess){
                Helper.createOrEditSharedPreference(PreferenceEnum.USER.toString(), (response as SingleResponse<*>).Data)
            }
            else {
                Helper.showLongToast(response.Message)
            }
        }
    }
}