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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.number869.telemone.MainViewModel
import com.number869.telemone.ui.Screens
import com.number869.telemone.ui.screens.main.components.DefaultThemesButtons


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
	navController: NavHostController,
	vm: MainViewModel = viewModel()
) {
	Column(Modifier.fillMaxSize()) {
		MainScreenAppBar(navController)

		Column(
			Modifier
				.padding(24.dp)
				.weight(1f),
			verticalArrangement = Arrangement.SpaceAround,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			DefaultThemesButtons(vm)

			OutlinedButton(onClick = { navController.navigate("EditorScreen") }) {
				Text(text = "Go to theme editor")
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreenAppBar(navController: NavHostController) {
	var showMenu by remember { mutableStateOf(false) }

	CenterAlignedTopAppBar(
		title = { Text(text = "TeleMone") },
		actions = {
			IconButton(onClick = { showMenu = true }) {
				Icon(Icons.Filled.MoreVert, contentDescription = "Menu")
			}

			DropdownMenu(
				expanded = showMenu,
				onDismissRequest = { showMenu = false }
			) {
				DropdownMenuItem(
					text = { Text("About") },
					onClick = {
						navController.navigate(Screens.AboutScreen.route)
						showMenu = false
					}
				)
			}
		}
	)
}