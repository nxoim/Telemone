package com.number869.telemone

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.FileProvider
import androidx.core.text.isDigitsOnly
import com.number869.decomposite.core.common.viewModel.ViewModel
import com.number869.telemone.data.ColorToken
import com.number869.telemone.data.LoadedTheme
import com.number869.telemone.data.ThemeRepository
import com.number869.telemone.data.fallbackKeys
import com.number869.telemone.ui.theme.PaletteState
import java.io.File
import java.util.UUID

@Stable
// funny of you to actually expect some sort of documentation in the
// comments
class MainViewModel(
	private val context: Context,
	private val paletteState: PaletteState,
	isDarkMode: Boolean
) : ViewModel() {
	val themeRepository = ThemeRepository(context)

	// god bless your eyes and brain that has to process this
	// color in the list has to be int because Color() returns ulong
	// anyway and so loading a theme after restarting the app causes a
	// crash.
	// don't ask me why i don't keep ulong and use ints instead
	val themeList get() = themeRepository.themeList
	val palette = paletteState.entirePaletteAsMap.value

	private var _mappedValues: LoadedTheme = mutableStateMapOf()
	val mappedValues get() = _mappedValues
	var defaultCurrentTheme: LoadedTheme = mutableStateMapOf()
	val selectedThemes = mutableStateListOf<String>()
	private var loadedFromFileTheme: LoadedTheme = mutableStateMapOf()

	var themeSelectionToolbarIsVisible by mutableStateOf(false)

	fun colorFromCurrentTheme(colorValueOf: ColorToken /*its a String*/): Color = try {
		mappedValues.getOrElse(colorValueOf) { Pair("", Color.Red) }.second
	} catch (e: NoSuchElementException) {
		Color.Red
	}

	init {
		startupConfigProcess(isDarkMode)
	}

	override fun onDestroy(removeFromViewModelStore: () -> Unit) {
		// do not destroy anything
	}

	fun selectOrUnselectSavedTheme(uuid: String) = if (selectedThemes.contains(uuid))
		selectedThemes.remove(uuid)
	else
		selectedThemes.add(uuid)

	fun selectAllThemes() {
		selectedThemes.clear()
		themeList.forEach {
			val uuid = it.keys.first()

			if (
				!selectedThemes.contains(uuid)
				&&
				uuid != defaultLightThemeUUID
				&&
				uuid != defaultDarkThemeUUID
			) {
				selectedThemes.add(uuid)
			}
		}
	}

	fun unselectAllThemes() = selectedThemes.clear()

	fun deleteSelectedThemes() = selectedThemes.forEach {
		themeRepository.deleteTheme(it)
	}

	fun saveCurrentTheme() {
		val uuid = UUID.randomUUID().toString()
		val map = mutableMapOf<String, Pair<String, Int>>()

		_mappedValues.forEach { (themeTokenName, colorPair) ->
			val colorMap = Pair(colorPair.first, colorPair.second.toArgb())

			map[themeTokenName] = colorMap
		}

		themeRepository.saveTheme(uuid, map)

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

		val loadedMap: LoadedTheme = mutableStateMapOf()
		var containsIncompatibleValues = false
		var isCorrectFormat = false

		context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { reader ->
			Log.d(TAG,"Loaded Theme: is txt or attheme")

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

								loadedMap[uiElementName] = Pair(colorToken, colorValue)
							} else if (isValueActuallyAColorToken) {
								// checks if the contents are like "uiElelemnt=neutral_80
								val colorToken = colorEitherValueOrTokenAsString
								// use device's color scheme when loading
								// telemone format themes
								val colorValue = getColorValueFromColorToken(colorToken, paletteState.entirePaletteAsMap.value)

								loadedMap[uiElementName] = Pair(colorToken, colorValue)
							} else {
								loadedMap[uiElementName] = Pair("INCOMPATIBLE VALUE", Color.Red)
								containsIncompatibleValues = true
							}
						}
					}
				}
			}

			Log.d(TAG, "Loaded Theme: processed")

			if (containsIncompatibleValues) {
				Toast.makeText(
					context,
					"Some colors are incompatible and were marked as such.",
					Toast.LENGTH_LONG
				).show()
			}

			if (isCorrectFormat) {
				if (clearCurrentTheme) _mappedValues.clear()

				for ((key, value) in fallbackKeys) {
					if (!_mappedValues.contains(key)) {
						//	Find the key in _mappedValues that matches the value in
						// fallbackKeys
						val matchedKey = _mappedValues.keys.find { it == value }
						//	If there is a match, clone the key-value pair from
						// _mappedValues and add it with the new key
						if (matchedKey != null) {
							_mappedValues[key] = _mappedValues[matchedKey]!!
						}
					}
				}

				loadedFromFileTheme.clear()
				if (clearCurrentTheme) {
					_mappedValues.putAll(loadedMap)
				} else {
					loadedMap.forEach {
						_mappedValues.putIfAbsent(it.key, it.value)
					}
				}
				loadedFromFileTheme.putAll(loadedMap)

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
		_mappedValues.putAll(defaultCurrentTheme)
		loadedFromFileTheme.clear()

		Toast.makeText(
			context,
			"Reset completed.",
			Toast.LENGTH_LONG
		).show()
	}

	fun overwriteTheme(uuid: String, isLightTheme: Boolean) {
		val targetThemeId = if (isLightTheme) defaultLightThemeUUID else defaultDarkThemeUUID
		val newDefaultTheme = themeRepository.getThemeByUUID(uuid) ?: return

		val index = themeList.indexOfFirst { it.containsKey(targetThemeId) }
		val defaultTheme = if (index != -1) {
			themeList[index].getValue(targetThemeId).toMutableMap()
		} else {
			mutableMapOf()
		}

		newDefaultTheme.forEach { (key, value) ->
			val colorToken = value.first
			val colorValueLoaded = value.second
			val colorValueFromToken = getColorValueFromColorToken(colorToken, paletteState.entirePaletteAsMap.value)

			// this will check if the color tokens name equals one of the
			// supported color tokens prom the palette list. if it does
			// then it will write not the color value thats loaded,
			// but will write a color value that matches the device's
			// current color scheme
			if (paletteState.allPossibleColorTokensAsList.contains(colorToken)) {
				defaultTheme[key] = Pair(colorToken, colorValueFromToken.toArgb())
			} else {
				defaultTheme[key] = Pair(colorToken, colorValueLoaded)
			}
		}

		themeRepository.replaceThemeByUUID(
			uuid = targetThemeId,
			newTheme = defaultTheme
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
		_mappedValues[key] = Pair(colorToken, colorValue)

		Log.d(TAG, "color value replaced at $key with $colorValue")
	}

	fun startupConfigProcess(isDarkMode: Boolean) {
		val defaultThemeKey = if (isDarkMode)
			defaultDarkThemeUUID
		else
			defaultLightThemeUUID

		if (themeList.firstOrNull() { it.containsKey(defaultLightThemeUUID) } == null) {
			val stockLightTheme = themeRepository.getStockTheme(
				palette = paletteState.entirePaletteAsMap.value,
				context = context,
				light = true
			)

			themeRepository.replaceThemeByUUID(defaultLightThemeUUID, stockLightTheme)
		}

		if (themeList.firstOrNull() { it.containsKey(defaultDarkThemeUUID) } == null) {
			val stockDarkTheme = themeRepository.getStockTheme(
				palette = paletteState.entirePaletteAsMap.value,
				context = context,
				light = false
			)

			themeRepository.replaceThemeByUUID(defaultDarkThemeUUID, stockDarkTheme)
		}

		// this will also fill in the missing values
		themeList.find { it.containsKey(defaultThemeKey) }
			?.getValue(defaultThemeKey)?.map {
				val uiItemName = it.key
				val colorToken = it.value.first
				val colorValueAsItWasSaved = Color(it.value.second)
				val colorValueFromToken = getColorValueFromColorToken(
					colorToken,
					paletteState.entirePaletteAsMap.value
				)

				// if color token is something that is in the palette
				// list - load it. if not - load what was saved
				if (paletteState.allPossibleColorTokensAsList.contains(colorToken)) {
					_mappedValues[uiItemName] = Pair(colorToken, colorValueFromToken)
					defaultCurrentTheme[uiItemName] = Pair(colorToken, colorValueFromToken)
				} else {
					_mappedValues[uiItemName] = Pair(colorToken, colorValueAsItWasSaved)
					defaultCurrentTheme[uiItemName] = Pair(colorToken, colorValueAsItWasSaved)
				}
			}

		for ((key, value) in fallbackKeys) {
			if (!_mappedValues.contains(key)) {
				//Find the key in _mappedValues that matches the value in
				// fallbackKeys
				val matchedKey = _mappedValues.keys.find { it == value }
				//If there is a match, clone the key-value pair from
				// _mappedValues and add it with the new key
				if (matchedKey != null) {
					_mappedValues[key] = _mappedValues[matchedKey]!!
					defaultCurrentTheme[key] = _mappedValues[matchedKey]!!
				}
			}
		}

	}

	fun exportTheme(uuid: String, exportDataType: ThemeColorDataType) {
		val theme = getThemeAsStringByUUID(uuid, exportDataType)
		val fileName = if (exportDataType == ThemeColorDataType.ColorTokens)
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
		withTokens: Boolean,
		clearCurrentTheme: Boolean
	) {
		val loadedMap = themeRepository.getThemeByUUID(uuid)

		if (loadedMap != null) {
			if (withTokens) {
				if (clearCurrentTheme) {
					_mappedValues.clear()
					loadedFromFileTheme.clear()
				}
				loadedMap.let {
					it.forEach { colorPair ->
						val colorToken = colorPair.value.first
						val colorValue = getColorValueFromColorToken(colorToken, palette)

						_mappedValues.putIfAbsent(
							colorPair.key,
							Pair(colorToken, colorValue)
						)
					}
				}
			} else {
				if (clearCurrentTheme) {
					_mappedValues.clear()
					loadedFromFileTheme.clear()
				}
				loadedMap.let {
					it.forEach { colorPair ->
						_mappedValues.putIfAbsent(
							colorPair.key,
							Pair(colorPair.value.first, Color(colorPair.value.second))
						)
					}
				}
			}
		}

		loadedFromFileTheme.clear()

		Toast.makeText(
			context,
			"Theme loaded successfully.",
			Toast.LENGTH_LONG
		).show()
	}

	fun loadSavedTheme(storedTheme: ThemeStorageType) {
		_mappedValues.clear()

		fun loadTheme(themeMap: Map<String, Pair<String, Int>>) {
			themeMap.map {
				val uiItemName = it.key
				val colorToken = it.value.first
				val colorValue = if (storedTheme is ThemeStorageType.Stock)
					Color(it.value.second)
				else
					getColorValueFromColorToken(colorToken, palette)

				_mappedValues[uiItemName] = Pair(colorToken, colorValue)
				defaultCurrentTheme[uiItemName] = Pair(colorToken, colorValue)
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
			is ThemeStorageType.Default -> {
				val defaultThemeUuid = if (storedTheme.isLight)
					defaultLightThemeUUID
				else
					defaultDarkThemeUUID

				loadTheme(themeRepository.getThemeByUUID(defaultThemeUuid)!!)
			}

			is ThemeStorageType.Stock -> loadTheme(
				themeRepository.getStockTheme(palette, context, storedTheme.isLight)
			)

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

	fun updateDefaultThemeFromStock(light: Boolean) {
		val targetThemeId = if (light)
			defaultLightThemeUUID
		else
			defaultDarkThemeUUID

		val stockTheme = themeRepository.getStockTheme(palette, context, light)
		val currentDefaultTheme = themeRepository.getThemeByUUID(targetThemeId)!!
		val newDefaultTheme = currentDefaultTheme.toMutableMap()

		// back the theme up before doing anything
		themeRepository.saveTheme(UUID.randomUUID().toString(), currentDefaultTheme)

		stockTheme.forEach { (key, value) ->
			val colorToken = value.first
			val colorValueLoaded = value.second
			val colorValueFromToken = getColorValueFromColorToken(colorToken, paletteState.entirePaletteAsMap.value)

			// this will check if the color tokens name equals one of the
			// supported color tokens from the palette list. if it does
			// then it will write not the color value thats loaded,
			// but a color value that matches the devices current color scheme
			if (paletteState.allPossibleColorTokensAsList.contains(colorToken)) {
				newDefaultTheme[key] = Pair(colorToken, colorValueFromToken.toArgb())
			} else {
				newDefaultTheme[key] = Pair(colorToken, colorValueLoaded)
			}
		}

		themeRepository.replaceThemeByUUID(targetThemeId, newDefaultTheme)

		val themeType = if (light) "light" else "dark"
		Toast.makeText(
			context,
			"Default $themeType theme has been overwritten successfully.",
			Toast.LENGTH_LONG
		).show()
	}

	fun exportTheme(light: Boolean) {
		val targetThemeId = if (light) defaultLightThemeUUID else defaultDarkThemeUUID
		val themeName = if (light) "Telemone Light" else "Telemone Dark"
		val theme = getThemeAsStringByUUID(
			targetThemeId,
			ThemeColorDataType.ColorValuesFromDevicesColorScheme,
		)

		with(context) {
			File(cacheDir, "$themeName.attheme").writeText(theme)

			val uri = FileProvider.getUriForFile(
				applicationContext,
				"${packageName}.provider",
				File(cacheDir, "$themeName.attheme")
			)

			val intent = Intent(Intent.ACTION_SEND)
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			intent.type = "*/attheme"
			intent.putExtra(Intent.EXTRA_STREAM, uri)
			startActivity(Intent.createChooser(intent, themeName))
		}
	}

	fun exportCustomTheme() {
		val map = _mappedValues.mapValues {
			it.value.second.toArgb()
		}.entries.joinToString("\n")
		val result = "${
			map.replace(")", "")
				.replace("(", "")
				.replace(", ", "=")
		}\n"

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

	private fun getThemeAsStringByUUID(
		uuid: String,
		loadThemeUsing: ThemeColorDataType = ThemeColorDataType.ColorValues,
	): String {
		val chosenTheme = themeList.find { it.containsKey(uuid) }
			?.getValue(uuid)
			?.mapValues {
				when (loadThemeUsing) {
					ThemeColorDataType.ColorValues -> Color(it.value.second).toArgb()
					ThemeColorDataType.ColorTokens -> it.value.first
					ThemeColorDataType.ColorValuesFromDevicesColorScheme -> {
						getColorValueFromColorToken(it.value.first, palette).toArgb()
					}
				}
			}
			?.toList()?.sortedBy { it.first }?.joinToString("\n")

		val themeAsString = "${
			chosenTheme?.replace(")", "")
				?.replace("(", "")
				?.replace(", ", "=")
		}\n"

		return themeAsString
	}

	fun hideThemeSelectionModeToolbar() {
		themeSelectionToolbarIsVisible = false
	}

	fun toggleThemeSelectionModeToolbar() {
		themeSelectionToolbarIsVisible = !themeSelectionToolbarIsVisible
	}
}

sealed interface ThemeStorageType {
	data class Default(val isLight: Boolean) : ThemeStorageType
	data class Stock(val isLight: Boolean) : ThemeStorageType
	data class ByUuid(
		val uuid: String,
		val withTokens: Boolean,
		val clearCurrentTheme: Boolean
	) : ThemeStorageType
	data class ExternalFile(val uri: Uri, val clearCurrentTheme: Boolean) : ThemeStorageType
}

const val defaultLightThemeUUID = "defaultLightThemeUUID"
const val defaultDarkThemeUUID = "defaultDarkThemeUUID"

fun getColorValueFromColorToken(tokenToLookFor: String, palette: Map<String, Color>): Color {
	return if (palette.containsKey(tokenToLookFor))
		palette.getValue(tokenToLookFor)
	else
		Color.Red
}

fun getColorTokenFromColorValue(valueToLookFor: Color, palette: Map<String, Color>): String {
	val tokenIndex = palette.values.indexOf(valueToLookFor)

	return if (palette.containsValue(valueToLookFor))
		palette.keys.elementAt(tokenIndex)
	else
		""
}

enum class ThemeColorDataType {
	ColorValues,
	ColorTokens,
	ColorValuesFromDevicesColorScheme
}