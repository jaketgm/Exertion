package com.example.exertion.ui_components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.exertion.R
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavController
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavBar(
    user_name: String,
    is_dark_mode: Boolean,
    show_back_button: Boolean = false,
    nav_controller: NavController?,
    on_profile_click: () -> Unit,
) {
    val namdhinggo = FontFamily(Font(R.font.namdhinggo_regular))
    val now = LocalDate.now()
    val day = now.dayOfMonth
    val suffix = when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }
    val formatted_date = now.format(DateTimeFormatter.ofPattern("MMMM d',' yyyy")).replace(Regex("\\d+,")) {
        match -> val number = match.value.dropLast(1).toInt()
        "$number$suffix"
    }

    // Variant Colours
    val dark_gradient = Brush.radialGradient(
        colors = listOf(
            Color(0x66000000),
            Color(0x1A723333)
        ),
        center = androidx.compose.ui.geometry.Offset(0f, 0f),
        radius = 800f
    )
}