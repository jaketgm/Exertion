package com.example.exertion.ui_components.cards

import android.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.exertion.data.ec.UIExerciseBlock
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Icon
import com.example.exertion.data.ec.UISetRow

@Composable
fun ExerciseCard(
    exercise: UIExerciseBlock,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onAddSet: () -> Unit,
    onEvaluateEC: (setIndex: Int) -> Unit
) {
    var expanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFF111111))
            .border(1.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = exercise.name,
                fontSize = 18.sp,
                color = Color.White
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onMoveUp) {
                    Text("↑", color = Color.White)
                }
                IconButton(onClick = onMoveDown) {
                    Text("↓", color = Color.White)
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "−" else "+", color = Color.White)
                }
            }
        }

        AnimatedVisibility(visible = expanded) {
            Column {
                Spacer(Modifier.height(8.dp))

                // Each set row
                exercise.sets.forEach { set ->
                    SetRow(
                        set = set,
                        onEvaluateEC = { onEvaluateEC(set.setIndex) }
                    )
                    Spacer(Modifier.height(6.dp))
                }

                AddSetButton(onAddSet)
            }
        }
    }
}

@Composable
fun SetRow(
    set: UISetRow,
    onEvaluateEC: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF1A1A1A))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Set ${set.setIndex}", color = Color.White, fontWeight = FontWeight.Bold)
            Text("${set.reps} reps • ${set.weight} lb", color = Color.White.copy(alpha = 0.7f))
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = set.oneRmPercent?.let { "$it%" } ?: "-",
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(Modifier.width(20.dp))

            IconButton(onClick = onEvaluateEC) {
                Icon(
                    painter = painterResource(com.example.exertion.R.drawable.ic_move_black),
                    contentDescription = "Evaluate EC",
                    tint = Color(0xFFFF6FAF)
                )
            }
        }
    }
}

@Composable
fun AddSetButton(onAddSet: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF242424))
            .clickable { onAddSet() }
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+ Add Set",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
