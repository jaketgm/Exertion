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
fun RectangularCard(
    header: String,
    focusText: String,
    modifier: Modifier = Modifier
) {
    val backgroundBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFC20E35), // stop 0%
            Color(0xFF5C0719)  // stop 100%
        ),
        start = Offset(0f, 0f),
        end = Offset(600f, 600f)
    )

    val outlineBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF252121), // 0%
            Color(0xE6342B2B), // 23%  (opacity 90%)
            Color(0xCC301919), // 77%  (opacity 80%)
            Color(0x99320C0C)  // 100% (opacity 60%)
        ),
        start = Offset(0f, 0f),
        end = Offset(400f, 400f)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(brush = backgroundBrush, shape = RoundedCornerShape(20.dp))
            .border(BorderStroke(1.5.dp, outlineBrush), shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp)
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
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = header,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Text(
                text = focusText,
                color = Color.White.copy(alpha = 0.7f),
                fontWeight = FontWeight.Medium,
                fontSize = 13.5.sp
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_forward),
            contentDescription = "Next",
            tint = Color.White.copy(alpha = 0.9f),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(22.dp)
        )
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF000000)
fun PreviewRectangularCards() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RectangularCard(
            header = "Today's Focus",
            focusText = "Upper Body ≪ Bench: Wave I ≪ 155x8 Queued"
        )

        RectangularCard(
            header = "Today's Focus",
            focusText = "Lower Body ≪ Squat: Wave II ≪ 225x6 Queued"
        )

        RectangularCard(
            header = "Today's Focus",
            focusText = "Core + Conditioning ≪ Circuit A ≪ Complete"
        )
    }
}