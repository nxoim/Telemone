package com.number869.telemone.ui.screens.editor.components.new

import android.content.Context
import android.widget.Toast
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
	deleteSelectedThemes: () -> Unit,
	unselectAllThemes: () -> Unit,
	selectedThemeCount: Int,
	context: Context
) {
	AlertDialog(
		icon = { Icon(Icons.Default.DeleteForever, contentDescription = "Delete icon") },
		title = { Text("Permanently delete these themes?") },
		text = { Text("These themes will be unrecoverable after deletion.") },
		confirmButton = {
			FilledTonalButton(
				onClick = {
					deleteSelectedThemes()
					unselectAllThemes()
					hideDialog()
					hideToolbar()

					Toast.makeText(
						context,
						"Themes ($selectedThemeCount) have been successfully deleted.",
						Toast.LENGTH_LONG
					).show()
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