package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.number869.telemone.R
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
				Text(stringResource(R.string.load_color_values_and_clear_current_theme))
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
				Text(stringResource(R.string.load_color_values_and_don_t_clear_current_theme))
			}
			TextButton(onClick = { close() }) {
				Text(stringResource(R.string.cancel))
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
				Text(stringResource(R.string.load_colors_from_material_color_scheme_tokens_and_clear_current_theme))
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
				Text(stringResource(R.string.load_colors_from_material_color_scheme_tokens_and_don_t_clear_current_theme))
			}
		}
	)
}