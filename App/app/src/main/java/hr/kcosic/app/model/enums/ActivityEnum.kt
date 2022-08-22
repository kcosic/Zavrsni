package hr.kcosic.app.model.enums

import androidx.appcompat.app.AppCompatActivity
import hr.kcosic.app.activity.LoginActivity
import hr.kcosic.app.activity.MainActivity
import hr.kcosic.app.activity.RegisterActivity
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
    REGISTER{
        override fun getClass(): KClass<RegisterActivity> = RegisterActivity::class
    },



    ;
    abstract fun getClass(): KClass<out AppCompatActivity>
}