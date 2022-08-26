package hr.kcosic.app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.BaseActivity

class RegisterShopActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_shop)
        initializeComponents()
    }

    override fun initializeComponents() {
        TODO("Not yet implemented")
    }
}