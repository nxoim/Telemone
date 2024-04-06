package com.number869.telemone.ui.screens.editor.components.new.popup

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.number869.telemone.shared.utils.sharedElement
import com.number869.telemone.ui.theme.ToneInfo

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TonalPaletteCategoryButton(
    modifier: Modifier = Modifier,
    expand: () -> Unit,
    enabled: Boolean,
    isOnHomePage: Boolean,
    label: String,
    listOfColors: List<ToneInfo>
) {
    val visibilityAlpha by animateFloatAsState(
        if (isOnHomePage) 1f else 0f,
        label = ""
    )

    Box(
        modifier
            .graphicsLayer { alpha = visibilityAlpha }
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column(
            Modifier
                .let { return@let if (enabled && isOnHomePage) it.clickable { expand() } else it }
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                Modifier
                    .height(24.dp)
                    .width(48 .dp)) {
                if (enabled) listOfColors.subList(1, listOfColors.size - 1).forEachIndexed { index, toneInfo ->
                    val zIndex = if (index in 4..6) (index).toFloat() else 0f
                    val startPadding = if (index in 4..6) ((index - 4) * 8).dp else 8.dp
                    Box(
                        modifier
                            .sharedElement(
                                listOfColors.hashCode().toString() + "shared" + toneInfo,
                                visible = true,
                                zIndexInOverlay = zIndex
                            )
                            .zIndex(zIndex)
                            .padding(start = startPadding)
                            .width(32.dp)
                            .height(24.dp)
                            .clip(CircleShape)
                            .background(toneInfo.colorValue)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = label,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(
                    MaterialTheme.typography.labelMedium
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}