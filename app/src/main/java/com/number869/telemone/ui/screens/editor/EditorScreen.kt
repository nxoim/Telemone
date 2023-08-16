@file:OptIn(ExperimentalFoundationApi::class)

package com.number869.telemone.ui.screens.editor

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.number869.telemone.MainViewModel
import com.number869.telemone.shared.ui.SmallTintedLabel
import com.number869.telemone.ui.screens.editor.components.new.CurrentThemePreview
import com.number869.telemone.ui.screens.editor.components.new.EditorTopAppBar
import com.number869.telemone.ui.screens.editor.components.new.ElementColorItem
import com.number869.telemone.ui.screens.editor.components.new.SavedThemeItem
import com.number869.telemone.ui.screens.editor.components.new.SavedThemeItemDisplayTypeChooserDialog
import com.number869.telemone.ui.screens.editor.components.new.ThemeSelectionToolbar
import com.number869.telemone.ui.theme.PaletteState
import my.nanihadesuka.compose.InternalLazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSelectionActionable


// this is prob gonna get redesigned
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
	navController: NavController,
	vm: MainViewModel,
	paletteState: PaletteState
) {
	val topAppBarState = TopAppBarDefaults.pinnedScrollBehavior()
	val context = LocalContext.current
	val preferences = LocalContext.current.getSharedPreferences(
		"AppPreferences.Settings",
		Context.MODE_PRIVATE
	)

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
	val mappedValuesAsList by remember { derivedStateOf { mappedValues.toList().sortedBy { it.first } } }
	val newUiElementsColors by remember { derivedStateOf { mappedValues.filter { !vm.defaultCurrentTheme.keys.contains(it.key) }.toList() } }
	val incompatibleValues by remember { derivedStateOf { mappedValues.filter { it.value.first == "INCOMPATIBLE VALUE" }.toList() } }
	val savedThemesRowState = rememberLazyListState()
	val wholeThingListState = rememberLazyListState()

	var themeSelectionModeIsActive by remember { mutableStateOf(false) }

	LaunchedEffect(themeList) {
		savedThemesRowState.animateScrollToItem(0)
	}

	Scaffold(
		Modifier.nestedScroll(topAppBarState.nestedScrollConnection),
		topBar = {
			EditorTopAppBar(
				topAppBarState,
				paletteState,
				navController,
				vm,
				mappedValues = { mappedValues }
			) { mappedValuesAsList }
		},
		bottomBar = { Box {} }, // edge to edge hello
	) { scaffoldPadding ->
		var showSavedThemeTypeDialog by remember { mutableStateOf(false) }

		// where do i put these damn dialogs while keeping animations pretty
		SavedThemeItemDisplayTypeChooserDialog(
			showSavedThemeTypeDialog,
			hideDialog = { showSavedThemeTypeDialog = false }
		)

		Column {
			Box() {
				LazyColumn(
					state = wholeThingListState,
					verticalArrangement = Arrangement.Absolute.spacedBy(4.dp),
					contentPadding = PaddingValues(
						bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 8.dp
					)
				) {
					item {
						Spacer(modifier = Modifier.padding(top = scaffoldPadding.calculateTopPadding()))

						SmallTintedLabel(Modifier.padding(start = 16.dp), labelText = "Current Theme")
						CurrentThemePreview(vm)

						Spacer(modifier = Modifier.height(8.dp))

						Row(
							Modifier
								.clip(CircleShape)
								.clickable { showSavedThemeTypeDialog = true },
							verticalAlignment = Alignment.Bottom,
							horizontalArrangement = spacedBy(8.dp)
						) {
							SmallTintedLabel(Modifier.padding(start = 16.dp), labelText = "Saved Themes")

							FilledTonalIconButton(
								onClick = { showSavedThemeTypeDialog = true },
								modifier = Modifier.size(18.dp)
							) {
								Icon(Icons.Default.MoreVert, contentDescription = "Saved theme display type")
							}
						}

						this@Column.AnimatedVisibility(visible = themeList.isEmpty()) {
							Box(
								Modifier
									.fillMaxWidth(1f)
									.height(120.dp)
									.clip(RoundedCornerShape(16.dp))
									.animateItemPlacement()
									.animateContentSize()
							) {
								Column(
									Modifier
										.fillMaxSize()
										.padding(horizontal = 32.dp, vertical = 16.dp)
										.clip(CircleShape)
										.background(MaterialTheme.colorScheme.surfaceVariant),
									horizontalAlignment = Alignment.CenterHorizontally,
									verticalArrangement = Arrangement.Center
								) {
									Text(
										"No saved themes",
										style = TextStyle(platformStyle = PlatformTextStyle(
											includeFontPadding = false
										)).plus(MaterialTheme.typography.headlineSmall),
										textAlign = TextAlign.Center
									)
								}
							}
						}

						this@Column.AnimatedVisibility(visible = themeList.isNotEmpty()) {
							Column {
								LazyRow(
									state = savedThemesRowState,
									contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp),
									horizontalArrangement = spacedBy(16.dp),
									modifier = Modifier.animateContentSize()
								) {
									itemsIndexed(themeList, key = { _, item -> item }) { index, uuid ->
										SavedThemeItem(
											Modifier.animateItemPlacement(),
											vm,
											uuid,
											paletteState,
											context,
											true,
											changeSelectionMode = { themeSelectionModeIsActive = !themeSelectionModeIsActive },
											themeSelectionModeIsActive = themeSelectionModeIsActive
										)
									}
								}

								Row(
									Modifier.fillMaxWidth(),
									horizontalArrangement = Arrangement.Center
								) {
									AnimatedVisibility(
										themeSelectionModeIsActive,
										enter = expandVertically(expandFrom = Alignment.CenterVertically) + fadeIn(),
										exit = shrinkVertically(shrinkTowards = Alignment.CenterVertically) + fadeOut(),
									) {
										ThemeSelectionToolbar(
											Modifier.padding(top = 16.dp),
											vm,
											hideToolbarAction = { themeSelectionModeIsActive = false },
											context
										)
									}
								}

								Spacer(
									Modifier
										.animateContentSize()
										.height(if (themeSelectionModeIsActive) 12.dp else 24.dp)
								)
							}
						}
					}

					if (newUiElementsColors.isNotEmpty()) {
						item {
							SmallTintedLabel(
								Modifier
									.padding(start = 16.dp)
									.animateItemPlacement(),
								labelText = "New Values"
							)
							Spacer(modifier = Modifier
								.height(16.dp)
								.animateItemPlacement())
						}

						itemsIndexed(newUiElementsColors) { index, uiElementData ->
							ElementColorItem(
								Modifier
									.padding(horizontal = 16.dp)
									.animateItemPlacement(),
								uiElementData = uiElementData,
								vm = vm,
								index = index,
								themeMap = mappedValues,
								lastIndexInList = newUiElementsColors.lastIndex
							)
						}

						item { Spacer(modifier = Modifier.height(24.dp)) }
					}

					if (incompatibleValues.isNotEmpty()) {
						item {
							SmallTintedLabel(
								Modifier
									.padding(start = 16.dp)
									.animateItemPlacement(),
								labelText = "Incompatible Values"
							)

							Spacer(modifier = Modifier
								.height(16.dp)
								.animateItemPlacement())
						}

						itemsIndexed(incompatibleValues) { index, uiElementData ->
							ElementColorItem(
								Modifier
									.padding(horizontal = 16.dp)
									.animateItemPlacement(),
								uiElementData = uiElementData,
								vm = vm,
								index = index,
								themeMap = mappedValues,
								lastIndexInList = mappedValuesAsList.lastIndex
							)
						}
					}

					item {
						SmallTintedLabel(
							Modifier
								.padding(start = 16.dp)
								.animateItemPlacement(),
							labelText = "All Colors"
						)

						Spacer(modifier = Modifier.height(16.dp))
					}

					itemsIndexed(mappedValuesAsList) {index, uiElementData ->
						ElementColorItem(
							Modifier
								.padding(horizontal = 16.dp)
								.animateItemPlacement(),
							uiElementData = uiElementData,
							vm = vm,
							index = index,
							themeMap = mappedValues,
							lastIndexInList = mappedValuesAsList.lastIndex
						)
					}
				}

				InternalLazyColumnScrollbar(
					listState = wholeThingListState,
					thumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
					thumbSelectedColor = MaterialTheme.colorScheme.primary,
					selectionActionable = ScrollbarSelectionActionable.WhenVisible,
					modifier = Modifier.padding(
						top = scaffoldPadding.calculateTopPadding() + 8.dp,
						bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 8.dp
					)
				)
			}
		}
	}
}