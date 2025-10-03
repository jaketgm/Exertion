package com.example.exertion.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomizableButton(
    text: String,
    text_color: Color,
    gradient: Brush,
    onClick: () -> Unit,
    cornerRadius: Dp = 6.dp
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = text_color
        ),
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(cornerRadius),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        onClick = { onClick() }
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = text_color)
        }
    }
}

@Composable
@Preview
private fun CustomizableButtonPreview() {
    CustomizableButton(
        text = "Button",
        text_color = Color.White,
        gradient = Brush.horizontalGradient(
            listOf(Color(0xFFC20E35), Color.Black)
        ),
        onClick = {}
    )
}