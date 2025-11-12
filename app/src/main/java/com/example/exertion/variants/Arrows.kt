package com.example.exertion.variants

import androidx.annotation.DrawableRes
import com.example.exertion.R

enum class ArrowVariant {
    Default,
    Forward,
    Up,
    Down
}

@DrawableRes
fun getArrowIcon(variant: ArrowVariant): Int {
    return when (variant) {
        ArrowVariant.Default -> R.drawable.ic_arrow_back
        ArrowVariant.Forward -> R.drawable.ic_arrow_forward
        ArrowVariant.Up -> R.drawable.ic_arrow_up
        ArrowVariant.Down -> R.drawable.ic_arrow_down
    }
}
