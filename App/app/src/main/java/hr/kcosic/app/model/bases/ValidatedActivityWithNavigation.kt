package hr.kcosic.app.model.bases

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.forEach
import com.google.android.material.bottomnavigation.BottomNavigationView
import hr.kcosic.app.R
import hr.kcosic.app.model.enums.ActivityEnum
import hr.kcosic.app.model.helpers.Helper

abstract class ValidatedActivityWithNavigation(val activity: ActivityEnum) : ValidatedActivity() {
    private lateinit var bottomNavigation: BottomNavigationView
    lateinit var progressBarHolder: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity.layoutId)
        progressBarHolder = findViewById(R.id.progressBarHolder)

        bottomNavigation = findViewById(activity.navigationId)
        bottomNavigation.selectedItemId = activity.menuItemId
        bottomNavigation.menu.forEach { item ->
                item.setOnMenuItemClickListener {
                    progressBarHolder.visibility = View.VISIBLE
                    if(activity.menuItemId != item.itemId){
                        Helper.openActivity(
                            this,
                            ActivityEnum.findByResourceId(it.itemId)
                        )

                    }
                    onMenuItemSelected(it.itemId, it )

                }
        }
    }

    protected fun showSpinner(){
        progressBarHolder.visibility = View.VISIBLE
    }
    protected fun hideSpinner(){
        progressBarHolder.visibility = View.GONE
    }
}