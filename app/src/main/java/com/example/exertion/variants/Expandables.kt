package com.example.exertion.variants

import androidx.annotation.DrawableRes
import com.example.exertion.R

enum class ExpandableVariant {
    White,
    Black
}

@DrawableRes
fun getExpandableIcon(variant: ExpandableVariant): Int {
    return when (variant) {
        ExpandableVariant.White -> R.drawable.ic_expand_more
        ExpandableVariant.Black -> R.drawable.ic_expand_less
    }
}
