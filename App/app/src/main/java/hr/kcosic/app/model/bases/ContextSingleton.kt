package hr.kcosic.app.model.bases

import android.annotation.SuppressLint
import android.content.Context

class ContextSingleton {
    companion object{
        @SuppressLint("StaticFieldLeak")
        var singleton: Context? = null

        fun initialize(ctx: Context){
            if(singleton == null){
                singleton = ctx.applicationContext
            }
        }

        fun getContext(): Context?{
            return singleton
        }
    }
}