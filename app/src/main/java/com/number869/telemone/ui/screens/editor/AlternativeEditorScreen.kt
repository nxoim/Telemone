package com.number869.telemone.ui.screens.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.number869.seksinavigation.OverlayItemWrapper
import com.number869.seksinavigation.OverlayLayoutState
import com.number869.telemone.MainViewModel
import com.number869.telemone.ui.screens.editor.components.old.ChatScreenPreview
import com.number869.telemone.ui.screens.editor.components.old.ClearBeforeLoadDialog
import com.number869.telemone.ui.screens.editor.components.old.DeleteThemeDialog
import com.number869.telemone.ui.screens.editor.components.old.LoadFromSavedDialog
import com.number869.telemone.ui.screens.editor.components.old.OverwriteChoiceDialog
import com.number869.telemone.ui.screens.editor.components.old.OverwriteDefaultsDialog
import com.number869.telemone.ui.screens.editor.components.old.PalettePopup
import com.number869.telemone.ui.screens.editor.components.old.SavedThemeItem
import com.number869.telemone.ui.screens.themeContents.ThemeContentsScreen
import com.number869.telemone.ui.theme.fullPalette


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AlternativeEditorScreen(overlayState: OverlayLayoutState, vm: MainViewModel) {
	val context = LocalContext.current
	val palette = fullPalette()

	val themeList by remember {
		derivedStateOf {
			vm.themeList.flatMap { map ->
				map.keys.filterNot {
					it == "defaultLightThemeUUID" || it == "defaultDarkThemeUUID"
				}
			}.reversed()
		}
	}
	val mappedValues by remember { derivedStateOf { vm.mappedValues }  }
	val mappedValuesAsList = mappedValues.toList().sortedBy { it.first }
	val pickedFileUriState = remember { mutableStateOf<Uri?>(null) }
	val savedThemesRowState = rememberLazyListState()

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

	LaunchedEffect(themeList) {
		savedThemesRowState.animateScrollToItem(0)
	}

	Column(Modifier.statusBarsPadding()) {
		Column() {
			Row() {
				Button(onClick = { vm.exportCustomTheme(context) }) {
					Text(text = "Export Current")
				}
				Button(onClick = { vm.saveCurrentTheme() }) {
					Text(text = "Save Current")
				}
				Button(onClick = { vm.resetCurrentTheme() }) {
					Text(text = "Reset current")
				}
			}

			Row() {
				Button(onClick = { vm.loadStockLightTheme(palette, context) }) {
					Text(text = "Load Stock Light")
				}
				Button(onClick = { vm.loadStockDarkTheme(palette, context) }) {
					Text(text = "Load Stock Dark")
				}

				val isContentsScreenExpanded by remember {
					derivedStateOf {
						overlayState.getIsExpanded("themeContentsScreen")
					}
				}
				OverlayItemWrapper(
					key = "themeContentsScreen",
					isOriginalItemStatic = true,
					state = overlayState
				) {
					AnimatedVisibility(visible = !isContentsScreenExpanded, enter = fadeIn(), exit = fadeOut()) {
						Button(onClick = { overlayState.addToOverlayStack("themeContentsScreen")  }) {
							Text(text = "Show Values")
						}
					}

					AnimatedVisibility(visible = isContentsScreenExpanded, enter = fadeIn(), exit = fadeOut()) {
						ThemeContentsScreen(vm)
					}
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
			}
		}

		LazyColumn(verticalArrangement = spacedBy(4.dp), contentPadding = PaddingValues(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 8.dp)) {
			// buttons. theme preview, saved themes
			// in the future this is supposed to be a menu where you choose
			// what to edit based on previews.
			// for example: chat screen preview. you press on chat bubble
			// and choose the chat bubbles color from the popup
			item {
				ChatScreenPreview(vm)

				Text(
					text = "Saved Themes",
					style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.headlineMedium),
					modifier = Modifier.padding(start = 24.dp)
				)

				LazyRow(
					state = savedThemesRowState,
					contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp),
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
							uuid = uuid,
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

						LoadFromSavedDialog(
							{ showApplyDialog = false },
							showApplyDialog,
							vm,
							palette,
							uuid
						)

						DeleteThemeDialog(
							close = { showDeleteDialog = false },
							showDeleteDialog,
							vm,
							uuid
						)

						OverwriteChoiceDialog(
							{ showOverwriteChoiceDialog = false },
							showOverwriteChoiceDialog,
							{ showOverwriteChoiceDialog = false; showOverwriteLightThemeDialog = true },
							{ showOverwriteChoiceDialog = false; showOverwriteDarkThemeDialog = true },
							vm
						)

						OverwriteDefaultsDialog(
							close = { showOverwriteDarkThemeDialog = false },
							showOverwriteDarkThemeDialog,
							overwrite = {
								showOverwriteDarkThemeDialog = false
								vm.overwriteDefaultDarkTheme(uuid, palette)
							},
							vm = vm,
							overwriteDark = true,
							uuid
						)

						OverwriteDefaultsDialog(
							close = { showOverwriteLightThemeDialog = false },
							showOverwriteLightThemeDialog,
							overwrite = {
								showOverwriteLightThemeDialog = false
								vm.overwriteDefaultLightTheme(uuid, palette)
							},
							vm = vm,
							overwriteDark = false,
							uuid
						)
					}
				}
			}



			if (mappedValues.values.contains(Pair("INCOMPATIBLE VALUE", Color.Red))) {
				item {
					Spacer(modifier = Modifier.height(16.dp))
					Text(
						text = "Incompatible Values",
						style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.headlineMedium),
						modifier = Modifier.padding(start = 24.dp)
					)
					Spacer(modifier = Modifier.height(12.dp))
				}
			}

			mappedValuesAsList.forEachIndexed { index, map ->
				when(map.second.first) {
					"INCOMPATIBLE VALUE" -> item {
						var showPopUp by remember { mutableStateOf(false) }
						val backgroundColor = when (mappedValues.containsKey(map.first)) {
							true -> mappedValues[map.first]!!.second
							else -> Color.Red
						}

						val roundedCornerShape = if (index == 0)
							RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
						else if (index == mappedValuesAsList.lastIndex)
							RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 32.dp, bottomEnd = 32.dp)
						else
							RoundedCornerShape(4.dp)

						Row(
							Modifier
								.padding(horizontal = 16.dp)
								.clip(roundedCornerShape)
								.background(backgroundColor)
								.height(64.dp)
								.fillMaxWidth()
								.clickable { showPopUp = !showPopUp }
								.animateItemPlacement(),
							horizontalArrangement = Arrangement.Center,
							verticalAlignment = Alignment.CenterVertically
						) {
							Column(horizontalAlignment = Alignment.CenterHorizontally) {
								// name
								Text(
									text = map.first,
									style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
									color = Color.White,
									modifier = Modifier
										.clip(CircleShape)
										.background(Color(0x4D000000))
										.padding(horizontal = 8.dp, vertical = 4.dp)
								)

								Spacer(modifier = Modifier.height(8.dp))

								// material you palette color token
								Text(
									text = map.second.first,
									style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
									color = Color.White,
									modifier = Modifier
										.clip(CircleShape)
										.background(Color(0x4D000000))
										.padding(horizontal = 8.dp, vertical = 4.dp)
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

			if (vm.differencesBetweenFileAndCurrent.isNotEmpty()) {
				item {
					Spacer(modifier = Modifier.height(16.dp))
					Text(
						text = "Incompatible Values",
						style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.headlineMedium),
						modifier = Modifier.padding(start = 24.dp)
					)
					Spacer(modifier = Modifier.height(12.dp))
				}

				itemsIndexed(vm.differencesBetweenFileAndCurrent.toList()) { index, map ->
					var showPopUp by remember { mutableStateOf(false) }
					val backgroundColor = when (vm.differencesBetweenFileAndCurrent.containsKey(map.first)) {
						true -> vm.differencesBetweenFileAndCurrent[map.first]!!.second
						else -> Color.Red
					}

					val roundedCornerShape = if (index == 0)
						RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
					else if (index == mappedValuesAsList.lastIndex)
						RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 32.dp, bottomEnd = 32.dp)
					else
						RoundedCornerShape(4.dp)

					Row(
						Modifier
							.padding(horizontal = 16.dp)
							.clip(roundedCornerShape)
							.background(backgroundColor)
							.height(64.dp)
							.fillMaxWidth()
							.clickable { showPopUp = !showPopUp }
							.animateItemPlacement(),
						horizontalArrangement = Arrangement.Center,
						verticalAlignment = Alignment.CenterVertically
					) {
						Column(horizontalAlignment = Alignment.CenterHorizontally) {
							// name
							Text(
								text = map.first,
								style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
								color = Color.White,
								modifier = Modifier
									.clip(CircleShape)
									.background(Color(0x4D000000))
									.padding(horizontal = 8.dp, vertical = 4.dp)
							)

							Spacer(modifier = Modifier.height(8.dp))

							// material you palette color token
							Text(
								text = map.second.first,
								style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
								color = Color.White,
								modifier = Modifier
									.clip(CircleShape)
									.background(Color(0x4D000000))
									.padding(horizontal = 8.dp, vertical = 4.dp)
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
			
			item {
				Spacer(modifier = Modifier.height(16.dp))
				Text(
					text = "All Colors",
					style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.headlineMedium),
					modifier = Modifier.padding(start = 24.dp)
				)
				Spacer(modifier = Modifier.height(12.dp))
			}

			itemsIndexed(mappedValuesAsList, key = { index, item ->  item.first }) {index, map ->
				var showPopUp by remember { mutableStateOf(false) }
				val backgroundColor = when (mappedValues.containsKey(map.first)) {
					true -> mappedValues[map.first]!!.second
					else -> Color.Red
				}

				val roundedCornerShape = if (index == 0)
					RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
				else if (index == mappedValuesAsList.lastIndex)
					RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 32.dp, bottomEnd = 32.dp)
				else
					RoundedCornerShape(4.dp)

				Row(
					Modifier
						.padding(horizontal = 16.dp)
						.clip(roundedCornerShape)
						.background(backgroundColor)
						.height(64.dp)
						.fillMaxWidth()
						.clickable { showPopUp = !showPopUp }
						.animateItemPlacement(),
					horizontalArrangement = Arrangement.Center,
					verticalAlignment = Alignment.CenterVertically
				) {
					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						// name
						Text(
							text = map.first,
							style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
							color = Color.White,
							modifier = Modifier
								.clip(CircleShape)
								.background(Color(0x4D000000))
								.padding(horizontal = 8.dp, vertical = 4.dp)
						)

						Spacer(modifier = Modifier.height(8.dp))

						// material you palette color token
						Text(
							text = map.second.first,
							style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
							color = Color.White,
							modifier = Modifier
								.clip(CircleShape)
								.background(Color(0x4D000000))
								.padding(horizontal = 8.dp, vertical = 4.dp)
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