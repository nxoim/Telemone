package com.number869.telemone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import com.number869.decomposite.core.common.navigation.NavigationRoot
import com.number869.decomposite.core.common.navigation.navigationRootDataProvider
import com.number869.decomposite.core.common.viewModel.viewModel
import com.number869.telemone.ui.Navigator
import com.number869.telemone.ui.theme.TelemoneTheme
import com.number869.telemone.ui.theme.rememberPaletteState
import com.tencent.mmkv.MMKV

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		MMKV.initialize(this)

		val navigationRootDataProvider = navigationRootDataProvider(defaultComponentContext())

		setContent {
			TelemoneTheme {
				val isDarkMode = isSystemInDarkTheme()
				val paletteState = rememberPaletteState()

				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					NavigationRoot(navigationRootDataProvider) {
						val vm = viewModel { MainViewModel(this@MainActivity, paletteState, isDarkMode) }

						Navigator()
					}
				}
			}
		}
	}
}