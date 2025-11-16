@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.number869.telemone.ui.screens.editor.components.new

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.number869.telemone.R
import com.number869.telemone.data.UiElementColorData
import com.number869.telemone.shared.utils.ThemeStorageType
import com.number869.telemone.ui.RootDestinations
import com.number869.telemone.ui.screens.editor.EditorDestinations
import com.number869.telemone.ui.theme.PaletteState
import com.nxoim.decomposite.core.common.navigation.NavController
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditorTopAppBar(
    topAppBarState: TopAppBarScrollBehavior,
    mappedValues: List<UiElementColorData>,
    exportCustomTheme: () -> Unit,
    saveCurrentTheme: () -> Unit,
    resetCurrentTheme: () -> Unit,
    loadSavedTheme: (ThemeStorageType) -> Unit,
    changeValue: (String, String, Color) -> Unit,
    editorNavController: NavController<EditorDestinations>,
    dialogsNavController: NavController<EditorDestinations.Dialogs>,
    rootNavController: NavController<RootDestinations>,
    paletteState: PaletteState
) {
    var searchbarVisible by rememberSaveable { mutableStateOf(false) }

    Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        SharedTransitionLayout {
            val key = rememberSharedContentState(0)

            AnimatedContent(searchbarVisible) { visible ->
                if (visible) {
                    TheSearchbar(
                        mappedValues = mappedValues,
                        changeValue = changeValue,
                        hideSearchbar = { searchbarVisible = false },
                        paletteState = paletteState,
                        modifier = Modifier.sharedBounds(key, this)
                    )
                } else {
                    TheAppBar(
                        showSearchbar = { searchbarVisible = true },
                        showClearBeforeLoadDialog = {
                            dialogsNavController.navigate(
                                EditorDestinations.Dialogs.ClearThemeBeforeLoadingFromFile
                            )
                        },
                        exportCustomTheme,
                        saveCurrentTheme,
                        resetCurrentTheme,
                        loadSavedTheme,
                        topAppBarState,
                        editorNavController,
                        rootNavController,
                        modifier = Modifier.sharedBounds(key, this)
                    )
                }
            }
        }
    }
}

private val centerVertically = BiasAlignment(-1f, 0f)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TheAppBar(
    showSearchbar: () -> Unit,
    showClearBeforeLoadDialog: () -> Unit,
    exportCustomTheme: () -> Unit,
    saveCurrentTheme: () -> Unit,
    resetCurrentTheme: () -> Unit,
    loadSavedTheme: (ThemeStorageType) -> Unit,
    topAppBarState: TopAppBarScrollBehavior,
    editorNavController: NavController<EditorDestinations>,
    rootNavController: NavController<RootDestinations>,
    modifier: Modifier = Modifier
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }
    var isShowingTapToSearchText by remember { mutableStateOf(false) }

    // switch texts every 3 seconds
    LaunchedEffect(isShowingTapToSearchText, Unit) {
        delay(3000)
        isShowingTapToSearchText = !isShowingTapToSearchText
    }

    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = { rootNavController.navigateBack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        title = {
            Box(contentAlignment = centerVertically) {
                AnimatedVisibility(
                    !isShowingTapToSearchText,
                    modifier = Modifier.clickable { showSearchbar() },
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(stringResource(R.string.theme_editor))
                }
                AnimatedVisibility(
                    isShowingTapToSearchText,
                    modifier = Modifier.clickable { showSearchbar() },
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(stringResource(R.string.tap_to_search))
                }
            }
        },
        actions = {
            IconButton(onClick = { exportCustomTheme() }) {
                Icon(Icons.Default.Upload, contentDescription = "Export current theme")
            }
            IconButton(onClick = { saveCurrentTheme() }) {
                Icon(Icons.Default.Save, contentDescription = "Save current theme")
            }
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Options")
                }

                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.reset_current_theme)) },
                        onClick = { resetCurrentTheme() },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = stringResource(R.string.reset_current_theme)
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.load_stock_light_theme)) },
                        onClick = { loadSavedTheme(ThemeStorageType.Stock(isLight = true)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.LightMode,
                                contentDescription = stringResource(R.string.load_stock_light_theme)
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.load_stock_dark_theme)) },
                        onClick = { loadSavedTheme(ThemeStorageType.Stock(isLight = false)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.DarkMode,
                                contentDescription = stringResource(R.string.load_stock_dark_theme)
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.show_values_action)) },
                        onClick = {
                            editorNavController.navigate(EditorDestinations.ThemeValues)
                            showMenu = false
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.ShortText,
                                contentDescription = stringResource(R.string.show_values_action)
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.load_default_light_theme)) },
                        onClick = { loadSavedTheme(ThemeStorageType.Default(isLight = true)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.LightMode,
                                contentDescription = stringResource(R.string.load_default_light_theme)
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.load_default_dark_theme)) },
                        onClick = { loadSavedTheme(ThemeStorageType.Default(isLight = false)) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.DarkMode,
                                contentDescription = stringResource(R.string.load_default_dark_theme)
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(text = stringResource(R.string.load_theme_from_file)) },
                        onClick = { showClearBeforeLoadDialog(); showMenu = false },
                        leadingIcon = {
                            Icon(
                                Icons.Default.UploadFile,
                                contentDescription = stringResource(R.string.load_theme_from_file)
                            )
                        }
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
    mappedValues: List<UiElementColorData>,
    changeValue: (String, String, Color) -> Unit,
    hideSearchbar: () -> Unit,
    paletteState: PaletteState,
    modifier: Modifier = Modifier
) {
    var fullscreen by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val searchQueryIsEmpty by remember { derivedStateOf { searchQuery == "" } }
    val searchedThings = mappedValues.filter {
        it.name.contains(searchQuery, true)
                ||
                it.colorToken.contains(searchQuery, true)
    }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(300) // feels nicer
        focusRequester.requestFocus()
    }

    // clears search when back button is pressed.
    // its here because it doesnt work if i put it
    // beside the other back handler at the top
    // of this composable
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = searchQuery,
                onQueryChange = {
                    searchQuery = it
                    fullscreen = true
                },
                onSearch = { }, // it desperately wants me to keep this line
                expanded = fullscreen,
                onExpandedChange = { },
                enabled = true,
                placeholder = { Text(text = stringResource(R.string.search_in_current_theme)) },
                leadingIcon = { Icon(Icons.Default.Search, stringResource(R.string.search_title)) },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = searchQueryIsEmpty && !fullscreen,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(onClick = { hideSearchbar() }) {
                            Icon(Icons.Default.ArrowUpward, stringResource(R.string.hide_searchbar_action))
                        }
                    }

                    AnimatedVisibility(
                        visible = !searchQueryIsEmpty,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, stringResource(R.string.clear_search_action))
                        }
                    }
                },
                interactionSource = null,
                modifier = Modifier.focusRequester(focusRequester)
            )
        },
        expanded = fullscreen,
        onExpandedChange = { },
        modifier = modifier,
        content = {
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
                            stringResource(R.string.search_is_empty_tap_this_to_close_notice),
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
                            bottom = 4.dp + WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding()
                        ),
                        verticalArrangement = spacedBy(4.dp)
                    ) {
                        itemsIndexed(searchedThings) { index, uiElementData ->
                            ElementColorItem(
                                Modifier
                                    .padding(horizontal = 16.dp)
                                    .animateItem(),
                                paletteState = paletteState,
                                uiElementData = uiElementData,
                                index = index,
                                changeValue = changeValue,
                                lastIndexInList = mappedValues.lastIndex
                            )
                        }
                    }
                }
            }
        },
    )
}