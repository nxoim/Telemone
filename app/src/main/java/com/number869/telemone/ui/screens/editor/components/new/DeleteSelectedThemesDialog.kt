package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.number869.telemone.R

@Composable
fun DeleteSelectedThemesDialog(
	hideToolbar: () -> Unit,
	hideDialog: () -> Unit,
	deleteSelectedThemes: (Int) -> Unit,
	unselectAllThemes: () -> Unit,
	selectedThemeCount: Int,
) {
	AlertDialog(
		icon = { Icon(Icons.Default.DeleteForever, contentDescription = stringResource(R.string.delete_icon)) },
		title = { Text(stringResource(R.string.permanently_delete_these_themes)) },
		text = { Text(stringResource(R.string.these_themes_will_be_unrecoverable_after_deletion)) },
		confirmButton = {
			FilledTonalButton(
				onClick = {
					deleteSelectedThemes(selectedThemeCount)
					unselectAllThemes()
					hideDialog()
					hideToolbar()
				},
				colors = ButtonDefaults.filledTonalButtonColors(
					containerColor = MaterialTheme.colorScheme.errorContainer,
					contentColor = MaterialTheme.colorScheme.onErrorContainer
				)
			) {
				Text(text = stringResource(R.string.delete_themes, selectedThemeCount))
			}
		},
		dismissButton = {
			TextButton(onClick = hideDialog) {
				Text(text = stringResource(R.string.cancel))
			}
		},
		onDismissRequest = hideDialog
	)
}