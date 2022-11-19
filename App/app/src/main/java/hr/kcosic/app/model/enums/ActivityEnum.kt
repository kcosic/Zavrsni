package hr.kcosic.app.model.enums

import androidx.appcompat.app.AppCompatActivity
import hr.kcosic.app.R
import hr.kcosic.app.activity.*
import hr.kcosic.app.model.helpers.Helper
import kotlin.reflect.KClass

enum class ActivityEnum(val layoutId: Int, val menuItemId: Int, val navigationId: Int) {
    HOME_USER(R.layout.activity_home_user, R.id.home, R.id.user_bottom_navigation) {
        override fun getClass(): KClass<HomeUserActivity> = HomeUserActivity::class
    },
    HOME_SHOP(R.layout.activity_home_shop, R.id.home, R.id.shop_bottom_navigation) {
        override fun getClass(): KClass<HomeShopActivity> = HomeShopActivity::class
    },
    SEARCH(R.layout.activity_search, R.id.search_service, R.id.user_bottom_navigation) {
        override fun getClass(): KClass<SearchActivity> = SearchActivity::class
    },
    NEW_REQUEST(R.layout.activity_new_request, R.id.search_service, R.id.user_bottom_navigation) {
        override fun getClass(): KClass<NewRequestActivity> = NewRequestActivity::class
    },
    REQUEST_LIST(R.layout.activity_request_list, R.id.requests, R.id.user_bottom_navigation) {
        override fun getClass(): KClass<RequestListActivity> = RequestListActivity::class
    },
    REQUEST_VIEW(R.layout.activity_request_view, R.id.requests, R.id.user_bottom_navigation) {
        override fun getClass(): KClass<RequestViewActivity> = RequestViewActivity::class
    },
    SETTINGS_USER(R.layout.activity_settings_user, R.id.settings, R.id.user_bottom_navigation) {
        override fun getClass(): KClass<SettingsUserActivity> = SettingsUserActivity::class
    },


    LOGIN(R.layout.activity_login, -1,-1) {
        override fun getClass(): KClass<LoginActivity> = LoginActivity::class
    },
    REGISTER_USER(R.layout.activity_register_user, -1,-1) {
        override fun getClass(): KClass<RegisterUserActivity> = RegisterUserActivity::class
    },
    REGISTER_SHOP(R.layout.activity_register_shop, -1,-1) {
        override fun getClass(): KClass<RegisterShopActivity> = RegisterShopActivity::class
    },


    ;

    abstract fun getClass(): KClass<out AppCompatActivity>

    companion object {
        fun findByResourceId(resId: Int): ActivityEnum {
            if (Helper.AUTH_FOR_KEY == "User") {
                when (resId) {
                    R.id.home -> return HOME_USER
                    R.id.search_service -> return SEARCH
                    R.id.requests -> return REQUEST_LIST
                    R.id.settings -> return SETTINGS_USER
                }
            }
            else if (Helper.AUTH_FOR_KEY == "Shop") {
                when (resId) {
                    R.id.home -> return HOME_SHOP
//                    R.id.current_repair -> return SEARCH
//                    R.id.repair_requests -> return REQUEST_LIST
//                    R.id.settings -> return SETTINGS_USER
                }
            }

            throw Exception("Wrong resource ID in ActivityEnum")
        }
    }
}