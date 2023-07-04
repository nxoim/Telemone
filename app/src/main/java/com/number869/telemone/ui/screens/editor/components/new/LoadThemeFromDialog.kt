package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.number869.telemone.MainViewModel
import com.number869.telemone.ui.theme.FullPaletteList

@Composable
fun LoadWithOptionsDialog(
	close: () -> Unit,
	isShowingApplyDialog: Boolean,
	vm: MainViewModel,
	palette: FullPaletteList,
	uuid: String
) {
	val context = LocalContext.current

	AnimatedVisibility(visible = isShowingApplyDialog) {
		AlertDialog(
			onDismissRequest = { close() },
			confirmButton = {
				TextButton(
					onClick = {
						close()

						vm.loadTheme(
							uuid,
							withTokens = false,
							palette,
							clearCurrentTheme = true,
							context
						)
					}
				) {
					Text("Load color values and clear current theme")
				}

				TextButton(
					onClick = {
						close()

						vm.loadTheme(
							uuid,
							withTokens = false,
							palette,
							clearCurrentTheme = false,
							context
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

						vm.loadTheme(
							uuid,
							withTokens = true,
							palette,
							clearCurrentTheme = true,
							context
						)
					}
				) {
					Text("Load colors from material color scheme tokens and clear current theme")
				}
				TextButton(
					onClick = {
						close()

						vm.loadTheme(
							uuid,
							withTokens = true,
							palette,
							clearCurrentTheme = false,
							context
						)
					}
				) {
					Text("Load colors from material color scheme tokens and don't clear current theme")
				}
			}
		)
	}
}