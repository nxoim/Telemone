package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.number869.telemone.shared.utils.ThemeStorageType

@Composable
fun LoadWithOptionsDialog(
	close: () -> Unit,
	loadSavedTheme: (ThemeStorageType) -> Unit,
	uuid: String
) {

	AlertDialog(
		onDismissRequest = { close() },
		confirmButton = {
			TextButton(
				onClick = {
					close()

					loadSavedTheme(
						ThemeStorageType.ByUuid(
							uuid,
							withTokens = false,
							clearCurrentTheme = true
						)
					)
				}
			) {
				Text("Load color values and clear current theme")
			}

			TextButton(
				onClick = {
					close()

					loadSavedTheme(
						ThemeStorageType.ByUuid(
							uuid,
							withTokens = false,
							clearCurrentTheme = false,
						)
					)
				}
			) {
				Text("Load color values and  don't clear current theme")
			}
			TextButton(onClick = { close() }) {
				Text("Cancel")
			}
		},
		dismissButton = {
			TextButton(
				onClick = {
					close()

					loadSavedTheme(
						ThemeStorageType.ByUuid(
							uuid,
							withTokens = true,
							clearCurrentTheme = true
						)
					)
				}
			) {
				Text("Load colors from material color scheme tokens and clear current theme")
			}
			TextButton(
				onClick = {
					close()

					loadSavedTheme(
						ThemeStorageType.ByUuid(
							uuid,
							withTokens = true,
							clearCurrentTheme = false
						)
					)
				}
			) {
				Text("Load colors from material color scheme tokens and don't clear current theme")
			}
		}
	)
}