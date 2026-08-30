package com.pm5clone.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pm5clone.ble.RawPacket
import com.pm5clone.ble.RowingData
import kotlin.math.floor

private val Pm5Bg = Color(0xFF1A1A1A)
private val Pm5ScreenBg = Color(0xFF3C4A3E) // характерный зеленовато-серый LCD PM5
private val Pm5Text = Color(0xFFEFEFEF)
private val Pm5Accent = Color(0xFF8FBF8F)

@Composable
fun Pm5Screen(
    data: RowingData,
    status: String,
    rawLog: List<RawPacket>,
    showSniffer: Boolean,
    onToggleSniffer: () -> Unit,
    onScan: () -> Unit,
    onDisconnect: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Pm5Bg)
            .padding(16.dp)
    ) {
        // Верхняя панель — статус подключения, как строка на PM5 при старте
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(status, color = Pm5Accent, fontSize = 14.sp, fontFamily = FontFamily.Monospace)
            Row {
                TextButton(onClick = onToggleSniffer) {
                    Text(if (showSniffer) "Скрыть сниффер" else "Сниффер (калибровка)")
                }
                TextButton(onClick = onScan) { Text("Сканировать") }
                TextButton(onClick = onDisconnect) { Text("Отключить") }
            }
        }

        Spacer(Modifier.height(12.dp))

        if (showSniffer) {
            SnifferPanel(rawLog, Modifier.weight(1f))
        } else {
            Pm5MonitorBody(data, Modifier.weight(1f))
        }
    }
}

@Composable
private fun Pm5MonitorBody(data: RowingData, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Pm5ScreenBg, RoundedCornerShape(8.dp))
            .border(3.dp, Color(0xFF222222), RoundedCornerShape(8.dp))
            .padding(20.dp)
    ) {
        Column(Modifier.fillMaxSize()) {
            // Главное окно — /500м, как центральный крупный дисплей PM5
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.4f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("/500м", color = Pm5Text, fontSize = 18.sp, fontFamily = FontFamily.Monospace)
                    Text(
                        formatSplit(data.splitPer500mSec),
                        color = Pm5Text,
                        fontSize = 96.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Пять малых окон — время, дистанция, темп, ватты, калории — как нижний ряд PM5
            Row(
                Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Pm5Field("ВРЕМЯ", formatTime(data.elapsedTimeSec))
                Pm5Field("МЕТРЫ", data.distanceMeters.toString())
                Pm5Field("ГР/МИН", data.strokeRatePerMin.toString())
                Pm5Field("ВАТТ", data.watts.toString())
                Pm5Field("ККАЛ/Ч", data.caloriesPerHour.toString())
                data.heartRateBpm?.let { Pm5Field("ПУЛЬС", it.toString()) }
            }
        }
    }
}

@Composable
private fun Pm5Field(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = Pm5Text.copy(alpha = 0.7f), fontSize = 12.sp, fontFamily = FontFamily.Monospace)
        Text(value, color = Pm5Text, fontSize = 30.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
}

@Composable
private fun SnifferPanel(rawLog: List<RawPacket>, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .background(Color(0xFF0D0D0D), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text(
            "Сырые пакеты (для подбора смещений в FieldOffsets):",
            color = Pm5Accent,
            fontSize = 13.sp
        )
        Spacer(Modifier.height(6.dp))
        LazyColumn {
            items(rawLog.reversed()) { p ->
                Text(
                    "${p.characteristicUuid.takeLast(8)}: ${p.hex}",
                    color = Color(0xFFB0FFB0),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

private fun formatTime(totalSec: Double): String {
    val minutes = floor(totalSec / 60).toInt()
    val seconds = (totalSec % 60)
    return String.format("%d:%04.1f", minutes, seconds)
}

private fun formatSplit(splitSec: Double): String {
    if (splitSec <= 0.0 || splitSec.isNaN() || splitSec.isInfinite()) return "-:--.-"
    val minutes = floor(splitSec / 60).toInt()
    val seconds = splitSec % 60
    return String.format("%d:%04.1f", minutes, seconds)
}
