package com.example.exertion.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.exertion.data.user_table.UserVM
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exertion.ui_components.navbar.NavBar
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsScreen(
    navController: NavController,
    userVM: UserVM = viewModel(),
    userId: Int
) {
    val scope = rememberCoroutineScope()

    val user by userVM.observeUser(userId).collectAsState(initial = null)

    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var gender: String? by remember { mutableStateOf("") }

    LaunchedEffect(user) {
        user?.let {
            name = it.username ?: ""
            age = it.age.toString()
            weight = it.weight_kg.toString()
            height = it.height_cm.toString()
            gender = it.gender
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
    ) {
        NavBar(
            user_name = name,
            is_dark_mode = true,
            show_back_button = true,
            nav_controller = navController,
            loggedInUserId = userId,
            on_profile_click = { /* already in settings */ },
            on_settings_click = { /* already in settings */ }
        )

        Spacer(modifier = Modifier.height(20.dp))

        SettingsInputRow(
            leftLabel = "Name:",
            leftValue = name,
            onLeftChanged = { name = it },
            rightLabel = "Age:",
            rightValue = age,
            onRightChanged = { age = it }
        )

        SettingsInputRow(
            leftLabel = "Weight (kg):",
            leftValue = weight,
            onLeftChanged = { weight = it },
            rightLabel = "Height (cm):",
            rightValue = height,
            onRightChanged = { height = it }
        )

        SettingsInputSingle(
            label = "Gender:",
            value = gender,
            onChanged = { gender = it }
        )

        Spacer(modifier = Modifier.height(40.dp))

        SaveSettingsButton(
            onSave = {
                scope.launch {
                    val updated = user?.copy(
                        username = name,
                        age = age.toIntOrNull(),
                        weight_kg = weight.toDoubleOrNull(),
                        height_cm = height.toDoubleOrNull(),
                        gender = gender
                    )
                    if (updated != null) {
                        userVM.updateUser(updated)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(80.dp))

        LogoutSection(
            onLogout = {
                scope.launch {
                    userVM.logout()
                }
                navController.navigate("login") {
                    popUpTo(0)
                }
            }
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun SettingsInputRow(
    leftLabel: String,
    leftValue: String,
    onLeftChanged: (String) -> Unit,
    rightLabel: String,
    rightValue: String,
    onRightChanged: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SettingsInputBox(
            label = leftLabel,
            value = leftValue,
            onChanged = onLeftChanged,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        SettingsInputBox(
            label = rightLabel,
            value = rightValue,
            onChanged = onRightChanged,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SettingsInputSingle(
    label: String,
    value: String?,
    onChanged: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        SettingsInputBox(
            label = label,
            value = value,
            onChanged = onChanged,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun SettingsInputBox(
    label: String,
    value: String?,
    onChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0D0D0D))
            .border(
                width = 1.dp,
                color = Color(0x55FFFFFF),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 12.dp)
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color.White.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(6.dp))

        if (value != null) {
            BasicTextField(
                value = value,
                onValueChange = onChanged,
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@Composable
fun SaveSettingsButton(onSave: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF292929))
            .clickable { onSave() }
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Save Changes",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun LogoutSection(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 26.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalDivider(thickness = 1.dp, color = Color.White.copy(alpha = 0.3f))
        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFFC20E35))
                .clickable { onLogout() }
                .padding(vertical = 14.dp, horizontal = 50.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Log Out",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.White.copy(alpha = 0.3f))
    }
}
