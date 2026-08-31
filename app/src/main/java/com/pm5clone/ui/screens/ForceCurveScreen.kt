package com.pm5clone.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pm5clone.ble.RowingData
import kotlin.math.sin

@Composable
fun ForceCurveScreen(data: RowingData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A))
            .padding(16.dp)
    ) {
        Text("Кривая силы", color = Color.White, fontSize = 18.sp, fontFamily = FontFamily.Monospace)
        Spacer(Modifier.height(8.dp))
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF3C4A3E), RoundedCornerShape(8.dp))
                .border(3.dp, Color(0xFF222222), RoundedCornerShape(8.dp))
                .padding(20.dp)
        ) {
            val points = 100
            val step = size.width / points
            val path = Path()
            for (i in 0..points) {
                val x = i * step
                val y = size.height / 2 + 100 * sin(i * 0.1f)
                if (i == 0) path.moveTo(x, y)
                else path.lineTo(x, y)
            }
            drawPath(path, color = Color(0xFF8FBF8F), style = Stroke(width = 4f))
        }
    }
}
