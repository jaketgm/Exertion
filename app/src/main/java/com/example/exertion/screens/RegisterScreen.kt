package com.example.exertion.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exertion.R
import com.example.exertion.data.user_table.UserVM
import com.example.exertion.ui.components.GradientInputField
import kotlinx.coroutines.launch
import com.example.exertion.utils.sha256
import androidx.compose.runtime.getValue

@Composable
fun RegisterScreen(
    userVM: UserVM,
    onRegisterSuccess: suspend (Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        GradientInputField(
            label = "User Name",
            placeholder = "athena919",
            icon = R.drawable.ic_profile_placeholder,
            inputType = KeyboardType.Text,
            text = username,
            onTextChange = { username = it }
        )

        GradientInputField(
            label = "Email",
            placeholder = "you@example.com",
            icon = R.drawable.ic_email,
            inputType = KeyboardType.Email,
            text = email,
            onTextChange = { email = it }
        )

        GradientInputField(
            label = "Password",
            placeholder = "********",
            icon = R.drawable.ic_lock,
            inputType = KeyboardType.Password,
            isPasswordField = true,
            text = password,
            onTextChange = { password = it }
        )

        GradientInputField(
            label = "Confirm Password",
            placeholder = "********",
            icon = R.drawable.ic_lock,
            inputType = KeyboardType.Password,
            isPasswordField = true,
            text = confirmPassword,
            onTextChange = { confirmPassword = it }
        )

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red, fontSize = 13.sp)
        }

        if (successMessage.isNotEmpty()) {
            Text(successMessage, color = Color(0xFF00FF99), fontSize = 13.sp)
        }

        Button(
            onClick = {
                scope.launch {
                    errorMessage = ""
                    successMessage = ""

                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "All fields must be filled."
                        return@launch
                    }

                    if (!email.contains("@")) {
                        errorMessage = "Invalid email format."
                        return@launch
                    }

                    if (password != confirmPassword) {
                        errorMessage = "Passwords do not match."
                        return@launch
                    }

                    val normalizedUsername: String? =
                        if (username.trim().isEmpty()) null else username.trim()
                    val hash = password.sha256()

                    val newId = userVM.createUser(
                        username = normalizedUsername,
                        email = email,
                        passwordHash = hash
                    )

                    successMessage = "Account created!"
                    onRegisterSuccess(newId)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF003C)),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Register", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
