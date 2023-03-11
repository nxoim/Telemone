package com.dotstealab.telemone.ui.screens.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import com.dotstealab.telemone.MainViewModel
import com.dotstealab.telemone.ui.screens.editor.components.ChatScreenPreview
import com.dotstealab.telemone.ui.screens.editor.components.ClearBeforeLoadDialog
import com.dotstealab.telemone.ui.screens.editor.components.DeleteThemeDialog
import com.dotstealab.telemone.ui.screens.editor.components.LoadFromSavedDialog
import com.dotstealab.telemone.ui.screens.editor.components.OverwriteChoiceDialog
import com.dotstealab.telemone.ui.screens.editor.components.OverwriteDefaultsDialog
import com.dotstealab.telemone.ui.screens.editor.components.PalettePopup
import com.dotstealab.telemone.ui.screens.editor.components.SavedThemeItem
import com.dotstealab.telemone.ui.theme.fullPalette


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlternativeEditorScreen(navController: NavHostController, vm: MainViewModel) {
	val context = LocalContext.current
	val palette = fullPalette()

	val themeList by remember {
		derivedStateOf {
			vm.themeList.flatMap { map ->
				map.keys.filterNot {
					it == "defaultLightThemeUUID" || it == "defaultDarkThemeUUID"
				}
			}
		}
	}
	val mappedValues by remember { derivedStateOf { vm.mappedValues }  }
	val mappedValuesAsList = mappedValues.toList().sortedBy { it.first }
	val pickedFileUriState = remember { mutableStateOf<Uri?>(null) }

	var showClearBeforeLoadDialog by remember { mutableStateOf(false) }

	val launcherThatClears = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.OpenDocument()
	) { result ->
		pickedFileUriState.value = result

		result?.let { uri ->
			vm.loadThemeFromFile(context, uri, palette, true)
		}
	}
	val launcherThatDoesnt = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.OpenDocument()
	) { result ->
		pickedFileUriState.value = result

		result?.let { uri ->
			vm.loadThemeFromFile(context, uri, palette, false)
		}
	}

	// Dialog that asks if the user wants to clear current theme
	// before loading one from file
	AnimatedVisibility(
		visible = showClearBeforeLoadDialog,
		enter = expandVertically(),
		exit = shrinkVertically()
	) {
		ClearBeforeLoadDialog(
			{ showClearBeforeLoadDialog = false },
			{
				showClearBeforeLoadDialog = false
				vm.saveCurrentTheme()
				launcherThatClears.launch(arrayOf("*/*"))
			},
			{
				showClearBeforeLoadDialog = false
				vm.saveCurrentTheme()
				launcherThatDoesnt.launch(arrayOf("*/*"))
			}
		)
	}

	Column(Modifier.statusBarsPadding()) {
		Column() {
			Row() {
				Button(onClick = { vm.shareCustomTheme(context) }) {
					Text(text = "Share Current")
				}
				Button(onClick = { vm.saveCurrentTheme() }) {
					Text(text = "Save")
				}
				Button(onClick = { vm.resetCurrentTheme() }) {
					Text(text = "Reset current theme")
				}
			}

			Row() {
				Button(onClick = { vm.loadStockLightTheme(palette, context) }) {
					Text(text = "Load Stock Light")
				}
				Button(onClick = { vm.loadStockDarkTheme(palette, context) }) {
					Text(text = "Load Stock Dark")
				}
				Button(onClick = { navController.navigate("ThemeContentsScreen") }) {
					Text(text = "Show Values")
				}
			}

			Row() {
				// hide these 2 buttons when default has been overwritten
				Button(onClick = { vm.loadDefaultLightTheme(palette) }) {
					Text(text = "Load Default Light")
				}
				Button(onClick = { vm.loadDefaultDarkTheme(palette) }) {
					Text(text = "Load Default Dark")
				}
				Button(onClick = { showClearBeforeLoadDialog = true }) {
					Text(text = "Load File")
				}
				Button(onClick = { navController.navigate("ThemePreviewScreen") }) {
					Text(text = "Preview")
				}
			}
		}

		LazyColumn() {
			// buttons. theme preview, saved themes
			// in the future this is supposed to be a menu where you choose
			// what to edit based on previews.
			// for example: chat screen preview. you press on chat bubble
			// and choose the chat bubbles color from the popup
			item {
				ChatScreenPreview(vm)

				Text(text = "Saved Themes")

				LazyRow(
					contentPadding = PaddingValues(horizontal = 16.dp),
					horizontalArrangement = Arrangement.spacedBy(16.dp)
				) {
					itemsIndexed(themeList, key = { _, item -> item }) { index, uuid ->
						var showMenu by remember { mutableStateOf(false) }
						var showDeleteDialog by remember { mutableStateOf(false) }
						var showApplyDialog by remember { mutableStateOf(false) }
						var showOverwriteChoiceDialog by remember { mutableStateOf(false) }
						var showOverwriteLightThemeDialog by remember { mutableStateOf(false) }
						var showOverwriteDarkThemeDialog by remember { mutableStateOf(false) }

						SavedThemeItem(
							Modifier
								.width(150.dp)
								.height(180.dp)
								.clip(RoundedCornerShape(16.dp))
								.animateItemPlacement()
								.combinedClickable(
									onClick = { showApplyDialog = true },
									onLongClick = { showMenu = true }
								),
							vm,
							theme = Pair(index, uuid),
							closeMenu = { showMenu = false},
							overwriteTheme = {
								showMenu = false
								showOverwriteChoiceDialog = true
							},
							deleteTheme = {
								showMenu = false
								showDeleteDialog = true
							},
							exportTheme = {
								showMenu = false
								vm.exportTheme(uuid, context)
							},
							showMenu = showMenu
						)

						AnimatedVisibility(
							visible = showApplyDialog,
							enter = expandVertically(),
							exit = shrinkVertically()
						) {
							LoadFromSavedDialog(
								{ showApplyDialog = false },
								vm,
								palette,
								uuid
							)
						}

						AnimatedVisibility(
							visible = showDeleteDialog,
							enter = expandVertically(),
							exit = shrinkVertically()
						) {
							DeleteThemeDialog(
								close = { showDeleteDialog = false },
								vm,
								index,
								uuid
							)
						}

						AnimatedVisibility(
							visible = showOverwriteChoiceDialog,
							enter = expandVertically(),
							exit = shrinkVertically()
						) {
							OverwriteChoiceDialog(
								{ showOverwriteChoiceDialog = false },
								{
									showOverwriteChoiceDialog = false
									showOverwriteLightThemeDialog = true
								},
								{
									showOverwriteChoiceDialog = false
									showOverwriteDarkThemeDialog = true
								}
							)
						}

						AnimatedVisibility(
							visible = showOverwriteDarkThemeDialog,
							enter = expandVertically(),
							exit = shrinkVertically()
						) {
							OverwriteDefaultsDialog(
								close = { showOverwriteDarkThemeDialog = false },
								overwrite = {
									showOverwriteDarkThemeDialog = false
									vm.overwriteCurrentDarkTheme(uuid, palette)
								},
								thingToOverwrite = "default dark theme"
							)
						}

						AnimatedVisibility(
							visible = showOverwriteLightThemeDialog,
							enter = expandVertically(),
							exit = shrinkVertically()
						) {
							OverwriteDefaultsDialog(
								close = { showOverwriteLightThemeDialog = false },
								overwrite = {
									showOverwriteLightThemeDialog = false
									vm.overwriteCurrentLightTheme(uuid, palette)
								},
								thingToOverwrite = "default light theme"
							)
						}
					}
				}
			}

			mappedValuesAsList.forEach { map ->
				when(map.second.first) {
					"INCOMPATIBLE VALUE" -> item {
						var showPopUp by remember { mutableStateOf(false) }
						val backgroundColor = when (mappedValues.containsKey(map.first)) {
							true -> mappedValues[map.first]!!.second
							else -> Color.Red
						}

						Row(
							Modifier
								.height(64.dp)
								.fillMaxWidth()
								.background(backgroundColor)
								.clickable { showPopUp = !showPopUp }
								.animateItemPlacement()
						) {
							Column() {
								// name
								Text(
									text = map.first,
									color = Color.White,
									modifier = Modifier.background(Color(0x4D000000))
								)
								// material you palette color token
								Text(
									text = map.second.first,
									color = Color.White,
									modifier = Modifier.background(Color(0x4D000000))
								)
							}

							if (showPopUp) {
								Popup(onDismissRequest = { showPopUp = false }) {
									PalettePopup(map.first, vm, palette)
								}
							}
						}
					}
				}
			}

			items(mappedValuesAsList, key = { it.first }) { map ->
				var showPopUp by remember { mutableStateOf(false) }
				val backgroundColor = when (mappedValues.containsKey(map.first)) {
					true -> mappedValues[map.first]!!.second
					else -> Color.Red
				}
				Row(
					Modifier
						.height(64.dp)
						.fillMaxWidth()
						.background(backgroundColor)
						.clickable { showPopUp = !showPopUp }
						.animateItemPlacement()
				) {
					Column() {
						// name
						Text(
							text = map.first,
							color = Color.White,
							modifier = Modifier.background(Color(0x4D000000)))
						// material you palette color token
						Text(
							text = map.second.first,
							color = Color.White,
							modifier = Modifier.background(Color(0x4D000000))
						)
					}

					if (showPopUp) {
						Popup(onDismissRequest = { showPopUp = false })  {
							PalettePopup(map.first, vm, palette)
						}
					}
				}
			}
		}
	}
}