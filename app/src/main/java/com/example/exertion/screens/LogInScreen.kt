package com.example.exertion.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.exertion.R
import com.example.exertion.data.user_table.UserVM
import com.example.exertion.ui.components.GradientInputField
import com.example.exertion.ui_components.navbar.NavBar
import com.example.exertion.utils.sha256
import kotlinx.coroutines.launch
import java.security.MessageDigest

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LoginScreen(
    navController: NavController? = null,
    userVM: UserVM,
    onLoginSuccess: suspend (Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    var tab by remember { mutableStateOf("login") }
    var usernameOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }

    // Background
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF0A0A0A), Color(0xFF000000))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavBar(
                user_name = "",
                is_dark_mode = true,
                show_back_button = true,
                nav_controller = navController,
                on_profile_click = {}
            )

            Spacer(Modifier.height(60.dp))

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xAA1A1A1A))
                    .padding(20.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Login",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (tab == "login") Color(0xFFFF2B2B) else Color.White,
                            modifier = Modifier
                                .clickable { tab = "login" }
                                .padding(end = 16.dp)
                        )
                        Text(
                            text = "Register",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (tab == "register") Color(0xFFFF2B2B) else Color.White,
                            modifier = Modifier.clickable { tab = "register" }
                        )
                    }

                    if (tab == "login") {

                        GradientInputField(
                            label = "User Name / Email",
                            placeholder = "sample918",
                            icon = R.drawable.ic_profile_placeholder,
                            inputType = KeyboardType.Text,
                            text = usernameOrEmail,
                            onTextChange = { usernameOrEmail = it },
                            showTrailingIcon = false,
                            isPasswordField = false,
                            modifier = Modifier.fillMaxWidth()
                        )

                        GradientInputField(
                            label = "Password",
                            placeholder = "**********",
                            icon = R.drawable.ic_lock,
                            inputType = KeyboardType.Password,
                            text = password,
                            onTextChange = { password = it },
                            isPasswordField = true,
                            showTrailingIcon = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (errorMessage.isNotEmpty()) {
                            Text(
                                text = errorMessage,
                                color = Color.Red,
                                fontSize = 14.sp
                            )
                        }

                        Button(
                            onClick = {
                                scope.launch {

                                    val user =
                                        userVM.getUserByEmailOrUsername(usernameOrEmail)

                                    if (user == null) {
                                        errorMessage = "User not found."
                                        return@launch
                                    }

                                    val incomingHash = password.sha256()

                                    if (incomingHash != user.password_hash) {
                                        errorMessage = "Incorrect password."
                                        return@launch
                                    }

                                    onLoginSuccess(user.user_id)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF003C)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(25.dp)
                        ) {
                            Text(
                                "Login",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (tab == "register") {
                        Text(
                            text = "Registration coming soon",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}