@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShortText
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.number869.telemone.R
import com.number869.telemone.shared.utils.ThemeStorageType
import com.number869.telemone.ui.RootDestinations
import com.number869.telemone.ui.screens.editor.EditorDestinations
import com.nxoim.decomposite.core.common.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun EditorTopAppBar(
    topAppBarState: TopAppBarScrollBehavior,
    resetCurrentTheme: () -> Unit,
    loadSavedTheme: (ThemeStorageType) -> Unit,
    editorNavController: NavController<EditorDestinations>,
    dialogsNavController: NavController<EditorDestinations.Dialogs>,
    rootNavController: NavController<RootDestinations>,
) {
    Box(
        Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        TheAppBar(
            showClearBeforeLoadDialog = {
                dialogsNavController.navigate(
                    EditorDestinations.Dialogs.ClearThemeBeforeLoadingFromFile
                )
            },
            resetCurrentTheme = resetCurrentTheme,
            loadSavedTheme = loadSavedTheme,
            topAppBarState = topAppBarState,
            editorNavController = editorNavController,
            rootNavController = rootNavController,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TheAppBar(
    showClearBeforeLoadDialog: () -> Unit,
    resetCurrentTheme: () -> Unit,
    loadSavedTheme: (ThemeStorageType) -> Unit,
    topAppBarState: TopAppBarScrollBehavior,
    editorNavController: NavController<EditorDestinations>,
    rootNavController: NavController<RootDestinations>,
    modifier: Modifier = Modifier
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = { rootNavController.navigateBack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back_label))
            }
        },
        title = { Text(stringResource(R.string.theme_editor)) },
        actions = {
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.options_label))
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