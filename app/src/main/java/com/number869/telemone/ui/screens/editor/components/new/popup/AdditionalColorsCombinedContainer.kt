package com.number869.telemone.ui.screens.editor.components.new.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.number869.telemone.ui.theme.AdditionalColors

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AdditionalColorsCombinedContainer(
    modifier: Modifier = Modifier,
    expand: () -> Unit,
    // passing as state because its an object
    isOnHomePage: Boolean,
    chosen: Boolean,
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String,
) {
    val animatedAlpha by animateFloatAsState(if (isOnHomePage) 1f else 0f)

    Box(
        modifier
            .fillMaxSize()
            .border(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant.copy(animatedAlpha),
                RoundedCornerShape(16.dp)
            )
            .let {
                return@let if (isOnHomePage)
                    it.clickable { expand() }
                else
                    it
            },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = isOnHomePage,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Icon(
                Icons.Filled.MoreVert,
                contentDescription = "Additional Colors",
            )
        }

        if(chosen) {
            Column(verticalArrangement = Arrangement.Absolute.spacedBy(8.dp)) {
                Row(horizontalArrangement = Arrangement.Absolute.spacedBy(8.dp)) {
                    ColorRoleItem(
                        Modifier.weight(1f),
                        AdditionalColors.White.dataAboutColors,
                        uiElementName = uiElementName,
                        changeValue = changeValue,
                        enabled = true
                    )

                    ColorRoleItem(
                        Modifier.weight(1f),
                        AdditionalColors.Black.dataAboutColors,
                        uiElementName = uiElementName,
                        changeValue = changeValue,
                        enabled = true
                    )
                }

                Row(horizontalArrangement = Arrangement.Absolute.spacedBy(8.dp)) {
                    ColorRoleItem(
                        Modifier.weight(1f),
                        AdditionalColors.SurfaceElevationLevel3Light.dataAboutColors,
                        uiElementName = uiElementName,
                        changeValue = changeValue,
                        enabled = true
                    )

                    ColorRoleItem(
                        Modifier.weight(1f),
                        AdditionalColors.SurfaceElevationLevel3Dark.dataAboutColors,
                        uiElementName = uiElementName,
                        changeValue = changeValue,
                        enabled = true
                    )
                }

                Box(contentAlignment = Alignment.Center) {
                    ColorRoleItem(
                        Modifier,
                        AdditionalColors.Transparent.dataAboutColors,
                        uiElementName = uiElementName,
                        changeValue = changeValue,
                        enabled = true
                    )

                    Text(text = "Transparent")
                }
            }
        }
    }
}