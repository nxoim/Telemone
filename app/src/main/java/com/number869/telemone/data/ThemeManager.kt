package com.number869.telemone.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.text.isDigitsOnly
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import com.number869.telemone.common.ThemeFileSource
import com.number869.telemone.utils.ThemeColorDataType
import com.number869.telemone.utils.ThemeStorageType
import com.number869.telemone.utils.getColorTokenFromColorValue
import com.number869.telemone.utils.getColorValueFromColorToken
import com.number869.telemone.utils.incompatibleUiElementColorData
import com.number869.telemone.utils.stringify
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ThemeManager(
    initialPaletteState: PaletteState,
    private val themeRepository: ThemeRepository,
) {
    private val _paletteState = MutableStateFlow(initialPaletteState)
    val paletteState = _paletteState.asStateFlow()
    private val palette get() = _paletteState.value.entirePaletteAsMap

    private val _mappedValues = MutableStateFlow(mapOf<String, UiElementColorData>())
    val mappedValues = _mappedValues.asStateFlow()
    var defaultCurrentTheme = mutableStateListOf<UiElementColorData>()

    fun updatePalette(new: PaletteState) {
        _paletteState.value = new
        _mappedValues.value = _mappedValues.value.mapValues { (_, uiElementColorData) ->
            uiElementColorData.copy(
                colorValue = getColorValueFromColorToken(
                    uiElementColorData.colorToken,
                    new.entirePaletteAsMap
                ).toArgb()
            )
        }
    }

    fun getThemes(range: IntRange) = themeRepository.getThemesInRange(range)
    fun countSavedThemes() = themeRepository.getThemeCount()
    fun getThemeByUUID(uuid: String) = themeRepository.getThemeByUUID(uuid)

    suspend fun changeValue(key: String, colorToken: String, colorValue: Color) {
        asyncBackUpLastSessionPersistently()
        _mappedValues.value += (key to UiElementColorData(key, colorToken, colorValue.toArgb()))
    }

    suspend fun saveCurrentTheme() {
        themeRepository.saveTheme(
            ThemeData(UUID.randomUUID().toString(), _mappedValues.value.values.toList())
        )
    }

    suspend fun deleteTheme(uuid: String) = themeRepository.deleteTheme(uuid)

    suspend fun overwriteTheme(uuid: String, isLightTheme: Boolean) {
        val targetThemeId = PredefinedTheme.Default(isLightTheme).uuid
        val newDefaultTheme = themeRepository.getThemeByUUID(uuid).first() ?: error("Theme not found")
        themeRepository.saveTheme(ThemeData(targetThemeId, newDefaultTheme.values))
    }

    suspend fun loadSavedThemeForResult(
        storedTheme: ThemeStorageType
    ): Result<ThemeLoadSuccess, ThemeLoadError> {
        asyncBackUpLastSessionPersistently()
        return loadSavedThemeInternal(storedTheme)
    }

    suspend fun updateDefaultThemeFromStock(light: Boolean) {
        val targetThemeId = PredefinedTheme.Default(light).uuid
        themeRepository.saveTheme(
            ThemeData(UUID.randomUUID().toString(), getThemeByUUID(targetThemeId).first()!!.values)
        )
        themeRepository.saveTheme(
            ThemeData(targetThemeId, themeRepository.getStockTheme(palette, light).values)
        )
    }

    suspend fun backUpAndPackageTheme(
        uuid: String,
        dataType: ThemeColorDataType
    ): Pair<String, String> {
        asyncBackUpLastSessionPersistently()
        val theme = themeRepository
            .getThemeByUUID(uuid)
            .first().let { it ?: error("Unable to package theme because it's not in the database") }
            .values
            .stringify(dataType, palette)

        val fileName = if (dataType == ThemeColorDataType.ColorTokens)
            "Telemone Export (Telemone Format).attheme"
        else
            "Telemone Export.attheme"
        return fileName to theme
    }

    suspend fun backUpAndPackageCustomTheme(): Pair<String, String> {
        asyncBackUpLastSessionPersistently()
        val content = _mappedValues.value.values.toList()
            .stringify(ThemeColorDataType.ColorValues, palette)
        return "Telemone Custom.attheme" to content
    }

    suspend fun packageDefaultTheme(light: Boolean): Pair<String, String> {
        val themeName = if (light) "Telemone Light" else "Telemone Dark"
        val content = stringify(
            themeRepository
                .getThemeByUUID(PredefinedTheme.Default(light).uuid)
                .first().let { it ?: error("It shouldn't be possible for default theme not to exist") }
                .values,
            ThemeColorDataType.ColorValuesFromDevicesColorScheme,
            palette
        )
        return "$themeName.attheme" to content
    }

    suspend fun seedDefaultThemes() {
        val stockLightTheme by lazy { themeRepository.getStockTheme(palette, light = true) }
        val stockDarkTheme by lazy { themeRepository.getStockTheme(palette, light = false) }

        if (getThemeByUUID(PredefinedTheme.Default(true).uuid).first() == null)
            themeRepository.saveTheme(
                ThemeData(PredefinedTheme.Default(true).uuid, stockLightTheme.values)
            )

        if (getThemeByUUID(PredefinedTheme.Default(false).uuid).first() == null)
            themeRepository.saveTheme(
                ThemeData(PredefinedTheme.Default(false).uuid, stockDarkTheme.values)
            )
    }

    suspend fun loadStartupTheme() {
        val defaultThemeMatchingSystemDarkMode = getThemeByUUID(
            PredefinedTheme.Default(!_paletteState.value.isDarkMode).uuid
        ).first()

        val startupTheme = getThemeByUUID(PredefinedTheme.LastSession.uuid).first()
            ?: defaultThemeMatchingSystemDarkMode

        _mappedValues.value = startupTheme!!.values.associateBy { it.name }
        defaultCurrentTheme.addAll(defaultThemeMatchingSystemDarkMode!!.values)
    }

    private suspend fun asyncBackUpLastSessionPersistently() = withContext(NonCancellable) {
        launch {
            themeRepository.saveTheme(
                ThemeData(PredefinedTheme.LastSession.uuid, _mappedValues.value.values.toList())
            )
        }
    }

    private suspend fun loadSavedThemeInternal(
        storedTheme: ThemeStorageType
    ): Result<ThemeLoadSuccess, ThemeLoadError> {
        val storageTypeText = when (storedTheme) {
            is ThemeStorageType.Default -> "Default"
            is ThemeStorageType.Stock -> "Stock"
            is ThemeStorageType.ByUuid -> "Saved"
            is ThemeStorageType.ExternalFile -> "External"
        }

        val appearanceTypeText = when (storedTheme) {
            ThemeStorageType.Default(isLight = true),
            ThemeStorageType.Stock(isLight = true) -> "light "

            ThemeStorageType.Default(isLight = false),
            ThemeStorageType.Stock(isLight = false) -> "dark "

            else -> ""
        }

        return when (storedTheme) {
            is ThemeStorageType.Default,
            is ThemeStorageType.Stock -> {
                loadDefaultOrStockTheme(storedTheme)
                Ok(ThemeLoadSuccess(storageTypeText, appearanceTypeText))
            }

            is ThemeStorageType.ByUuid -> {
                loadSavedThemeByUuid(
                    storedTheme.uuid,
                    storedTheme.withTokens,
                    storedTheme.clearCurrentTheme
                )
                Ok(ThemeLoadSuccess(storageTypeText, appearanceTypeText))
            }

            is ThemeStorageType.ExternalFile -> loadThemeFromFile(
                storedTheme.source,
                storedTheme.clearCurrentTheme,
                storageTypeText,
                appearanceTypeText
            )
        }
    }

    private suspend fun loadDefaultOrStockTheme(storedTheme: ThemeStorageType) {
        _mappedValues.value = mapOf()
        when (storedTheme) {
            is ThemeStorageType.Default -> {
                _mappedValues.value = themeRepository
                    .getThemeByUUID(
                        PredefinedTheme.Default(storedTheme.isLight).uuid
                    )
                    .first().let { it ?: error("It shouldn't be possible for default theme not to exist") }
                    .values
                    .associate {
                        it.name to it.copy(
                            colorValue = getColorValueFromColorToken(
                                it.colorToken,
                                palette
                            ).toArgb()
                        )
                    }
            }

            is ThemeStorageType.Stock -> {
                _mappedValues.value = themeRepository.getStockTheme(palette, storedTheme.isLight)
                    .values.associateBy { it.name }
            }

            else -> {}
        }
    }

    private suspend fun loadSavedThemeByUuid(
        uuid: String,
        loadSystemsColorsUsingTokens: Boolean,
        clearCurrentTheme: Boolean
    ) {
        val loadedTheme = themeRepository.getThemeByUUID(uuid).first() ?: error("Theme not found")
        var themeData = loadedTheme.values

        if (loadSystemsColorsUsingTokens) themeData = themeData.map {
            it.copy(colorValue = getColorValueFromColorToken(it.colorToken, palette).toArgb())
        }

        val uiElementDataToAdd = themeData.asSequence().filter { it.name !in _mappedValues.value }

        if (clearCurrentTheme) _mappedValues.value = mapOf()

        _mappedValues.value += uiElementDataToAdd.associateBy { it.name }
    }

    private suspend fun loadThemeFromFile(
        source: ThemeFileSource,
        clearCurrentTheme: Boolean,
        storageTypeText: String,
        appearanceTypeText: String
    ): Result<ThemeLoadSuccess, ThemeLoadError> =
        parseAtTheme(source, _paletteState.value)
            .andThen { loadedList ->
                if (clearCurrentTheme) _mappedValues.value = mapOf()

                fallbackKeys.asSequence().forEach { (source, targetFallback) ->
                    if (source !in _mappedValues.value) {
                        val sourceColorDataForFallback = _mappedValues.value[targetFallback]

                        if (sourceColorDataForFallback != null) {
                            _mappedValues.value += (targetFallback to sourceColorDataForFallback.copy(
                                name = targetFallback
                            ))
                        }
                    }
                }

                if (clearCurrentTheme)
                    _mappedValues.value = loadedList
                else
                    _mappedValues.value += loadedList.filter { it.key !in _mappedValues.value }

                Ok(ThemeLoadSuccess(storageTypeText, appearanceTypeText))
            }

}

