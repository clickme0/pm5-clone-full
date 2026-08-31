package com.pm5clone.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pm5clone.ble.RowingData

@Composable
fun LargePrintScreen(data: RowingData) {
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1A1A)).padding(16.dp)) {
        Text("Крупный текст (в разработке)", color = Color.White, fontSize = 18.sp, fontFamily = FontFamily.Monospace)
    }
}
