package com.pm5clone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.pm5clone.ble.RowingData
import kotlin.math.floor

private val Pm5Bg = Color(0xFF1A1A1A)
private val Pm5ScreenBg = Color(0xFF3C4A3E)
private val Pm5Text = Color(0xFFEFEFEF)

@Composable
fun AllDataScreen(data: RowingData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Pm5Bg)
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.4f)
                .background(Pm5ScreenBg, RoundedCornerShape(8.dp))
                .border(3.dp, Color(0xFF222222), RoundedCornerShape(8.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("/500м", color = Pm5Text, fontSize = 18.sp, fontFamily = FontFamily.Monospace)
                Text(
                    formatSplit(data.splitPer500mSec),
                    color = Pm5Text,
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth().weight(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
            Pm5Field("ВРЕМЯ", formatTime(data.elapsedTimeSec))
            Pm5Field("МЕТРЫ", data.distanceMeters.toInt().toString())
            Pm5Field("ГР/МИН", data.strokeRatePerMin.toInt().toString())
            Pm5Field("ВАТТ", data.watts.toString())
            Pm5Field("ККАЛ/Ч", data.caloriesPerHour.toInt().toString())
            data.heartRateBpm?.let { Pm5Field("ПУЛЬС", it.toString()) }
        }
    }
}

@Composable
private fun Pm5Field(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Pm5Text.copy(alpha = 0.7f), fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        Text(value, color = Pm5Text, fontSize = 28.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
}

private fun formatTime(totalSec: Double): String {
    val minutes = floor(totalSec / 60).toInt()
    val seconds = totalSec % 60
    return String.format("%d:%04.1f", minutes, seconds)
}

private fun formatSplit(splitSec: Double): String {
    if (splitSec <= 0.0 || splitSec.isNaN() || splitSec.isInfinite()) return "-:--.-"
    val minutes = floor(splitSec / 60).toInt()
    val seconds = splitSec % 60
    return String.format("%d:%04.1f", minutes, seconds)
}
