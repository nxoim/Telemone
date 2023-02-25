package com.dotstealab.telemone.ui.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.dotstealab.telemone.MainViewModel
import com.dotstealab.telemone.ui.screens.main.components.DefaultThemesButtons


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, vm: MainViewModel = viewModel()) {
	Column(Modifier.fillMaxSize()) {
		CenterAlignedTopAppBar(title = { Text(text = "TeleMone") }, windowInsets = WindowInsets.statusBars)

		Column(
			Modifier.padding(24.dp).weight(1f),
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