package com.number869.telemone

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.coroutineScope
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.number869.telemone.data.AppSettings
import com.number869.telemone.data.SettingsManager
import com.number869.telemone.data.ThemeManager
import com.number869.telemone.data.ThemeRepository
import com.number869.telemone.data.settingsManagerInitializer
import com.number869.telemone.data.themeRepositoryInitializer
import com.number869.telemone.ui.Navigator
import com.number869.telemone.ui.theme.PaletteState
import com.number869.telemone.ui.theme.TelemoneTheme
import com.number869.telemone.ui.theme.createPaletteState
import com.nxoim.decomposite.core.android.navigation.NavigationRootProvider
import com.nxoim.decomposite.core.android.navigation.defaultNavigationRootData
import com.nxoim.decomposite.core.common.navigation.BackGestureProviderContainer
import io.github.sudarshanmhasrup.localina.api.LocalinaApp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
	@OptIn(ExperimentalDecomposeApi::class)
	@SuppressLint("RememberReturnType")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

        if (!App.hasInitialized) lifecycle.coroutineScope.launch {
            App.initialize()
        }

		installSplashScreen().setKeepOnScreenCondition {
            !App.hasInitialized
        }

		val navigationRootDataProvider = defaultNavigationRootData()

		setContent {
            LocalinaApp {
                TelemoneTheme {
                    BackGestureProviderContainer(
                        navigationRootDataProvider.defaultComponentContext,
                        blockChildDragInputs = false,
                        edgeWidth = null
                    ) {
                        Surface(modifier = Modifier.fillMaxSize()) {
                            NavigationRootProvider(navigationRootDataProvider) {
                                Navigator(App.themeManager, AppSettings.Companion)
                            }
                        }
                    }
                }
            }
		}
	}
}
@SuppressLint("StaticFieldLeak")
class App() : Application() {
    companion object {
        var hasInitialized by mutableStateOf(false)
            private set
        lateinit var context: Context
            private set
        lateinit var themeRepository: ThemeRepository
            private set
        lateinit var settingsManager: SettingsManager
            private set
        lateinit var paletteState: PaletteState
            private set
        lateinit var themeManager: ThemeManager
            private set

        suspend fun initialize() {
            themeManager.initialize()
            hasInitialized = true
        }
    }

    override fun onCreate() {
        context = applicationContext
        themeRepository = themeRepositoryInitializer(applicationContext)
        settingsManager = settingsManagerInitializer()
        paletteState = createPaletteState(applicationContext)
        themeManager = ThemeManager(
            themeRepository = themeRepository,
            paletteStateAccessor = { paletteState },
            context = applicationContext
        )
        super.onCreate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        paletteState = createPaletteState(applicationContext)
        super.onConfigurationChanged(newConfig)
    }
}
