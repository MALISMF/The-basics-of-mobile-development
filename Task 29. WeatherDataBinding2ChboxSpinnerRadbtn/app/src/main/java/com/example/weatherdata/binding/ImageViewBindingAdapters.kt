package com.example.weatherdata.binding

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

@BindingAdapter("imageResource")
fun ImageView.setImageResourceCompat(@DrawableRes resId: Int?) {
    if (resId == null || resId == 0) {
        setImageDrawable(null)
    } else {
        setImageResource(resId)
    }
}

