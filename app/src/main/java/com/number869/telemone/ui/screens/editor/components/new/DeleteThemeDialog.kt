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
import com.number869.telemone.data.ThemeData
import com.number869.telemone.shared.utils.ThemeColorPreviewDisplayType
import com.number869.telemone.shared.utils.colorOf
import com.number869.telemone.shared.utils.incorrectUiElementColorData

@Composable
fun DeleteThemeDialog(
	close: () -> Unit,
	deleteTheme: () -> Unit,
	theme: ThemeData,
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
				theme,
				loadSavedTheme = {  },
				selectOrUnselectSavedTheme = {  },
				exportTheme = { },
				changeSelectionMode = {  },
				colorOf = { targetUiElement ->
					val data = theme.values
						.find { it.name == targetUiElement }
						?: incorrectUiElementColorData(targetUiElement)

					colorOf(data, ThemeColorPreviewDisplayType.SavedColorValues)
				},
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