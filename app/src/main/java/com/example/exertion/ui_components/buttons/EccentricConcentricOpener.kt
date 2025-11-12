package com.example.exertion.ui_components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exertion.R

enum class OpenerVariant {
    Default,
    Dark
}

@Composable
fun EccentricConcentricOpener(
    variant: OpenerVariant = OpenerVariant.Default,
    size: Float = 150f,
    onClick: (() -> Unit)? = null
) {
    // Determine the gradient based on variant
    val brush = when (variant) {
        OpenerVariant.Default -> Brush.radialGradient(
            colors = listOf(
                Color(0xFFEAABAB).copy(alpha = 1f),
                Color(0xFFD59B9B).copy(alpha = 1f)
            ),
            center = Offset.Unspecified,
            radius = size * 1.2f
        )
        OpenerVariant.Dark -> Brush.radialGradient(
            colors = listOf(
                Color(0xFF3C0202).copy(alpha = 1f),
                Color(0xFF060606).copy(alpha = 1f)
            ),
            center = Offset.Unspecified,
            radius = size * 1.2f
        )
    }

    Box(
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(brush)
            .clickable { onClick?.invoke() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ec_logo),
            contentDescription = "EC Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size((size * 0.6f).dp)
        )
    }
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFF000000)
fun EccentricConcentricOpenerPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.spacedBy(40.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EccentricConcentricOpener(
            variant = OpenerVariant.Default,
            size = 150f
        )
        EccentricConcentricOpener(
            variant = OpenerVariant.Dark,
            size = 150f
        )
    }
}
