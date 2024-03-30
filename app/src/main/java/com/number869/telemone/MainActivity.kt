package com.number869.telemone

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import com.number869.decomposite.core.common.navigation.NavigationRoot
import com.number869.decomposite.core.common.navigation.navigationRootDataProvider
import com.number869.decomposite.core.common.viewModel.viewModel
import com.number869.telemone.data.InstanceLocator
import com.number869.telemone.data.ThemeRepository
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
				val paletteState = rememberPaletteState()

				remember {
					// not in a launched effect because this needs to be
					// executed synchronously
					App.instanceLocator.putOrOverwrite<Context>(this@MainActivity)
					App.instanceLocator.getOrPut(
						ThemeRepository(context = App.instanceLocator.get())
					)
					// update palette state on config changes
					App.instanceLocator.putOrOverwrite(paletteState)
				}

				Surface(modifier = Modifier.fillMaxSize()) {
					NavigationRoot(navigationRootDataProvider) {
						val vm = viewModel { MainViewModel() }

						Navigator()
					}
				}
			}
		}
	}
}

class App() : Application() {
	companion object {
		val instanceLocator = InstanceLocator()
	}

	init {
		println("APP CREATED")
	}
}