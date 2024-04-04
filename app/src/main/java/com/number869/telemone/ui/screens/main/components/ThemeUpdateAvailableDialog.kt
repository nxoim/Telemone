package com.number869.telemone.ui.screens.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.number869.telemone.ui.RootDestinations
import com.nxoim.decomposite.core.common.navigation.getExistingNavController

@Composable
fun ThemeUpdateAvailableDialog(
	ofLight: Boolean,
	decline: () -> Unit,
	acceptStockThemeUpdate: () -> Unit
) {
	val navController = getExistingNavController<RootDestinations>()
	val typeText = if (ofLight) "light" else "dark"

	AlertDialog(
		onDismissRequest = { navController.navigateBack() },
		title = {
			Text("Update default $typeText theme?")
		},
		text = {
			Text(text = """Stock $typeText theme was updated by the developer. Would you like to apply the update?

This will replace your current default $typeText theme and your current default $typeText theme will be backed up before the update is applied. The backed up theme will be available in the theme editor so you can restore it when you need to.

If you decline - you can always change your mind and update by pressing the menu button (three dots) above and pressing "Update default $typeText theme".""".trimMargin())
		},
		icon = {
			Icon(Icons.Default.TipsAndUpdates, contentDescription = null)
		},
		confirmButton = {
			Button(
				onClick = {
					acceptStockThemeUpdate()
					navController.navigateBack()
				}
			) {
				Text(text = "Apply")
			}
		},
		dismissButton = {
			OutlinedButton(
				onClick = {
					decline()
					navController.navigateBack()
				}
			) {
				Text(text = "Decline")
			}
		}
	)
}