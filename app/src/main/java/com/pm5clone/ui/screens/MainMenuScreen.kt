package com.pm5clone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Pm5Black = Color(0xFF000000)
private val Pm5Green = Color(0xFF33FF33)
private val Pm5White = Color(0xFFFFFFFF)

@Composable
fun MainMenuScreen(
    onJustRow: () -> Unit = {},
    onSelectWorkout: () -> Unit = {},
    onConnect: () -> Unit = {},
    onMemory: () -> Unit = {},
    onMoreOptions: () -> Unit = {},
    onUnits: () -> Unit = {},
    onDisplay: () -> Unit = {},
    onMenu: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Pm5Black)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Верхняя строка
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("OK CONCEPT 2", color = Pm5Green, fontSize = 14.sp,
                fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
            Text("APR 11 2025", color = Pm5Green, fontSize = 14.sp,
                fontFamily = FontFamily.Monospace, letterSpacing = 1.sp)
        }

        Spacer(Modifier.height(20.dp))
        Text("MAIN MENU", color = Pm5Green, fontSize = 20.sp,
            fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, letterSpacing = 3.sp)
        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MenuItem("Just Row", onJustRow, isSelected = true)
                MenuItem("Select Workout", onSelectWorkout)
                MenuItem("Connect", onConnect)
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MenuItem("Memory", onMemory)
                MenuItem("More Options", onMoreOptions)
                Text(" ", color = Pm5Black, fontSize = 20.sp, fontFamily = FontFamily.Monospace)
            }
        }

        Spacer(Modifier.weight(1f))
        Row(Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly) {
            BottomButton("UNITS", onUnits)
            BottomButton("DISPLAY", onDisplay)
            BottomButton("MENU", onMenu)
        }
    }
}

@Composable
private fun MenuItem(text: String, onClick: () -> Unit, isSelected: Boolean = false) {
    Text(text = text,
        color = if (isSelected) Pm5White else Pm5Green,
        fontSize = 18.sp,
        fontFamily = FontFamily.Monospace,
        letterSpacing = 1.sp,
        modifier = Modifier.fillMaxWidth()
            .background(if (isSelected) Pm5Green.copy(alpha = 0.2f) else Color.Transparent,
                RoundedCornerShape(4.dp))
            .padding(vertical = 4.dp, horizontal = 8.dp)
    )
}

@Composable
private fun BottomButton(text: String, onClick: () -> Unit) {
    Text(text = text,
        color = Pm5Green,
        fontSize = 16.sp,
        fontFamily = FontFamily.Monospace,
        letterSpacing = 2.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(8.dp)
            .background(Pm5Green.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}
