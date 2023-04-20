package com.number869.telemone.ui.screens.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import com.number869.seksinavigation.OverlayItemWrapper
import com.number869.seksinavigation.OverlayLayoutState
import com.number869.telemone.MainViewModel
import com.number869.telemone.ui.screens.editor.components.old.PalettePopup
import com.number869.telemone.ui.screens.editor.components.old.preview.GroupInfoTopBar
import com.number869.telemone.ui.screens.editor.components.old.preview.PinnedMessages
import com.number869.telemone.ui.theme.FullPaletteList
import com.number869.telemone.ui.theme.fullPalette


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditorScreen(navController: NavHostController, vm: MainViewModel, state: OverlayLayoutState) {
	val topPaddingAsDp = WindowInsets.systemBars.getTop(LocalDensity.current).dp
	val bottomPaddingAsDp = WindowInsets.systemBars.getBottom(LocalDensity.current).dp

	val palette = fullPalette()
	val context = LocalContext.current

	Column(
		Modifier
			.fillMaxSize()
			.navigationBarsPadding(),
		verticalArrangement = Arrangement.SpaceAround,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		LazyColumn(
			contentPadding = PaddingValues(
				top = topPaddingAsDp,
				bottom = bottomPaddingAsDp,
				start = 16.dp,
				end = 16.dp
			)
		) {
			item {
				AppBarColoursItem(vm, palette)
			}

			item {
				val yes = vm.disableShadows
				Row(
					Modifier
						.height(32.dp)
						.fillMaxWidth(),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically
				) {
					Text(text = "Disable shadows", style = MaterialTheme.typography.headlineMedium)
					Checkbox(checked = yes, onCheckedChange = { vm.switchShadowsOnOff() })
				}
			}
		}

		Column {
			Button(onClick = { vm.shareCustomTheme(context) }) {
				Text(text = "Share Current")
			}

			OverlayItemWrapper(
				isOriginalItemStatic = true,
				originalCornerRadius = 16.dp,
				key = "alternativeEditorScreen",
				state = state
			) {
				Column(Modifier.background(MaterialTheme.colorScheme.background)) {
					val itemState = state.itemsState["alternativeEditorScreen"] ?: state.emptyOverlayItemValues

					AnimatedVisibility(visible = !itemState.isExpanded) {
						OutlinedButton(onClick = { state.addToOverlayStack("alternativeEditorScreen") }) {
							Text(text = "Go to alternative editor")
						}
					}

					AnimatedVisibility(visible = itemState.isExpanded) {
						AlternativeEditorScreen(navController, vm)
					}
				}
			}
		}
	}
}

@Composable
fun AppBarColoursItem(vm: MainViewModel, palette: FullPaletteList) {
	Row(
		Modifier.fillMaxWidth(),
		verticalAlignment = Alignment.Bottom
	) {
		Column(Modifier.weight(1f, false)) {
			Text(
				"Top App Bars",
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.headlineMedium)
			)

			Spacer(Modifier.height(16.dp))

			Column(
				Modifier
					.clip(RoundedCornerShape(32.dp))
					.border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(32.dp))
			) {
				GroupInfoTopBar(
					vm.colorOfCurrentTheme("actionBarDefault"),
					vm.colorOfCurrentTheme("actionBarDefaultIcon"),
					vm.colorOfCurrentTheme("avatar_backgroundOrange"),
					vm.colorOfCurrentTheme("avatar_text"),
					vm.colorOfCurrentTheme("actionBarDefaultTitle"),
					vm.colorOfCurrentTheme("actionBarDefaultSubtitle"))
				PinnedMessages(vm.colorOfCurrentTheme("actionBarDefault"))
			}
		}

		Spacer(Modifier.width(16.dp))

		Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
			ColorSelectionItem("actionBarDefault", vm, palette)
			ColorSelectionItem("actionBarDefaultIcon", vm, palette)
			ColorSelectionItem("actionBarDefaultTitle", vm, palette)

		}

	}
}

@Composable
fun ColorSelectionItem(itemName: String, vm: MainViewModel, palette: FullPaletteList) {
	var showPopUp by remember { mutableStateOf(false) }
	val color by remember { derivedStateOf { vm.colorOfCurrentTheme(itemName) } }

	Box(
		Modifier
			.size(48.dp)
			.clip(CircleShape)
			.border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
			.clickable { showPopUp = true }
			.aspectRatio(1f)
			.background(color)
	)

	if (showPopUp) {
		Popup(onDismissRequest = { showPopUp = false }) {
			PalettePopup(itemName, vm, palette)
		}
	}
}