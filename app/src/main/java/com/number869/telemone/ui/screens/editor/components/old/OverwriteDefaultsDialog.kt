package com.number869.telemone.ui.screens.editor.components.old

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.number869.telemone.MainViewModel

@Composable
fun OverwriteChoiceDialog(
	close: () -> Unit,
	chooseLight: () -> Unit,
	chooseDark: () -> Unit,
	vm: MainViewModel
) {
	AlertDialog(
		onDismissRequest = { close() },
		title = { Text(text = "Which Theme to Overwrite?") },
		confirmButton = {
			Column {
				Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						SavedThemeItem(
							Modifier
								.width(120.dp)
								.height(150.dp)
								.clickable { chooseLight() }
								.clip(RoundedCornerShape(16.dp))
								.weight(1f, false),
							vm,
							"defaultLightThemeUUID"
						)

						TextButton(onClick = { chooseLight() }) {
							Text("Overwrite Light")
						}
					}

					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						SavedThemeItem(
							Modifier
								.width(120.dp)
								.height(150.dp)
								.clickable { chooseDark() }
								.clip(RoundedCornerShape(16.dp))
								.weight(1f, false),
							vm,
							"defaultDarkThemeUUID"
						)

						TextButton(onClick = { chooseDark() }) {
							Text("Overwrite Dark")
						}
					}
				}

				FilledTonalButton(onClick = { close() }, modifier = Modifier.align(End)) {
					Text("Cancel")
				}
			}
		}
	)
}
@Composable
fun OverwriteDefaultsDialog(
	close: () -> Unit,
	overwrite: () -> Unit,
	vm: MainViewModel,
	overwriteDark: Boolean,
	overwriteWith: String
) {
	val thingThatsBeingOverwritten = if (overwriteDark) "default dark theme" else "default light theme"
	AlertDialog(
		onDismissRequest = { close() },
		confirmButton = {
			FilledTonalButton(onClick = { overwrite() }) {
				Text("Overwrite")
			}
		},
		dismissButton = {
			TextButton(onClick = { close() }) {
				Text("Cancel")
			}
		},
		title = { Text("Do you really want to overwrite the $thingThatsBeingOverwritten?")},
		text = { Text("""Overwriting will only save the Material You color scheme tokens as their color depends from your device's color scheme settings. You can revert this change any time inside the theme editor.""") },
		icon = {
			Column(Modifier.fillMaxWidth()) {
				Row(verticalAlignment = Alignment.CenterVertically) {
					// current default
					SavedThemeItem(
						Modifier
							.width(110.dp)
							.height(140.dp)
							.clip(RoundedCornerShape(16.dp)),
						vm,
						uuid = if (overwriteDark) "defaultDarkThemeUUID" else "defaultLightThemeUUID"
					)

					Spacer(Modifier.width(8.dp))
					Icon(
						Icons.Default.ArrowForward,
						contentDescription = "Arrow pointing at the prompted new default theme.",
						modifier = Modifier.size(32.dp)
					)
					Spacer(Modifier.width(8.dp))

					// new default
					SavedThemeItem(
						Modifier
							.width(110.dp)
							.height(140.dp)
							.clip(RoundedCornerShape(16.dp)),
						vm,
						uuid = overwriteWith
					)
				}
			}
		}
	)
}
