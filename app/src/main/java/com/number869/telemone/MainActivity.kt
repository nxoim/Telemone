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
import com.number869.telemone.data.InstanceLocator
import com.number869.telemone.data.ThemeManager
import com.number869.telemone.data.initializeThemeRepository
import com.number869.telemone.ui.Navigator
import com.number869.telemone.ui.theme.TelemoneTheme
import com.number869.telemone.ui.theme.rememberPaletteState
import com.nxoim.decomposite.core.common.navigation.NavigationRoot
import com.nxoim.decomposite.core.common.navigation.navigationRootDataProvider
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
					// executed synchronously, before other ui
					single<Context>(cacheInstance = false) { this@MainActivity }
//					single<ThemeRepository> { SharedPreferencesThemeRepositoryImpl(inject()) }
					single { initializeThemeRepository(context = inject()) }
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
	companion object {
		val instanceLocator = InstanceLocator()
	}

	init {
		println("APP CREATED")
	}
}

// Utils. Theres not many of them so they are here
inline fun <reified T : Any> single(
	cacheInstance: Boolean = true,
	noinline instanceProvider: () -> T
) = App.instanceLocator.put(cacheInstance, instanceProvider)

inline fun <reified T : Any> inject() = App.instanceLocator.get<T>()
