package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import kotlin.math.sin

/**
 * Animated harmonic multi-layered ocean wave canvas.
 * Renders rising water level based on tidal height (0.2m to 4.5m).
 */
@Composable
fun WaveCanvas(
    tideHeightMeters: Double,
    isRising: Boolean,
    modifier: Modifier = Modifier.fillMaxWidth().height(160.dp)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "WaveAnimation")
    
    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase1"
    )

    val phase2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase2"
    )

    val phase3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase3"
    )

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Calculate baseline water line from tidal height (higher tide = higher water line)
        // Normalized between 0.35 (high water) and 0.70 (low water)
        val normalizedTide = ((tideHeightMeters - 0.5) / 3.0).coerceIn(0.0, 1.0).toFloat()
        val baseWaterY = height * (0.68f - (normalizedTide * 0.32f))

        // Background Layer (Deepest swell)
        drawWaveLayer(
            width = width,
            height = height,
            baseY = baseWaterY - 14f,
            amplitude = 12f,
            frequency = 0.010f,
            phase = phase3,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0077B6).copy(alpha = 0.45f),
                    Color(0xFF023E8A).copy(alpha = 0.65f)
                )
            )
        )

        // Mid Layer (Medium crest swell)
        drawWaveLayer(
            width = width,
            height = height,
            baseY = baseWaterY - 4f,
            amplitude = 16f,
            frequency = 0.015f,
            phase = phase2,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF0096C7).copy(alpha = 0.65f),
                    Color(0xFF0077B6).copy(alpha = 0.85f)
                )
            )
        )

        // Foreground Layer (Active surface wave + foam)
        drawWaveLayer(
            width = width,
            height = height,
            baseY = baseWaterY + 6f,
            amplitude = 20f,
            frequency = 0.018f,
            phase = phase1,
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF48CAE4),
                    Color(0xFF0077B6)
                )
            )
        )

        // Seafoam crest line
        drawWaveCrest(
            width = width,
            baseY = baseWaterY + 6f,
            amplitude = 20f,
            frequency = 0.018f,
            phase = phase1,
            color = Color(0xFFE0F7FA)
        )
    }
}

private fun DrawScope.drawWaveLayer(
    width: Float,
    height: Float,
    baseY: Float,
    amplitude: Float,
    frequency: Float,
    phase: Float,
    brush: Brush
) {
    val path = Path()
    path.moveTo(0f, height)
    path.lineTo(0f, baseY)

    val step = 8f
    var x = 0f
    while (x <= width) {
        val y = baseY + (amplitude * sin(x * frequency + phase))
        path.lineTo(x, y)
        x += step
    }

    path.lineTo(width, height)
    path.close()
    drawPath(path = path, brush = brush)
}

private fun DrawScope.drawWaveCrest(
    width: Float,
    baseY: Float,
    amplitude: Float,
    frequency: Float,
    phase: Float,
    color: Color
) {
    val path = Path()
    var isFirst = true
    val step = 8f
    var x = 0f

    while (x <= width) {
        val y = baseY + (amplitude * sin(x * frequency + phase))
        if (isFirst) {
            path.moveTo(x, y)
            isFirst = false
        } else {
            path.lineTo(x, y)
        }
        x += step
    }

    drawPath(
        path = path,
        color = color,
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.5f)
    )
}
