package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.number869.telemone.MainViewModel
import com.number869.telemone.ThemeStorageType

@Composable
fun LoadWithOptionsDialog(
	close: () -> Unit,
	isShowingApplyDialog: Boolean,
	vm: MainViewModel,
	uuid: String
) {
	AnimatedVisibility(visible = isShowingApplyDialog) {
		AlertDialog(
			onDismissRequest = { close() },
			confirmButton = {
				TextButton(
					onClick = {
						close()

						vm.loadSavedTheme(
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

						vm.loadSavedTheme(
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

						vm.loadSavedTheme(
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

						vm.loadSavedTheme(
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
}