package hr.kcosic.app.activity

import android.os.Bundle
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.ValidatedActivity

class RegisterActivity : ValidatedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }
}