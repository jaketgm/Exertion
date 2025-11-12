package com.example.exertion.variants

import androidx.annotation.DrawableRes
import com.example.exertion.R

enum class VisibilityVariant {
    On,
    Off
}

@DrawableRes
fun getVisibilityIcon(variant: VisibilityVariant): Int {
    return when (variant) {
        VisibilityVariant.On -> R.drawable.visibility_on
        VisibilityVariant.Off -> R.drawable.visibility_off
    }
}