package com.example.exertion.variants

import androidx.annotation.DrawableRes
import com.example.exertion.R

enum class SettingsVariant {
    Default, // white
    Accent // #601C2A
}

@DrawableRes
fun getSettingsIcon(variant: SettingsVariant): Int {
    return when (variant) {
        SettingsVariant.Default -> R.drawable.ic_settings
        SettingsVariant.Accent -> R.drawable.ic_settings_accent
    }
}