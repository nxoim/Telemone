package com.number869.telemone

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.arkivanov.decompose.defaultComponentContext
import com.number869.telemone.data.StockThemeUpdateManager
import com.number869.telemone.data.ThemeManager
import com.number869.telemone.data.createPaletteState
import com.number869.telemone.data.settingsManagerInitializer
import com.number869.telemone.data.themeRepositoryInitializer
import com.number869.telemone.ui.RootDestinations
import com.number869.telemone.ui.shared.BackGestureProviderContainer
import com.number869.telemone.ui.shared.theme.TelemoneTheme
import io.github.sudarshanmhasrup.localina.api.LocalinaApp
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.dialogs.init
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private var appState by mutableStateOf<AppState>(AppState.Uninitialized)
    private var themeManager: ThemeManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FileKit.init(this)

        installSplashScreen()
            .setKeepOnScreenCondition { appState is AppState.Uninitialized }

        val defaultComponentContext = defaultComponentContext()

        if (appState is AppState.Uninitialized) {
            lifecycleScope.launch {
                val context = this@MainActivity

                val appSettings = AppSettings(settingsManagerInitializer())
                val manager = ThemeManager(
                    initialPaletteState = createPaletteState(context),
                    themeRepository = themeRepositoryInitializer(context)
                )
                val stockThemeUpdateManager = StockThemeUpdateManager(
                    updateSettings = appSettings,
                    stockThemeHashSource = stockThemeHashSource(context),
                    themeManager = manager
                )

                launch { collectToasts(context) }

                manager.seedDefaultThemes()
                stockThemeUpdateManager.initialize()
                manager.loadStartupTheme()
                themeManager = manager

                appState = AppState.Initialized(
                    rootComponent = RootComponent(
                        defaultComponentContext,
                        manager,
                        appSettings,
                        themeExporter(context),
                        themePicker = fileKitThemeFilePicker(),
                        startDestination = if (appSettings.hasAgreedToConditions().first())
                            RootDestinations.Main
                        else
                            RootDestinations.Welcome,
                        linkHandler = customTabsLinkHandler(),
                        buildInfo = BuildInfoImpl(context),
                        stockThemeUpdateManager
                    )
                )
            }
        }

        setContent {
            BackGestureProviderContainer(
                defaultComponentContext,
                blockChildDragInputs = false,
                edgeWidth = null
            ) {
                LocalinaApp {
                    TelemoneTheme {
                        Surface(Modifier.fillMaxSize()) {
                            Crossfade(appState) {
                                if (it is AppState.Initialized) {
                                    RootContent(it.rootComponent)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        themeManager?.updatePalette(createPaletteState(applicationContext))
        super.onConfigurationChanged(newConfig)
    }
}

private sealed interface AppState {
    data object Uninitialized : AppState
    class Initialized(val rootComponent: RootComponent) : AppState
}
