package hr.kcosic.app.activity

import android.os.Bundle
import hr.kcosic.app.model.bases.ValidatedActivityWithNavigation
import hr.kcosic.app.model.enums.ActivityEnum

class SettingsUserActivity : ValidatedActivityWithNavigation(ActivityEnum.SETTINGS_USER) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeComponents()
    }

    override fun initializeComponents() {
        TODO("Not yet implemented")
    }
}