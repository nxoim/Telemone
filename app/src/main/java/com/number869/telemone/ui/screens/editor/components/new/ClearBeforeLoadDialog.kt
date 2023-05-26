package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ClearBeforeLoadDialog(
	close: () -> Unit,
	clear: () -> Unit,
	leaveAsIs: () -> Unit
) {
	AlertDialog(
		onDismissRequest = { close() },
		title = { Text("Clear current theme before loading?") },
		text = { Text("""Loading a theme will save your current theme to "Saved Themes".""") },
		confirmButton = {
			TextButton(onClick = { clear() }) {
				Text("Clear")
			}

			TextButton(onClick = { leaveAsIs() }) {
				Text("Leave as is")
			}
		},
		dismissButton = {
			TextButton(onClick = { close() }) {
				Text("Cancel")
			}
		}
	)
}