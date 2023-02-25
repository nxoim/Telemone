package com.dotstealab.telemone.ui.screens.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.dotstealab.telemone.ui.screens.main.ChatScreenPreview
import com.dotstealab.telemone.ui.screens.main.components.PalettePopup
import com.dotstealab.telemone.ui.screens.main.components.SavedThemeItem
import com.dotstealab.telemone.ui.theme.fullPalette

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditorScreen(navController: NavHostController, vm: MainViewModel) {
	val context = LocalContext.current
	val palette = fullPalette()
	val isDarkTheme = isSystemInDarkTheme()

	val themeList by remember { derivedStateOf { vm.themeList }  }
	val mappedValues by remember { derivedStateOf { vm.mappedValues }  }
	val pickedFileUriState = remember { mutableStateOf<Uri?>(null) }
	val launcher =
		rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { result ->
			pickedFileUriState.value = result
			result?.let { uri ->
				vm.loadThemeFromFile(context, uri, palette, isDarkTheme)
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
						SavedThemeItem(
							Modifier
								.width(150.dp)
								.height(180.dp)
								.clip(RoundedCornerShape(16.dp))
								.animateItemPlacement()
								.combinedClickable(
									onClick = { vm.setCurrentMapTo(uuid) },
									onLongClick = { vm.removeMap(uuid) }
								),
							vm,
							Pair(index, uuid)
						)
					}
				}
			}

			items(mappedValues.toList().sortedBy { it.first }, key = { it.first }) { map ->
				var showPopUp by remember { mutableStateOf(false) }
				val backgroundColor = mappedValues.getValue(map.first).second

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
							modifier = Modifier.background(Color(0xB3FFFFFF))
						)
						// material you palette color token
						Text(
							text = map.second.first,
							modifier = Modifier.background(Color(0xB3FFFFFF))
						)
					}

					if (showPopUp) {
						Popup(onDismissRequest = { showPopUp = false })  {
							PalettePopup(map.first, vm, palette)
						}
					}
				}
			}

			items(mappedValues.toList().sortedBy { it.first }) { map ->
				var showPopUp by remember { mutableStateOf(false) }
				val backgroundColor = mappedValues.getValue(map.first).second

				if (map.second.first != "INCOMPATIBLE VALUE") Row(
					Modifier
						.height(64.dp)
						.fillMaxWidth()
						.background(backgroundColor)
						.clickable { showPopUp = !showPopUp }
						.animateItemPlacement()
				) {
					Column() {
						// name
						Text(text = map.first, modifier = Modifier.background(Color(0x4D000000)))
						// material you palette color token
						Text(text = map.second.first, modifier = Modifier.background(Color(0x4D000000)))
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