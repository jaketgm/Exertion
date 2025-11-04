package com.example.exertion.ui_components

import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exertion.R

@Composable
fun MiniBarChart(
    data: List<Float>,
    barColor: Color,
    barWidth: Dp,
    spaceWidth: Dp
) {
    val maxHeight = 50.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(maxHeight)
            .padding(top = 8.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEachIndexed { _, value ->
            Box(
                modifier = Modifier
                    .width(barWidth)
                    .fillMaxHeight(value.coerceIn(0f, 1f))
                    .background(barColor, shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp))
            )
        }
    }
}

@Composable
fun MetricCard(
    header: String,
    subHeader: String,
    barData: List<Float>, // normalized values 0.0–1.0
    modifier: Modifier = Modifier
) {
    // --- Background gradient ---
    val backgroundBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFC20E35), // top
            Color(0xFF5C0719)  // bottom
        ),
        start = Offset(0f, 0f),
        end = Offset(400f, 400f)
    )

    // --- Outline gradient ---
    val outlineBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF252121),
            Color(0xE6342B2B),
            Color(0xCC301919),
            Color(0x99320C0C)
        ),
        start = Offset(0f, 0f),
        end = Offset(400f, 400f)
    )

    Box(
        modifier = modifier
            .size(180.dp)
            .background(brush = backgroundBrush, shape = RoundedCornerShape(20.dp))
            .border(BorderStroke(1.5.dp, outlineBrush), shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        // 1. Noise texture overlay
        Image(
            painter = painterResource(id = R.drawable.random_static),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            alpha = 0.08f,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(20.dp))
        )

        // 2. Card content
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Column {
                // Header
                Text(
                    text = header,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )

                // Sub-header
                Text(
                    text = subHeader,
                    color = Color.White.copy(alpha = 0.75f),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Bar chart
            MiniBarChart(
                data = barData,
                barColor = Color(0xFFFF2B2B),
                barWidth = 3.dp,
                spaceWidth = 4.dp
            )

            // Arrow (bottom-right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = "Next",
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF000000)
fun PreviewMetricCards() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MetricCard(
            header = "RIR/RPE",
            subHeader = "Today: 2/8",
            barData = listOf(
                0.1f, 0.3f, 0.4f, 0.6f, 0.8f, 0.5f, 0.9f, 0.4f,
                0.2f, 0.6f, 0.5f, 0.9f, 0.7f, 0.3f, 0.8f, 0.6f
            )
        )

        MetricCard(
            header = "RIR/RPE",
            subHeader = "Today: 2/8",
            barData = listOf(
                0.2f, 0.1f, 0.3f, 0.4f, 0.7f, 0.6f, 0.8f, 0.9f,
                0.3f, 0.7f, 0.5f, 0.6f, 0.8f, 0.9f, 0.4f, 0.7f
            )
        )
    }
}