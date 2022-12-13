package hr.kcosic.app.model.helpers

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import android.text.TextPaint
import androidx.annotation.ColorLong
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import hr.kcosic.app.R
import hr.kcosic.app.model.bases.ContextInstance

class IconHelper {
    companion object {
        @SuppressLint("PrivateResource")
        fun getErrorIcon(): Drawable {
            val icon = AppCompatResources.getDrawable(
                ContextInstance.getContext()!!,
                com.google.android.material.R.drawable.mtrl_ic_error
            )!!
            DrawableCompat.setTint(icon, Color.parseColor(getColor(R.color.danger_500)))
            icon.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
            return icon
        }

        @SuppressLint("PrivateResource")
        fun getMenuIcon(): VectorDrawable {
            val icon = AppCompatResources.getDrawable(
                ContextInstance.getContext()!!,
                R.drawable.ellipsis_vertical
            )!! as VectorDrawable
            DrawableCompat.setTint(icon, Color.parseColor(getColor(R.color.primary_500)))
            icon.setBounds(0, 0, icon.intrinsicWidth, icon.intrinsicHeight)
            return icon
        }


        /**
         * Gets a Bitmap from provided Vector Drawable image
         *
         * @param vd VectorDrawable
         * @return Bitmap
         */
        fun createBitmapFromVectorDrawable(vd: Drawable, width: Int?, height: Int?): Bitmap? {
            return try {
                val bitmap: Bitmap = Bitmap.createBitmap(
                    vd.intrinsicWidth,
                    vd.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )

                val canvas = Canvas(bitmap)
                vd.setBounds(0, 0, width ?: canvas.width, height ?: canvas.height)
                vd.draw(canvas)
                bitmap
            } catch (e: OutOfMemoryError) {
                return null
            }
        }

        /**
         * Loads vector drawable and apply tint color on it.
         */
        fun loadVectorDrawableWithTintColor(
            @DrawableRes vdRes: Int,
            @ColorRes clrRes: Int
        ): Drawable {
            val drawable = ContextCompat.getDrawable(ContextInstance.getContext()!!, vdRes)
            DrawableCompat.setTint(
                drawable!!,
                ContextInstance.getContext()!!.resources.getColor(clrRes)
            )
            return drawable
        }

        /**
         * Converts given vector drawable to Bitmap drawable
         */
        fun convertVectorDrawableToBitmapDrawable(vd: Drawable, width: Int?, height: Int?): BitmapDrawable {
            //it is safe to create empty bitmap drawable from null source
            return BitmapDrawable(createBitmapFromVectorDrawable(vd, width, height))
        }

        /**
         * Loads vector drawable , applies tint on it and returns a wrapped bitmap drawable.
         * Bitmap drawable can be resized using setBounds method (unlike the VectorDrawable)
         * @param context Requires view context !
         */
        fun loadVectorDrawableWithTint(
            @DrawableRes vectorDrawableRes: Int, @ColorRes colorRes: Int, width: Int? = null, height: Int? = null
        ): Drawable {
            val vd: Drawable = loadVectorDrawableWithTintColor(
                vectorDrawableRes,
                colorRes
            )
            val bitmapDrawable: BitmapDrawable =
                convertVectorDrawableToBitmapDrawable(vd, width, height)
            val tint = ContextCompat.getColorStateList(ContextInstance.getContext()!!, colorRes)
            val wrappedDrawable = DrawableCompat.wrap(bitmapDrawable)
            DrawableCompat.setTintList(wrappedDrawable, tint)
            return wrappedDrawable
        }


        fun getColor(color: Int): String {
            //noinspection ResourceType
            return ContextInstance.getContext()!!.resources.getString(color)
        }

        fun textAsBitmap(text: String?, textSize: Float,@ColorRes textColor: Int): Bitmap? {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint.textSize = textSize
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.color = Color.MAGENTA
            paint.textAlign = Paint.Align.LEFT
            val baseline: Float = -paint.ascent() // ascent() is negative
            val width = (paint.measureText(text) + 0.0f).toInt() // round
            val height = (baseline + paint.descent() + 0.0f).toInt()
            val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(image)
            canvas.drawText(text!!, 0f, baseline, paint)
            return image
        }


    }
}