package com.example.exertion.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.exertion.ui_components.buttons.EccentricConcentricButton
import com.example.exertion.ui_components.navbar.NavBar
import com.example.exertion.utils.camera.InlineCameraPreview

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EccentricConcentricEvaluatorScreen(
    navController: NavController? = null,
    exerciseName: String = "Bench Press"
) {
    var isDetecting by remember { mutableStateOf(false) }
    var detectionText by remember { mutableStateOf("Exercise: $exerciseName") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        NavBar(
            user_name = "",
            is_dark_mode = true,
            show_back_button = true,
            nav_controller = navController,
            on_profile_click = {}
        )

        Spacer(Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp)
                .background(Color(0xFF000000))
        ) {
            if (isDetecting) {
                InlineCameraPreview(
                    modifier = Modifier.matchParentSize(),
                    onDetectionUpdated = { detected ->
                        detectionText = detected
                    }
                )
            }

            Text(
                text = detectionText,
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )

            if (!isDetecting) {
                EccentricConcentricButton(
                    text = "Detect",
                    size = 160f,
                    onClick = {
                        isDetecting = true
                        detectionText = "Detecting..."
                    },
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(
    showBackground = true,
    backgroundColor = 0xFF000000,
    name = "Eccentric Concentric – Preview"
)
@Composable
fun PreviewEccentricConcentricEvaluatorScreen() {
    var isDetecting by remember { mutableStateOf(false) }
    var detectionText by remember { mutableStateOf("Exercise: Bench Press") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavBar(
            user_name = "Jake",
            is_dark_mode = true,
            show_back_button = true,
            nav_controller = null,
            on_profile_click = {}
        )

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .background(Color(0xFF0A0A0A))
                .padding(12.dp)
        ) {
            Text(
                text = detectionText,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            if (!isDetecting) {
                EccentricConcentricButton(
                    text = "Detect",
                    size = 160f,
                    onClick = { isDetecting = true },
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Text(
                    text = "Detecting… (Preview Mode)",
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 18.sp
                )
            }
        }
    }
}