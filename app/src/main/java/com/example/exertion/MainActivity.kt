package com.example.exertion

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.exertion.data.datastore.UserPreferencesDataStore
import com.example.exertion.data.user_table.UserVM
import com.example.exertion.screens.EccentricConcentricEvaluatorScreen
import com.example.exertion.screens.HomeScreen
import com.example.exertion.screens.LoginScreen
import com.example.exertion.ui.theme.ExertionTheme
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
