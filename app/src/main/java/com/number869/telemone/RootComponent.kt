package com.number869.telemone

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.router.stack.replaceAll
import com.number869.telemone.data.StockThemeUpdateManager
import com.number869.telemone.data.ThemeManager
import com.number869.telemone.ui.RootDestinations
import com.number869.telemone.ui.screens.about.AboutComponent
import com.number869.telemone.ui.screens.about.BuildInfo
import com.number869.telemone.ui.screens.common.LinkHandler
import com.number869.telemone.ui.screens.common.ThemeExporter
import com.number869.telemone.ui.screens.common.ThemeFilePicker
import com.number869.telemone.ui.screens.editor.EditorComponent
import com.number869.telemone.ui.screens.main.MainModel
import com.number869.telemone.ui.screens.main.MainNavigation
import com.number869.telemone.utils.lifecycledCoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.serializer

class RootComponent(
    private val context: ComponentContext,
    private val themeManager: ThemeManager,
    private val appSettings: AppSettings,
    private val themeExporter: ThemeExporter,
    private val themePicker: ThemeFilePicker,
    private val startDestination: RootDestinations,
    private val linkHandler: LinkHandler,
    private val buildInfo: BuildInfo,
    private val stockThemeUpdateManager: StockThemeUpdateManager
) {
    private val scope = context.lifecycledCoroutineScope()
    val backHandler = context.backHandler
    private val navigation = MainNavigationImpl()

    val stack = context.childStack(
        navigation,
        serializer = serializer(),
        initialStack = { listOf(startDestination) },
        handleBackButton = true,
        childFactory = { destination, componentContext ->
            when (destination) {
                RootDestinations.Welcome -> RootDestinationsInstance.Welcome(
                    onAgree = {
                        scope.launch { appSettings.setAgreedToConditions(true) }
                        navigation.replaceAll(RootDestinations.Main)
                    }
                )

                RootDestinations.Main -> RootDestinationsInstance.Main(
                    MainModel(
                        themeManager,
                        navigation = navigation,
                        themeExporter = themeExporter,
                        coroutineScope = componentContext.lifecycledCoroutineScope(),
                        stockThemeUpdateManager = stockThemeUpdateManager
                    )
                )

                RootDestinations.Editor -> RootDestinationsInstance.Editor(
                    EditorComponent(
                        componentContext,
                        themeManager,
                        themeExporter,
                        themePicker,
                        displayTypeSettings = appSettings
                    )
                )

                RootDestinations.About -> RootDestinationsInstance.About(
                    AboutComponent(
                        componentContext,
                        linkHandler,
                        buildInfo
                    )
                )
            }
        }
    )

    fun navigateBack() = navigation.navigateBack()
}

private class MainNavigationImpl() :
    MainNavigation,
    StackNavigation<RootDestinations> by StackNavigation() {
    override fun navigateToEditor() = pushNew(RootDestinations.Editor)
    override fun navigateToAbout() = pushToFront(RootDestinations.About)
    override fun navigateBack() = pop()
}

sealed interface RootDestinationsInstance {
    class Welcome(val onAgree: () -> Unit) : RootDestinationsInstance
    class Main(val model: MainModel) : RootDestinationsInstance
    class Editor(val component: EditorComponent) : RootDestinationsInstance
    class About(val component: AboutComponent) : RootDestinationsInstance
}

interface OnboardingSettings {
    fun hasAgreedToConditions(): Flow<Boolean>
    suspend fun setAgreedToConditions(agreed: Boolean)
}