package com.number869.telemone.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.number869.telemone.MainViewModel
import com.number869.telemone.ui.Screens
import com.number869.telemone.ui.screens.main.components.DefaultThemesButtons
import com.tencent.mmkv.MMKV


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
	navController: NavHostController,
	vm: MainViewModel
) {
	val context = LocalContext.current
	val storageForStockThemeComparison = MMKV.mmkvWithID("storageForStockThemeComparison")

	// using this to detect if stock themes were updated
	val currentStockLightThemeHash by remember {
		derivedStateOf {
			val themeAsText = context.assets.open("defaultLightFile.attheme").bufferedReader().readText()

			// not int because mmkv's decodeInt() apparently isn't nullable
			themeAsText.hashCode().toString()
		}
	}

	val currentStockDarkThemeHash by remember {
		derivedStateOf {
			val themeAsText = context.assets.open("defaultDarkFile.attheme").bufferedReader().readText()

			themeAsText.hashCode().toString()
		}
	}

	val lastRememberedStockLightTheme = storageForStockThemeComparison
		.decodeString("lastRememberedStockLightThemeHash")

	val lastRememberedStockDarkTheme = storageForStockThemeComparison
		.decodeString("lastRememberedStockDarkThemeHash")

	// since mmkv cant do states and i have not made any workaround -
	// update these manually
	var lightThemeCanBeUpdated by remember {
		mutableStateOf(lastRememberedStockLightTheme != currentStockLightThemeHash)
	}

	var darkThemeCanBeUpdated by remember {
		mutableStateOf(lastRememberedStockDarkTheme != currentStockDarkThemeHash)
	}

	var displayLightThemeUpdateChoiceDialog by remember {
		mutableStateOf(false)
	}
	var displayDarkThemeUpdateChoiceDialog by remember {
		mutableStateOf(false)
	}

	val lightThemeSettingKey = "displayLightThemeUpdateChoiceDialogFor$currentStockLightThemeHash"
	val darkThemeSettingKey = "displayDarkThemeUpdateChoiceDialogFor$currentStockDarkThemeHash"

	LaunchedEffect(Unit) {
		// its null, for example, at first launch
		if (lastRememberedStockLightTheme == null) {
			storageForStockThemeComparison.encode("lastRememberedStockLightThemeHash", currentStockLightThemeHash)
		} else {
			if (lightThemeCanBeUpdated) {
				displayLightThemeUpdateChoiceDialog = storageForStockThemeComparison.decodeBool(
					lightThemeSettingKey,
					true // if user has not declined in the dialog
				)
			}
		}

		if (lastRememberedStockDarkTheme == null) {
			storageForStockThemeComparison.encode("lastRememberedStockDarkThemeHash", currentStockDarkThemeHash)
		} else {
			if (darkThemeCanBeUpdated) {
				displayDarkThemeUpdateChoiceDialog = storageForStockThemeComparison.decodeBool(
					darkThemeSettingKey,
					true // if user has not declined in the dialog
				)
			}
		}
	}

	if (displayLightThemeUpdateChoiceDialog) AlertDialog(
		onDismissRequest = { displayLightThemeUpdateChoiceDialog = false },
		title = {
			Text("Update default light theme?")
		},
		text = {
			Text(text = """Stock light theme was updated by the developer. Would you like to apply the update?

This will replace your current default light theme and your current theme will be backed up before the update is applied. The backed up theme will be available in the theme editor so you can restore it when you need to.

If you decline - you can always change your mind and update by pressing the menu button (three dots) above and pressing "Update default light theme".""".trimMargin())
		},
		icon = {
			Icon(Icons.Default.TipsAndUpdates, contentDescription = null)
		},
		confirmButton = {
			Button(
				onClick = {
					// remove preferences for the previously saved hashes
					// so they don't accumulate
					storageForStockThemeComparison.allKeys()?.forEach {
						if (it.contains("displayLightThemeUpdateChoiceDialogFor")) {
							storageForStockThemeComparison.remove(it)
						}
					}

					// remember the new hash
					storageForStockThemeComparison.encode(
						"lastRememberedStockLightThemeHash",
						currentStockLightThemeHash
					)

					vm.updateDefaultThemeFromStock(light = true)
					displayLightThemeUpdateChoiceDialog = false
					lightThemeCanBeUpdated = false
				}
			) {
				Text(text = "Apply")
			}
		},
		dismissButton = {
			OutlinedButton(
				onClick = {
					storageForStockThemeComparison.encode(
						lightThemeSettingKey,
						false
					)
					displayLightThemeUpdateChoiceDialog = false
				}
			) {
				Text(text = "Decline")
			}
		}
	)

	if (displayDarkThemeUpdateChoiceDialog) AlertDialog(
		onDismissRequest = { displayDarkThemeUpdateChoiceDialog = false },
		title = {
			Text("Update default dark theme?")
		},
		text = {
			Text(text = """Stock dark theme was updated by the developer. Would you like to apply the update?

This will replace your current default dark theme and your current theme will be backed up before the update is applied. The backed up theme will be available in the theme editor so you can restore it when you need to.

If you decline - you can always change your mind and update by pressing the menu button (three dots) above and pressing "Update default dark theme".""".trimMargin())
		},
		icon = {
			Icon(Icons.Default.TipsAndUpdates, contentDescription = null)
		},
		confirmButton = {
			Button(
				onClick = {
					// remove preferences for the previously saved hashes
					// so they don't accumulate
					storageForStockThemeComparison.allKeys()?.forEach {
						if (it.contains("displayDarkThemeUpdateChoiceDialogFor")) {
							storageForStockThemeComparison.remove(it)
						}
					}

					// remember the hash
					storageForStockThemeComparison.encode(
						"lastRememberedStockDarkThemeHash",
						currentStockDarkThemeHash
					)

					// save the current dark theme and update
					vm.updateDefaultThemeFromStock(light = false)
					displayDarkThemeUpdateChoiceDialog = false
					darkThemeCanBeUpdated = false
				}
			) {
				Text(text = "Apply")
			}
		},
		dismissButton = {
			OutlinedButton(
				onClick = {
					storageForStockThemeComparison.encode(
						darkThemeSettingKey,
						false
					)
					displayDarkThemeUpdateChoiceDialog = false
				}
			) {
				Text(text = "Decline")
			}
		}
	)

	Column(Modifier.fillMaxSize()) {
		var showMenu by remember { mutableStateOf(false) }

		CenterAlignedTopAppBar(
			title = { Text(text = "Telemone") },
			actions = {
				IconButton(onClick = { showMenu = true }) {
					Icon(Icons.Filled.MoreVert, contentDescription = "Menu")
				}

				DropdownMenu(
					expanded = showMenu,
					onDismissRequest = { showMenu = false }
				) {
					if (lightThemeCanBeUpdated) {
						DropdownMenuItem(
							text = { Text("Update default light theme") },
							onClick = {
								displayLightThemeUpdateChoiceDialog = true
								showMenu = false
							}
						)
					}

					if (darkThemeCanBeUpdated) {
						DropdownMenuItem(
							text = { Text("Update default dark theme") },
							onClick = {
								displayDarkThemeUpdateChoiceDialog = true
								showMenu = false
							}
						)
					}

					DropdownMenuItem(
						text = { Text("About") },
						onClick = {
							navController.navigate(Screens.AboutScreen.route)
							showMenu = false
						}
					)
				}
			}
		)

		Column(
			Modifier
				.padding(24.dp)
				.weight(1f),
			verticalArrangement = Arrangement.SpaceAround,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			DefaultThemesButtons(vm)

			OutlinedButton(onClick = { navController.navigate("EditorScreen") }) {
				Text(text = "Go to theme editor")
			}
		}
	}
}