package com.example.exertion.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.example.exertion.R

@Composable
fun GradientInputField(
    label: String,
    placeholder: String,
    icon: Int,
    inputType: KeyboardType,
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    showTrailingIcon: Boolean = false,
    trailingIcon: (@Composable (() -> Unit))? = null,
    isPasswordField: Boolean = false
) {
    var text by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val backgroundBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xC2C20E35), // #C20E35 @ 80%
            Color(0xFFBC8F8F)  // #BC8F8F
        ),
        start = Offset(0f, 0f),
        end = Offset(400f, 400f)
    )

    val outlineBrush = Brush.linearGradient(
        colors = listOf(Color.White, Color.Red),
        start = Offset(0f, 0f),
        end = Offset(400f, 0f)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(brush = backgroundBrush, shape = RoundedCornerShape(16.dp))
            .border(BorderStroke(1.5.dp, outlineBrush), RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.noise_pattern),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            alpha = 0.08f,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(16.dp))
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
                    .padding(end = 8.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )

                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .focusable(true),
                    textStyle = TextStyle(color = Color.White),
                    placeholder = {
                        Text(
                            text = placeholder,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    },
                    visualTransformation = if (isPasswordField && !passwordVisible)
                        PasswordVisualTransformation()
                    else
                        VisualTransformation.None,
                    keyboardOptions = KeyboardOptions(keyboardType = inputType),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        cursorColor = Color.White
                    ),
                    trailingIcon = {
                        // FIXED composable control flow
                        when {
                            showTrailingIcon && trailingIcon != null -> {
                                trailingIcon.invoke()
                            }
                            isPasswordField -> {
                                val visibilityIcon = if (passwordVisible)
                                    R.drawable.visibility_on else R.drawable.visibility_off
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        painter = painterResource(id = visibilityIcon),
                                        contentDescription = "Toggle password visibility",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun InputFieldsPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

    }
}