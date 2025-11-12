package com.example.exertion.ui.components

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
import androidx.compose.ui.draw.clip
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
fun SquareCard(
    header: String,
    bodyText: String,
    modifier: Modifier = Modifier
) {
    val backgroundBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFC20E35), // stop 0%
            Color(0xFF5C0719)  // stop 100%
        ),
        start = Offset(0f, 0f),
        end = Offset(400f, 400f)
    )

    val outlineBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF252121), // 0%
            Color(0xE6342B2B), // 23%
            Color(0xCC301919), // 77%
            Color(0x99320C0C)  // 100%
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
        Image(
            painter = painterResource(id = R.drawable.random_static),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            alpha = 0.08f,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(20.dp))
        )

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = header,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = bodyText,
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )

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
fun PreviewSquareCards() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SquareCard(
            header = "Performance\nFeedback",
            bodyText = "DOTS • RIR •\n1RM • TUT"
        )

        SquareCard(
            header = "Recovery\nOverview",
            bodyText = "Sleep • HRV •\nCalories • Steps"
        )
    }
}