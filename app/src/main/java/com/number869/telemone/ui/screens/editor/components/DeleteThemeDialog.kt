package com.number869.telemone.ui.screens.editor.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.number869.telemone.MainViewModel

@Composable
fun DeleteThemeDialog(
	close: () -> Unit,
	vm: MainViewModel,
	index: Int,
	uuid: String
) {
	AlertDialog(
		onDismissRequest = { close() },
		title = { Text("Delete this theme?") },
		icon = {
			SavedThemeItem(
				Modifier
					.width(150.dp)
					.height(180.dp)
					.clip(RoundedCornerShape(16.dp)),
				vm,
				Pair(index, uuid)
			)
		},
		text = { Text(text = "Are you sure you want to delete this theme? You will not be able to recover this theme if you delete it.",) },
		confirmButton = {
			FilledTonalButton(onClick = { vm.deleteTheme(uuid) }) {
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