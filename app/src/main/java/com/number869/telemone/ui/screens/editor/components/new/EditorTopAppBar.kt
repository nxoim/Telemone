package com.number869.telemone.ui.screens.editor.components.new

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShortText
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.number869.decomposite.core.common.navigation.navController
import com.number869.decomposite.core.common.ultils.ContentType
import com.number869.decomposite.core.common.viewModel.viewModel
import com.number869.telemone.MainViewModel
import com.number869.telemone.ThemeStorageType
import com.number869.telemone.data.LoadedTheme
import com.number869.telemone.ui.Destinations
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditorTopAppBar(
	topAppBarState: TopAppBarScrollBehavior,
	mappedValues: LoadedTheme,
	mappedValuesAsList: List<Pair<String, Pair<String, Color>>>
) {
	val navController = navController<Destinations>()

	var searchbarVisible by remember { mutableStateOf(false) }

	Box(
		Modifier.fillMaxWidth(),
		contentAlignment = Alignment.TopCenter
	) {
		AnimatedVisibility(
			!searchbarVisible,
			enter = fadeIn() + slideInVertically(),
			exit = fadeOut() + slideOutVertically()
		) {
			TheAppBar(
				showSearchbar = { searchbarVisible = true },
				showClearBeforeLoadDialog = {
					navController.navigate(
						Destinations.EditorScreen.Dialogs.ClearThemeBeforeLoadingFromFile,
						ContentType.Overlay
					)
				},
				topAppBarState
			)
		}

		AnimatedVisibility(
			searchbarVisible,
			enter = fadeIn() + slideInVertically(),
			exit = fadeOut() + slideOutVertically()
		) {
			TheSearchbar(
				mappedValues = mappedValues ,
				mappedValuesAsList = mappedValuesAsList,
				hideSearchbar = { searchbarVisible = false },
			)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TheAppBar(
	showSearchbar: () -> Unit,
	showClearBeforeLoadDialog: () -> Unit,
	topAppBarState: TopAppBarScrollBehavior
) {
	val vm = viewModel<MainViewModel>()
	val navController = navController<Destinations>()
	var showMenu by remember { mutableStateOf(false) }
	var isShowingTapToSearchText by remember { mutableStateOf(false) }

	// switch texts every 3 seconds
	LaunchedEffect(isShowingTapToSearchText, Unit) {
		delay(3000)
		isShowingTapToSearchText = !isShowingTapToSearchText
	}

	TopAppBar(
		navigationIcon = {
			IconButton(onClick = { navController.navigateBack() }) {
				Icon(Icons.Default.ArrowBack, contentDescription = "Back")
			}
		},
		title = {
			AnimatedVisibility(
				!isShowingTapToSearchText,
				modifier = Modifier.clickable { showSearchbar() },
				enter = fadeIn(),
				exit = fadeOut()
			) {
				Text("Theme Editor")
			}
			AnimatedVisibility(
				isShowingTapToSearchText,
				modifier = Modifier.clickable { showSearchbar() },
				enter = fadeIn(),
				exit = fadeOut()
			) {
				Text("Tap to search")
			}
		},
		actions = {
			IconButton(onClick = { vm.exportCustomTheme() }) {
				Icon(Icons.Default.Upload, contentDescription = "Export current theme")
			}
			IconButton(onClick = { vm.saveCurrentTheme() }) {
				Icon(Icons.Default.Save, contentDescription = "Save current theme")
			}
			Box {
				IconButton(onClick = { showMenu = true }) {
					Icon(Icons.Default.MoreVert, contentDescription = "Options")
				}

				DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
					DropdownMenuItem(
						text = { Text(text = "Reset current theme") },
						onClick = { vm.resetCurrentTheme() },
						leadingIcon = { Icon(Icons.Default.Refresh, contentDescription = "Reset current theme") }
					)
					DropdownMenuItem(
						text = { Text(text = "Load stock light theme") },
						onClick = { vm.loadSavedTheme(ThemeStorageType.Stock(isLight = true)) },
						leadingIcon = { Icon(Icons.Default.LightMode, contentDescription = "Load stock light theme") }
					)
					DropdownMenuItem(
						text = { Text(text = "Load stock dark theme") },
						onClick = { vm.loadSavedTheme(ThemeStorageType.Stock(isLight = false)) },
						leadingIcon = { Icon(Icons.Default.DarkMode, contentDescription = "Load stock dark theme") }
					)
					DropdownMenuItem(
						text = { Text(text = "Show values") },
						onClick = {
							navController.navigate(Destinations.EditorScreen.ThemeValuesScreen)
							showMenu = false
						},
						leadingIcon = { Icon(Icons.Default.ShortText, contentDescription = "Show values") }
					)
					DropdownMenuItem(
						text = { Text(text = "Load default light theme") },
						onClick = { vm.loadSavedTheme(ThemeStorageType.Default(isLight = true)) },
						leadingIcon = { Icon(Icons.Default.LightMode, contentDescription = "Load default light theme") }
					)
					DropdownMenuItem(
						text = { Text(text = "Load default dark theme") },
						onClick = { vm.loadSavedTheme(ThemeStorageType.Default(isLight = false)) },
						leadingIcon = { Icon(Icons.Default.DarkMode, contentDescription = "Load default dark theme") }
					)
					DropdownMenuItem(
						text = { Text(text = "Load theme from file") },
						onClick = { showClearBeforeLoadDialog(); showMenu = false },
						leadingIcon = { Icon(Icons.Default.UploadFile, contentDescription = "Load default dark theme") }
					)
				}
			}
		},
		scrollBehavior = topAppBarState
	)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun TheSearchbar(
	mappedValues: LoadedTheme,
	mappedValuesAsList: List<Pair<String, Pair<String, Color>>>,
	hideSearchbar: () -> Unit,
) {
	var fullscreen by remember { mutableStateOf(false) }
	var searchQuery by remember { mutableStateOf("") }
	val searchQueryIsEmpty by remember { derivedStateOf { searchQuery == "" } }
	val searchedThings = mappedValuesAsList.filter {
		it.first.contains(searchQuery, true)
				||
				it.second.first.contains(searchQuery, true)
	}

	SearchBar(
		query = searchQuery,
		onQueryChange = {
			searchQuery = it
			fullscreen = true
		},
		onSearch = {  }, // it desperately wants me to keep this line
		placeholder = { Text(text = "Search in current theme") },
		leadingIcon = { Icon(Icons.Default.Search, "Search")},
		trailingIcon = {
			AnimatedVisibility(
				visible = searchQueryIsEmpty && !fullscreen,
				enter = fadeIn(),
				exit = fadeOut()
			) {
				IconButton(onClick = { hideSearchbar() }) {
					Icon(Icons.Default.ArrowUpward, "Hide searchbar")
				}
			}

			AnimatedVisibility(
				visible = !searchQueryIsEmpty,
				enter = fadeIn(),
				exit = fadeOut()
			) {
				IconButton(onClick = { searchQuery = "" }) {
					Icon(Icons.Default.Clear, "Clear search")
				}
			}
		},
		// TODO maybe make it active when its focused
		active = fullscreen,
		onActiveChange = {  }
	) {
		// clears search when back button is pressed.
		// its here because it doesnt work if i put it
		// beside the other back handler at the top
		// of this composable
		BackHandler(fullscreen) {
			fullscreen = false
		}

		Box {
			this@SearchBar.AnimatedVisibility(
				visible = searchQueryIsEmpty,
				enter = fadeIn(),
				exit = fadeOut()
			) {
				Box(
					Modifier
						.clickable(
							indication = null,
							interactionSource = remember { MutableInteractionSource() }
						) { fullscreen = false }
						.imePadding()
						.fillMaxSize()
				) {
					Text(
						"Search is empty. Tap this to close",
						modifier = Modifier.align(Alignment.Center)
					)
				}
			}

			this@SearchBar.AnimatedVisibility(
				visible = !searchQueryIsEmpty,
				enter = fadeIn(),
				exit = fadeOut()
			) {
				LazyColumn(
					contentPadding = PaddingValues(
						top = 8.dp,
						bottom = 4.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
					),
					verticalArrangement = spacedBy(4.dp)
				) {
					itemsIndexed(searchedThings) { index, uiElementData ->
						ElementColorItem(
							Modifier
								.padding(horizontal = 16.dp)
								.animateItemPlacement(),
							uiElementData = uiElementData,
							index = index,
							themeMap = mappedValues,
							lastIndexInList = mappedValuesAsList.lastIndex
						)
					}
				}
			}
		}
	}
}