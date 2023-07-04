package com.number869.telemone

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.FileProvider
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.number869.telemone.ui.theme.FullPaletteList
import com.number869.telemone.ui.theme.allColorTokensAsList
import java.io.File
import java.util.UUID

// no im not making a data class
typealias ThemeUUID = String
typealias UiElementName = String
typealias ColorToken = String
typealias ColorValue = Int
typealias DataAboutColors = Pair<ColorToken, ColorValue>
typealias UiElementData = Map<UiElementName, DataAboutColors>
typealias Themes = Map<ThemeUUID, UiElementData>
typealias ThemeList = SnapshotStateList<Themes>
typealias LoadedTheme = SnapshotStateMap<String, Pair<String, Color>>


// i will refactor all of this. someday. maybe. probably not.

// funny of you to actually expect some sort of documentation in the
// comments
class MainViewModel(application: Application) : AndroidViewModel(application) {
	private val themeListKey = "AppPreferences.ThemeList"
	private val preferences = application.getSharedPreferences(
		"AppPreferences",
		Context.MODE_PRIVATE
	)

	// god bless your eyes and brain that has to process this
	// color in the list has to be int because Color() returns ulong anyway
	// anyway and so loading a theme after restarting the app causes a
	// crash.
	// don't ask me why i don't keep ulong and use ints instead
	// i recommend you checking out
	// i tried savedStateHandle with state flows and i couldnt figure out
	// how to save the maps
	private var _themeList: ThemeList = mutableStateListOf()
	val themeList: ThemeList get() = _themeList
	private var _mappedValues: LoadedTheme = mutableStateMapOf()
	val mappedValues: LoadedTheme get() = _mappedValues
	var defaultCurrentTheme: LoadedTheme = mutableStateMapOf()
	private var loadedFromFileTheme: LoadedTheme = mutableStateMapOf()

	val savedShadowValues: LoadedTheme = mutableStateMapOf()
	var disableShadows by mutableStateOf(true)

	init {
		val savedThemeList = preferences.getString(themeListKey, "[]")
		val type = object : TypeToken<ThemeList>() {}.type
		val list = Gson().fromJson<ThemeList>(savedThemeList, type)
		_themeList.addAll(
			list.map { map ->
				map.mapValues { entry ->
					entry.value.mapValues { (_, value) -> Pair(value.first, value.second) }
				}
			}
		)
	}

	fun colorFromCurrentTheme(colorValueOf: ColorToken /*its a String*/): Color {
		return try {
			mappedValues.getOrElse(colorValueOf) { Pair("", Color.Red) }.second
		} catch (e: NoSuchElementException) {
			Color.Red
		}
	}

	fun switchShadowsOnOff() {
		disableShadows = !disableShadows

		if (disableShadows) {
			defaultShadowTokens.forEach { (key, value) ->
				_mappedValues[key] = value
			}
		} else {
			savedShadowValues.forEach { (key, value) ->
				_mappedValues[key] = value
			}
		}
	}

	fun saveCurrentTheme(context: Context) {
		val mapWithUuid = mutableMapOf<String, Map<String, Pair<String, Int>>>()
		val uuid = UUID.randomUUID().toString()
		val map = mutableMapOf<String, Pair<String, Int>>()

		_mappedValues.forEach { (themeTokenName, colorPair) ->
			val colorMap = Pair(colorPair.first, colorPair.second.toArgb())

			map[themeTokenName] = colorMap
			mapWithUuid.putIfAbsent(uuid, map)
		}
		_themeList.add(mapWithUuid)

		val contents = Gson().toJson(_themeList)
		preferences.edit().putString(themeListKey, contents).apply()

		Toast.makeText(
			context,
			"Theme has been saved successfully.",
			Toast.LENGTH_LONG
		).show()
	}

	fun deleteTheme(uuid: String, context: Context) {
		val mapToRemove = _themeList.find { it.containsKey(uuid) }
		_themeList.remove(mapToRemove)

		val contents = Gson().toJson(_themeList)
		preferences.edit().clear().putString(themeListKey, contents).apply()

		Toast.makeText(
			context,
			"Theme has been deleted successfully.",
			Toast.LENGTH_LONG
		).show()
	}

