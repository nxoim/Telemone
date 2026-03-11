package com.number869.telemone.ui.screens.main

import com.number869.telemone.R
import com.number869.telemone.data.StockThemeUpdateManager
import com.number869.telemone.data.ThemeManager
import com.number869.telemone.ui.screens.common.ThemeExporter
import com.number869.telemone.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainModel(
    private val themeManager: ThemeManager,
    private val stockThemeUpdateManager: StockThemeUpdateManager,
    val navigation: MainNavigation,
    private val themeExporter: ThemeExporter,
    private val coroutineScope: CoroutineScope
) {
    val canLightBeUpdated = stockThemeUpdateManager.lightThemeUpdateAvailable
        .stateIn(coroutineScope, WhileSubscribed(), null)

    val canDarkBeUpdated = stockThemeUpdateManager.darkThemeUpdateAvailable
        .stateIn(coroutineScope, WhileSubscribed(), null)

    val shouldDisplayLightUpdateDialog = stockThemeUpdateManager.shouldPromptLightUpdate
        .stateIn(coroutineScope, WhileSubscribed(), null)

    val shouldDisplayDarkUpdateDialog = stockThemeUpdateManager.shouldPromptDarkUpdate
        .stateIn(coroutineScope, WhileSubscribed(), null)

    fun exportDefaultTheme(light: Boolean) = coroutineScope.launch {
        val (fileName, content) = themeManager.packageDefaultTheme(light)
        themeExporter.export(fileName, content)
    }

    fun acceptThemeUpdate(light: Boolean) = coroutineScope.launch {
        stockThemeUpdateManager.acceptThemeUpdate(light)
        showToast(R.string.default_theme_updated)
    }

    fun declineThemeUpdate(light: Boolean) = coroutineScope.launch {
        stockThemeUpdateManager.declineThemeUpdate(light)
    }
}

interface MainNavigation {
    fun navigateToEditor()
    fun navigateToAbout()
    fun navigateBack()
}
