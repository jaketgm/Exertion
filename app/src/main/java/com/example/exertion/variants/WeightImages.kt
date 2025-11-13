package com.example.exertion.variants

import androidx.annotation.DrawableRes
import com.example.exertion.R

enum class WeightImageVariant {
    FiveLbs,
    TenLbs,
    TwentyFiveLbs,
    ThirtyFiveLbs,
    FortyFiveLbs
}

@DrawableRes
fun getWeightImage(variant: WeightImageVariant): Int {
    return when (variant) {
        WeightImageVariant.FiveLbs -> R.drawable.five_lbs
        WeightImageVariant.TenLbs -> R.drawable.ten_lbs
        WeightImageVariant.TwentyFiveLbs -> R.drawable.twenty_five_lbs
        WeightImageVariant.ThirtyFiveLbs -> R.drawable.thirty_five_lbs
        WeightImageVariant.FortyFiveLbs -> R.drawable.fourty_five_lbs
    }
}