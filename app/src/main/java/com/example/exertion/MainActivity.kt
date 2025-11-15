package com.example.exertion

import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.trace
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.exertion.data.datastore.UserPreferencesDataStore
import com.example.exertion.data.user_table.UserVM
import com.example.exertion.screens.EccentricConcentricEvaluatorScreen
import com.example.exertion.screens.HomeScreen
import com.example.exertion.screens.LoginScreen
import com.example.exertion.ui.theme.BLACK_COLOR
import com.example.exertion.ui.theme.EXERTION_RED
import com.example.exertion.ui.theme.DARK_GREY
import com.example.exertion.ui.theme.ExertionTheme
import com.example.exertion.ui.theme.Typography
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import androidx.navigation.compose.composable
import com.example.exertion.utils.camera.CameraPermissionGate

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExertionTheme {

                val navController = rememberNavController()
                val userVM: UserVM = viewModel()
                val userPrefs = UserPreferencesDataStore(this)
                val scope = rememberCoroutineScope()

                val userId by userPrefs.userIdFlow.collectAsState(initial = null)

                // 1. LOGIN GATE   -------------------------------------------------------
                if (userId == null) {
                    LoginScreen(
                        navController = navController,
                        userVM = userVM,
                        onLoginSuccess = { uid ->
                            scope.launch { userPrefs.setLoggedInUserId(uid) }
                        }
                    )
                    return@ExertionTheme
                }

                val currentUser by userVM.observeUser(userId!!)
                    .collectAsState(initial = null)

                // 2. MAIN APP ROUTER (NavHost)  -----------------------------------------
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = Color.Transparent
                        ) {
                            HomeScreen(
                                navController = navController,
                                userName = currentUser?.username ?: "Loading...",
                                isDarkMode = true,
                                onProfileClick = {}
                            )
                        }
                    }

                    composable("eccentric_concentric") {
                        CameraPermissionGate {
                            EccentricConcentricEvaluatorScreen(
                                navController = navController,
                                exerciseName = "Bench Press"
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExertionTheme {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, Color.Magenta),
            color = Color.Transparent
        ) {
            HomeScreen(
                navController = navController,
                userName = "Jake",
                isDarkMode = true,
                onProfileClick = {}
            )
        }
    }
}
