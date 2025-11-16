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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.number869.telemone.R
import com.number869.telemone.ui.theme.AdditionalColors
import com.number869.telemone.ui.theme.PaletteState

@Composable
fun AdditionalColorsContent(
    modifier: Modifier = Modifier,
    additionalColors: AdditionalColors,
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Column(modifier, verticalArrangement = Arrangement.Absolute.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.Absolute.spacedBy(8.dp)) {
            ColorRoleItem(
                Modifier.weight(1f),
                additionalColors.white,
                uiElementName = uiElementName,
                changeValue = changeValue,
                enabled = true
            )

            ColorRoleItem(
                Modifier.weight(1f),
                additionalColors.black,
                uiElementName = uiElementName,
                changeValue = changeValue,
                enabled = true
            )
        }

        Row(horizontalArrangement = Arrangement.Absolute.spacedBy(8.dp)) {
            ColorRoleItem(
                Modifier.weight(1f),
                additionalColors.surfaceElevationLevel3Light,
                uiElementName = uiElementName,
                changeValue = changeValue,
                enabled = true
            )

            ColorRoleItem(
                Modifier.weight(1f),
                additionalColors.surfaceElevationLevel3Dark,
                uiElementName = uiElementName,
                changeValue = changeValue,
                enabled = true
            )
        }

        Box(contentAlignment = Alignment.Center) {
            ColorRoleItem(
                Modifier,
                additionalColors.transparent,
                uiElementName = uiElementName,
                changeValue = changeValue,
                enabled = true
            )

            Text(text = stringResource(R.string.transparent))
        }
    }
}
