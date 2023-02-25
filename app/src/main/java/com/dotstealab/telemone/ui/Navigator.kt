package com.dotstealab.telemone.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.dotstealab.telemone.MainViewModel
import com.dotstealab.telemone.ui.screens.editor.EditorScreen
import com.dotstealab.telemone.ui.screens.main.MainScreen
import com.dotstealab.telemone.ui.screens.themeContents.ThemeContentsScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigator(navController: NavHostController, vm: MainViewModel) {
	AnimatedNavHost(
		navController = navController,
		startDestination = "MainScreen"
	) {
		composable("MainScreen") {
			MainScreen(navController, vm)
		}
		composable("EditorScreen") {
			EditorScreen(navController, vm)
		}
		composable("ThemeContentsScreen") {
			ThemeContentsScreen(vm)
		}
	}
}