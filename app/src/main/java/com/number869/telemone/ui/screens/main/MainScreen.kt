package com.number869.telemone.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.number869.telemone.R
import com.number869.telemone.ui.screens.main.components.DefaultThemesButtons
import com.number869.telemone.ui.screens.main.components.ThemeUpdateAvailableDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    vm: MainModel,
    onNavigateToAbout: () -> Unit,
    onNavigateToEditor: () -> Unit
) {
    var userChoseToSeeUpdateLight by remember { mutableStateOf(false) }
    var userChoseToSeeUpdateDark by remember { mutableStateOf(false) }
    val canThemeBeUpdatedLight by vm.canLightBeUpdated.collectAsStateWithLifecycle()
    val canThemeBeUpdatedDark by vm.canDarkBeUpdated.collectAsStateWithLifecycle()
    val shouldDisplayUpdateDialogLight by vm.shouldDisplayLightUpdateDialog.collectAsStateWithLifecycle()
    val shouldDisplayUpdateDialogDark by vm.shouldDisplayDarkUpdateDialog.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        var showMenu by rememberSaveable { mutableStateOf(false) }

        CenterAlignedTopAppBar(
            title = { Text(text = "Telemone") },
            actions = {
                IconButton(onClick = { showMenu = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.menu))
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    if (canThemeBeUpdatedLight == true) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.update_default_light_theme)) },
                            onClick = {
                                userChoseToSeeUpdateLight = true
                                showMenu = false
                            }
                        )
                    }

                    if (canThemeBeUpdatedDark == true) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.update_default_dark_theme)) },
                            onClick = {
                                userChoseToSeeUpdateDark = true
                                showMenu = false
                            }
                        )
                    }

                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.about_title)) },
                        onClick = {
                            onNavigateToAbout()
                            showMenu = false
                        }
                    )
                }
            }
        )

        FlowRow(
            Modifier
                .fillMaxSize()
                .padding(24.dp)
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            DefaultThemesButtons(exportTheme = { vm.exportDefaultTheme(it) })

            TextButton(onClick = { onNavigateToEditor() }) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = stringResource(R.string.theme_editor))

                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        Modifier.size(20.dp)
                    )
                }
            }
        }
    }

    if (shouldDisplayUpdateDialogLight == true || userChoseToSeeUpdateLight)
        ThemeUpdateAvailableDialog(
            ofLight = true,
            decline = {
                vm.declineThemeUpdate(true)
                userChoseToSeeUpdateLight = false
            },
            acceptStockThemeUpdate = {
                vm.acceptThemeUpdate(true)
                userChoseToSeeUpdateLight = false
            },
        )

    if (shouldDisplayUpdateDialogDark == true || userChoseToSeeUpdateDark)
        ThemeUpdateAvailableDialog(
            ofLight = false,
            decline = {
                vm.declineThemeUpdate(false)
                userChoseToSeeUpdateDark = false
            },
            acceptStockThemeUpdate = {
                vm.acceptThemeUpdate(false)
                userChoseToSeeUpdateDark = false
            }
        )
}