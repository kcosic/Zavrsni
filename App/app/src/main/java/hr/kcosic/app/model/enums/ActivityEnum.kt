package hr.kcosic.app.model.enums

import androidx.appcompat.app.AppCompatActivity
import hr.kcosic.app.R
import hr.kcosic.app.activity.*
import hr.kcosic.app.model.helpers.Helper
import java.lang.Exception
import kotlin.reflect.KClass

enum class ActivityEnum(val layoutId: Int, val menuItemId: Int) {
    HOME_USER(R.layout.activity_home_user, R.id.home) {
        override fun getClass(): KClass<HomeUserActivity> = HomeUserActivity::class
    },
    SEARCH(R.layout.activity_search,R.id.search_service) {
        override fun getClass(): KClass<SearchActivity> = SearchActivity::class
    },
    NEW_REQUEST(R.layout.activity_new_request,R.id.search_service) {
        override fun getClass(): KClass<NewRequestActivity> = NewRequestActivity::class
    },
    LOGIN(R.layout.activity_login, -1) {
        override fun getClass(): KClass<LoginActivity> = LoginActivity::class
    },
    REGISTER_USER(R.layout.activity_register_user,-1) {
        override fun getClass(): KClass<RegisterUserActivity> = RegisterUserActivity::class
    },
    REGISTER_SHOP(R.layout.activity_register_shop,-1) {
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
                }
            }

            throw Exception("Wrong resource ID in ActivityEnum")
        }
    }
}