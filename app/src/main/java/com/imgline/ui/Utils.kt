package com.imgline.ui

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.imgline.R
import com.imgline.data.network.imgur.ImgurDefaultSource
import java.lang.IllegalArgumentException

fun getCircleProgressDrawable(ctx: Context): Drawable {
    val drawable = CircularProgressDrawable(ctx)
    drawable.setStyle(CircularProgressDrawable.LARGE)
    drawable.start()
    return drawable
}

private fun getInSampleSize(options: BitmapFactory.Options, requiredWidth: Int, requiredHeight: Int): Int {
    val (width, height) = options.run{outWidth to outHeight}
    var sampleSize = 1
    //Do not calculate sampling if  both width & height < output size.
    if (width > requiredWidth || height > requiredHeight) {
        val scaledWidth = width / 2
        val scaledHeight = height / 2
        // Calculate sample size (power of 2) which still is larger than requested size.
        while (scaledWidth/sampleSize >= requiredWidth && scaledHeight/sampleSize >= requiredHeight) {
            sampleSize *= 2
        }
    }
    return sampleSize
}

fun scaleBitmap(res: Resources, @DrawableRes resID : Int, requiredWidth: Int, requiredHeight: Int): Bitmap {
    BitmapFactory.Options().run{
        inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resID, this)
        inSampleSize = getInSampleSize(this, requiredWidth, requiredHeight)
        inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resID, this)
    }
}

fun autosizeGridLayout(cardSize: Int, ctx: Context): Int {
    val dpUtils = ctx.resources.displayMetrics
    val screenWidth = dpUtils.widthPixels
    val columns = screenWidth / cardSize
    return columns
}
