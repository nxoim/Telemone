package com.number869.telemone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.number869.seksinavigation.OverlayLayout
import com.number869.seksinavigation.rememberOverlayLayoutState
import com.number869.telemone.ui.screens.main.MainScreen
import com.number869.telemone.ui.theme.TeleMoneTheme
import com.number869.telemone.ui.theme.fullPalette
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
	@OptIn(ExperimentalAnimationApi::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			TeleMoneTheme {
				val navController = rememberAnimatedNavController()
				val isDarkMode = isSystemInDarkTheme()
				val palette = fullPalette()
				val vm: MainViewModel = viewModel()
				val scope = rememberCoroutineScope()
				val overlayLayoutState = rememberOverlayLayoutState()

				LaunchedEffect(Unit) {
					scope.launch {
						vm.startupConfigProcess(palette, isDarkMode, applicationContext)
					}
				}

				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					OverlayLayout(overlayLayoutState, this) {
						MainScreen(navController, vm, overlayLayoutState)
					}
				}
			}
		}
	}
}