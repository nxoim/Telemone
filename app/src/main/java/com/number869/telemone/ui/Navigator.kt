package com.number869.telemone.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.number869.telemone.data.AppSettings
import com.number869.telemone.ui.screens.about.AboutNavigator
import com.number869.telemone.ui.screens.editor.EditorNavigator
import com.number869.telemone.ui.screens.main.MainScreen
import com.number869.telemone.ui.screens.welcome.WelcomeScreen
import com.nxoim.decomposite.core.common.navigation.NavHost
import com.nxoim.decomposite.core.common.navigation.navController
import kotlinx.serialization.Serializable
import javax.annotation.concurrent.Immutable

@Composable
fun Navigator() {
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
}
