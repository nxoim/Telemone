package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.number869.telemone.shared.utils.ThemeColorDataType

@Composable
fun SavedThemeDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onLoadThemeWithOptionsRequest: () -> Unit,
    onExportTheme: (ThemeColorDataType) -> Unit,
    onOverwriteDefaultThemeChoiceRequest: () -> Unit,
    onDeleteRequest: () -> Unit,
    onSelectRequest: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text("Load theme with options") },
            onClick = onLoadThemeWithOptionsRequest
        )
        DropdownMenuItem(
            text = { Text("Export this theme") },
            onClick = {
                onExportTheme(ThemeColorDataType.ColorValues)
            }
        )
        DropdownMenuItem(
            text = { Text("Export this theme in Telemone format") },
            onClick = {
                onExportTheme(ThemeColorDataType.ColorTokens)
            }
        )
        DropdownMenuItem(
            text = { Text("Overwrite a default theme") },
            onClick = onOverwriteDefaultThemeChoiceRequest
        )
        DropdownMenuItem(
            text = { Text("Delete theme") },
            onClick = onDeleteRequest
        )
        DropdownMenuItem(
            text = { Text("Select") },
            onClick = onSelectRequest
        )
    }
}
