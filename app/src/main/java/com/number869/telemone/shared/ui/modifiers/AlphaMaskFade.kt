package com.number869.telemone.shared.ui.modifiers

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush.Companion.verticalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy.Companion.Offscreen
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
@NonRestartableComposable
fun Modifier.edgeToEdgeSystemBarsAlphaMaskFade(
    gradient: List<Color> = blackToWhiteGradient,
    additionalAtStatusBar: Dp = additionalAtStatusBarDp,
    additionalAtNavigationBar: Dp = additionalAtNavigationBarDp,
): Modifier {
    val topLengthPx = WindowInsets.statusBars.getTop(LocalDensity.current)
    val bottomLengthPx = WindowInsets.navigationBars.getBottom(LocalDensity.current)

    return this
        .graphicsLayer() { this.compositingStrategy = Offscreen }
        .drawWithContent {
            drawContent()
            drawRect(
                brush = verticalGradient(
                    colors = gradient,
                    startY = topLengthPx + additionalAtStatusBar.toPx(),
                    endY = 0f,
                ),
                blendMode = BlendMode.DstIn
            )

            drawRect(
                brush = verticalGradient(
                    colors = gradient,
                    startY = size.height - bottomLengthPx - additionalAtNavigationBar.toPx(),
                    endY = size.height,
                ),
                blendMode = BlendMode.DstIn
            )
        }
}

private val blackToWhiteGradient = listOf(Color.Black, Color.Black.copy(0.2f))

private val additionalAtStatusBarDp = 24.dp
private val additionalAtNavigationBarDp = 8.dp
