package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import com.number869.telemone.data.ThemeData
import com.number869.telemone.data.colorOf
import com.number869.telemone.ui.screens.editor.ThemeColorPreviewDisplayType

@Composable
fun OverwriteChoiceDialog(
	close: () -> Unit,
	chooseLight: () -> Unit,
	chooseDark: () -> Unit,
	lightTheme: ThemeData,
	darkTheme: ThemeData,
) {
	AlertDialog(
		onDismissRequest = { close() },
		title = { Text(text = "Which theme to overwrite?") },
		confirmButton = {
			Column {
				Row(Modifier.widthIn(max = 320.dp).fillMaxWidth().padding(bottom = 8.dp)) {
					Column(
						Modifier.weight(1f),
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						SavedThemeItem(
							Modifier
								.width(120.dp)
								.height(150.dp)
								.clickable { chooseLight() }
								.clip(RoundedCornerShape(16.dp))
								.weight(1f, false),
							lightTheme,
							loadSavedTheme = {  },
							selectOrUnselectSavedTheme = {  },
							exportTheme = {  },
							changeSelectionMode = {  },
							colorOf = { targetUiElementColor ->
								val data = lightTheme.values.find { it.name == targetUiElementColor }!!

								colorOf(data, ThemeColorPreviewDisplayType.CurrentColorScheme)
							},
						)

						Spacer(modifier = Modifier.height(16.dp))

						FilledTonalButton(onClick = { chooseLight() }) {
							Text("Light")
						}
					}

					Column(
						Modifier.weight(1f),
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						SavedThemeItem(
							Modifier
								.width(120.dp)
								.height(150.dp)
								.clickable { chooseDark() }
								.clip(RoundedCornerShape(16.dp))
								.weight(1f, false),
							darkTheme,
							loadSavedTheme = {  },
							selectOrUnselectSavedTheme = {  },
							exportTheme = {  },
							changeSelectionMode = {  },
							colorOf = { targetUiElementColor ->
								val data = darkTheme.values.find { it.name == targetUiElementColor }!!

								colorOf(data, ThemeColorPreviewDisplayType.CurrentColorScheme)
							},
						)

						Spacer(modifier = Modifier.height(16.dp))

						FilledTonalButton(onClick = { chooseDark() }) {
							Text("Dark")
						}
					}
				}

				TextButton(onClick = { close() }, modifier = Modifier.align(End)) {
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
	overwriteDark: Boolean,
	lightTheme: ThemeData,
	darkTheme: ThemeData,
	overwriteWith: ThemeData,
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
		title = { Text("Overwrite the $thingThatsBeingOverwritten?")},
		text = { Text("""Overwriting will only save the Material You color scheme tokens as their color depends from your device's color scheme settings. This change can be reverted any time in theme editor.""") },
		icon = {
			Row(
				Modifier.widthIn(max = 280.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceAround
			) {
				// current default
				SavedThemeItem(
					Modifier
						.weight(1f)
						.width(110.dp)
						.height(140.dp)
						.clip(RoundedCornerShape(16.dp)),
					if (overwriteDark) darkTheme else lightTheme,
					loadSavedTheme = {  },
					selectOrUnselectSavedTheme = {  },
					exportTheme = {  },
					changeSelectionMode = {  },
					colorOf = { targetUiElementColor ->
						val data = darkTheme.values.find { it.name == targetUiElementColor }!!

						colorOf(data, ThemeColorPreviewDisplayType.CurrentColorScheme)
					},
				)

				Icon(
					Icons.Default.ArrowForward,
					contentDescription = "Arrow pointing at the prompted new default theme.",
					modifier = Modifier.size(32.dp).weight(1f)
				)

				// new default
				SavedThemeItem(
					Modifier
						.weight(1f)
						.width(110.dp)
						.height(140.dp)
						.clip(RoundedCornerShape(16.dp)),
					overwriteWith,
					loadSavedTheme = {  },
					selectOrUnselectSavedTheme = {  },
					exportTheme = {  },
					changeSelectionMode = {  },
					colorOf = { targetUiElementColor ->
						val data = overwriteWith.values.find { it.name == targetUiElementColor }!!

						colorOf(data, ThemeColorPreviewDisplayType.CurrentColorScheme)
					},
				)
			}
		}
	)
}
