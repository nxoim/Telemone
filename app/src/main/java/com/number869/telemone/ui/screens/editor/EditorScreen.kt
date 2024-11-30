@file:OptIn(ExperimentalFoundationApi::class)

package com.number869.telemone.ui.screens.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.number869.telemone.data.ThemeData
import com.number869.telemone.data.UiElementColorData
import com.number869.telemone.shared.ui.SmallTintedLabel
import com.number869.telemone.shared.utils.ThemeColorPreviewDisplayType
import com.number869.telemone.shared.utils.ThemeStorageType
import com.number869.telemone.shared.utils.colorOf
import com.number869.telemone.shared.utils.getColorDisplayType
import com.number869.telemone.shared.utils.incompatibleUiElementColorData
import com.number869.telemone.shared.utils.inject
import com.number869.telemone.shared.utils.showToast
import com.number869.telemone.ui.RootDestinations
import com.number869.telemone.ui.screens.editor.components.new.CheckboxSelectionOverlay
import com.number869.telemone.ui.screens.editor.components.new.CurrentThemePreview
import com.number869.telemone.ui.screens.editor.components.new.EditorTopAppBar
import com.number869.telemone.ui.screens.editor.components.new.ElementColorItem
import com.number869.telemone.ui.screens.editor.components.new.SavedThemeDropdownMenu
import com.number869.telemone.ui.screens.editor.components.new.SavedThemeItem
import com.number869.telemone.ui.screens.editor.components.new.ThemeSelectionToolbar
import com.nxoim.decomposite.core.common.navigation.NavController
import kotlinx.coroutines.Dispatchers
import my.nanihadesuka.compose.InternalLazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSelectionActionable


