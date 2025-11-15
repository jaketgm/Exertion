package com.example.exertion.ui_components.buttons

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun EccentricConcentricButton(
    modifier: Modifier = Modifier,
    text: String = "Detect",
    size: Float = 160f,
    onClick: (() -> Unit)? = null
) {
    var isPulsing by remember { mutableStateOf(false) }
    var pulsePhase by remember { mutableStateOf(0) }

    val transition = rememberInfiniteTransition(label = "pulseTransition")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseAnim"
    )

    LaunchedEffect(isPulsing) {
        if (isPulsing) {
            val startTime = System.currentTimeMillis()
            while (System.currentTimeMillis() - startTime < 5000L) {
                pulsePhase = when {
                    progress < 1f -> 1
                    progress < 2f -> 2
                    else -> 3
                }
                delay(100L)
            }
            isPulsing = false
            pulsePhase = 0
        }
    }

    Box(
        modifier = modifier
            .size(size.dp)
            .clickable(enabled = !isPulsing) {
                isPulsing = true
                onClick?.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size / 2f, size / 2f)
            val radius = size / 2f

            // --- Outer glow layer ---
            if (isPulsing) {
                val (outerScale, outerAlpha) = when (pulsePhase) {
                    1 -> 1.1f to 0.8f
                    2 -> 1.2f to 0.5f
                    3 -> 1.25f to 0.1f
                    else -> 1.0f to 0.0f
                }
                drawCircle(
                    color = Color(0xFFFFC300).copy(alpha = outerAlpha),
                    radius = radius * outerScale
                )
            }

            // --- Inner gradient based on phase ---
            val endStop = when (pulsePhase) {
                1 -> 0.76f
                2 -> 0.63f
                3 -> 0.53f
                else -> 1.0f
            }

            val gradient = Brush.radialGradient(
                colorStops = arrayOf(
                    0f to Color(0xFFFB0D0D).copy(alpha = 1f),
                    endStop to Color(0xFFFF9F19).copy(alpha = if (pulsePhase == 0) 0.8f else 1f)
                ),
                center = center,
                radius = radius * 1.3f
            )

            drawCircle(brush = gradient, radius = radius)
        }

        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000)
fun EccentricConcentricButtonPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        EccentricConcentricButton(
            text = "Detect",
            size = 160f
        )
    }
}