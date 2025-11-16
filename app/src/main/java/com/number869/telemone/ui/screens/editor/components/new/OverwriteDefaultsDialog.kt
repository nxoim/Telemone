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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.number869.telemone.R
import com.number869.telemone.data.ThemeData
import com.number869.telemone.shared.utils.ThemeColorPreviewDisplayType
import com.number869.telemone.shared.utils.colorOf

@Composable
fun OverwriteChoiceDialog(
    close: () -> Unit,
    chooseLight: () -> Unit,
    chooseDark: () -> Unit,
    lightTheme: ThemeData,
    darkTheme: ThemeData,
    entirePaletteAsMap: LinkedHashMap<String, Color>,
) {
	AlertDialog(
		onDismissRequest = { close() },
		title = { Text(text = stringResource(R.string.which_theme_to_overwrite_prompt)) },
		confirmButton = {
			Column {
				Row(Modifier
                    .widthIn(max = 320.dp)
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)) {
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
							colorOf = { targetUiElementColor ->
								val data = lightTheme.values.find { it.name == targetUiElementColor }!!

								colorOf(data, ThemeColorPreviewDisplayType.CurrentColorScheme, entirePaletteAsMap)
							},
						)

						Spacer(modifier = Modifier.height(16.dp))

						FilledTonalButton(onClick = { chooseLight() }) {
							Text(stringResource(R.string.light_adjective))
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
							colorOf = { targetUiElementColor ->
								val data = darkTheme.values.find { it.name == targetUiElementColor }!!

								colorOf(data, ThemeColorPreviewDisplayType.CurrentColorScheme, entirePaletteAsMap)
							},
						)

						Spacer(modifier = Modifier.height(16.dp))

						FilledTonalButton(onClick = { chooseDark() }) {
							Text(stringResource(R.string.dark_adjective))
						}
					}
				}

				TextButton(onClick = { close() }, modifier = Modifier.align(End)) {
					Text(stringResource(R.string.cancel))
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
    entirePaletteAsMap: LinkedHashMap<String, Color>
) {
	val targetTheme = remember { if (overwriteDark) darkTheme else lightTheme }
	val thingThatsBeingOverwritten = if (overwriteDark) stringResource(R.string.default_dark_theme_title) else stringResource(
        R.string.default_light_theme
    )

	AlertDialog(
		onDismissRequest = { close() },
		confirmButton = {
			FilledTonalButton(onClick = { overwrite() }) {
				Text(stringResource(R.string.overwrite))
			}
		},
		dismissButton = {
			TextButton(onClick = { close() }) {
				Text(stringResource(R.string.cancel))
			}
		},
		title = { Text(stringResource(R.string.overwrite_the, thingThatsBeingOverwritten))},
		text = { Text(stringResource(R.string.default_theme_override_notice)) },
		icon = {
			Row(
				Modifier.widthIn(max = 280.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceAround
			) {
				// current default
				SavedThemeItem(
					modifier = Modifier
                        .weight(1f)
                        .width(110.dp)
                        .height(140.dp)
                        .clip(RoundedCornerShape(16.dp)),
					colorOf = { targetUiElementColor ->
						val data = targetTheme.values.find { it.name == targetUiElementColor }!!

						colorOf(data, ThemeColorPreviewDisplayType.CurrentColorScheme, entirePaletteAsMap)
					},
				)

				Icon(
					Icons.Default.ArrowForward,
					contentDescription = stringResource(R.string.arrow_pointing_at_the_prompted_new_default_theme),
					modifier = Modifier
                        .size(32.dp)
                        .weight(1f)
				)

				// new default
				SavedThemeItem(
					modifier = Modifier
                        .weight(1f)
                        .width(110.dp)
                        .height(140.dp)
                        .clip(RoundedCornerShape(16.dp)),
					colorOf = { targetUiElementColor ->
						val data = overwriteWith.values.find { it.name == targetUiElementColor }!!

						colorOf(data, ThemeColorPreviewDisplayType.CurrentColorScheme, entirePaletteAsMap)
					},
				)
			}
		}
	)
}
