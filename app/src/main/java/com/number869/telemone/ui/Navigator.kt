package com.number869.telemone.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.number869.telemone.data.AppSettings
import com.number869.telemone.ui.screens.about.AboutNavigator
import com.number869.telemone.ui.screens.editor.EditorNavigator
import com.number869.telemone.ui.screens.main.MainScreen
import com.number869.telemone.ui.screens.main.MainViewModel
import com.number869.telemone.ui.screens.main.components.ThemeUpdateAvailableDialog
import com.number869.telemone.ui.screens.welcome.WelcomeScreen
import com.nxoim.decomposite.core.common.navigation.NavHost
import com.nxoim.decomposite.core.common.navigation.navController
import com.nxoim.decomposite.core.common.viewModel.viewModel
import kotlinx.serialization.Serializable
import javax.annotation.concurrent.Immutable

@Composable
fun Navigator() {
	val vm = viewModel { MainViewModel() }
	val preferences = LocalContext.current.getSharedPreferences(
		"AppPreferences.Settings",
		Context.MODE_PRIVATE
	)
	val skipWelcomeScreen = preferences.getBoolean(AppSettings.AgreedToPpAndTos.id, false)
	val startDestination = if (skipWelcomeScreen) RootDestinations.Main else RootDestinations.Welcome

	val mainNavController = navController(startingDestination = startDestination)
	NavHost(mainNavController) {
		when (it) {
			RootDestinations.Welcome -> WelcomeScreen()
			RootDestinations.Main -> MainScreen()
			RootDestinations.Editor -> EditorNavigator()
			RootDestinations.About -> AboutNavigator()

			is RootDestinations.GlobalDialogs.ThemeUpdateAvailable -> {
				ThemeUpdateAvailableDialog(
					ofLight = it.ofLight,
					decline = { vm.declineDefaultThemeUpdate(it.ofLight) }
				) {
					vm.acceptTheStockThemeUpdate(it.ofLight)
				}
			}
		}
	}
}

@Immutable
@Serializable
sealed interface RootDestinations {
	@Serializable
	data object Welcome : RootDestinations

	@Serializable
	data object Main : RootDestinations

	@Serializable
	data object Editor : RootDestinations

	@Serializable
	data object About : RootDestinations

	@Serializable
	sealed interface GlobalDialogs : RootDestinations {
		@Serializable
		data class ThemeUpdateAvailable(val ofLight: Boolean) : GlobalDialogs
	}
}
