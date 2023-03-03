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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import com.dotstealab.telemone.MainViewModel
import com.dotstealab.telemone.ui.screens.editor.components.ChatScreenPreview
import com.dotstealab.telemone.ui.screens.editor.components.PalettePopup
import com.dotstealab.telemone.ui.screens.editor.components.SavedThemeItem
import com.dotstealab.telemone.ui.theme.fullPalette

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditorScreen(navController: NavHostController, vm: MainViewModel) {
	val context = LocalContext.current
	val palette = fullPalette()

	val themeList by remember { derivedStateOf { vm.themeList }  }
	val mappedValues by remember { derivedStateOf { vm.mappedValues }  }
	val mappedValuesAsList = mappedValues.toList().sortedBy { it.first }
	val pickedFileUriState = remember { mutableStateOf<Uri?>(null) }

	val launcher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.OpenDocument()
	) { result ->
		pickedFileUriState.value = result

		result?.let { uri ->
			vm.loadThemeFromFile(context, uri, palette)
		}
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
				Button(onClick = { vm.loadLightTheme(palette) }) {
					Text(text = "Load Light Theme")
				}
				Button(onClick = { vm.loadDarkTheme(palette) }) {
					Text(text = "Load Dark Theme")
				}
				Button(onClick = { navController.navigate("ThemeContentsScreen") }) {
					Text(text = "Show Values")
				}
			}
			Row(Modifier.padding(24.dp)) {
				Button(onClick = { launcher.launch(arrayOf("*/*")) }) {
					Text(text = "Load File")
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
					itemsIndexed(themeList.flatMap { it.keys }, key = { _, item -> item }) { index, uuid ->
						var showMenu by remember { mutableStateOf(false) }
						var showDeleteDialog by remember { mutableStateOf(false) }
						var showApplyDialog by remember { mutableStateOf(false) }

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
							Pair(index, uuid)
						)

						DropdownMenu(
							expanded = showMenu,
							onDismissRequest = { showMenu = false }
						) {
							DropdownMenuItem(
								text = { Text("Override defaults (not implemented)")},
								onClick = { /*TODO*/ }
							)
							DropdownMenuItem(
								text = { Text("Delete theme") },
								onClick = {
									showDeleteDialog = true
									showMenu = false
								}
							)
						}


						AnimatedVisibility(
							visible = showApplyDialog,
							enter = expandVertically(),
							exit = shrinkVertically()
						) {
							AlertDialog(
								onDismissRequest = { showApplyDialog = false },
								confirmButton = {
									TextButton(
										onClick = {
											showApplyDialog = false

											vm.setCurrentMapTo(
												uuid,
												loadTokens = false,
												palette,
												clearCurrentTheme = true
											)
										}
									) {
										Text("Load color values and clear current theme")
									}

									TextButton(
										onClick = {
											showApplyDialog = false

											vm.setCurrentMapTo(
												uuid,
												loadTokens = false,
												palette,
												clearCurrentTheme = false
											)
										}
									) {
										Text("Load color values and  don't clear current theme")
									}
								},
								dismissButton = {
									TextButton(
										onClick = {
											showApplyDialog = false

											vm.setCurrentMapTo(
												uuid,
												loadTokens = true,
												palette,
												clearCurrentTheme = true
											)
										}
									) {
										Text("Load colors from material color scheme tokens and clear current theme")
									}
									TextButton(
										onClick = {
											showApplyDialog = false

											vm.setCurrentMapTo(
												uuid,
												loadTokens = true,
												palette,
												clearCurrentTheme = false
											)
										}
									) {
										Text("Load colors from material color scheme tokens and don't clear current theme")
									}
								}
							)
						}

						AnimatedVisibility(
							visible = showDeleteDialog,
							enter = expandVertically(),
							exit = shrinkVertically()
						) {
							Dialog(onDismissRequest = { showDeleteDialog = false }) {
								AlertDialog(
									onDismissRequest = { showDeleteDialog = false },
									title = { Text("Delete this theme?") },
									icon = {
										SavedThemeItem(
											Modifier
												.width(150.dp)
												.height(180.dp)
												.clip(RoundedCornerShape(16.dp)),
											vm,
											Pair(index, uuid)
										)
									},
									text = { Text(text = "Are you sure you want to delete this theme? You will not be able to recover this theme if you delete it.",) },
									confirmButton = {
										FilledTonalButton(onClick = { vm.deleteTheme(uuid) }) {
											Text("Delete")
										}
									},
									dismissButton = {
										TextButton(onClick = { showDeleteDialog = false },) {
											Text("Cancel")
										}
									}
								)
							}
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

						if (map.second.first == "INCOMPATIBLE VALUE") Row(
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