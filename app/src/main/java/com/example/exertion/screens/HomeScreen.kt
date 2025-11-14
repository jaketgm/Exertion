package com.example.exertion.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.exertion.R
import com.example.exertion.ui.components.RectangularCard
import com.example.exertion.ui.components.SquareCard
import com.example.exertion.ui_components.cards.MetricCard
import com.example.exertion.ui_components.cards.MiniBarChart
import com.example.exertion.ui_components.navbar.NavBar

@Composable
fun SectionDivider(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(38.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0x661A1818),  // 40% opacity
                        Color(0x661D080D)   // 40% opacity
                    ),
                    start = Offset.Zero,
                    end = Offset(400f, 400f)
                ),
                shape = RoundedCornerShape(14.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun FeaturedWeeklyDataCard(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
            .padding(horizontal = 16.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFC20E35),
                        Color(0xFF5C0719)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(400f, 400f)
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .border(
                BorderStroke(
                    1.5.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF252121),
                            Color(0xE6342B2B),
                            Color(0xCC301919),
                            Color(0x99320C0C)
                        )
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            // Title + arrow
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Workout: Bench Press (Ecc/Con Rep)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Icon(
                    painter = painterResource(id =  R.drawable.ic_arrow_forward),
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bar chart (reuse MiniBarChart)
            MiniBarChart(
                data = listOf(
                    0.7f, 0.4f, 0.6f, 0.9f, 0.3f, 0.5f, 0.8f,
                    0.4f, 0.7f, 0.6f, 0.9f, 0.2f, 0.5f, 0.7f
                ),
                barColor = Color(0xFFFF4D4D),
                barWidth = 4.dp,
                spaceWidth = 4.dp
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController?,
    userName: String,
    isDarkMode: Boolean,
    onProfileClick: () -> Unit
) {
    // Background gradient for the whole screen.
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0A0A0A),
            Color(0xFF000000)
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Transparent      // <-- IMPORTANT
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0A0A0A),
                            Color(0xFF000000)
                        )
                    )
                )
                .verticalScroll(rememberScrollState())
        ) {
            // Top Navigation Bar
            NavBar(
                user_name = userName,
                is_dark_mode = isDarkMode,
                show_back_button = false,
                nav_controller = navController,
                on_profile_click = onProfileClick
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Today's Focus (FULL WIDTH CARD)
            RectangularCard(
                header = "Today’s Focus",
                focusText = "Upper Body  •  Bench: Wave I  ≪  155×8 Queued",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(22.dp))

            // Grid of 4 Cards
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // Row 1 — Performance Feedback + EC Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SquareCard(
                        header = "Performance\nFeedback",
                        bodyText = "DOTS  •  RIR  •\n1RM  •  TUT",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    SquareCard(
                        header = "Eccentric\nConcentric",
                        bodyText = "Default: Bench Press",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 2 — RIR/RPE + TUT
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SquareCard(
                        header = "RIR/RPE",
                        bodyText = "Today: 2/8",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    MetricCard(
                        header = "TUT",
                        subHeader = "Today: 40 seconds",
                        barData = listOf(
                            0.2f, 0.5f, 0.8f, 0.4f, 0.7f,
                            0.6f, 0.9f, 0.3f, 0.2f, 0.4f,
                            0.5f, 0.7f, 0.9f, 0.4f, 0.6f
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            SectionDivider(title = "Featured Weekly Data")

            Spacer(modifier = Modifier.height(20.dp))

            FeaturedWeeklyDataCard()

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}