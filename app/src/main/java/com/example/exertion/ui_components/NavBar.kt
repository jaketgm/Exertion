package com.example.exertion.ui_components

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun NavBar(
    user_name: String,
    is_dark_mode: Boolean,
    show_back_button: Boolean = false,
    nav_controller: NavController?,
    on_profile_click: () -> Unit,
) {

}