// this is prob gonna get redesigned
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
	rootNavController: NavController<RootDestinations>,
	editorNavController: NavController<EditorDestinations>,
	dialogsNavController: NavController<EditorDestinations.Dialogs>,
	vm: EditorViewModel
) {
	val topAppBarState = TopAppBarDefaults.pinnedScrollBehavior()
	val wholeThingListState = rememberLazyListState()

	val colorDisplayType = getColorDisplayType()

	val themeList = vm.themeList.collectAsState(listOf(), Dispatchers.Default)
	val mappedValuesAsList by vm.mappedValuesAsList.collectAsState(Dispatchers.Default)
	val newUiElements by vm.newUiElements.collectAsState(listOf(), Dispatchers.Default)
	val incompatibleValues by vm.incompatibleValues.collectAsState(listOf(), Dispatchers.Default)

	Scaffold(
		Modifier.nestedScroll(topAppBarState.nestedScrollConnection),
		topBar = {
			EditorTopAppBar(
				topAppBarState,
				mappedValuesAsList,
				exportCustomTheme = vm::exportCustomTheme,
				saveCurrentTheme = vm::saveCurrentTheme,
				resetCurrentTheme = vm::resetCurrentTheme,
				loadSavedTheme = vm::loadSavedTheme,
				changeValue = vm::changeValue,
				editorNavController = editorNavController,
				dialogsNavController = dialogsNavController,
				rootNavController = rootNavController
			)
		},
		bottomBar = { Box {} } // hello edge-to-edge
	) { scaffoldPadding ->
		Box() {
			AnimatedVisibility(
				visible = vm.loadingMappedValues,
				enter = fadeIn(tween(300)),
				exit = fadeOut(tween(300)),
				modifier = Modifier.align(Alignment.Center)
			) {
				CircularProgressIndicator()
			}

			AnimatedVisibility(
				visible = !vm.loadingMappedValues,
				enter = fadeIn(tween(300)),
				exit = fadeOut(tween(300)),
			) {
				LazyColumn(
					state = wholeThingListState,
					verticalArrangement = Arrangement.Absolute.spacedBy(4.dp),
					contentPadding = PaddingValues(
						bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 8.dp
					)
				) {
					item {
						CurrentThemeSection(
							Modifier.padding(top = scaffoldPadding.calculateTopPadding()),
							colorOf = {
								val color by vm.colorFromCurrentTheme(it).collectAsState()

								animateColorAsState(color).value
							}
						)
						SavedThemesSection(
							dialogsNavController,
							vm,
							themeList,
							colorDisplayType
						)
					}

					NewValuesSection(vm, newUiElements)
					IncompatibleValuesSection(vm, incompatibleValues, mappedValuesAsList)

					item {
						SmallTintedLabel(
							Modifier.padding(start = 16.dp),
							labelText = "All Colors"
						)
						Spacer(modifier = Modifier.height(16.dp))
					}

					AllColorsSection(vm, mappedValuesAsList)
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

@Composable
private fun CurrentThemeSection(
	modifier: Modifier = Modifier,
	colorOf: @Composable (String) -> Color
) = Column(modifier) {
	SmallTintedLabel(Modifier.padding(start = 16.dp), labelText = "Current Theme")
	CurrentThemePreview(colorOf)
	Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun SavedThemesSection(
	dialogsNavController: NavController<EditorDestinations.Dialogs>,
	vm: EditorViewModel,
	themeListState: State<List<ThemeData>>,
	colorDisplayType: ThemeColorPreviewDisplayType
) {
	val themeList by themeListState
	val savedThemesRowState = rememberLazyListState()
	val displayingSavedThemes by remember {
		derivedStateOf { themeList.isNotEmpty() }
	}

	LaunchedEffect(themeList) {
		if (themeList.isNotEmpty()) savedThemesRowState.animateScrollToItem(0)
	}

	Row(
		Modifier
			.clip(CircleShape)
			.clickable {
				dialogsNavController.navigate(
					EditorDestinations.Dialogs.SavedThemeTypeSelection,
				)
			},
		verticalAlignment = Alignment.Bottom,
		horizontalArrangement = spacedBy(8.dp)
	) {
		SmallTintedLabel(Modifier.padding(start = 16.dp), labelText = "Saved Themes")

		FilledTonalIconButton(
			onClick = {
				dialogsNavController.navigate(
					EditorDestinations.Dialogs.SavedThemeTypeSelection,
				)
			},
			modifier = Modifier.size(18.dp)
		) {
			Icon(Icons.Default.MoreVert, contentDescription = "Saved theme display type")
		}
	}


	Box {
		AnimatedVisibility(
			visible = vm.loadingThemes,
			enter = fadeIn(tween(200)),
			exit = fadeOut(tween(200))
		) {
			Box(modifier = Modifier.fillMaxWidth().height(64.dp)) {
				CircularProgressIndicator(Modifier.align(Alignment.Center))
			}
		}

		AnimatedVisibility(
			visible = !vm.loadingThemes && !displayingSavedThemes,
			enter = fadeIn() + expandVertically(),
			exit = fadeOut() + shrinkVertically(),
			modifier = Modifier.align(Alignment.Center)
		) {
			NoSavedThemesPlaceholder()
		}

		AnimatedVisibility(
			visible = displayingSavedThemes,
			enter = fadeIn() + expandVertically(),
			exit = fadeOut() + shrinkVertically()
		) {
			Column {
				LazyRow(
					state = savedThemesRowState,
					contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp),
					horizontalArrangement = spacedBy(16.dp),
					modifier = Modifier.animateContentSize()
				) {
					items(themeList, key = { item -> item.uuid }, contentType = { it::class.simpleName }) { theme ->
						var showDropDown by rememberSaveable { mutableStateOf(false) }

						Box {
							SavedThemeItem(
								Modifier
									.animateItem()
									.let {
										return@let if (!vm.themeSelectionToolbarIsVisible)
											it.combinedClickable(
												onClick = {
													vm.loadSavedTheme(
														ThemeStorageType.ByUuid(
															theme.uuid,
															withTokens = false,
															clearCurrentTheme = true
														)
													)

													showToast("Theme loaded")
												},
												onLongClick = { showDropDown = true }
											)
										else
											it.clickable { vm.selectOrUnselectSavedTheme(theme.uuid) }
									},
								colorOf = { targetUiElement ->
									colorOf(
										theme.values
											.find { it.name == targetUiElement }
											?: incompatibleUiElementColorData(targetUiElement),
										colorDisplayType
									)
								},
								overlay = {
									CheckboxSelectionOverlay(
										selected = vm.selectedThemes.contains(theme.uuid),
										onCheckedChange = { vm.selectOrUnselectSavedTheme(theme.uuid) }
									)
								}
							)
						}


						SavedThemeDropdownMenu(
							showDropDown,
							onDismissRequest = { showDropDown = false },
							onLoadThemeWithOptionsRequest = {
								dialogsNavController.navigate(
									EditorDestinations.Dialogs.LoadThemeWithOptions(theme.uuid)
								)
							},
							onExportTheme = { vm.exportTheme(theme.uuid, it) },
							onOverwriteDefaultThemeChoiceRequest = {
								showDropDown = false
								dialogsNavController.navigate(
									EditorDestinations.Dialogs.OverwriteDefaultThemeChoice(theme)
								)
							},
							onDeleteRequest = {
								showDropDown = false
								dialogsNavController.navigate(
									EditorDestinations.Dialogs.DeleteOneTheme(theme)
								)
							},
							onSelectRequest = {
								showDropDown = false
								vm.toggleThemeSelectionModeToolbar()
								vm.selectOrUnselectSavedTheme(theme.uuid)
							}
						)
					}
				}

				ThemeSelectionToolbarSection(vm, themeList = themeList, dialogsNavController = dialogsNavController)
			}
		}
	}
}

@Composable
private fun NoSavedThemesPlaceholder() {
	Box(
		Modifier
			.fillMaxWidth(1f)
			.height(120.dp)
			.clip(RoundedCornerShape(16.dp))
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

@Composable
private fun ThemeSelectionToolbarSection(
	vm: EditorViewModel,
	dialogsNavController: NavController<EditorDestinations.Dialogs>,
	themeList: List<ThemeData>
) {
	Row(
		Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.Center
	) {
		AnimatedVisibility(
			vm.themeSelectionToolbarIsVisible,
			enter = expandVertically(expandFrom = Alignment.CenterVertically) + fadeIn(),
			exit = shrinkVertically(shrinkTowards = Alignment.CenterVertically) + fadeOut(),
		) {
			ThemeSelectionToolbar(
				Modifier.padding(top = 16.dp),
				selectedThemeCount = vm.selectedThemes.count(),
				allThemesAreSelected = vm.selectedThemes.count() == themeList.count(),
				unselectAllThemes = vm::unselectAllThemes,
				selectAllThemes = vm::selectAllThemes,
				hideToolbarAction = { vm.hideThemeSelectionModeToolbar() },
				onRequestDeletion = {
					dialogsNavController.navigate(
						EditorDestinations.Dialogs.DeleteSelectedThemes(vm.selectedThemes.count()),
					)
				}
			)
		}
	}

	Spacer(
		Modifier
			.animateContentSize()
			.height(if (vm.themeSelectionToolbarIsVisible) 12.dp else 24.dp)
	)
}

private fun LazyListScope.NewValuesSection(vm: EditorViewModel, newUiElements: List<UiElementColorData>) {
	if (newUiElements.isNotEmpty()) {
		item {
			SmallTintedLabel(
				Modifier
					.padding(start = 16.dp)
					.animateItem(),
				labelText = "New Values"
			)

			Spacer(modifier = Modifier
				.height(16.dp)
				.animateItem())
		}

		itemsIndexed(newUiElements) { index, uiElementData ->
			ElementColorItem(
				Modifier
					.padding(horizontal = 16.dp)
					.animateItem(),
				paletteState = inject(),
				uiElementData = uiElementData,
				index = index,
				changeValue = vm::changeValue,
				lastIndexInList = newUiElements.lastIndex
			)
		}

		item { Spacer(modifier = Modifier.height(24.dp)) }
	}
}

@OptIn(ExperimentalFoundationApi::class)
private fun LazyListScope.IncompatibleValuesSection(
	vm: EditorViewModel,
	incompatibleValues: List<UiElementColorData>,
	mappedValuesAsList: List<UiElementColorData>
) {
	if (incompatibleValues.isNotEmpty()) {
		item {
			SmallTintedLabel(
				Modifier
					.padding(start = 16.dp)
					.animateItem(),
				labelText = "Incompatible Values"
			)

			Spacer(modifier = Modifier
				.height(16.dp)
				.animateItem())
		}

		itemsIndexed(incompatibleValues) { index, uiElementData ->
			ElementColorItem(
				Modifier
					.padding(horizontal = 16.dp)
					.animateItem(),
				paletteState = inject(),
				uiElementData = uiElementData,
				index = index,
				changeValue = vm::changeValue,
				lastIndexInList = mappedValuesAsList.lastIndex
			)
		}
	}
}

private fun LazyListScope.AllColorsSection(vm: EditorViewModel, mappedValuesAsList: List<UiElementColorData>) {
	itemsIndexed(mappedValuesAsList, key = { _, it -> it.name }) {index, uiElementColorData ->
		ElementColorItem(
			Modifier
				.padding(horizontal = 16.dp)
				.animateItem(),
			paletteState = inject(),
			uiElementData = uiElementColorData,
			index = index,
			changeValue = vm::changeValue,
			lastIndexInList = mappedValuesAsList.size
		)
	}
}