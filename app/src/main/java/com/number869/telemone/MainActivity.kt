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
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.number869.telemone.ui.Navigator
import com.number869.telemone.ui.theme.TelemoneTheme
import com.number869.telemone.ui.theme.rememberPaletteState
import com.tencent.mmkv.MMKV

class MainActivity : ComponentActivity() {
	@OptIn(ExperimentalAnimationApi::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		MMKV.initialize(this)

		setContent {
			TelemoneTheme {
				val navController = rememberAnimatedNavController()
				val isDarkMode = isSystemInDarkTheme()
				val paletteState = rememberPaletteState()
				val vm = viewModel<MainViewModel>(
					factory = MainViewModelFactory(application, paletteState)
				)

				LaunchedEffect(Unit) {
					vm.startupConfigProcess(isDarkMode)
				}

				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					Navigator(navController, vm, paletteState)
				}
			}
		}
	}
}