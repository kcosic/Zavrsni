package hr.kcosic.app.model.enums

import androidx.appcompat.app.AppCompatActivity
import hr.kcosic.app.activity.LoginActivity
import hr.kcosic.app.activity.MainActivity
import hr.kcosic.app.activity.RegisterUserActivity
import kotlin.reflect.KClass

enum class ActivityEnum {
    MAIN{
        override fun getClass(): KClass<MainActivity> = MainActivity::class
    },
    MAP{
        override fun getClass(): KClass<MainActivity> = MainActivity::class
    },
    USER_PROFILE{
        override fun getClass(): KClass<MainActivity> = MainActivity::class
    },
    SHOP_PROFILE{
        override fun getClass(): KClass<MainActivity> = MainActivity::class
    },
    LOGIN{
        override fun getClass(): KClass<LoginActivity> = LoginActivity::class
    },
    REGISTER_USER{
        override fun getClass(): KClass<RegisterUserActivity> = RegisterUserActivity::class
    },
    /*REGISTER_SHOP{
        override fun getClass(): KClass<RegisterUserActivity> = RegisterUserActivity::class
    },*/



    ;
    abstract fun getClass(): KClass<out AppCompatActivity>
}