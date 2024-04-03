package com.number869.telemone.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.service.controls.ControlsProviderService
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.FileProvider
import androidx.core.text.isDigitsOnly
import com.number869.telemone.shared.utils.ThemeColorDataType
import com.number869.telemone.shared.utils.ThemeStorageType
import com.number869.telemone.shared.utils.getColorTokenFromColorValue
import com.number869.telemone.shared.utils.getColorValueFromColorToken
import com.number869.telemone.shared.utils.incompatibleUiElementColorData
import com.number869.telemone.shared.utils.stringify
import com.number869.telemone.ui.theme.PaletteState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

class ThemeManager(
    private val themeRepository: ThemeRepository,
    private val paletteState: PaletteState,
    private val context: Context
) {
    private val scope = CoroutineScope(Dispatchers.IO)
    // god bless your eyes and brain that has to process this
    // color in the list has to be int because Color() returns ulong
    // anyway and so loading a theme after restarting the app causes a
    // crash.
    // don't ask me why i don't keep ulong and use ints instead
    val themeList = themeRepository.getAllThemes()

    private val palette get() = paletteState.entirePaletteAsMap

    private var _mappedValues = mutableStateMapOf<String, UiElementColorData>()
    val mappedValues get() = _mappedValues.toSortedMap()
    var defaultCurrentTheme = mutableStateListOf<UiElementColorData>()
    private var loadedFromFileTheme = mutableStateMapOf<String, UiElementColorData>()

    init {
        println("ThemeManager initialized")
        startupConfigProcess()
        val defaultThemeKey = if (paletteState.isDarkMode)
            defaultDarkThemeUUID
        else
            defaultLightThemeUUID

        defaultCurrentTheme.addAll(getThemeByUUID(defaultThemeKey)!!.values)
    }

    fun saveCurrentTheme() = scope.launch  {
        themeRepository.saveTheme(
            ThemeData(UUID.randomUUID().toString(), mappedValues.values.toList())
        )
    }

    fun deleteTheme(uuid: String) = scope.launch { themeRepository.deleteTheme(uuid) }

    // idk if im dum but i don't think this is able to properly load telegrams
    // stock themes, like "Day".
    private fun loadThemeFromFile(
        uri: Uri,
        clearCurrentTheme: Boolean,
        onSuccess: () -> Unit,
        onIncompatibleValuesFound: () -> Unit,
        onIncompatibleFileType: () -> Unit,
    ) = scope.launch {
        val loadedList = mutableStateMapOf<String, UiElementColorData>()

        runCatching {
            context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { reader ->
                Log.d(ControlsProviderService.TAG, "Loaded Theme: is txt or attheme")

                reader.forEachLine { line ->
                    if (line.contains("=") && line.replace(" ", "") != "") {
                        // remove spaces and split lines into 2 elements
                        line.replace(" ", "").split("=").let { splitLine ->
                            val uiElementName = splitLine[0]
                            val colorEitherValueOrTokenAsString = splitLine[1]

                            // check if the ui component's name is present
                            if (uiElementName.isNotEmpty() && colorEitherValueOrTokenAsString.isNotEmpty()) {
                                val isValueANumber = colorEitherValueOrTokenAsString
                                    .replace("-", "")
                                    .isDigitsOnly()
                                val isValueActuallyAColorToken = paletteState
                                    .allPossibleColorTokensAsList
                                    .contains(colorEitherValueOrTokenAsString)

                                if (isValueANumber) {
                                    val colorValue = Color(colorEitherValueOrTokenAsString.toLong())
                                    // also seeing if colors match the device's
                                    // color scheme
                                    val colorToken = getColorTokenFromColorValue(colorValue, paletteState.entirePaletteAsMap)

                                    loadedList[uiElementName] = UiElementColorData(
                                        uiElementName,
                                        colorToken,
                                        colorEitherValueOrTokenAsString.toInt()
                                    )
                                } else if (isValueActuallyAColorToken) {
                                    // checks if the contents are like "uiElelemnt=neutral_80
                                    val colorToken = colorEitherValueOrTokenAsString
                                    // use device's color scheme when loading
                                    // telemone format themes
                                    val colorValue = getColorValueFromColorToken(colorToken, paletteState.entirePaletteAsMap)

                                    loadedList[uiElementName] = UiElementColorData(
                                        uiElementName,
                                        colorToken,
                                        colorValue.toArgb()
                                    )
                                } else {
                                    loadedList[uiElementName] = incompatibleUiElementColorData(uiElementName)

                                    throw IncompatibleValuesException()
                                }
                            } else {
                                throw IncompatibleFileTypeException()
                            }
                        }
                    }
                }
            }
        }.onSuccess {
            loadedFromFileTheme.clear()
            if (clearCurrentTheme) _mappedValues.clear()

            for ((sourceFallbackUiElement, targetFallbackUiElement) in fallbackKeys.asSequence()) {
                if (sourceFallbackUiElement !in _mappedValues) {
                    //	Find the key in _mappedValues that matches the value in
                    // fallbackKeys
                    val sourceColorDataForFallback = _mappedValues[targetFallbackUiElement]
                    //	If there is a match, clone the key-value pair from
                    // _mappedValues and add it with the new key
                    if (sourceColorDataForFallback != null) {
                        _mappedValues[targetFallbackUiElement] =
                            sourceColorDataForFallback.copy(name = targetFallbackUiElement)
                    }
                }
            }

            loadedFromFileTheme.clear()
            if (clearCurrentTheme) {
                _mappedValues.putAll(loadedList)
            } else {
                val newUiElements = loadedList
                    .asSequence()
                    .filter { loadedElement -> loadedElement.key !in _mappedValues }
                    .associate { it.key to it.value }

                _mappedValues.putAll(newUiElements)
            }
            loadedFromFileTheme.putAll(loadedList)

            onSuccess()
        }.onFailure { exception ->
            when (exception) {
                is IncompatibleValuesException -> onIncompatibleValuesFound()
                is IncompatibleFileTypeException -> onIncompatibleFileType()
                else -> Log.e(ControlsProviderService.TAG, "Error loading theme", exception)
            }
        }
    }

    fun resetCurrentTheme() = scope.launch(Dispatchers.Default) {
        _mappedValues.clear()
        _mappedValues.putAll(defaultCurrentTheme.associateBy { it.name })
        loadedFromFileTheme.clear()
    }

    fun overwriteTheme(uuid: String, isLightTheme: Boolean) = scope.launch {
        val targetThemeId = if (isLightTheme) defaultLightThemeUUID else defaultDarkThemeUUID
        val newDefaultTheme = themeRepository.getThemeByUUID(uuid) ?: error(
            "Upon overwriteTheme() theme did not exist in themeList"
        )

        themeRepository.saveTheme(ThemeData(targetThemeId, newDefaultTheme.values))
    }

    fun changeValue(key: String, colorToken: String, colorValue: Color) {
        // lets not use getColorTokenFromColorValue() to not run a loop
        // each time
        _mappedValues[key] = UiElementColorData(key, colorToken, colorValue.toArgb())
    }

    fun exportTheme(uuid: String, dataType: ThemeColorDataType) = scope.launch {
        val theme = themeRepository.getThemeByUUID(uuid)!!.values.stringify(dataType)
        val fileName = if (dataType == ThemeColorDataType.ColorTokens)
            "Telemone Export (Telemone Format).attheme"
        else
            "Telemone Export.attheme"

        with(context) {
            File(cacheDir, fileName).writeText(theme)

            val uri = FileProvider.getUriForFile(
                this,
                "${packageName}.provider",
                File(cacheDir, fileName)
            )

            val intent = Intent(Intent.ACTION_SEND)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "*/attheme"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(intent, fileName))
        }
    }

    private fun loadSavedThemeByUuid(
        uuid: String,
        loadSystemsColorsUsingTokens: Boolean,
        clearCurrentTheme: Boolean
    ) = scope.launch {
        val loadedTheme = themeRepository.getThemeByUUID(uuid) ?: error(
            "Theme was not available upon loading using loadSavedThemeByUuid()"
        )
        var themeData = loadedTheme.values
        val uiElementDataToAdd = themeData.asSequence().filter { it.name !in _mappedValues }

        if (loadSystemsColorsUsingTokens) themeData = themeData.map {
            it.copy(
                colorValue = getColorValueFromColorToken(it.colorToken, palette).toArgb()
            )
        }
        if (clearCurrentTheme) {
            _mappedValues.clear()
            loadedFromFileTheme.clear()
        }

        _mappedValues.putAll(uiElementDataToAdd.associateBy { it.name })
        loadedFromFileTheme.clear()
    }

    fun loadSavedTheme(
        storedTheme: ThemeStorageType,
        onSuccess: (storageTypeText: String, appearanceTypeText: String) -> Unit,
        onIncompatibleValuesFound: () -> Unit,
        onIncompatibleFileType: () -> Unit
    ) = scope.launch {
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
            else -> "" // empty
        }

        fun loadDefaultOrStockTheme() {
            _mappedValues.clear()
            when(storedTheme) {
                is ThemeStorageType.Default -> {
                    _mappedValues.putAll(
                        themeRepository.getThemeByUUID(
                            if (storedTheme.isLight)
                                defaultLightThemeUUID
                            else
                                defaultDarkThemeUUID
                        )!!.values.associateBy { it.name }
                    )
                }
                is ThemeStorageType.Stock -> {
                    _mappedValues.putAll(
                        themeRepository.getStockTheme(
                            palette,
                            storedTheme.isLight
                        ).values.associateBy { it.name }
                    )
                }
                else -> { }
            }

            loadedFromFileTheme.clear()

            onSuccess(storageTypeText, appearanceTypeText)
        }

        when (storedTheme) {
            is ThemeStorageType.Default,
            is ThemeStorageType.Stock -> loadDefaultOrStockTheme()

            is ThemeStorageType.ByUuid -> {
                loadSavedThemeByUuid(
                    storedTheme.uuid,
                    storedTheme.withTokens,
                    storedTheme.clearCurrentTheme
                )

                onSuccess(storageTypeText, appearanceTypeText)
            }

            is ThemeStorageType.ExternalFile -> loadThemeFromFile(
                storedTheme.uri,
                storedTheme.clearCurrentTheme,
                onSuccess = { onSuccess(storageTypeText, appearanceTypeText) },
                onIncompatibleFileType = onIncompatibleFileType,
                onIncompatibleValuesFound = onIncompatibleValuesFound
            )
        }
    }

    fun exportCustomTheme() = scope.launch {
        val result = _mappedValues.values.toList().stringify(ThemeColorDataType.ColorValues)

        with(context) {
            File(cacheDir, "Telemone Custom.attheme").writeText(result)

            val uri = FileProvider.getUriForFile(
                applicationContext,
                "${packageName}.provider",
                File(cacheDir, "Telemone Custom.attheme")
            )

            val intent = Intent(Intent.ACTION_SEND)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "*/attheme"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            startActivity(Intent.createChooser(intent, "Telemone Custom"))
        }
    }

    private fun startupConfigProcess() {
        val stockLightTheme by lazy {
            themeRepository.getStockTheme(palette = palette, light = true)
        }

        val stockDarkTheme by lazy {
            themeRepository.getStockTheme(palette, light = false)
        }

        if (getThemeByUUID(defaultLightThemeUUID) == null) {
            themeRepository.saveTheme(ThemeData(defaultLightThemeUUID, stockLightTheme.values))
        }

        if (getThemeByUUID(defaultDarkThemeUUID) == null) {
            themeRepository.saveTheme(ThemeData(defaultDarkThemeUUID, stockDarkTheme.values))
        }

        if (paletteState.isDarkMode)
            _mappedValues.putAll(getThemeByUUID(defaultDarkThemeUUID)!!.values.associateBy { it.name })
        else
            _mappedValues.putAll(getThemeByUUID(defaultLightThemeUUID)!!.values.associateBy { it.name })
    }

    fun updateDefaultThemeFromStock(light: Boolean) {
        val targetThemeId = if (light)
            defaultLightThemeUUID
        else
            defaultDarkThemeUUID

        // back the current default theme up before replacing it
        themeRepository.saveTheme(
            ThemeData(
                UUID.randomUUID().toString(),
                getThemeByUUID(targetThemeId)!!.values
            )
        )

        themeRepository.saveTheme(
            ThemeData(
                targetThemeId,
                themeRepository.getStockTheme(palette, light).values
            )
        )
    }

    fun getThemeByUUID(uuid: String) = themeRepository.getThemeByUUID(uuid)
}

private class IncompatibleValuesException : Exception()
private class IncompatibleFileTypeException : Exception()
