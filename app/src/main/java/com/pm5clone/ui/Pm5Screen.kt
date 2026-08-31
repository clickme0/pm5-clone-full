package com.pm5clone.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pm5clone.ble.RawPacket
import com.pm5clone.ble.RowingData
import com.pm5clone.ui.screens.*

@Composable
fun Pm5Screen(
    data: RowingData,
    status: String,
    rawLog: List<RawPacket>,
    showSniffer: Boolean,
    onToggleSniffer: () -> Unit,
    onScan: () -> Unit,
    onDisconnect: () -> Unit,
    onConnectToDevice: (BluetoothDevice) -> Unit = {} // пока не используется
) {
    var currentScreen by remember { mutableStateOf("menu") } // "menu" или "workout"
    var selectedMode by remember { mutableStateOf(0) } // 0-4 для режимов

    if (currentScreen == "menu") {
        MainMenuScreen(
            onJustRow = { currentScreen = "workout" },
            onSelectWorkout = { /* TODO */ },
            onConnect = { /* TODO */ },
            onMemory = { /* TODO */ },
            onMoreOptions = { /* TODO */ },
            onUnits = { /* TODO */ },
            onDisplay = { /* TODO */ },
            onMenu = { /* TODO */ }
        )
    } else {
        // Экран тренировки
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF1A1A1A))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(status, color = Color(0xFF8FBF8F), fontSize = 14.sp, fontFamily = FontFamily.Monospace)
                Row {
                    TextButton(onClick = { currentScreen = "menu" }) { Text("Меню") }
                    TextButton(onClick = onToggleSniffer) { Text(if (showSniffer) "Скрыть сниффер" else "Сниффер") }
                    TextButton(onClick = onScan) { Text("Сканировать") }
                    TextButton(onClick = onDisconnect) { Text("Отключить") }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Вкладки режимов
            val modes = listOf("Все данные", "Кривая силы", "График", "Лодка", "Крупно")
            ScrollableTabRow(
                selectedTabIndex = selectedMode,
                backgroundColor = Color(0xFF2A2A2A),
                contentColor = Color.White,
                edgePadding = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                modes.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedMode == index,
                        onClick = { selectedMode = index },
                        text = { Text(title, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            when (selectedMode) {
                0 -> AllDataScreen(data)
                1 -> ForceCurveScreen(data)
                2 -> BarChartScreen(data)
                3 -> PaceBoatScreen(data)
                4 -> LargePrintScreen(data)
            }

            if (showSniffer) {
                SnifferPanel(rawLog, Modifier.weight(0.5f))
            }
        }
    }
}

@Composable
private fun SnifferPanel(rawLog: List<RawPacket>, modifier: Modifier = Modifier) {
    Column(
        modifier
            .fillMaxWidth()
            .background(Color(0xFF0D0D0D), androidx.compose.foundation.shape.RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Text("Сырые пакеты:", color = Color(0xFF8FBF8F), fontSize = 13.sp)
        androidx.compose.foundation.lazy.LazyColumn {
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
