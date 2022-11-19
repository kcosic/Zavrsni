package hr.kcosic.app.activity

import android.os.Bundle
import hr.kcosic.app.model.bases.ValidatedActivityWithNavigation
import hr.kcosic.app.model.enums.ActivityEnum

class HomeShopActivity : ValidatedActivityWithNavigation(ActivityEnum.HOME_SHOP) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeComponents()
    }

    override fun initializeComponents() {

    }
}