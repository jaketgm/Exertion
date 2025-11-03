package com.example.exertion.ui_components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.exertion.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("UnusedBoxWithConstraintsScope")
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
    val formatted_date =
        now.format(DateTimeFormatter.ofPattern("MMMM d',' yyyy")).replace(Regex("\\d+,")) { match ->
            val number = match.value.dropLast(1).toInt()
            "$number$suffix"
        }

    // Gradients
    val dark_gradient = Brush.radialGradient(
        colors = listOf(Color(0x66000000), Color(0x1A723333)),
        center = androidx.compose.ui.geometry.Offset(0f, 0f),
        radius = 800f
    )

    val light_gradient = Brush.radialGradient(
        colors = listOf(Color(0xFFFF8080), Color(0x99FF5959)),
        center = androidx.compose.ui.geometry.Offset(0f, 0f),
        radius = 900f
    )

    val background_brush = if (is_dark_mode) dark_gradient else light_gradient
    val text_colour = if (is_dark_mode) Color.White else Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(background_brush)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Back button or date/greeting
            if (show_back_button) {
                IconButton(onClick = { nav_controller?.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = "Back",
                        tint = text_colour,
                        modifier = Modifier.size(28.dp)
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = formatted_date,
                        style = TextStyle(
                            color = text_colour.copy(alpha = 0.8f),
                            fontFamily = namdhinggo,
                            fontSize = 12.sp
                        )
                    )
                    Text(
                        text = "Hello, $user_name",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = text_colour,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                }
            }

            // Right side: Profile/Settings button (XML placeholder)
            IconButton(onClick = on_profile_click) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile_placeholder), // make a placeholder XML vector
                    contentDescription = "Profile",
                    tint = text_colour,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Light Mode - Default")
@Composable
fun PreviewNavBarLight() {
    NavBar(
        user_name = "Jake",
        is_dark_mode = false,
        show_back_button = false,
        nav_controller = rememberNavController(),
        on_profile_click = {}
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Dark Mode - Default", backgroundColor = 0xFF000000)
@Composable
fun PreviewNavBarDark() {
    NavBar(
        user_name = "Jake",
        is_dark_mode = true,
        show_back_button = false,
        nav_controller = rememberNavController(),
        on_profile_click = {}
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Light Mode - With Back Button")
@Composable
fun PreviewNavBarLightWithBack() {
    NavBar(
        user_name = "Jake", // ignored since show_back_button = true
        is_dark_mode = false,
        show_back_button = true,
        nav_controller = rememberNavController(),
        on_profile_click = {}
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, name = "Dark Mode - With Back Button", backgroundColor = 0xFF000000)
@Composable
fun PreviewNavBarDarkWithBack() {
    NavBar(
        user_name = "Jake", // ignored since show_back_button = true
        is_dark_mode = true,
        show_back_button = true,
        nav_controller = rememberNavController(),
        on_profile_click = {}
    )
}