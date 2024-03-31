package com.number869.telemone.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.service.controls.ControlsProviderService
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.FileProvider
import androidx.core.text.isDigitsOnly
import com.number869.telemone.ui.theme.PaletteState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

class ThemeManager(
    private val themeRepository: ThemeRepository,
    private val paletteState: PaletteState,
    private val context: Context
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    // god bless your eyes and brain that has to process this
    // color in the list has to be int because Color() returns ulong
    // anyway and so loading a theme after restarting the app causes a
    // crash.
    // don't ask me why i don't keep ulong and use ints instead
    private var _themeList by mutableStateOf(themeRepository.getAllThemes().value)
    val themeList get() = _themeList
    private val palette = paletteState.entirePaletteAsMap.value

    private var _mappedValues = mutableStateListOf<UiElementColorData>()
    val mappedValues get() = _mappedValues
    var defaultCurrentTheme = mutableStateListOf<UiElementColorData>()
    private var loadedFromFileTheme = mutableStateListOf<UiElementColorData>()

    init {
        println("ThemeManager initialized")
        startupConfigProcess()
        scope.launch {
            themeRepository.getAllThemes().collectLatest {
                _themeList = it

                val defaultThemeKey = if (paletteState.isDarkMode)
                    defaultDarkThemeUUID
                else
                    defaultLightThemeUUID

                it.find { it.uuid == defaultThemeKey }?.let {
                    if (defaultCurrentTheme.toList() != it.values) {
                        defaultCurrentTheme.clear()
                        defaultCurrentTheme.addAll(it.values)
                    }
                }
            }
        }
    }

    fun saveCurrentTheme() {
        themeRepository.saveTheme(
            ThemeData(
                UUID.randomUUID().toString(),
                mappedValues
            )
        )

        Toast.makeText(
            context,
            "Theme has been saved successfully.",
            Toast.LENGTH_LONG
        ).show()
    }

    fun deleteTheme(uuid: String) {
        themeRepository.deleteTheme(uuid)

        Toast.makeText(
            context,
            "Theme has been deleted successfully.",
            Toast.LENGTH_LONG
        ).show()
    }

    // idk if im dum but i don't think this is able to properly load telegrams
    // stock themes, like "Day".
    fun loadThemeFromFile(uri: Uri, clearCurrentTheme: Boolean) {
        loadedFromFileTheme.clear()

        val loadedList = mutableStateListOf<UiElementColorData>()
        var containsIncompatibleValues = false
        var isCorrectFormat = false

        context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { reader ->
            Log.d(ControlsProviderService.TAG,"Loaded Theme: is txt or attheme")

            reader.forEachLine { line ->
                if (
                    line.contains("=")
                    &&
                    line.replace(" ", "") != ""
                ) {
                    // remove spaces and split lines into 2 elements
                    line.replace(" ", "").split("=").let { splitLine ->
                        val uiElementName = splitLine[0]
                        val colorEitherValueOrTokenAsString = splitLine[1]

                        // check if there is the ui components name is present
                        if (uiElementName.isNotEmpty() && colorEitherValueOrTokenAsString.isNotEmpty()) {
                            isCorrectFormat = true

                            val isValueANumber = colorEitherValueOrTokenAsString.replace("-", "").isDigitsOnly()
                            val isValueActuallyAColorToken = paletteState.allPossibleColorTokensAsList.contains(colorEitherValueOrTokenAsString)

                            if (isValueANumber) {
                                val colorValue = Color(colorEitherValueOrTokenAsString.toLong())
                                // also seeing if colors match the device's
                                // color scheme
                                val colorToken = getColorTokenFromColorValue(colorValue, paletteState.entirePaletteAsMap.value)

                                loadedList.add(
                                    UiElementColorData(
                                        uiElementName,
                                        colorToken,
                                        colorEitherValueOrTokenAsString.toInt()
                                    )
                                )
                            } else if (isValueActuallyAColorToken) {
                                // checks if the contents are like "uiElelemnt=neutral_80
                                val colorToken = colorEitherValueOrTokenAsString
                                // use device's color scheme when loading
                                // telemone format themes
                                val colorValue = getColorValueFromColorToken(colorToken, paletteState.entirePaletteAsMap.value)

                                loadedList.add(
                                    UiElementColorData(
                                        uiElementName,
                                        colorToken,
                                        colorValue.toArgb()
                                    )
                                )
                            } else {
                                loadedList.add(
                                    UiElementColorData(
                                        uiElementName,
                                        "INCOMPATIBLE VALUE",
                                        Color.Red.toArgb()
                                    )
                                )

                                containsIncompatibleValues = true
                            }
                        }
                    }
                }
            }

            Log.d(ControlsProviderService.TAG, "Loaded Theme: processed")

            if (containsIncompatibleValues) {
                Toast.makeText(
                    context,
                    "Some colors are incompatible and were marked as such.",
                    Toast.LENGTH_LONG
                ).show()
            }

            if (isCorrectFormat) {
                if (clearCurrentTheme) _mappedValues.clear()

                for ((sourceFallbackUiElement, targetFallbackUiElement) in fallbackKeys) {
                    if (!_mappedValues.any { it.name == sourceFallbackUiElement }) {
                        //	Find the key in _mappedValues that matches the value in
                        // fallbackKeys
                        val sourceColorDataForFallback = _mappedValues.find { it.name == targetFallbackUiElement }
                        //	If there is a match, clone the key-value pair from
                        // _mappedValues and add it with the new key
                        if (sourceColorDataForFallback != null) {
                            _mappedValues.add(
                                sourceColorDataForFallback.copy(name = targetFallbackUiElement)
                            )
                        }
                    }
                }

                loadedFromFileTheme.clear()
                if (clearCurrentTheme) {
                    _mappedValues.addAll(loadedList)
                } else {
                    val newUiElements = loadedList.filter { loadedElement ->
                        loadedElement.name !in _mappedValues.map { it.name }
                    }
                    _mappedValues.addAll(newUiElements)
                }
                loadedFromFileTheme.addAll(loadedList)

                Toast.makeText(
                    context,
                    "File loaded successfully",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Chosen file isn't a Telegram (not Telegram X) theme.",
                    Toast.LENGTH_LONG
                ).show()
            }

            // reset
            containsIncompatibleValues = false
            isCorrectFormat = false
        }
    }

    fun resetCurrentTheme() {
        _mappedValues.clear()
        _mappedValues.addAll(defaultCurrentTheme)
        loadedFromFileTheme.clear()

        Toast.makeText(
            context,
            "Reset completed.",
            Toast.LENGTH_LONG
        ).show()
    }

    fun overwriteTheme(uuid: String, isLightTheme: Boolean) {
        val targetThemeId = if (isLightTheme) defaultLightThemeUUID else defaultDarkThemeUUID
        val newDefaultTheme = themeRepository.getThemeByUUID(uuid) ?: error(
            "Upon overwriteTheme() theme did not exist in themeList"
        )

        themeRepository.replaceTheme(
            targetThemeId,
            newDefaultTheme.values
        )

        val themeType = if (isLightTheme) "light" else "dark"
        Toast.makeText(
            context,
            "Default $themeType theme has been overwritten successfully.",
            Toast.LENGTH_LONG
        ).show()
    }

    fun changeValue(key: String, colorToken: String, colorValue: Color) {
        // lets not use getColorTokenFromColorValue() to not run a loop
        // each time
        _mappedValues.replaceAll {
            if (it.name == key)
                UiElementColorData(key, colorToken, colorValue.toArgb())
            else
                it
        }

        Log.d(ControlsProviderService.TAG, "color value replaced at $key with $colorValue")
    }

    fun exportTheme(uuid: String, dataType: ThemeColorDataType) {
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
    ) {
        val loadedTheme = themeRepository.getThemeByUUID(uuid) ?: error(
            "Theme was not available upon loading using loadSavedThemeByUuid()"
        )

        var themeData = loadedTheme.values

        if (loadSystemsColorsUsingTokens) themeData = themeData.map {
            it.copy(
                colorValue = getColorValueFromColorToken(it.colorToken, palette).toArgb()
            )
        }

        if (clearCurrentTheme) {
            _mappedValues.clear()
            loadedFromFileTheme.clear()
        }

        val uiElementDataToAdd = loadedTheme.values.filter { it !in _mappedValues }
        _mappedValues.addAll(uiElementDataToAdd)

        loadedFromFileTheme.clear()

        Toast.makeText(
            context,
            "Theme loaded successfully.",
            Toast.LENGTH_LONG
        ).show()
    }

    fun loadSavedTheme(storedTheme: ThemeStorageType) {
        _mappedValues.clear()

        fun loadDefaultOrStockTheme() {
            _mappedValues.clear()
            when(storedTheme) {
                is ThemeStorageType.Default -> {
                    _mappedValues.addAll(
                        themeRepository.getThemeByUUID(
                            if (storedTheme.isLight)
                                defaultLightThemeUUID
                            else
                                defaultDarkThemeUUID
                        )!!.values
                    )
                }
                is ThemeStorageType.Stock -> {
                    _mappedValues.addAll(
                        themeRepository.getStockTheme(
                            palette,
                            storedTheme.isLight
                        ).values
                    )
                }
                else -> { }
            }

            loadedFromFileTheme.clear()

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

            Toast.makeText(
                context,
                "$storageTypeText ${appearanceTypeText}theme has been loaded successfully.",
                Toast.LENGTH_LONG
            ).show()
        }

        when (storedTheme) {
            is ThemeStorageType.Default,
            is ThemeStorageType.Stock -> loadDefaultOrStockTheme()

            is ThemeStorageType.ByUuid -> loadSavedThemeByUuid(
                storedTheme.uuid,
                storedTheme.withTokens,
                storedTheme.clearCurrentTheme
            )

            is ThemeStorageType.ExternalFile -> loadThemeFromFile(
                storedTheme.uri,
                storedTheme.clearCurrentTheme
            )
        }
    }

    fun exportCustomTheme() {
        val result = _mappedValues.stringify(ThemeColorDataType.ColorValues)

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


    @JvmName("stringify2") // cuz compile issue "declaration clash"
    fun stringify(
        source: List<UiElementColorData>,
        using: ThemeColorDataType
    ) = source.stringify(using)

    private fun List<UiElementColorData>.stringify(using: ThemeColorDataType): String {
        val theme = when(using) {
            ThemeColorDataType.ColorValues -> {
                this.sortedBy { it.name }.associate { it.name to it.colorValue.toString() }
            }
            ThemeColorDataType.ColorTokens -> {
                this.sortedBy { it.name }.associate { it.name to it.colorToken }
            }
            ThemeColorDataType.ColorValuesFromDevicesColorScheme -> {
                this
                    .sortedBy { it.name }
                    .associate {
                        it.name to getColorValueFromColorToken(it.colorToken, palette).toArgb().toString()
                    }
            }
        }


        val themeAsString = theme.entries.joinToString("\n")
            .replace(")", "")
            .replace("(", "")
            .replace(", ", "=")

        return "${
            themeAsString
        }\n"
    }

    private fun startupConfigProcess() {
        val stockLightTheme by lazy {
            themeRepository.getStockTheme(
                palette = palette,
                light = true
            )
        }

        val stockDarkTheme by lazy {
            themeRepository.getStockTheme(
                palette,
                light = false
            )
        }

        if (themeList.find { it.uuid == defaultLightThemeUUID } == null) {
            themeRepository.replaceTheme(defaultLightThemeUUID, stockLightTheme.values)
        }

        if (themeList.find { it.uuid == defaultDarkThemeUUID } == null) {
            themeRepository.replaceTheme(defaultDarkThemeUUID, stockDarkTheme.values)
        }

        if (paletteState.isDarkMode)
            _mappedValues.addAll(themeRepository.getThemeByUUID(defaultDarkThemeUUID)!!.values)
        else
            _mappedValues.addAll(themeRepository.getThemeByUUID(defaultLightThemeUUID)!!.values)
    }

    fun updateDefaultThemeFromStock(light: Boolean) {
        val targetThemeId = if (light)
            defaultLightThemeUUID
        else
            defaultDarkThemeUUID

        val stockTheme = themeRepository.getStockTheme(palette, light)
        val currentDefaultTheme = themeRepository.getThemeByUUID(targetThemeId)!!

        // back the current default theme up before replacing it
        themeRepository.saveTheme(
            ThemeData(UUID.randomUUID().toString(), currentDefaultTheme.values)
        )

        themeRepository.replaceTheme(targetThemeId, stockTheme.values)

        val themeType = if (light) "light" else "dark"
        Toast.makeText(
            context,
            "Default $themeType theme has been overwritten successfully.",
            Toast.LENGTH_LONG
        ).show()
    }

    fun getThemeByUUID(uuid: String) = themeRepository.getThemeByUUID(uuid)
}