	fun loadTheme(
		uuid: String,
		withTokens: Boolean,
		palette: FullPaletteList,
		clearCurrentTheme: Boolean,
		context: Context
	) {
		val loadedMap = _themeList.firstOrNull { it.containsKey(uuid) }?.get(uuid)

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

	// idk if im dum but i don't think this is able to properly load telegrams
	// stock themes, like "Day".
	fun loadThemeFromFile(
		context: Context,
		uri: Uri,
		palette: FullPaletteList,
		clearCurrentTheme: Boolean
	) {
		loadedFromFileTheme.clear()

		val loadedMap: LoadedTheme = mutableStateMapOf()
		var containsIncompatibleValues = false
		var isCorrectFormat = false

		context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { reader ->
			if (
				uri.path.toString().contains(".txt")
				||
				uri.path.toString().contains(".attheme")
			)  {
				reader.forEachLine { line ->
					if (
						line.contains("=")
						&&
						line.replace(" ", "") != ""
					) {
						line.replace(" ", "").split("=").let { splitLine ->
							// check if tere is the ui components name is present
							if (splitLine[0].isNotEmpty()) {
								isCorrectFormat = true

								val uiElementName = splitLine[0]
								val colorValueAsString = splitLine[1]

								val isValueANumber = colorValueAsString.replace("-", "").isDigitsOnly()
								val isValueActuallyAColorToken = allColorTokensAsList.contains(splitLine[1])

								if (isValueANumber) {
									val colorValue = Color(colorValueAsString.toLong())
									val colorToken = getColorTokenFromColorValue(palette, colorValue)

									loadedMap[uiElementName] = Pair(colorToken, colorValue)
								} else if (isValueActuallyAColorToken) {
									// checks if the contents are like "uiElelemnt=neutral_80
									val colorToken = colorValueAsString
									val colorValue = getColorValueFromColorToken(colorToken, palette)

									loadedMap[uiElementName] = Pair(colorToken, colorValue)
								} else {
									loadedMap[uiElementName] = Pair("INCOMPATIBLE VALUE", Color.Red)
									containsIncompatibleValues = true
								}
							}
						}
					}
				}

				if (containsIncompatibleValues) {
					Toast.makeText(
						context,
						"Some colors are incompatible and were marked as such.",
						Toast.LENGTH_LONG
					).show()
				}

				if (isCorrectFormat) {
					if (clearCurrentTheme) _mappedValues.clear()

					// check if has shadows specified
					for ((key, value) in defaultShadowTokens) {
						if (!_mappedValues.contains(key)) {
							_mappedValues[key] = value
							savedShadowValues[key] = value
						}
					}

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

					if (disableShadows) {
						defaultShadowTokens.forEach { (key, value) ->
							_mappedValues[key] = value
						}
					} else {
						savedShadowValues.forEach { (key, value) ->
							_mappedValues[key] = value
						}
					}

					loadedFromFileTheme.clear()
					_mappedValues.putAll(loadedMap)
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

	fun resetCurrentTheme(context: Context) {
		_mappedValues.clear()
		_mappedValues.putAll(defaultCurrentTheme)
		loadedFromFileTheme.clear()

		Toast.makeText(
			context,
			"Reset completed.",
			Toast.LENGTH_LONG
		).show()
	}

	fun overwriteDefaultLightTheme(
		uuid: String,
		palette: FullPaletteList,
		context: Context
	) {
		val newDefaultTheme = _themeList.find { it.containsKey(uuid) }?.getValue(uuid) ?: return

		val index = _themeList.indexOfFirst { it.containsKey("defaultLightThemeUUID") }

		val defaultTheme = if (index != -1) {
			_themeList[index].getValue("defaultLightThemeUUID").toMutableMap()
		} else {
			mutableMapOf()
		}

		newDefaultTheme.forEach { (key, value) ->
			val colorToken = value.first
			val colorValueLoaded = value.second
			val colorValueFromToken = getColorValueFromColorToken(colorToken, palette)

			// this will check if the color tokens name equals one of the
			// supported color tokens prom the palette list. if it does
			// then it will write not the color value thats loaded,
			// but will write a color value that matches the device's
			// current color scheme
			if (allColorTokensAsList.contains(colorToken)) {
				defaultTheme[key] = Pair(colorToken, colorValueFromToken.toArgb())
			} else {
				defaultTheme[key] = Pair(colorToken, colorValueLoaded)
			}
		}

		_themeList[index] = mapOf("defaultLightThemeUUID" to defaultTheme)

		val contents = Gson().toJson(_themeList)
		preferences.edit().putString(themeListKey, contents).apply()

		Toast.makeText(
			context,
			"Default light theme has been overwritten successfully.",
			Toast.LENGTH_LONG
		).show()
	}

	fun overwriteDefaultDarkTheme(
		uuid: String,
		palette: FullPaletteList,
		context: Context
	) {
		val newDefaultTheme = _themeList.find { it.containsKey(uuid) }?.getValue(uuid) ?: return

		val index = _themeList.indexOfFirst { it.containsKey("defaultDarkThemeUUID") }

		val defaultTheme = if (index != -1) {
			_themeList[index].getValue("defaultDarkThemeUUID").toMutableMap()
		} else {
			mutableMapOf()
		}

		newDefaultTheme.forEach { (key, value) ->
			val colorToken = value.first
			val colorValueAsItWasLoaded = value.second
			val colorValueFromToken = getColorValueFromColorToken(colorToken, palette)

			if (allColorTokensAsList.contains(colorToken)) {
				defaultTheme[key] = Pair(colorToken, colorValueFromToken.toArgb())
			} else {
				defaultTheme[key] = Pair(colorToken, colorValueAsItWasLoaded)
			}
		}

		_themeList[index] = mapOf("defaultDarkThemeUUID" to defaultTheme)

		val contents = Gson().toJson(_themeList)
		preferences.edit().putString(themeListKey, contents).apply()

		Toast.makeText(
			context,
			"Default dark theme has been overwritten successfully.",
			Toast.LENGTH_LONG
		).show()
	}

	fun changeValue(key: String, colorValue: Color, colorToken: String) {
		// lets not use getColorTokenFromColorValue() to not run a loop
		// each time
		_mappedValues[key] = Pair(colorToken, colorValue)

		Log.d(TAG, "color value replaced at $key with $colorValue")
	}

	fun startupConfigProcess(palette: FullPaletteList, isDarkMode: Boolean, context: Context) {
		val darkTheme = stockDarkTheme(palette, context)
		val lightTheme = stockLightTheme(palette, context)
		val defaultThemeKey = if (isDarkMode)
			"defaultDarkThemeUUID"
		else
			"defaultLightThemeUUID"

		// check if default themes are put in the list
		if (!_themeList.any { it.containsKey("defaultDarkThemeUUID") }) {
			_themeList.add(mapOf("defaultDarkThemeUUID" to darkTheme))
		}
		// dont do if else or when here because it will only check the first one
		if (!_themeList.any { it.containsKey("defaultLightThemeUUID") }) {
			_themeList.add(mapOf("defaultLightThemeUUID" to lightTheme))
		}

		// this will also fill in the missing values
		_themeList.find { it.containsKey(defaultThemeKey) }
			?.getValue(defaultThemeKey)?.map {
				val uiItemName = it.key
				val colorToken = it.value.first
				val colorValueAsItWasSaved = Color(it.value.second)
				val colorValueFromToken = getColorValueFromColorToken(colorToken, palette)

				// if color token is something that is in the palette
				// list - load it. if not - load what was saved
				if (allColorTokensAsList.contains(colorToken)) {
					_mappedValues[uiItemName] = Pair(colorToken, colorValueFromToken)
					defaultCurrentTheme[uiItemName] = Pair(colorToken, colorValueFromToken)
				} else {
					_mappedValues[uiItemName] = Pair(colorToken, colorValueAsItWasSaved)
					defaultCurrentTheme[uiItemName] = Pair(colorToken, colorValueAsItWasSaved)
				}
			}

		// check if has shadows specified
		for ((key, value) in defaultShadowTokens) {
			if (!_mappedValues.contains(key)) {
				_mappedValues[key] = value
				defaultCurrentTheme[key] = value
				savedShadowValues[key] = value
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

		if (disableShadows) {
			defaultShadowTokens.forEach { (key, value) ->
				_mappedValues[key] = value
			}
		} else {
			savedShadowValues.forEach { (key, value) ->
				_mappedValues[key] = value
			}
		}
	}

	fun exportTheme(uuid: String, context: Context) {
		val map = _themeList.find { it.containsKey(uuid) }?.getValue(uuid)
			?.mapValues { it.value.second }
			?.entries?.joinToString("\n")
		val result = "${
			map?.replace(")", "")
				?.replace("(", "")
				?.replace(", ", "=")
		}\n"

		File(context.cacheDir, "Telemone Export.attheme").writeText(result)

		val uri = FileProvider.getUriForFile(
			context,
			"${context.packageName}.provider",
			File(context.cacheDir, "Telemone Export.attheme")
		)

		val intent = Intent(Intent.ACTION_SEND)
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
		intent.type = "*/attheme"
		intent.putExtra(Intent.EXTRA_STREAM, uri)
		context.startActivity(Intent.createChooser(intent, "Telemone Export"))
	}

	fun loadDefaultDarkTheme(palette: FullPaletteList, context: Context) {
		_themeList.find { it.containsKey("defaultDarkThemeUUID") }
			?.getValue("defaultDarkThemeUUID")?.map {
				val uiItemName = it.key
				val colorToken = it.value.first
				val colorValue = getColorValueFromColorToken(colorToken, palette)

				_mappedValues[uiItemName] = Pair(it.value.first, colorValue)
				defaultCurrentTheme.put(uiItemName, Pair(colorToken, colorValue))
			}

		loadedFromFileTheme.clear()

		Toast.makeText(
			context,
			"Default dark theme has been loaded successfully.",
			Toast.LENGTH_LONG
		).show()
	}

	fun loadDefaultLightTheme(palette: FullPaletteList, context: Context) {
		_themeList.find { it.containsKey("defaultLightThemeUUID") }
			?.getValue("defaultLightThemeUUID")?.map {
				val uiItemName = it.key
				val colorToken = it.value.first
				val colorValue = getColorValueFromColorToken(colorToken, palette)

				_mappedValues[uiItemName] = Pair(colorToken, colorValue)
				defaultCurrentTheme.put(uiItemName, Pair(colorToken, colorValue))
			}

		loadedFromFileTheme.clear()

		Toast.makeText(
			context,
			"Default light theme has been loaded successfully.",
			Toast.LENGTH_LONG
		).show()
	}

	fun loadStockDarkTheme(palette: FullPaletteList, context: Context) {
		_mappedValues.clear()

		stockDarkTheme(palette, context).map {
			val uiItemName = it.key
			val colorToken = it.value.first
			val colorValue = Color(it.value.second)

			_mappedValues[uiItemName] = Pair(colorToken, colorValue)
			defaultCurrentTheme[uiItemName] = Pair(colorToken, colorValue)
		}

		loadedFromFileTheme.clear()

		Toast.makeText(
			context,
			"Stock dark theme has been loaded successfully.",
			Toast.LENGTH_LONG
		).show()
	}

	fun loadStockLightTheme(palette: FullPaletteList, context: Context) {
		_mappedValues.clear()

		stockLightTheme(palette, context).map {
			val uiItemName = it.key
			val colorToken = it.value.first
			val colorValue = Color(it.value.second)

			_mappedValues[uiItemName] = Pair(colorToken, colorValue)
			defaultCurrentTheme[uiItemName] = Pair(colorToken, colorValue)
		}

		loadedFromFileTheme.clear()

		Toast.makeText(
			context,
			"Stock light theme has been loaded successfully.",
			Toast.LENGTH_LONG
		).show()
	}

	fun exportCustomTheme(context: Context) {
		val map = _mappedValues.mapValues {
			it.value.second.toArgb()
		}.entries.joinToString("\n")
		val result = "${
			map.replace(")", "")
				.replace("(", "")
				.replace(", ", "=")
		}\n"

		File(context.cacheDir, "Telemone Custom.attheme").writeText(result)

		val uri = FileProvider.getUriForFile(
			context,
			"${context.packageName}.provider",
			File(context.cacheDir, "Telemone Custom.attheme")
		)

		val intent = Intent(Intent.ACTION_SEND)
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
		intent.type = "*/attheme"
		intent.putExtra(Intent.EXTRA_STREAM, uri)
		context.startActivity(Intent.createChooser(intent, "Telemone Custom"))
	}

	fun saveLightTheme(context: Context, palette: FullPaletteList) {
		val source = _themeList.find { it.containsKey("defaultLightThemeUUID") }
			?.getValue("defaultLightThemeUUID")
		// prints ui element name = the color we gave it
		val result = source!!.run {
			this.mapNotNull {
				val colorValue = getColorValueFromColorToken(it.value.first, palette)
				"${it.key}=${colorValue.toArgb()}"
			}
		}.joinToString("\n")

		File(context.cacheDir, "Telemone Light.attheme").writeText(result)

		val uri = FileProvider.getUriForFile(
			context,
			"${context.packageName}.provider",
			File(context.cacheDir, "Telemone Light.attheme")
		)

		val intent = Intent(Intent.ACTION_SEND)
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
		intent.type = "*/attheme"
		intent.putExtra(Intent.EXTRA_STREAM, uri)
		context.startActivity(Intent.createChooser(intent, "Telemone Light"))
	}

	fun saveDarkTheme(context: Context, palette: FullPaletteList) {
		val source = _themeList.find { it.containsKey("defaultDarkThemeUUID") }
			?.getValue("defaultDarkThemeUUID")

		val result = source!!.run {
			this.mapNotNull {
				val colorValue = getColorValueFromColorToken(it.value.first, palette)
				"${it.key}=${colorValue.toArgb()}"
			}
		}.joinToString("\n")

		File(context.cacheDir, "Telemone Dark.attheme").writeText(result)

		val uri = FileProvider.getUriForFile(
			context,
			"${context.packageName}.provider",
			File(context.cacheDir, "Telemone Dark.attheme")
		)

		val intent = Intent(Intent.ACTION_SEND)
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
		intent.type = "*/attheme"
		intent.putExtra(Intent.EXTRA_STREAM, uri)
		context.startActivity(Intent.createChooser(intent, "Telemone Dark"))
	}
}


// at the moment the defaults are kanged from c3r5b8's app.
// at the moment(!!)

// themes in assets folder MUST have values existent in fullPalette()
private fun stockLightTheme(
	palette: FullPaletteList,
	context: Context
): Map<String, Pair<String, Int>> {
	val themeMap = mutableMapOf<String, Pair<String, Int>>()

	context.assets.open("defaultLightFile.attheme")
		.bufferedReader().use { reader ->
			reader.forEachLine { line ->
				if (line.isNotEmpty()) {
					val splitLine = line.replace(" ", "").split("=")
					val key = splitLine[0]
					val value = splitLine[1]

					val colorValue = getColorValueFromColorToken(value, palette)

					themeMap[key] = Pair(value, colorValue.toArgb())
				}
			}
		}

	return themeMap.toMap()
}

// also at least pretend to like these funny little silly haha comments
private fun stockDarkTheme(
	palette: FullPaletteList,
	context: Context
): Map<String, Pair<String, Int>> {
	val themeMap = mutableMapOf<String, Pair<String, Int>>()

	context.assets.open("defaultDarkFile.attheme")
		.bufferedReader().use { reader ->
			reader.forEachLine { line ->
				if (line.isNotEmpty()) {
					val splitLine = line.replace(" ", "").split("=")
					val key = splitLine[0]
					val value = splitLine[1]

					val colorValue = getColorValueFromColorToken(value, palette)

					themeMap[key] = Pair(value, colorValue.toArgb())
				}
			}
		}

	return themeMap.toMap()
}

fun getColorValueFromColorToken(token: String, palette: FullPaletteList): Color {
	return when (token) {
		"primary_light" -> palette.colorRoles.primaryLight
		"on_primary_light" -> palette.colorRoles.onPrimaryLight
		"primary_container_light" -> palette.colorRoles.primaryContainerLight
		"on_primary_container_light" -> palette.colorRoles.onPrimaryContainerLight
		"secondary_light" -> palette.colorRoles.secondaryLight
		"on_secondary_light" -> palette.colorRoles.onSecondaryLight
		"secondary_container_light" -> palette.colorRoles.secondaryContainerLight
		"on_secondary_container_light" -> palette.colorRoles.onSecondaryContainerLight
		"tertiary_light" -> palette.colorRoles.tertiaryLight
		"on_tertiary_light" -> palette.colorRoles.onTertiaryLight
		"tertiary_container_light" -> palette.colorRoles.tertiaryContainerLight
		"on_tertiary_container_light" -> palette.colorRoles.onTertiaryContainerLight
		"surface_light" -> palette.colorRoles.surfaceLight
		"surface_dim_light" -> palette.colorRoles.surfaceDimLight
		"surface_bright_light" -> palette.colorRoles.surfaceBrightLight
		"on_surface_light" -> palette.colorRoles.onSurfaceLight
		"surface_elevation_level_3_light" -> palette.surfaceElevationLevel3Light
		"surface_container_lowest_light" -> palette.colorRoles.surfaceContainerLowestLight
		"surface_container_low_light" -> palette.colorRoles.surfaceContainerLowLight
		"surface_container_light" -> palette.colorRoles.surfaceContainerLight
		"surface_container_high_light" -> palette.colorRoles.surfaceContainerHighLight
		"surface_container_highest_light" -> palette.colorRoles.surfaceContainerHighestLight
		"on_surface_variant_light" -> palette.colorRoles.onSurfaceVariantLight
		"error_light" -> palette.colorRoles.errorLight
		"on_error_light" -> palette.colorRoles.onErrorLight
		"error_container_light" -> palette.colorRoles.errorContainerLight
		"on_error_container_light" -> palette.colorRoles.onErrorContainerLight
		"outline_light" -> palette.colorRoles.outlineLight
		"outline_variant_light" -> palette.colorRoles.outlineVariantLight
		"scrim_light" -> palette.colorRoles.scrimLight
		"primary_dark" -> palette.colorRoles.primaryDark
		"on_primary_dark" -> palette.colorRoles.onPrimaryDark
		"primary_container_dark" -> palette.colorRoles.primaryContainerDark
		"on_primary_container_dark" -> palette.colorRoles.onPrimaryContainerDark
		"secondary_dark" -> palette.colorRoles.secondaryDark
		"on_secondary_dark" -> palette.colorRoles.onSecondaryDark
		"secondary_container_dark" -> palette.colorRoles.secondaryContainerDark
		"on_secondary_container_dark" -> palette.colorRoles.onSecondaryContainerDark
		"tertiary_dark" -> palette.colorRoles.tertiaryDark
		"on_tertiary_dark" -> palette.colorRoles.onTertiaryDark
		"tertiary_container_dark" -> palette.colorRoles.tertiaryContainerDark
		"on_tertiary_container_dark" -> palette.colorRoles.onTertiaryContainerDark
		"surface_dark" -> palette.colorRoles.surfaceDark
		"surface_dim_dark" -> palette.colorRoles.surfaceDimDark
		"surface_bright_dark" -> palette.colorRoles.surfaceBrightDark
		"on_surface_dark" -> palette.colorRoles.onSurfaceDark
		"surface_elevation_level_3_dark" -> palette.surfaceElevationLevel3Dark
		"surface_container_lowest_dark" -> palette.colorRoles.surfaceContainerLowestDark
		"surface_container_low_dark" -> palette.colorRoles.surfaceContainerLowDark
		"surface_container_dark" -> palette.colorRoles.surfaceContainerDark
		"surface_container_high_dark" -> palette.colorRoles.surfaceContainerHighDark
		"surface_container_highest_dark" -> palette.colorRoles.surfaceContainerHighestDark
		"on_surface_variant_dark" -> palette.colorRoles.onSurfaceVariantDark
		"error_dark" -> palette.colorRoles.errorDark
		"on_error_dark" -> palette.colorRoles.onErrorDark
		"error_container_dark" -> palette.colorRoles.errorContainerDark
		"on_error_container_dark" -> palette.colorRoles.onErrorContainerDark
		"outline_dark" -> palette.colorRoles.outlineDark
		"outline_variant_dark" -> palette.colorRoles.outlineVariantDark
		"scrim_dark" -> palette.colorRoles.scrimDark
		"transparent" -> Color.Transparent
		"primary_0" -> palette.primary_0
		"primary_10" -> palette.primary_10
		"primary_20" -> palette.primary_20
		"primary_30" -> palette.primary_30
		"primary_40" -> palette.primary_40
		"primary_50" -> palette.primary_50
		"primary_60" -> palette.primary_60
		"primary_70" -> palette.primary_70
		"primary_80" -> palette.primary_80
		"primary_90" -> palette.primary_90
		"primary_95" -> palette.primary_95
		"primary_99" -> palette.primary_99
		"primary_100" -> palette.primary_100
		"secondary_0" -> palette.secondary_0
		"secondary_10" -> palette.secondary_10
		"secondary_20" -> palette.secondary_20
		"secondary_30" -> palette.secondary_30
		"secondary_40" -> palette.secondary_40
		"secondary_50" -> palette.secondary_50
		"secondary_60" -> palette.secondary_60
		"secondary_70" -> palette.secondary_70
		"secondary_80" -> palette.secondary_80
		"secondary_90" -> palette.secondary_90
		"secondary_95" -> palette.secondary_95
		"secondary_99" -> palette.secondary_99
		"secondary_100" -> palette.secondary_100
		"tertiary_0" -> palette.tertiary_0
		"tertiary_10" -> palette.tertiary_10
		"tertiary_20" -> palette.tertiary_20
		"tertiary_30" -> palette.tertiary_30
		"tertiary_40" -> palette.tertiary_40
		"tertiary_50" -> palette.tertiary_50
		"tertiary_60" -> palette.tertiary_60
		"tertiary_70" -> palette.tertiary_70
		"tertiary_80" -> palette.tertiary_80
		"tertiary_90" -> palette.tertiary_90
		"tertiary_95" -> palette.tertiary_95
		"tertiary_99" -> palette.tertiary_99
		"tertiary_100" -> palette.tertiary_100
		"neutral_0" -> palette.neutral_0
		"neutral_10" -> palette.neutral_10
		"neutral_20" -> palette.neutral_20
		"neutral_30" -> palette.neutral_30
		"neutral_40" -> palette.neutral_40
		"neutral_50" -> palette.neutral_50
		"neutral_60" -> palette.neutral_60
		"neutral_70" -> palette.neutral_70
		"neutral_80" -> palette.neutral_80
		"neutral_90" -> palette.neutral_90
		"neutral_95" -> palette.neutral_95
		"neutral_99" -> palette.neutral_99
		"neutral_100" -> palette.neutral_100
		"neutral_variant_0" -> palette.neutralVariant_0
		"neutral_variant_10" -> palette.neutralVariant_10
		"neutral_variant_20" -> palette.neutralVariant_20
		"neutral_variant_30" -> palette.neutralVariant_30
		"neutral_variant_40" -> palette.neutralVariant_40
		"neutral_variant_50" -> palette.neutralVariant_50
		"neutral_variant_60" -> palette.neutralVariant_60
		"neutral_variant_70" -> palette.neutralVariant_70
		"neutral_variant_80" -> palette.neutralVariant_80
		"neutral_variant_90" -> palette.neutralVariant_90
		"neutral_variant_95" -> palette.neutralVariant_95
		"neutral_variant_99" -> palette.neutralVariant_99
		"neutral_variant_100" -> palette.neutralVariant_100
		"blue_0" -> palette.blue.getValue(0)
		"blue_10" -> palette.blue.getValue(10)
		"blue_20" -> palette.blue.getValue(20)
		"blue_30" -> palette.blue.getValue(30)
		"blue_40" -> palette.blue.getValue(40)
		"blue_50" -> palette.blue.getValue(50)
		"blue_60" -> palette.blue.getValue(60)
		"blue_70" -> palette.blue.getValue(70)
		"blue_80" -> palette.blue.getValue(80)
		"blue_90" -> palette.blue.getValue(90)
		"blue_95" -> palette.blue.getValue(95)
		"blue_99" -> palette.blue.getValue(99)
		"blue_100" -> palette.blue.getValue(100)
		"red_0" -> palette.red.getValue(0)
		"red_10" -> palette.red.getValue(10)
		"red_20" -> palette.red.getValue(20)
		"red_30" -> palette.red.getValue(30)
		"red_40" -> palette.red.getValue(40)
		"red_50" -> palette.red.getValue(50)
		"red_60" -> palette.red.getValue(60)
		"red_70" -> palette.red.getValue(70)
		"red_80" -> palette.red.getValue(80)
		"red_90" -> palette.red.getValue(90)
		"red_95" -> palette.red.getValue(95)
		"red_99" -> palette.red.getValue(99)
		"red_100" -> palette.red.getValue(100)
		"green_0" -> palette.green.getValue(0)
		"green_10" -> palette.green.getValue(10)
		"green_20" -> palette.green.getValue(20)
		"green_30" -> palette.green.getValue(30)
		"green_40" -> palette.green.getValue(40)
		"green_50" -> palette.green.getValue(50)
		"green_60" -> palette.green.getValue(60)
		"green_70" -> palette.green.getValue(70)
		"green_80" -> palette.green.getValue(80)
		"green_90" -> palette.green.getValue(90)
		"green_95" -> palette.green.getValue(95)
		"green_99" -> palette.green.getValue(99)
		"green_100" -> palette.green.getValue(100)
		"orange_0" -> palette.orange.getValue(0)
		"orange_10" -> palette.orange.getValue(10)
		"orange_20" -> palette.orange.getValue(20)
		"orange_30" -> palette.orange.getValue(30)
		"orange_40" -> palette.orange.getValue(40)
		"orange_50" -> palette.orange.getValue(50)
		"orange_60" -> palette.orange.getValue(60)
		"orange_70" -> palette.orange.getValue(70)
		"orange_80" -> palette.orange.getValue(80)
		"orange_90" -> palette.orange.getValue(90)
		"orange_95" -> palette.orange.getValue(95)
		"orange_99" -> palette.orange.getValue(99)
		"orange_100" -> palette.orange.getValue(100)
		"violet_0" -> palette.violet.getValue(0)
		"violet_10" -> palette.violet.getValue(10)
		"violet_20" -> palette.violet.getValue(20)
		"violet_30" -> palette.violet.getValue(30)
		"violet_40" -> palette.violet.getValue(40)
		"violet_50" -> palette.violet.getValue(50)
		"violet_60" -> palette.violet.getValue(60)
		"violet_70" -> palette.violet.getValue(70)
		"violet_80" -> palette.violet.getValue(80)
		"violet_90" -> palette.violet.getValue(90)
		"violet_95" -> palette.violet.getValue(95)
		"violet_99" -> palette.violet.getValue(99)
		"violet_100" -> palette.violet.getValue(100)
		"cyan_0" -> palette.cyan.getValue(0)
		"cyan_10" -> palette.cyan.getValue(10)
		"cyan_20" -> palette.cyan.getValue(20)
		"cyan_30" -> palette.cyan.getValue(30)
		"cyan_40" -> palette.cyan.getValue(40)
		"cyan_50" -> palette.cyan.getValue(50)
		"cyan_60" -> palette.cyan.getValue(60)
		"cyan_70" -> palette.cyan.getValue(70)
		"cyan_80" -> palette.cyan.getValue(80)
		"cyan_90" -> palette.cyan.getValue(90)
		"cyan_95" -> palette.cyan.getValue(95)
		"cyan_99" -> palette.cyan.getValue(99)
		"cyan_100" -> palette.cyan.getValue(100)
		"pink_0" -> palette.pink.getValue(0)
		"pink_10" -> palette.pink.getValue(10)
		"pink_20" -> palette.pink.getValue(20)
		"pink_30" -> palette.pink.getValue(30)
		"pink_40" -> palette.pink.getValue(40)
		"pink_50" -> palette.pink.getValue(50)
		"pink_60" -> palette.pink.getValue(60)
		"pink_70" -> palette.pink.getValue(70)
		"pink_80" -> palette.pink.getValue(80)
		"pink_90" -> palette.pink.getValue(90)
		"pink_95" -> palette.pink.getValue(95)
		"pink_99" -> palette.pink.getValue(99)
		"pink_100" -> palette.pink.getValue(100)
		else -> Color.Red
	}
}

val fallbackKeys = mapOf(
	"chat_inAdminText" to "chat_inTimeText",
	"chat_inAdminSelectedText" to "chat_inTimeSelectedText",
	"player_progressCachedBackground" to "player_progressBackground",
	"chat_inAudioCacheSeekbar" to "chat_inAudioSeekbar",
	"chat_outAudioCacheSeekbar" to "chat_outAudioSeekbar",
	"chat_emojiSearchBackground" to "chat_emojiPanelStickerPackSelector",
	"location_sendLiveLocationIcon" to "location_sendLocationIcon",
	"changephoneinfo_image2" to "featuredStickers_addButton",
	"graySectionText" to "windowBackgroundWhiteGrayText2",
	"chat_inMediaIcon" to "chat_inBubble",
	"chat_outMediaIcon" to "chat_outBubble",
	"chat_inMediaIconSelected" to "chat_inBubbleSelected",
	"chat_outMediaIconSelected" to "chat_outBubbleSelected",
	"chats_actionUnreadIcon" to "profile_actionIcon",
	"chats_actionUnreadBackground" to "profile_actionBackground",
	"chats_actionUnreadPressedBackground" to "profile_actionPressedBackground",
	"dialog_inlineProgressBackground" to "windowBackgroundGray",
	"dialog_inlineProgress" to "chats_menuItemIcon",
	"groupcreate_spanDelete" to "chats_actionIcon",
	"sharedMedia_photoPlaceholder" to "windowBackgroundGray",
	"chat_attachPollBackground" to "chat_attachAudioBackground",
	"chat_attachPollIcon" to "chat_attachAudioIcon",
	"chats_onlineCircle" to "windowBackgroundWhiteBlueText",
	"windowBackgroundWhiteBlueButton" to "windowBackgroundWhiteValueText",
	"windowBackgroundWhiteBlueIcon" to "windowBackgroundWhiteValueText",
	"undo_background" to "chat_gifSaveHintBackground",
	"undo_cancelColor" to "chat_gifSaveHintText",
	"undo_infoColor" to "chat_gifSaveHintText",
	"windowBackgroundUnchecked" to "windowBackgroundWhite",
	"windowBackgroundChecked" to "windowBackgroundWhite",
	"switchTrackBlue" to "switchTrack",
	"switchTrackBlueChecked" to "switchTrackChecked",
	"switchTrackBlueThumb" to "windowBackgroundWhite",
	"switchTrackBlueThumbChecked" to "windowBackgroundWhite",
	"windowBackgroundCheckText" to "windowBackgroundWhite",
	"contextProgressInner4" to "contextProgressInner1",
	"contextProgressOuter4" to "contextProgressOuter1",
	"switchTrackBlueSelector" to "listSelector",
	"switchTrackBlueSelectorChecked" to "listSelector",
	"chat_emojiBottomPanelIcon" to "chat_emojiPanelIcon",
	"chat_emojiSearchIcon" to "chat_emojiPanelIcon",
	"chat_emojiPanelStickerSetNameHighlight" to "windowBackgroundWhiteBlueText4",
	"chat_emojiPanelStickerPackSelectorLine" to "chat_emojiPanelIconSelected",
	"sharedMedia_actionMode" to "actionBarDefault",
	"sheet_scrollUp" to "chat_emojiPanelStickerPackSelector",
	"sheet_other" to "player_actionBarItems",
	"dialogSearchBackground" to "chat_emojiPanelStickerPackSelector",
	"dialogSearchHint" to "chat_emojiPanelIcon",
	"dialogSearchIcon" to "chat_emojiPanelIcon",
	"dialogSearchText" to "windowBackgroundWhiteBlackText",
	"dialogFloatingButtonPressed" to "dialogRoundCheckBox",
	"dialogFloatingIcon" to "dialogRoundCheckBoxCheck",
	"dialogShadowLine" to "chat_emojiPanelShadowLine",
	"chat_emojiPanelIconSelector" to "listSelector",
	"actionBarDefaultArchived" to "actionBarDefault",
	"actionBarDefaultArchivedSelector" to "actionBarDefaultSelector",
	"actionBarDefaultArchivedIcon" to "actionBarDefaultIcon",
	"actionBarDefaultArchivedTitle" to "actionBarDefaultTitle",
	"actionBarDefaultArchivedSearch" to "actionBarDefaultSearch",
	"actionBarDefaultArchivedSearchPlaceholder" to "actionBarDefaultSearchPlaceholder",
	"chats_message_threeLines" to "chats_message",
	"chats_nameMessage_threeLines" to "chats_nameMessage",
	"chats_nameArchived" to "chats_name",
	"chats_nameMessageArchived" to "chats_nameMessage",
	"chats_nameMessageArchived_threeLines" to "chats_nameMessage",
	"chats_messageArchived" to "chats_message",
	"avatar_backgroundArchived" to "chats_unreadCounterMuted",
	"chats_archiveBackground" to "chats_actionBackground",
	"chats_archivePinBackground" to "chats_unreadCounterMuted",
	"chats_archiveIcon" to "chats_actionIcon",
	"chats_archiveText" to "chats_actionIcon",
	"actionBarDefaultSubmenuItemIcon" to "dialogIcon",
	"checkboxDisabled" to "chats_unreadCounterMuted",
	"chat_status" to "actionBarDefaultSubtitle",
	"chat_inGreenCall" to "calls_callReceivedGreenIcon",
	"chat_inRedCall" to "calls_callReceivedRedIcon",
	"chat_outGreenCall" to "calls_callReceivedGreenIcon",
	"actionBarTabActiveText" to "actionBarDefaultTitle",
	"actionBarTabUnactiveText" to "actionBarDefaultSubtitle",
	"actionBarTabLine" to "actionBarDefaultTitle",
	"actionBarTabSelector" to "actionBarDefaultSelector",
	"profile_status" to "avatar_subtitleInProfileBlue",
	"chats_menuTopBackgroundCats" to "avatar_backgroundActionBarBlue",
	"chat_outLinkSelectBackground" to "chat_linkSelectBackground",
	"actionBarDefaultSubmenuSeparator" to "windowBackgroundGray",
	"chat_attachPermissionImage" to "dialogTextBlack",
	"chat_attachPermissionMark" to "chat_sentError",
	"chat_attachPermissionText" to "dialogTextBlack",
	"chat_attachEmptyImage" to "emptyListPlaceholder",
	"chat_attachEmptyImage" to "emptyListPlaceholder",
	"actionBarBrowser" to "actionBarDefault",
	"chats_sentReadCheck" to "chats_sentCheck",
	"chat_outSentCheckRead" to "chat_outSentCheck",
	"chat_outSentCheckReadSelected" to "chat_outSentCheckSelected",
	"chats_archivePullDownBackground" to "chats_unreadCounterMuted",
	"chats_archivePullDownBackgroundActive" to "chats_actionBackground",
	"avatar_backgroundArchivedHidden" to "avatar_backgroundSaved",
	"featuredStickers_removeButtonText" to "featuredStickers_addButtonPressed",
	"dialogEmptyImage" to "player_time",
	"dialogEmptyText" to "player_time",
	"location_actionIcon" to "dialogTextBlack",
	"location_actionActiveIcon" to "windowBackgroundWhiteBlueText7",
	"location_actionBackground" to "dialogBackground",
	"location_actionPressedBackground" to "dialogBackgroundGray",
	"location_sendLocationText" to "windowBackgroundWhiteBlueText7",
	"location_sendLiveLocationText" to "windowBackgroundWhiteGreenText",
	"chat_outTextSelectionHighlight" to "chat_textSelectBackground",
	"chat_inTextSelectionHighlight" to "chat_textSelectBackground",
	"chat_TextSelectionCursor" to "chat_messagePanelCursor",
	"chat_outTextSelectionCursor" to "chat_TextSelectionCursor",
	"chat_inPollCorrectAnswer" to "chat_attachLocationBackground",
	"chat_outPollCorrectAnswer" to "chat_attachLocationBackground",
	"chat_inPollWrongAnswer" to "chat_attachAudioBackground",
	"chat_outPollWrongAnswer" to "chat_attachAudioBackground",
	"windowBackgroundWhiteYellowText" to "avatar_nameInMessageOrange",
	"profile_tabText" to "windowBackgroundWhiteGrayText",
	"profile_tabSelectedText" to "windowBackgroundWhiteBlueHeader",
	"profile_tabSelectedLine" to "windowBackgroundWhiteBlueHeader",
	"profile_tabSelector" to "listSelector",
	"statisticChartPopupBackground" to "dialogBackground",
	"chat_attachGalleryText" to "chat_attachGalleryBackground",
	"chat_attachAudioText" to "chat_attachAudioBackground",
	"chat_attachFileText" to "chat_attachFileBackground",
	"chat_attachContactText" to "chat_attachContactBackground",
	"chat_attachLocationText" to "chat_attachLocationBackground",
	"chat_attachPollText" to "chat_attachPollBackground",
	"chat_inPsaNameText" to "avatar_nameInMessageGreen",
	"chat_outPsaNameText" to "avatar_nameInMessageGreen",
	"chat_outAdminText" to "chat_outTimeText",
	"chat_outAdminSelectedText" to "chat_outTimeSelectedText",
	"returnToCallMutedBackground" to "windowBackgroundWhite",
	"dialogSwipeRemove" to "avatar_backgroundRed",
	"chat_inReactionButtonBackground" to "chat_inLoader",
	"chat_outReactionButtonBackground" to "chat_outLoader",
	"chat_inReactionButtonText" to "chat_inPreviewInstantText",
	"chat_outReactionButtonText" to "chat_outPreviewInstantText",
	"chat_inReactionButtonTextSelected" to "windowBackgroundWhite",
	"chat_outReactionButtonTextSelected" to "windowBackgroundWhite",
	"dialogReactionMentionBackground" to "voipgroup_mutedByAdminGradient2",
	"topics_unreadCounter" to "chats_unreadCounter",
	"topics_unreadCounterMuted" to "chats_message",
	"avatar_background2Saved" to "avatar_backgroundSaved",
	"avatar_background2Red" to "avatar_backgroundRed",
	"avatar_background2Orange" to "avatar_backgroundOrange",
	"avatar_background2Violet" to "avatar_backgroundViolet",
	"avatar_background2Green" to "avatar_backgroundGreen",
	"avatar_background2Cyan" to "avatar_backgroundCyan",
	"avatar_background2Blue" to "avatar_backgroundBlue",
	"avatar_background2Pink" to "avatar_backgroundPink",
	"statisticChartLine_orange" to "color_orange",
	"statisticChartLine_blue" to "color_blue",
	"statisticChartLine_red" to "color_red",
	"statisticChartLine_lightblue" to "color_lightblue",
	"statisticChartLine_golden" to "color_yellow",
	"statisticChartLine_purple" to "color_purple",
	"statisticChartLine_indigo" to "color_purple",
	"statisticChartLine_cyan" to "color_cyan"
)

// maybe we'll still need this in the future
fun getColorTokenFromColorValue(palette: FullPaletteList, color: Color): String {
	return when(color) {
		palette.colorRoles.primaryLight -> "primary_light"
		palette.colorRoles.onPrimaryLight -> "on_primary_light"
		palette.colorRoles.primaryContainerLight -> "primary_container_light"
		palette.colorRoles.onPrimaryContainerLight -> "on_primary_container_light"
		palette.colorRoles.secondaryLight -> "secondary_light"
		palette.colorRoles.onSecondaryLight -> "on_secondary_light"
		palette.colorRoles.secondaryContainerLight -> "secondary_container_light"
		palette.colorRoles.onSecondaryContainerLight -> "on_secondary_container_light"
		palette.colorRoles.tertiaryLight -> "tertiary_light"
		palette.colorRoles.onTertiaryLight -> "on_tertiary_light"
		palette.colorRoles.tertiaryContainerLight -> "tertiary_container_light"
		palette.colorRoles.onTertiaryContainerLight -> "on_tertiary_container_light"
		palette.colorRoles.surfaceLight -> "surface_light"
		palette.colorRoles.surfaceDimLight -> "surface_dim_light"
		palette.colorRoles.surfaceBrightLight -> "surface_bright_light"
		palette.colorRoles.onSurfaceLight -> "on_surface_light"
		palette.surfaceElevationLevel3Light -> "surface_elevation_level_3_light"
		palette.colorRoles.surfaceContainerLowestLight -> "surface_container_lowest_light"
		palette.colorRoles.surfaceContainerLowLight -> "surface_container_low_light"
		palette.colorRoles.surfaceContainerLight -> "surface_container_light"
		palette.colorRoles.surfaceContainerHighLight -> "surface_container_high_light"
		palette.colorRoles.surfaceContainerHighestLight -> "surface_container_highest_light"
		palette.colorRoles.onSurfaceVariantLight -> "on_surface_variant_light"
		palette.colorRoles.errorLight -> "error_light"
		palette.colorRoles.onErrorLight -> "on_error_light"
		palette.colorRoles.errorContainerLight -> "error_container_light"
		palette.colorRoles.onErrorContainerLight -> "on_error_container_light"
		palette.colorRoles.outlineLight -> "outline_light"
		palette.colorRoles.outlineVariantLight -> "outline_variant_light"
		palette.colorRoles.scrimLight -> "scrim_light"
		palette.colorRoles.primaryDark -> "primary_dark"
		palette.colorRoles.onPrimaryDark -> "on_primary_dark"
		palette.colorRoles.primaryContainerDark -> "primary_container_dark"
		palette.colorRoles.onPrimaryContainerDark -> "on_primary_container_dark"
		palette.colorRoles.secondaryDark -> "secondary_dark"
		palette.colorRoles.onSecondaryDark -> "on_secondary_dark"
		palette.colorRoles.secondaryContainerDark -> "secondary_container_dark"
		palette.colorRoles.onSecondaryContainerDark -> "on_secondary_container_dark"
		palette.colorRoles.tertiaryDark -> "tertiary_dark"
		palette.colorRoles.onTertiaryDark -> "on_tertiary_dark"
		palette.colorRoles.tertiaryContainerDark -> "tertiary_container_dark"
		palette.colorRoles.onTertiaryContainerDark -> "on_tertiary_container_dark"
		palette.colorRoles.surfaceDark -> "surface_dark"
		palette.colorRoles.surfaceDimDark -> "surface_dim_dark"
		palette.colorRoles.surfaceBrightDark -> "surface_bright_dark"
		palette.colorRoles.onSurfaceDark -> "on_surface_dark"
		palette.surfaceElevationLevel3Dark -> "surface_elevation_level_3_dark"
		palette.colorRoles.surfaceContainerLowestDark -> "surface_container_lowest_dark"
		palette.colorRoles.surfaceContainerLowDark -> "surface_container_low_dark"
		palette.colorRoles.surfaceContainerDark -> "surface_container_dark"
		palette.colorRoles.surfaceContainerHighDark -> "surface_container_high_dark"
		palette.colorRoles.surfaceContainerHighestDark -> "surface_container_highest_dark"
		palette.colorRoles.onSurfaceVariantDark -> "on_surface_variant_dark"
		palette.colorRoles.errorDark -> "error_dark"
		palette.colorRoles.onErrorDark -> "on_error_dark"
		palette.colorRoles.errorContainerDark -> "error_container_dark"
		palette.colorRoles.onErrorContainerDark -> "on_error_container_dark"
		palette.colorRoles.outlineDark -> "outline_dark"
		palette.colorRoles.outlineVariantDark -> "outline_variant_dark"
		palette.colorRoles.scrimDark -> "scrim_dark"
		Color.Transparent -> "transparent"
		palette.primary_0 -> "primary_0"
		palette.primary_10 -> "primary_10"
		palette.primary_20 -> "primary_20"
		palette.primary_30 -> "primary_30"
		palette.primary_40 -> "primary_40"
		palette.primary_50 -> "primary_50"
		palette.primary_60 -> "primary_60"
		palette.primary_70 -> "primary_70"
		palette.primary_80 -> "primary_80"
		palette.primary_90 -> "primary_90"
		palette.primary_95 -> "primary_95"
		palette.primary_99 -> "primary_99"
		palette.primary_100 -> "primary_100"
		palette.secondary_0 -> "secondary_0"
		palette.secondary_10 -> "secondary_10"
		palette.secondary_20 -> "secondary_20"
		palette.secondary_30 -> "secondary_30"
		palette.secondary_40 -> "secondary_40"
		palette.secondary_50 -> "secondary_50"
		palette.secondary_60 -> "secondary_60"
		palette.secondary_70 -> "secondary_70"
		palette.secondary_80 -> "secondary_80"
		palette.secondary_90 -> "secondary_90"
		palette.secondary_95 -> "secondary_95"
		palette.secondary_99 -> "secondary_99"
		palette.secondary_100 -> "secondary_100"
		palette.tertiary_0 -> "tertiary_0"
		palette.tertiary_10 -> "tertiary_10"
		palette.tertiary_20 -> "tertiary_20"
		palette.tertiary_30 -> "tertiary_30"
		palette.tertiary_40 -> "tertiary_40"
		palette.tertiary_50 -> "tertiary_50"
		palette.tertiary_60 -> "tertiary_60"
		palette.tertiary_70 -> "tertiary_70"
		palette.tertiary_80 -> "tertiary_80"
		palette.tertiary_90 -> "tertiary_90"
		palette.tertiary_95 -> "tertiary_95"
		palette.tertiary_99 -> "tertiary_99"
		palette.tertiary_100 -> "tertiary_100"
		palette.neutral_0 -> "neutral_0"
		palette.neutral_10 -> "neutral_10"
		palette.neutral_20 -> "neutral_20"
		palette.neutral_30 -> "neutral_30"
		palette.neutral_40 -> "neutral_40"
		palette.neutral_50 -> "neutral_50"
		palette.neutral_60 -> "neutral_60"
		palette.neutral_70 -> "neutral_70"
		palette.neutral_80 -> "neutral_80"
		palette.neutral_90 -> "neutral_90"
		palette.neutral_95 -> "neutral_95"
		palette.neutral_99 -> "neutral_99"
		palette.neutral_100 -> "neutral_100"
		palette.neutralVariant_0 -> "neutral_variant_0"
		palette.neutralVariant_10 -> "neutral_variant_10"
		palette.neutralVariant_20 -> "neutral_variant_20"
		palette.neutralVariant_30 -> "neutral_variant_30"
		palette.neutralVariant_40 -> "neutral_variant_40"
		palette.neutralVariant_50 -> "neutral_variant_50"
		palette.neutralVariant_60 -> "neutral_variant_60"
		palette.neutralVariant_70 -> "neutral_variant_70"
		palette.neutralVariant_80 -> "neutral_variant_80"
		palette.neutralVariant_90 -> "neutral_variant_90"
		palette.neutralVariant_95 -> "neutral_variant_95"
		palette.neutralVariant_99 -> "neutral_variant_99"
		palette.neutralVariant_100 -> "neutral_variant_100"
		palette.blue.getValue(0) -> "blue_0"
		palette.blue.getValue(10) -> "blue_10"
		palette.blue.getValue(20) -> "blue_20"
		palette.blue.getValue(30) -> "blue_30"
		palette.blue.getValue(40) -> "blue_40"
		palette.blue.getValue(50) -> "blue_50"
		palette.blue.getValue(60) -> "blue_60"
		palette.blue.getValue(70) -> "blue_70"
		palette.blue.getValue(80) -> "blue_80"
		palette.blue.getValue(90) -> "blue_90"
		palette.blue.getValue(95) -> "blue_95"
		palette.blue.getValue(99) -> "blue_99"
		palette.blue.getValue(100) -> "blue_100"
		palette.red.getValue(0) -> "red_0"
		palette.red.getValue(10) -> "red_10"
		palette.red.getValue(20) -> "red_20"
		palette.red.getValue(30) -> "red_30"
		palette.red.getValue(40) -> "red_40"
		palette.red.getValue(50) -> "red_50"
		palette.red.getValue(60) -> "red_60"
		palette.red.getValue(70) -> "red_70"
		palette.red.getValue(80) -> "red_80"
		palette.red.getValue(90) -> "red_90"
		palette.red.getValue(95) -> "red_95"
		palette.red.getValue(99) -> "red_99"
		palette.red.getValue(100) -> "red_100"
		palette.green.getValue(0) -> "green_0"
		palette.green.getValue(10) -> "green_10"
		palette.green.getValue(20) -> "green_20"
		palette.green.getValue(30) -> "green_30"
		palette.green.getValue(40) -> "green_40"
		palette.green.getValue(50) -> "green_50"
		palette.green.getValue(60) -> "green_60"
		palette.green.getValue(70) -> "green_70"
		palette.green.getValue(80) -> "green_80"
		palette.green.getValue(90) -> "green_90"
		palette.green.getValue(95) -> "green_95"
		palette.green.getValue(99) -> "green_99"
		palette.green.getValue(100) -> "green_100"
		palette.orange.getValue(0) -> "orange_0"
		palette.orange.getValue(10) -> "orange_10"
		palette.orange.getValue(20) -> "orange_20"
		palette.orange.getValue(30) -> "orange_30"
		palette.orange.getValue(40) -> "orange_40"
		palette.orange.getValue(50) -> "orange_50"
		palette.orange.getValue(60) -> "orange_60"
		palette.orange.getValue(70) -> "orange_70"
		palette.orange.getValue(80) -> "orange_80"
		palette.orange.getValue(90) -> "orange_90"
		palette.orange.getValue(95) -> "orange_95"
		palette.orange.getValue(99) -> "orange_99"
		palette.orange.getValue(100) -> "orange_100"
		palette.violet.getValue(0) -> "violet_0"
		palette.violet.getValue(10) -> "violet_10"
		palette.violet.getValue(20) -> "violet_20"
		palette.violet.getValue(30) -> "violet_30"
		palette.violet.getValue(40) -> "violet_40"
		palette.violet.getValue(50) -> "violet_50"
		palette.violet.getValue(60) -> "violet_60"
		palette.violet.getValue(70) -> "violet_70"
		palette.violet.getValue(80) -> "violet_80"
		palette.violet.getValue(90) -> "violet_90"
		palette.violet.getValue(95) -> "violet_95"
		palette.violet.getValue(99) -> "violet_99"
		palette.violet.getValue(100) -> "violet_100"
		palette.cyan.getValue(0) -> "cyan_0"
		palette.cyan.getValue(10) -> "cyan_10"
		palette.cyan.getValue(20) -> "cyan_20"
		palette.cyan.getValue(30) -> "cyan_30"
		palette.cyan.getValue(40) -> "cyan_40"
		palette.cyan.getValue(50) -> "cyan_50"
		palette.cyan.getValue(60) -> "cyan_60"
		palette.cyan.getValue(70) -> "cyan_70"
		palette.cyan.getValue(80) -> "cyan_80"
		palette.cyan.getValue(90) -> "cyan_90"
		palette.cyan.getValue(95) -> "cyan_95"
		palette.cyan.getValue(99) -> "cyan_99"
		palette.cyan.getValue(100) -> "cyan_100"
		palette.pink.getValue(0) -> "pink_0"
		palette.pink.getValue(10) -> "pink_10"
		palette.pink.getValue(20) -> "pink_20"
		palette.pink.getValue(30) -> "pink_30"
		palette.pink.getValue(40) -> "pink_40"
		palette.pink.getValue(50) -> "pink_50"
		palette.pink.getValue(60) -> "pink_60"
		palette.pink.getValue(70) -> "pink_70"
		palette.pink.getValue(80) -> "pink_80"
		palette.pink.getValue(90) -> "pink_90"
		palette.pink.getValue(95) -> "pink_95"
		palette.pink.getValue(99) -> "pink_99"
		palette.pink.getValue(100) -> "pink_100"
		else -> ""
	}
}

val defaultShadowTokens = mapOf(
	"windowBackgroundGrayShadow" to Pair("TRANSPARENT", Color.Transparent),
	"chat_inBubbleShadow" to Pair("TRANSPARENT", Color.Transparent),
	"chat_outBubbleShadow" to Pair("TRANSPARENT", Color.Transparent),
	"chats_menuTopShadow" to Pair("TRANSPARENT", Color.Transparent),
	"chats_menuTopShadowCats" to Pair("TRANSPARENT", Color.Transparent),
	"dialogShadowLine" to Pair("TRANSPARENT", Color.Transparent),
	"key_chat_messagePanelVoiceLockShadow" to Pair("TRANSPARENT", Color.Transparent),
	"chat_emojiPanelShadowLine" to Pair("TRANSPARENT", Color.Transparent),
	"chat_messagePanelShadow" to Pair("TRANSPARENT", Color.Transparent),
	// TODO choose which shadows you want to be gone
//	"chat_goDownButtonShadow" to Pair("TRANSPARENT", Color.Transparent)
)
