package com.number869.telemone

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import com.number869.telemone.data.InstanceDeclarator
import com.number869.telemone.data.InstanceLocator
import com.number869.telemone.data.ThemeManager
import com.number869.telemone.data.settingsManagerInitializer
import com.number869.telemone.data.themeRepositoryInitializer
import com.number869.telemone.shared.utils.inject
import com.number869.telemone.ui.Navigator
import com.number869.telemone.ui.theme.TelemoneTheme
import com.number869.telemone.ui.theme.rememberPaletteState
import com.nxoim.decomposite.core.common.navigation.NavigationRoot
import com.nxoim.decomposite.core.common.navigation.navigationRootDataProvider
import com.tencent.mmkv.MMKV

class MainActivity : ComponentActivity() {
	@SuppressLint("RememberReturnType")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		MMKV.initialize(this)

		val navigationRootDataProvider = navigationRootDataProvider(defaultComponentContext())

		setContent {
			TelemoneTheme {
				val paletteState = rememberPaletteState()

				InstanceDeclarator {
					// not in a launched effect because this needs to be
					// executed synchronously, before other ui
					single<Context>(cacheInstance = false) { this@MainActivity }
					single { themeRepositoryInitializer(context = inject()) }
					settingsManagerInitializer(context = inject())
					// update palette state on config changes
					single(cacheInstance = false) { paletteState }
					single {
						ThemeManager(
							themeRepository = inject(),
							paletteState = inject(),
							context = inject()
						)
					}
				}

				Surface(modifier = Modifier.fillMaxSize()) {
					NavigationRoot(navigationRootDataProvider) { Navigator() }
				}
			}
		}
	}
}

class App() : Application() {
	companion object { val instanceLocator = InstanceLocator() }
}
