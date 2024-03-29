package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun DeleteThemeDialog(close: () -> Unit, deleteTheme: () -> Unit, uuid: String, ) {
	AlertDialog(
		onDismissRequest = { close() },
		title = { Text("Delete this theme?") },
		icon = {
			SavedThemeItem(
				Modifier
					.width(150.dp)
					.height(180.dp)
					.clip(RoundedCornerShape(16.dp)),
				uuid
			)
		},
		text = {
			Text(text = "This theme will be unrecoverable after deletion.")
		},
		confirmButton = {
			FilledTonalButton(
				onClick = {
					close()
					deleteTheme()
				},
				colors = ButtonDefaults.filledTonalButtonColors(
					containerColor = MaterialTheme.colorScheme.errorContainer,
					contentColor = MaterialTheme.colorScheme.onErrorContainer
				)
			) {
				Text("Delete")
			}
		},
		dismissButton = {
			TextButton(onClick = { close() },) {
				Text("Cancel")
			}
		}
	)
}