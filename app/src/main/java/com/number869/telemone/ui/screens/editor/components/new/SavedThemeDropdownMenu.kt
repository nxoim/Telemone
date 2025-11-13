package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.number869.telemone.R
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
            text = { Text(stringResource(R.string.load_theme_with_options)) },
            onClick = onLoadThemeWithOptionsRequest
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.export_this_theme)) },
            onClick = {
                onExportTheme(ThemeColorDataType.ColorValues)
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.export_this_theme_in_telemone_format)) },
            onClick = {
                onExportTheme(ThemeColorDataType.ColorTokens)
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.overwrite_a_default_theme)) },
            onClick = onOverwriteDefaultThemeChoiceRequest
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.delete_theme)) },
            onClick = onDeleteRequest
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.select)) },
            onClick = onSelectRequest
        )
    }
}
