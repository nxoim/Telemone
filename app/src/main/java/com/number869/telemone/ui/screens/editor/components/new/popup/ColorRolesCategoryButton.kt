package com.number869.telemone.ui.screens.editor.components.new.popup

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
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.zIndex
import com.number869.telemone.ui.theme.ColorRolesLight
import com.number869.telemone.ui.theme.DataAboutColors

@Composable
fun ColorRolesCategoryButton(
    modifier: Modifier,
    expand: () -> Unit,
    isOnHomePage: Boolean,
    label: String,
    changeValue: (String, String, Color) -> Unit,
    listOfColors: List<DataAboutColors>,
    key: String
) {
    val animatedAlpha by animateFloatAsState(if (isOnHomePage) 1f else 0f)

    Row(
        modifier
            .graphicsLayer { alpha = animatedAlpha }
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp))
            .let { return@let if (isOnHomePage) it.clickable { expand() } else it }
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(Modifier.width(72.dp).height(32.dp)) {
            listOfColors.forEach {
                val zIndex = when (it) {
                    ColorRolesLight.PrimaryContainer.dataAboutColors -> 1f
                    ColorRolesLight.SecondaryContainer.dataAboutColors -> 2f
                    ColorRolesLight.TertiaryContainer.dataAboutColors -> 3f
                    else -> 0f
                }
                val startPadding = if(zIndex in 1f..3f)
                    ((zIndex.coerceAtLeast(1f) - 1) * 8).dp
                else
                    16.dp

                ColorRoleItem(
                    Modifier.padding(start = startPadding).zIndex(zIndex),
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