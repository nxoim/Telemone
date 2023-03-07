package com.dotstealab.telemone.ui.screens.editor.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun OverwriteChoiceDialog(
	close: () -> Unit,
	chooseLight: () -> Unit,
	chooseDark: () -> Unit
) {
	AlertDialog(
		onDismissRequest = { close() },
		confirmButton = {
			TextButton(onClick = { chooseLight() }) {
				Text("Overwrite default light theme")
			}
			TextButton(onClick = { chooseDark() }) {
				Text("Overwrite default dark theme")
			}
		},
		dismissButton = {
			TextButton(onClick = { close() } ) {
				Text("Cancel")
			}
		}
	)
}
@Composable
fun OverwriteDefaultsDialog(
	close: () -> Unit,
	overwrite: () -> Unit,
	thingToOverwrite: String
) {
	AlertDialog(
		onDismissRequest = { close() },
		confirmButton = {
			TextButton(onClick = { overwrite() }) {
				Text("Overwrite")
			}
		},
		dismissButton = {
			TextButton(onClick = { close() }) {
				Text("Cancel")
			}
		},
		title = { Text("Do you really want to overwrite $thingToOverwrite with the selected theme?")},
		text = { Text("""Overwriting will only save the Material You color scheme tokens as their color depends from your device's color scheme settings. You can revert this change any time inside the theme editor. If you would like to save the theme fully - saving using the "Save" button is recommended.""") },
		icon = {
			Icon(Icons.Sharp.Warning, contentDescription = "Warning")
		}
	)
}
