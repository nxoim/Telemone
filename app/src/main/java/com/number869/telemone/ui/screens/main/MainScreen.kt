package com.number869.telemone.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.number869.telemone.App
import com.number869.telemone.ui.RootDestinations
import com.number869.telemone.ui.screens.main.components.DefaultThemesButtons
import com.number869.telemone.ui.screens.main.components.ThemeUpdateAvailableDialog
import com.nxoim.decomposite.core.common.navigation.NavController
import com.nxoim.decomposite.core.common.viewModel.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
	navController: NavController<RootDestinations>,
	vm: MainViewModel
) {
    val context = LocalContext.current
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
					Icon(Icons.Filled.MoreVert, contentDescription = "Menu")
				}

				DropdownMenu(
					expanded = showMenu,
					onDismissRequest = { showMenu = false }
				) {
					if (canThemeBeUpdatedLight) {
						DropdownMenuItem(
							text = { Text("Update default light theme") },
							onClick = {
								userChoseToSeeUpdateLight = true
								showMenu = false
							}
						)
					}

					if (canThemeBeUpdatedDark) {
						DropdownMenuItem(
							text = { Text("Update default dark theme") },
							onClick = {
								userChoseToSeeUpdateDark = true
								showMenu = false
							}
						)
					}

					DropdownMenuItem(
						text = { Text("About") },
						onClick = {
							navController.navigate(RootDestinations.About)
							showMenu = false
						}
					)
				}
			}
		)

		Column(
			Modifier
                .fillMaxSize()
                .padding(24.dp)
                .weight(1f),
			verticalArrangement = Arrangement.SpaceAround,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
            val context = LocalContext.current
			DefaultThemesButtons(exportTheme = { vm.exportDefaultTheme(it, context) })

            TextButton(onClick = { navController.navigate(RootDestinations.Editor) }) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Theme Editor")

                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        Modifier.size(20.dp)
                    )
                }
            }
		}
	}

	if (shouldDisplayUpdateDialogLight || userChoseToSeeUpdateLight)
		ThemeUpdateAvailableDialog(
			ofLight = true,
			decline = {
				vm.declineThemeUpdate(true, context)
				userChoseToSeeUpdateLight = false
			},
			acceptStockThemeUpdate = {
				vm.acceptThemeUpdate(true, context)
				userChoseToSeeUpdateLight = false
			},
		)

	if (shouldDisplayUpdateDialogDark || userChoseToSeeUpdateDark)
		ThemeUpdateAvailableDialog(
			ofLight = false,
			decline = {
				vm.declineThemeUpdate(false, context)
				userChoseToSeeUpdateDark = false
			},
			acceptStockThemeUpdate = {
				vm.acceptThemeUpdate(false, context)
				userChoseToSeeUpdateDark = false
			}
		)
}