data class ThemeLoadSuccess(
    val storageTypeText: String,
    val appearanceTypeText: String
)

sealed interface ThemeLoadError {
    data object IncompatibleValues : ThemeLoadError
    data object IncompatibleFileType : ThemeLoadError
}

fun interface StockThemeHashSource {
    suspend fun getHash(light: Boolean): String
}

interface StockThemeUpdateSettings {
    fun acceptedHashLight(): Flow<String?>
    fun acceptedHashDark(): Flow<String?>
    fun declinedHashLight(): Flow<String?>
    fun declinedHashDark(): Flow<String?>
    suspend fun setAcceptedHash(light: Boolean, hash: String)
    suspend fun setDeclinedHash(light: Boolean, hash: String)
}

private suspend fun parseAtTheme(
    source: ThemeFileSource,
    paletteState: PaletteState
): Result<Map<String, UiElementColorData>, ThemeLoadError> {
    val loadedList = mutableMapOf<String, UiElementColorData>()
    var error: ThemeLoadError? = null

    source.openStream()?.collect { line ->
        if (error != null) return@collect
        if (!line.contains("=") || line.replace(" ", "") == "") return@collect

        line
            .replace(" ", "")
            .split("=")
            .let { splitLine ->
                val uiElementName = splitLine[0]
                val colorEitherValueOrTokenAsString = splitLine[1]

                if (uiElementName.isEmpty() || colorEitherValueOrTokenAsString.isEmpty()) {
                    error = ThemeLoadError.IncompatibleFileType
                    return@collect
                }

                val isValueANumber = colorEitherValueOrTokenAsString
                    .replace("-", "").isDigitsOnly()
                val isValueActuallyAColorToken = paletteState
                    .allPossibleColorTokensAsList
                    .contains(colorEitherValueOrTokenAsString)

                when {
                    isValueANumber -> {
                        val colorValue = Color(colorEitherValueOrTokenAsString.toInt())
                        val colorToken = getColorTokenFromColorValue(
                            colorValue,
                            paletteState.entirePaletteAsMap
                        ) ?: ""
                        loadedList[uiElementName] = UiElementColorData(
                            uiElementName,
                            colorToken,
                            colorEitherValueOrTokenAsString.toInt()
                        )
                    }

                    isValueActuallyAColorToken -> {
                        val colorValue = getColorValueFromColorToken(
                            colorEitherValueOrTokenAsString,
                            paletteState.entirePaletteAsMap
                        )
                        loadedList[uiElementName] = UiElementColorData(
                            uiElementName,
                            colorEitherValueOrTokenAsString,
                            colorValue.toArgb()
                        )
                    }

                    else -> {
                        loadedList[uiElementName] = incompatibleUiElementColorData(uiElementName)
                        error = ThemeLoadError.IncompatibleValues
                    }
                }
            }
    }

    return error?.let { Err(it) } ?: Ok(loadedList)
}