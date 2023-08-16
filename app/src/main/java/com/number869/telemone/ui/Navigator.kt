package com.number869.telemone.ui

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.dialog
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.number869.telemone.MainViewModel
import com.number869.telemone.data.AppSettings
import com.number869.telemone.ui.screens.about.AboutScreen
import com.number869.telemone.ui.screens.about.components.PrivacyPolicyDialog
import com.number869.telemone.ui.screens.about.components.TosDialog
import com.number869.telemone.ui.screens.editor.EditorScreen
import com.number869.telemone.ui.screens.main.MainScreen
import com.number869.telemone.ui.screens.themeValues.ThemeValuesScreen
import com.number869.telemone.ui.screens.welcome.WelcomeScreen
import com.number869.telemone.ui.theme.PaletteState

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigator(
	navController: NavHostController,
	vm: MainViewModel,
	paletteState: PaletteState
) {
	val screenWidth = LocalConfiguration.current.screenWidthDp
	val easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs = CubicBezierEasing(0.48f,0.19f,0.05f,1.03f)
	val preferences = LocalContext.current.getSharedPreferences(
		"AppPreferences.Settings",
		Context.MODE_PRIVATE
	)
	val skipWelcomeScreen = preferences.getBoolean(AppSettings.AgreedToPpAndTos.id, false)
	val startDestination = if (skipWelcomeScreen) Screens.MainScreen.route else Screens.WelcomeScreen.route

	AnimatedNavHost(navController, startDestination) {
		composable(Screens.WelcomeScreen.route) {
			WelcomeScreen(navController)
		}

		composable(
			Screens.MainScreen.route,
			enterTransition = {
				when (initialState.destination.route) {
					Screens.EditorScreen.route -> slideInHorizontally(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), initialOffsetX = { -screenWidth }) + fadeIn(tween(300, 100))
					else -> null
				}
			},
			exitTransition = {
				when (targetState.destination.route) {
					Screens.EditorScreen.route -> slideOutHorizontally(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), targetOffsetX = { -screenWidth }) + fadeOut(tween(300, 100))
					else -> null
				}
			}
		) {
			MainScreen(navController, vm, paletteState)
		}

		composable(
			Screens.AboutScreen.route,
			enterTransition = {
				when (initialState.destination.route) {
					Screens.MainScreen.route -> slideInVertically(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), initialOffsetY = { -screenWidth }) + fadeIn(tween(300, 100))
					else -> null
				}
			},
			exitTransition = {
				when (targetState.destination.route) {
					Screens.MainScreen.route -> slideOutVertically(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), targetOffsetY = { -screenWidth }) + fadeOut(tween(300, 100))
					else -> null
				}
			}
		) {
			AboutScreen(navController)
		}

		composable(
			Screens.EditorScreen.route,
			enterTransition = {
				when (initialState.destination.route) {
					Screens.MainScreen.route -> slideInHorizontally(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), initialOffsetX = { screenWidth }) + fadeIn(tween(300, 100))
					Screens.ThemeValuesScreen.route -> slideInHorizontally(initialOffsetX = { -screenWidth }) + fadeIn(tween(300, 100, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs))
					else -> null
				}
			},
			exitTransition = {
				when (targetState.destination.route) {
					Screens.ThemeValuesScreen.route -> slideOutHorizontally(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), targetOffsetX = { -screenWidth }) + fadeOut(tween(300, 100))
					Screens.MainScreen.route -> slideOutHorizontally(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), targetOffsetX = { screenWidth }) + fadeOut(tween(300, 100))
					else -> null
				}
			}
		) {
			EditorScreen(navController, vm, paletteState)
		}

		composable(
			Screens.ThemeValuesScreen.route,
			enterTransition = {
				when (initialState.destination.route) {
					Screens.EditorScreen.route -> slideInHorizontally(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), initialOffsetX = { screenWidth }) + fadeIn(tween(300, 100))
					else -> null
				}
			},
			exitTransition = {
				when (targetState.destination.route) {
					Screens.EditorScreen.route -> slideOutHorizontally(tween(600, easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs), targetOffsetX = { screenWidth }) + fadeOut(tween(300, 100))
					else -> null
				}
			}
		) {
			ThemeValuesScreen(vm)
		}

		dialog(Dialogs.PrivacyPolicyDialog.route) {
			PrivacyPolicyDialog(navController)
		}

		dialog(Dialogs.TosDialog.route) {
			TosDialog(navController)
		}
	}
}

enum class Screens(val route: String) {
	WelcomeScreen("WelcomeScreen"),
	MainScreen("MainScreen"),
	EditorScreen("EditorScreen"),
	ThemeValuesScreen("ThemeValuesScreen"),
	AboutScreen("AboutScreen")
}

enum class Dialogs(val route: String) {
	PrivacyPolicyDialog("PrivacyPolicyDialog"),
	TosDialog("TosDialog")
}