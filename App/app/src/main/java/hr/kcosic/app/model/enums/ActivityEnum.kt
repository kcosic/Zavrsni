package hr.kcosic.app.model.enums

import androidx.appcompat.app.AppCompatActivity
import hr.kcosic.app.activity.*
import kotlin.reflect.KClass

enum class ActivityEnum {
//    MAIN{
//        override fun getClass(): KClass<MainActivity> = MainActivity::class
//    },
//    MAP{
//        override fun getClass(): KClass<MainActivity> = MainActivity::class
//    },
    HOME_USER{
        override fun getClass(): KClass<HomeUserActivity> = HomeUserActivity::class
    },
//    HOME_SHOP{
//        override fun getClass(): KClass<
//                HomeShopActivity> = HomeShopActivity::class
//    },
    LOGIN{
        override fun getClass(): KClass<LoginActivity> = LoginActivity::class
    },
    REGISTER_USER{
        override fun getClass(): KClass<RegisterUserActivity> = RegisterUserActivity::class
    },
    REGISTER_SHOP{
        override fun getClass(): KClass<RegisterShopActivity> = RegisterShopActivity::class
    },



    ;
    abstract fun getClass(): KClass<out AppCompatActivity>
}