package com.number869.telemone.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.number869.seksinavigation.OverlayLayoutState
import com.number869.telemone.MainViewModel
import com.number869.telemone.ui.screens.editor.EditorScreen
import com.number869.telemone.ui.screens.main.MainScreen
import com.number869.telemone.ui.screens.themeValues.ThemeValuesScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigator(navController: NavHostController, vm: MainViewModel, overlayLayoutState: OverlayLayoutState) {
	val screenWidth = LocalConfiguration.current.screenWidthDp / 2
	val easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs = CubicBezierEasing(0.48f,0.19f,0.05f,1.03f)

	AnimatedNavHost(navController, Screens.MainScreen.route) {
		composable(
			Screens.MainScreen.route,
			enterTransition = {
				when (initialState.destination.route) {
					Screens.EditorScreen.route -> slideInHorizontally(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), initialOffsetX = { -screenWidth }) + fadeIn(tween(500, 100))
					else -> null
				}
			},
			exitTransition = {
				when (targetState.destination.route) {
					Screens.EditorScreen.route -> slideOutHorizontally(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), targetOffsetX = { -screenWidth }) + fadeOut(tween(500, 100))
					else -> null
				}
			}
		) {
			MainScreen(navController, vm)
		}

		composable(
			Screens.EditorScreen.route,
			enterTransition = {
				when (initialState.destination.route) {
					Screens.MainScreen.route -> slideInHorizontally(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), initialOffsetX = { screenWidth }) + fadeIn(tween(500, 100))
					Screens.ThemeValuesScreen.route -> slideInHorizontally(initialOffsetX = { -screenWidth }) + fadeIn(tween(500, 100, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs))
					else -> null
				}
			},
			exitTransition = {
				when (targetState.destination.route) {
					Screens.ThemeValuesScreen.route -> slideOutHorizontally(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), targetOffsetX = { -screenWidth }) + fadeOut(tween(500, 100))
					Screens.MainScreen.route -> slideOutHorizontally(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), targetOffsetX = { screenWidth }) + fadeOut(tween(500, 100))
					else -> null
				}
			}
		) {
			EditorScreen(overlayLayoutState, navController, vm)
		}

		composable(
			Screens.ThemeValuesScreen.route,
			enterTransition = {
				when (initialState.destination.route) {
					Screens.EditorScreen.route -> slideInHorizontally(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), initialOffsetX = { screenWidth }) + fadeIn(tween(500, 100))
					else -> null
				}
			},
			exitTransition = {
				when (targetState.destination.route) {
					Screens.EditorScreen.route -> slideOutHorizontally(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), targetOffsetX = { screenWidth }) + fadeOut(tween(500, 100))
					else -> null
				}
			}
		) {
			ThemeValuesScreen(vm)
		}
	}
}

enum class Screens(val route: String) {
	MainScreen("MainScreen"),
	EditorScreen("EditorScreen"),
	ThemeValuesScreen("ThemeValuesScreen")
}