package com.number869.telemone.data

import android.content.Context
import com.number869.telemone.App

sealed class AppSettings(
    private val settingsManager: SettingsManager
) {
    // incorrect but very convenient at current scale
    companion object : AppSettings(App.settingsManager)

    val agreedToConditions =
        Setting("userAgreedToV1OnWelcomeScreen", false, targetSettingsManager = settingsManager)
    val savedThemeDisplayType =
        Setting("savedThemeItemDisplayType", 1, targetSettingsManager = settingsManager)

    val lastAcceptedStockThemeHashDark = NullableSetting(
        "lastAcceptedStockThemeHashDark",
        String::class,
        targetSettingsManager = settingsManager
    )
    val lastAcceptedStockThemeHashLight = NullableSetting(
        "lastAcceptedStockThemeHashLight",
        String::class,
        targetSettingsManager = settingsManager
    )

    val lastDeclinedStockThemeHashDark =
        Setting("lastDeclinedStockThemeHashDark", "", targetSettingsManager = settingsManager)
    val lastDeclinedStockThemeHashLight =
        Setting("lastDeclinedStockThemeHashLight", "", targetSettingsManager = settingsManager)
}