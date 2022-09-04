package hr.kcosic.app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.ValidatedActivity

class HomeUserActivity : ValidatedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_user)
        initializeComponents()
    }

    override fun initializeComponents() {

    }
}