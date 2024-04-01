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

@Composable
fun DeleteSelectedThemesDialog(
	hideToolbar: () -> Unit,
	hideDialog: () -> Unit,
	deleteSelectedThemes: (Int) -> Unit,
	unselectAllThemes: () -> Unit,
	selectedThemeCount: Int,
) {
	AlertDialog(
		icon = { Icon(Icons.Default.DeleteForever, contentDescription = "Delete icon") },
		title = { Text("Permanently delete these themes?") },
		text = { Text("These themes will be unrecoverable after deletion.") },
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
				Text(text = "Delete themes ($selectedThemeCount)")
			}
		},
		dismissButton = {
			TextButton(onClick = hideDialog) {
				Text(text = "Cancel")
			}
		},
		onDismissRequest = hideDialog
	)
}