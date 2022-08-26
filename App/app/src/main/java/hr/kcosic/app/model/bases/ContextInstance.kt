package hr.kcosic.app.model.bases

import android.annotation.SuppressLint
import android.content.Context

class ContextInstance {
    companion object{
        @SuppressLint("StaticFieldLeak")
        var singleton: Context? = null

        fun initialize(ctx: Context){
            singleton = ctx
        }

        fun getContext(): Context?{
            return singleton
        }
    }
}