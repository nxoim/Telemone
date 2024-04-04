package com.number869.telemone.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.number869.telemone.ui.RootDestinations
import com.number869.telemone.ui.screens.main.components.DefaultThemesButtons
import com.number869.telemone.ui.screens.main.components.ThemeUpdateAvailableDialog
import com.nxoim.decomposite.core.common.navigation.NavController
import com.nxoim.decomposite.core.common.navigation.getExistingNavController
import com.nxoim.decomposite.core.common.viewModel.viewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
	navController: NavController<RootDestinations> = getExistingNavController(),
	vm: MainViewModel = viewModel { MainViewModel() }
) {
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
					if (vm.lightThemeCanBeUpdated) {
						DropdownMenuItem(
							text = { Text("Update default light theme") },
							onClick = {
								vm.displayLightThemeUpdateChoiceDialog = true
								showMenu = false
							}
						)
					}

					if (vm.darkThemeCanBeUpdated) {
						DropdownMenuItem(
							text = { Text("Update default dark theme") },
							onClick = {
								vm.displayDarkThemeUpdateChoiceDialog = true
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
				.padding(24.dp)
				.weight(1f),
			verticalArrangement = Arrangement.SpaceAround,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			DefaultThemesButtons(exportTheme = vm::exportDefaultTheme)

			OutlinedButton(onClick = { navController.navigate(RootDestinations.Editor) }) {
				Text(text = "Go to theme editor")
			}
		}
	}

	if (vm.displayLightThemeUpdateChoiceDialog) ThemeUpdateAvailableDialog(
		ofLight = true,
		decline = { vm.declineDefaultThemeUpdate(true) },
		acceptStockThemeUpdate = { vm.acceptTheStockThemeUpdate(true) }
	)

	if (vm.displayDarkThemeUpdateChoiceDialog) ThemeUpdateAvailableDialog(
		ofLight = false,
		decline = { vm.declineDefaultThemeUpdate(false) },
		acceptStockThemeUpdate = { vm.acceptTheStockThemeUpdate(false) }
	)
}