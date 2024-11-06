package com.number869.telemone.ui.screens.editor.components.new.popup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.number869.telemone.ui.theme.AdditionalColors

@Composable
fun AdditionalColorsContent(
    modifier: Modifier = Modifier,
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Column(modifier, verticalArrangement = Arrangement.Absolute.spacedBy(8.dp)) {
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
