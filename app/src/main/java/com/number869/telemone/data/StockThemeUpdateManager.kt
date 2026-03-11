package com.number869.telemone.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class StockThemeUpdateManager(
    private val updateSettings: StockThemeUpdateSettings,
    private val stockThemeHashSource: StockThemeHashSource,
    private val themeManager: ThemeManager
) {
    val lightThemeUpdateAvailable = updateSettings.acceptedHashLight()
        .map { accepted ->
            val hash = stockThemeHashSource.getHash(light = true)
            accepted.isNullOrEmpty() || hash != accepted
        }

    val darkThemeUpdateAvailable = updateSettings.acceptedHashDark()
        .map { accepted ->
            val hash = stockThemeHashSource.getHash(light = false)
            accepted.isNullOrEmpty() || hash != accepted
        }

    val shouldPromptLightUpdate = updateSettings.declinedHashLight()
        .map { declined ->
            val hash = stockThemeHashSource.getHash(light = true)
            declined != hash
        }

    val shouldPromptDarkUpdate = updateSettings.declinedHashDark()
        .map { declined ->
            val hash = stockThemeHashSource.getHash(light = false)
            declined != hash
        }

    suspend fun acceptThemeUpdate(light: Boolean) {
        themeManager.updateDefaultThemeFromStock(light)
        updateSettings.setAcceptedHash(light, stockThemeHashSource.getHash(light))
    }

    suspend fun declineThemeUpdate(light: Boolean) {
        updateSettings.setDeclinedHash(light, stockThemeHashSource.getHash(light))
    }

    suspend fun initialize() {
        with(updateSettings) {
            if (declinedHashLight().first().isNullOrEmpty())
                setDeclinedHash(light = true, stockThemeHashSource.getHash(light = true))

            if (declinedHashDark().first().isNullOrEmpty())
                setDeclinedHash(light = false, stockThemeHashSource.getHash(light = false))

            if (acceptedHashLight().first().isNullOrEmpty())
                setAcceptedHash(light = true, stockThemeHashSource.getHash(light = true))

            if (acceptedHashDark().first().isNullOrEmpty())
                setAcceptedHash(light = false, stockThemeHashSource.getHash(light = false))
        }
    }
}