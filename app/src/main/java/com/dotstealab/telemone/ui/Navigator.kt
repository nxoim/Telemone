package com.dotstealab.telemone.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
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
	val screenWidth = LocalConfiguration.current.screenWidthDp * 5

	AnimatedNavHost(navController, "MainScreen") {
		composable(
			"MainScreen",
			enterTransition = {
				when (initialState.destination.route) {
					"EditorScreen" -> slideInHorizontally(initialOffsetX = { -screenWidth })
					else -> null
				}
			},
			exitTransition = {
				when (targetState.destination.route) {
					"EditorScreen" -> slideOutHorizontally(targetOffsetX = { -screenWidth })
					else -> null
				}
			}
		) {
			MainScreen(navController, vm)
		}

		composable(
			"EditorScreen",
			enterTransition = {
				when (initialState.destination.route) {
					"MainScreen" -> slideInHorizontally(initialOffsetX = { screenWidth })
					"ThemeContentsScreen" -> slideInHorizontally(initialOffsetX = { -screenWidth })
					else -> null
				}
			},
			exitTransition = {
				when (targetState.destination.route) {
					"ThemeContentsScreen" -> slideOutHorizontally(targetOffsetX = { -screenWidth })
					"MainScreen" -> slideOutHorizontally(targetOffsetX = { screenWidth })
					else -> null
				}
			}
		) {
			EditorScreen(navController, vm)
		}

		composable(
			"ThemeContentsScreen",
			enterTransition = {
				when (initialState.destination.route) {
					"EditorScreen" -> slideInHorizontally(initialOffsetX = { screenWidth })
					else -> null
				}
			},
			exitTransition = {
				when (targetState.destination.route) {
					"EditorScreen" -> slideOutHorizontally(targetOffsetX = { screenWidth })
					else -> null
				}
			}
		) {
			ThemeContentsScreen(vm)
		}
	}
}