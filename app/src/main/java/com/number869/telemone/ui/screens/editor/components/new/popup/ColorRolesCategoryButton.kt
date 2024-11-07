package com.number869.telemone.ui.screens.editor.components.new.popup

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.zIndex
import com.number869.telemone.ui.theme.ColorRoles
import com.number869.telemone.ui.theme.DataAboutColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ColorRolesCategoryButton(
    modifier: Modifier,
    expand: () -> Unit,
    label: String,
    colorRolesLight: ColorRoles,
    changeValue: (String, String, Color) -> Unit,
    key: String
) {
    Row(
        modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = expand)
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant,
                RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier
                .width(72.dp)
                .height(32.dp)
        ) {
            remember {
                setOf(
                    colorRolesLight.primaryContainer,
                    colorRolesLight.secondaryContainer,
                    colorRolesLight.tertiaryContainer,
                )
            }.forEach {
                val zIndex = when (it) {
                    colorRolesLight.primaryContainer -> 1f
                    colorRolesLight.secondaryContainer -> 2f
                    colorRolesLight.tertiaryContainer -> 3f
                    else -> 0f
                }
                val startPadding = if (zIndex in 1f..3f)
                    ((zIndex.coerceAtLeast(1f) - 1) * 8).dp
                else
                    16.dp

                ColorRoleItem(
                    Modifier
                        .padding(start = startPadding)
                        .zIndex(zIndex),
                    dataAboutColors = it,
                    uiElementName = key,
                    changeValue,
                    enabled = false
                )
            }
        }

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = label,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
            fontSize = 20.sp,
            fontWeight = FontWeight.W400,
        )
    }
}
