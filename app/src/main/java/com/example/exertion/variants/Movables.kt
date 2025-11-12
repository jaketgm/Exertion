package com.example.exertion.variants

import androidx.annotation.DrawableRes
import com.example.exertion.R

enum class MovableVariant {
    White,
    Black
}

@DrawableRes
fun getMovableIcon(variant: MovableVariant): Int {
    return when (variant) {
        MovableVariant.White -> R.drawable.ic_move_white
        MovableVariant.Black -> R.drawable.ic_move_black
    }
}
