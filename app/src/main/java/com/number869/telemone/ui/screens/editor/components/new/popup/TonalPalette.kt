package com.number869.telemone.ui.screens.editor.components.new.popup

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.number869.telemone.shared.utils.CombinedSharedTransitionScope
import com.number869.telemone.ui.theme.ToneInfo

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CombinedSharedTransitionScope.TonalPalette(
    modifier: Modifier = Modifier,
    tones: List<ToneInfo>,
    changeValue: (String, String, Color) -> Unit,
    touchActionEnabled: Boolean,
    uiElementName: String
) {
    Column(modifier) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            remember { tones.subList(1, 6) }.fastForEachIndexed { index, toneInfo ->
                TonalPaletteItem(
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState("shared$toneInfo"),
                            zIndexInOverlay = index.toFloat()
                        )
                        .weight(1f),
                    toneInfo = toneInfo,
                    uiElementName = uiElementName,
                    changeValue = changeValue,
                    enabled = touchActionEnabled
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            remember { tones.subList(6, tones.lastIndex) }.fastForEachIndexed { index, toneInfo ->
                TonalPaletteItem(
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState("shared$toneInfo"),
                            zIndexInOverlay = if (index == 1) 6f else 0f
                        )
                        .weight(1f),
                    toneInfo = toneInfo,
                    uiElementName = uiElementName,
                    changeValue = changeValue,
                    enabled = touchActionEnabled
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TonalPaletteItem(
    modifier: Modifier = Modifier,
    toneInfo: ToneInfo,
    uiElementName: String,
    changeValue: (String, String, Color) -> Unit,
    enabled: Boolean
) {
    val size = if (!enabled)
        Size(32f, 24f)
    else
        Size(9999f, 80f)

    Box(
        modifier
            .width(size.width.dp)
            .height(size.height.dp)
            .clip(CircleShape)
            .background(toneInfo.colorValue)
            .let {
                // no, the enabled parameter in the clickable modifier
                // doesn't cut it
                return@let if (enabled) it.clickable {
                    changeValue(
                        uiElementName,
                        toneInfo.colorToken,
                        toneInfo.colorValue
                    )
                } else it
            }
    )
}