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
typealias Theme = Map<UiElementName, DataAboutColors>
typealias Themes = Map<ThemeUUID, Theme>
typealias ThemeList = SnapshotStateList<Themes>
typealias LoadedTheme = SnapshotStateMap<String, Pair<String, Color>>



// This does not take long to load but an advice with explanations
// will be greatly appreciated. *with examples* though cuz i think from top
// to bottom cuz i mainly look for patterns cuz attention span
// comparable to that one of a chimpanzee if not less

// i will refactor all of this. someday. if i only focused on cleanness
// i would've never made this a real thing. saying this cuz i know y'all are
// gonna be coming after me for the things i wrote 💀

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
	private var _themeList: ThemeList = mutableStateListOf()
	val themeList: ThemeList get() = _themeList
	private var _mappedValues: LoadedTheme = mutableStateMapOf()
	val mappedValues: LoadedTheme get() = _mappedValues
	private var defaultCurrentTheme: LoadedTheme = mutableStateMapOf()
	private var loadedFromFileTheme: LoadedTheme = mutableStateMapOf()
	// will need this to update source theme files or something
	// TODO DEBUG THIS
	val differencesBetweenFileAndCurrent = loadedFromFileTheme.filterKeys { it !in _mappedValues.keys }
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

	fun colorOfCurrentTheme(colorValueOf: ColorToken /*its a String*/): Color {
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

	fun saveCurrentTheme() {
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
	}

	fun deleteTheme(uuid: String) {
		val mapToRemove = _themeList.find { it.containsKey(uuid) }
		_themeList.remove(mapToRemove)

		val contents = Gson().toJson(_themeList)
		preferences.edit().clear().putString(themeListKey, contents).apply()
	}

	fun setCurrentMapTo(
		uuid: String,
		loadTokens: Boolean,
		palette: FullPaletteList,
		clearCurrentTheme: Boolean
	) {
		val loadedMap = _themeList.firstOrNull { it.containsKey(uuid) }?.get(uuid)

		if (loadedMap != null) {
			if (loadTokens) {
				if (clearCurrentTheme) { _mappedValues.clear() }
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
				if (clearCurrentTheme) { _mappedValues.clear() }
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
	}

	// idk if im dum but i don't think this is able to properly load telegrams
	// stock themes, like "Day".
	fun loadThemeFromFile(
		context: Context,
		uri: Uri,
		palette: FullPaletteList,
		clearCurrentTheme: Boolean
	) {
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
									// checks if the contents are like "uiElelemnt=n1_800"
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

	fun resetCurrentTheme() {
		_mappedValues.clear()
		_mappedValues.putAll(defaultCurrentTheme)
	}

	fun overwriteCurrentLightTheme(uuid: String, palette: FullPaletteList) {
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
	}

	fun overwriteCurrentDarkTheme(uuid: String, palette: FullPaletteList) {
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
	}

	fun changeValue(key: String, colorValue: Color, colorToken: String) {
		_mappedValues[key] = Pair(colorToken, colorValue)

		Log.d(TAG, "color value replaced at $key with $colorValue")
	}

	fun startupConfigProcess(palette: FullPaletteList, isDarkMode: Boolean, context: Context) {
		val darkTheme = defaultDarkTheme(palette, context)
		val lightTheme = defaultLightTheme(palette, context)
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

		File(context.cacheDir, "TeleMone Export.attheme").writeText(result)

		val uri = FileProvider.getUriForFile(
			context,
			"${context.packageName}.provider",
			File(context.cacheDir, "TeleMone Export.attheme")
		)

		val intent = Intent(Intent.ACTION_SEND)
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
		intent.type = "*/attheme"
		intent.putExtra(Intent.EXTRA_STREAM, uri)
		context.startActivity(Intent.createChooser(intent, "TeleMone Export"))
	}

	fun loadDefaultDarkTheme(palette: FullPaletteList) {
		_themeList.find { it.containsKey("defaultDarkThemeUUID") }
			?.getValue("defaultDarkThemeUUID")?.map {
				val uiItemName = it.key
				val colorToken = it.value.first
				val colorValue = getColorValueFromColorToken(colorToken, palette)

				_mappedValues[uiItemName] = Pair(it.value.first, colorValue)
				defaultCurrentTheme.put(uiItemName, Pair(colorToken, colorValue))
			}
	}

	fun loadDefaultLightTheme(palette: FullPaletteList) {
		_themeList.find { it.containsKey("defaultLightThemeUUID") }
			?.getValue("defaultLightThemeUUID")?.map {
				val uiItemName = it.key
				val colorToken = it.value.first
				val colorValue = getColorValueFromColorToken(colorToken, palette)

				_mappedValues[uiItemName] = Pair(colorToken, colorValue)
				defaultCurrentTheme.put(uiItemName, Pair(colorToken, colorValue))
			}
	}

	fun loadStockDarkTheme(palette: FullPaletteList, context: Context) {
		_mappedValues.clear()

		defaultDarkTheme(palette, context).map {
			val uiItemName = it.key
			val colorToken = it.value.first
			val colorValue = Color(it.value.second)

			_mappedValues[uiItemName] = Pair(colorToken, colorValue)
			defaultCurrentTheme[uiItemName] = Pair(colorToken, colorValue)
		}
	}

	fun loadStockLightTheme(palette: FullPaletteList, context: Context) {
		_mappedValues.clear()

		defaultLightTheme(palette, context).map {
			val uiItemName = it.key
			val colorToken = it.value.first
			val colorValue = Color(it.value.second)

			_mappedValues[uiItemName] = Pair(colorToken, colorValue)
			defaultCurrentTheme[uiItemName] = Pair(colorToken, colorValue)
		}
	}

	fun shareCustomTheme(context: Context) {
		val map = _mappedValues.mapValues {
			it.value.second.toArgb()
		}.entries.joinToString("\n")
		val result = "${
			map.replace(")", "")
				.replace("(", "")
				.replace(", ", "=")
		}\n"

		File(context.cacheDir, "TeleMone Custom.attheme").writeText(result)

		val uri = FileProvider.getUriForFile(
			context,
			"${context.packageName}.provider",
			File(context.cacheDir, "TeleMone Custom.attheme")
		)

		val intent = Intent(Intent.ACTION_SEND)
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
		intent.type = "*/attheme"
		intent.putExtra(Intent.EXTRA_STREAM, uri)
		context.startActivity(Intent.createChooser(intent, "TeleMone Custom"))
	}

	fun saveLightModeTheme(context: Context, palette: FullPaletteList) {
		val source = _themeList.find { it.containsKey("defaultLightThemeUUID") }
			?.getValue("defaultLightThemeUUID")
		// prints ui element name = the color we gave it
		val result = source!!.run {
			this.mapNotNull {
				val colorValue = getColorValueFromColorToken(it.value.first, palette)
				"${it.key}=${colorValue.toArgb()}"
			}
		}.joinToString("\n")

		File(context.cacheDir, "TeleMone Light.attheme").writeText(result)

		val uri = FileProvider.getUriForFile(
			context,
			"${context.packageName}.provider",
			File(context.cacheDir, "TeleMone Light.attheme")
		)

		val intent = Intent(Intent.ACTION_SEND)
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
		intent.type = "*/attheme"
		intent.putExtra(Intent.EXTRA_STREAM, uri)
		context.startActivity(Intent.createChooser(intent, "TeleMone Light"))
	}

	fun saveDarkModeTheme(context: Context, palette: FullPaletteList) {
		val source = _themeList.find { it.containsKey("defaultDarkThemeUUID") }
			?.getValue("defaultDarkThemeUUID")

		val result = source!!.run {
			this.mapNotNull {
				val colorValue = getColorValueFromColorToken(it.value.first, palette)
				"${it.key}=${colorValue.toArgb()}"
			}
		}.joinToString("\n")

		File(context.cacheDir, "TeleMone Dark.attheme").writeText(result)

		val uri = FileProvider.getUriForFile(
			context,
			"${context.packageName}.provider",
			File(context.cacheDir, "TeleMone Dark.attheme")
		)

		val intent = Intent(Intent.ACTION_SEND)
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
		intent.type = "*/attheme"
		intent.putExtra(Intent.EXTRA_STREAM, uri)
		context.startActivity(Intent.createChooser(intent, "TeleMone Dark"))
	}
}


// at the moment the defaults are kanged from c3r5b8's app.
// at the moment(!!)

// themes in assets folder MUST have values existent in fullPalette()
private fun defaultLightTheme(
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
private fun defaultDarkTheme(
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
		"a1_0" -> palette.a1_0
		"a1_10" -> palette.a1_10
		"a1_50" -> palette.a1_50
		"a1_100" -> palette.a1_100
		"a1_200" -> palette.a1_200
		"a1_300" -> palette.a1_300
		"a1_400" -> palette.a1_400
		"a1_500" -> palette.a1_500
		"a1_600" -> palette.a1_600
		"a1_700" -> palette.a1_700
		"a1_800" -> palette.a1_800
		"a1_900" -> palette.a1_900
		"a1_1000" -> palette.a1_1000
		"a2_0" -> palette.a2_0
		"a2_10" -> palette.a2_10
		"a2_50" -> palette.a2_50
		"a2_100" -> palette.a2_100
		"a2_200" -> palette.a2_200
		"a2_300" -> palette.a2_300
		"a2_400" -> palette.a2_400
		"a2_500" -> palette.a2_500
		"a2_600" -> palette.a2_600
		"a2_700" -> palette.a2_700
		"a2_800" -> palette.a2_800
		"a2_900" -> palette.a2_900
		"a2_1000" -> palette.a2_1000
		"a3_0" -> palette.a3_0
		"a3_10" -> palette.a3_10
		"a3_50" -> palette.a3_50
		"a3_100" -> palette.a3_100
		"a3_200" -> palette.a3_200
		"a3_300" -> palette.a3_300
		"a3_400" -> palette.a3_400
		"a3_500" -> palette.a3_500
		"a3_600" -> palette.a3_600
		"a3_700" -> palette.a3_700
		"a3_800" -> palette.a3_800
		"a3_900" -> palette.a3_900
		"a3_1000" -> palette.a3_1000
		"n1_0" -> palette.n1_0
		"n1_10" -> palette.n1_10
		"n1_50" -> palette.n1_50
		"n1_100" -> palette.n1_100
		"n1_200" -> palette.n1_200
		"n1_300" -> palette.n1_300
		"n1_400" -> palette.n1_400
		"n1_500" -> palette.n1_500
		"n1_600" -> palette.n1_600
		"n1_700" -> palette.n1_700
		"n1_800" -> palette.n1_800
		"n1_900" -> palette.n1_900
		"n1_1000" -> palette.n1_1000
		"n2_0" -> palette.n2_0
		"n2_10" -> palette.n2_10
		"n2_50" -> palette.n2_50
		"n2_100" -> palette.n2_100
		"n2_200" -> palette.n2_200
		"n2_300" -> palette.n2_300
		"n2_400" -> palette.n2_400
		"n2_500" -> palette.n2_500
		"n2_600" -> palette.n2_600
		"n2_700" -> palette.n2_700
		"n2_800" -> palette.n2_800
		"n2_900" -> palette.n2_900
		"n2_1000" -> palette.n2_1000
		"background_light" -> palette.backgroundLight
		"surface_light" -> palette.surfaceLight
		"surface_elevation_level_3_light" -> palette.surfaceElevationLevel3Light
		"background_dark" -> palette.backgroundDark
		"surface_dark" -> palette.surfaceDark
		"surface_elevation_level_3_dark" -> palette.surfaceElevationLevel3Dark
		"TRANSPARENT" -> Color.Transparent
		"blue_0" -> palette.blueTonalPalette.getValue(0)
		"blue_100" -> palette.blueTonalPalette.getValue(10)
		"blue_200" -> palette.blueTonalPalette.getValue(20)
		"blue_300" -> palette.blueTonalPalette.getValue(30)
		"blue_400" -> palette.blueTonalPalette.getValue(40)
		"blue_500" -> palette.blueTonalPalette.getValue(50)
		"blue_600" -> palette.blueTonalPalette.getValue(60)
		"blue_700" -> palette.blueTonalPalette.getValue(70)
		"blue_800" -> palette.blueTonalPalette.getValue(80)
		"blue_900" -> palette.blueTonalPalette.getValue(90)
		"blue_950" -> palette.blueTonalPalette.getValue(95)
		"blue_990" -> palette.blueTonalPalette.getValue(99)
		"blue_1000" -> palette.blueTonalPalette.getValue(100)
		"red_0" -> palette.redTonalPalette.getValue(0)
		"red_100" -> palette.redTonalPalette.getValue(10)
		"red_200" -> palette.redTonalPalette.getValue(20)
		"red_300" -> palette.redTonalPalette.getValue(30)
		"red_400" -> palette.redTonalPalette.getValue(40)
		"red_500" -> palette.redTonalPalette.getValue(50)
		"red_600" -> palette.redTonalPalette.getValue(60)
		"red_700" -> palette.redTonalPalette.getValue(70)
		"red_800" -> palette.redTonalPalette.getValue(80)
		"red_900" -> palette.redTonalPalette.getValue(90)
		"red_950" -> palette.redTonalPalette.getValue(95)
		"red_990" -> palette.redTonalPalette.getValue(99)
		"red_1000" -> palette.redTonalPalette.getValue(100)
		"green_0" -> palette.greenTonalPalette.getValue(0)
		"green_100" -> palette.greenTonalPalette.getValue(10)
		"green_200" -> palette.greenTonalPalette.getValue(20)
		"green_300" -> palette.greenTonalPalette.getValue(30)
		"green_400" -> palette.greenTonalPalette.getValue(40)
		"green_500" -> palette.greenTonalPalette.getValue(50)
		"green_600" -> palette.greenTonalPalette.getValue(60)
		"green_700" -> palette.greenTonalPalette.getValue(70)
		"green_800" -> palette.greenTonalPalette.getValue(80)
		"green_900" -> palette.greenTonalPalette.getValue(90)
		"green_950" -> palette.greenTonalPalette.getValue(95)
		"green_990" -> palette.greenTonalPalette.getValue(99)
		"green_1000" -> palette.greenTonalPalette.getValue(100)
		"orange_0" -> palette.orangeTonalPalette.getValue(0)
		"orange_100" -> palette.orangeTonalPalette.getValue(10)
		"orange_200" -> palette.orangeTonalPalette.getValue(20)
		"orange_300" -> palette.orangeTonalPalette.getValue(30)
		"orange_400" -> palette.orangeTonalPalette.getValue(40)
		"orange_500" -> palette.orangeTonalPalette.getValue(50)
		"orange_600" -> palette.orangeTonalPalette.getValue(60)
		"orange_700" -> palette.orangeTonalPalette.getValue(70)
		"orange_800" -> palette.orangeTonalPalette.getValue(80)
		"orange_900" -> palette.orangeTonalPalette.getValue(90)
		"orange_950" -> palette.orangeTonalPalette.getValue(95)
		"orange_990" -> palette.orangeTonalPalette.getValue(99)
		"orange_1000" -> palette.orangeTonalPalette.getValue(100)
		"violet_0" -> palette.violetTonalPalette.getValue(0)
		"violet_100" -> palette.violetTonalPalette.getValue(10)
		"violet_200" -> palette.violetTonalPalette.getValue(20)
		"violet_300" -> palette.violetTonalPalette.getValue(30)
		"violet_400" -> palette.violetTonalPalette.getValue(40)
		"violet_500" -> palette.violetTonalPalette.getValue(50)
		"violet_600" -> palette.violetTonalPalette.getValue(60)
		"violet_700" -> palette.violetTonalPalette.getValue(70)
		"violet_800" -> palette.violetTonalPalette.getValue(80)
		"violet_900" -> palette.violetTonalPalette.getValue(90)
		"violet_950" -> palette.violetTonalPalette.getValue(95)
		"violet_990" -> palette.violetTonalPalette.getValue(99)
		"violet_1000" -> palette.violetTonalPalette.getValue(100)
		"cyan_0" -> palette.cyanTonalPalette.getValue(0)
		"cyan_100" -> palette.cyanTonalPalette.getValue(10)
		"cyan_200" -> palette.cyanTonalPalette.getValue(20)
		"cyan_300" -> palette.cyanTonalPalette.getValue(30)
		"cyan_400" -> palette.cyanTonalPalette.getValue(40)
		"cyan_500" -> palette.cyanTonalPalette.getValue(50)
		"cyan_600" -> palette.cyanTonalPalette.getValue(60)
		"cyan_700" -> palette.cyanTonalPalette.getValue(70)
		"cyan_800" -> palette.cyanTonalPalette.getValue(80)
		"cyan_900" -> palette.cyanTonalPalette.getValue(90)
		"cyan_950" -> palette.cyanTonalPalette.getValue(95)
		"cyan_990" -> palette.cyanTonalPalette.getValue(99)
		"cyan_1000" -> palette.cyanTonalPalette.getValue(100)
		"pink_0" -> palette.pinkTonalPalette.getValue(0)
		"pink_100" -> palette.pinkTonalPalette.getValue(10)
		"pink_200" -> palette.pinkTonalPalette.getValue(20)
		"pink_300" -> palette.pinkTonalPalette.getValue(30)
		"pink_400" -> palette.pinkTonalPalette.getValue(40)
		"pink_500" -> palette.pinkTonalPalette.getValue(50)
		"pink_600" -> palette.pinkTonalPalette.getValue(60)
		"pink_700" -> palette.pinkTonalPalette.getValue(70)
		"pink_800" -> palette.pinkTonalPalette.getValue(80)
		"pink_900" -> palette.pinkTonalPalette.getValue(90)
		"pink_950" -> palette.pinkTonalPalette.getValue(95)
		"pink_990" -> palette.pinkTonalPalette.getValue(99)
		"pink_1000" -> palette.pinkTonalPalette.getValue(100)
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
		palette.a1_0 -> "a1_0"
		palette.a1_10 -> "a1_10"
		palette.a1_50 -> "a1_50"
		palette.a1_100 -> "a1_100"
		palette.a1_200 -> "a1_200"
		palette.a1_300 -> "a1_300"
		palette.a1_400 -> "a1_400"
		palette.a1_500 -> "a1_500"
		palette.a1_600 -> "a1_600"
		palette.a1_700 -> "a1_700"
		palette.a1_800 -> "a1_800"
		palette.a1_900 -> "a1_900"
		palette.a1_1000 -> "a1_1000"
		palette.a2_0 -> "a2_0"
		palette.a2_10 -> "a2_10"
		palette.a2_50 -> "a2_50"
		palette.a2_100 -> "a2_100"
		palette.a2_200 -> "a2_200"
		palette.a2_300 -> "a2_300"
		palette.a2_400 -> "a2_400"
		palette.a2_500 -> "a2_500"
		palette.a2_600 -> "a2_600"
		palette.a2_700 -> "a2_700"
		palette.a2_800 -> "a2_800"
		palette.a2_900 -> "a2_900"
		palette.a2_1000 -> "a2_1000"
		palette.a3_0 -> "a3_0"
		palette.a3_10 -> "a3_10"
		palette.a3_50 -> "a3_50"
		palette.a3_100 -> "a3_100"
		palette.a3_200 -> "a3_200"
		palette.a3_300 -> "a3_300"
		palette.a3_400 -> "a3_400"
		palette.a3_500 -> "a3_500"
		palette.a3_600 -> "a3_600"
		palette.a3_700 -> "a3_700"
		palette.a3_800 -> "a3_800"
		palette.a3_900 -> "a3_900"
		palette.a3_1000 -> "a3_1000"
		palette.n1_0 -> "n1_0"
		palette.n1_10 -> "n1_10"
		palette.n1_50 -> "n1_50"
		palette.n1_100 -> "n1_100"
		palette.n1_200 -> "n1_200"
		palette.n1_300 -> "n1_300"
		palette.n1_400 -> "n1_400"
		palette.n1_500 -> "n1_500"
		palette.n1_600 -> "n1_600"
		palette.n1_700 -> "n1_700"
		palette.n1_800 -> "n1_800"
		palette.n1_900 -> "n1_900"
		palette.n1_1000 -> "n1_1000"
		palette.n2_0 -> "n2_0"
		palette.n2_10 -> "n2_10"
		palette.n2_50 -> "n2_50"
		palette.n2_100 -> "n2_100"
		palette.n2_200 -> "n2_200"
		palette.n2_300 -> "n2_300"
		palette.n2_400 -> "n2_400"
		palette.n2_500 -> "n2_500"
		palette.n2_600 -> "n2_600"
		palette.n2_700 -> "n2_700"
		palette.n2_800 -> "n2_800"
		palette.n2_900 -> "n2_900"
		palette.n2_1000 -> "n2_1000"
		palette.backgroundLight -> "background_light"
		palette.surfaceLight -> "surface_light"
		palette.surfaceElevationLevel3Light -> "surface_elevation_level_3_light"
		palette.backgroundDark -> "background_dark"
		palette.surfaceDark -> "surface_dark"
		palette.surfaceElevationLevel3Dark -> "surface_elevation_level_3_dark"
		Color.Transparent -> "TRANSPARENT"
		palette.blueTonalPalette.getValue(0) -> "blue_0"
		palette.blueTonalPalette.getValue(10) -> "blue_100"
		palette.blueTonalPalette.getValue(20) -> "blue_200"
		palette.blueTonalPalette.getValue(30) -> "blue_300"
		palette.blueTonalPalette.getValue(40) -> "blue_400"
		palette.blueTonalPalette.getValue(50) -> "blue_500"
		palette.blueTonalPalette.getValue(60) -> "blue_600"
		palette.blueTonalPalette.getValue(70) -> "blue_700"
		palette.blueTonalPalette.getValue(80) -> "blue_800"
		palette.blueTonalPalette.getValue(90) -> "blue_900"
		palette.blueTonalPalette.getValue(95) -> "blue_950"
		palette.blueTonalPalette.getValue(99) -> "blue_990"
		palette.blueTonalPalette.getValue(100) -> "blue_1000"
		palette.redTonalPalette.getValue(0) -> "red_0"
		palette.redTonalPalette.getValue(10) -> "red_100"
		palette.redTonalPalette.getValue(20) -> "red_200"
		palette.redTonalPalette.getValue(30) -> "red_300"
		palette.redTonalPalette.getValue(40) -> "red_400"
		palette.redTonalPalette.getValue(50) -> "red_500"
		palette.redTonalPalette.getValue(60) -> "red_600"
		palette.redTonalPalette.getValue(70) -> "red_700"
		palette.redTonalPalette.getValue(80) -> "red_800"
		palette.redTonalPalette.getValue(90) -> "red_900"
		palette.redTonalPalette.getValue(95) -> "red_950"
		palette.redTonalPalette.getValue(99) -> "red_990"
		palette.redTonalPalette.getValue(100) -> "red_1000"
		palette.greenTonalPalette.getValue(0) -> "green_0"
		palette.greenTonalPalette.getValue(10) -> "green_100"
		palette.greenTonalPalette.getValue(20) -> "green_200"
		palette.greenTonalPalette.getValue(30) -> "green_300"
		palette.greenTonalPalette.getValue(40) -> "green_400"
		palette.greenTonalPalette.getValue(50) -> "green_500"
		palette.greenTonalPalette.getValue(60) -> "green_600"
		palette.greenTonalPalette.getValue(70) -> "green_700"
		palette.greenTonalPalette.getValue(80) -> "green_800"
		palette.greenTonalPalette.getValue(90) -> "green_900"
		palette.greenTonalPalette.getValue(95) -> "green_950"
		palette.greenTonalPalette.getValue(99) -> "green_990"
		palette.greenTonalPalette.getValue(100) -> "green_1000"
		palette.orangeTonalPalette.getValue(0) -> "orange_0"
		palette.orangeTonalPalette.getValue(10) -> "orange_100"
		palette.orangeTonalPalette.getValue(20) -> "orange_200"
		palette.orangeTonalPalette.getValue(30) -> "orange_300"
		palette.orangeTonalPalette.getValue(40) -> "orange_400"
		palette.orangeTonalPalette.getValue(50) -> "orange_500"
		palette.orangeTonalPalette.getValue(60) -> "orange_600"
		palette.orangeTonalPalette.getValue(70) -> "orange_700"
		palette.orangeTonalPalette.getValue(80) -> "orange_800"
		palette.orangeTonalPalette.getValue(90) -> "orange_900"
		palette.orangeTonalPalette.getValue(95) -> "orange_950"
		palette.orangeTonalPalette.getValue(99) -> "orange_990"
		palette.orangeTonalPalette.getValue(100) -> "orange_1000"
		palette.violetTonalPalette.getValue(0) -> "violet_0"
		palette.violetTonalPalette.getValue(10) -> "violet_100"
		palette.violetTonalPalette.getValue(20) -> "violet_200"
		palette.violetTonalPalette.getValue(30) -> "violet_300"
		palette.violetTonalPalette.getValue(40) -> "violet_400"
		palette.violetTonalPalette.getValue(50) -> "violet_500"
		palette.violetTonalPalette.getValue(60) -> "violet_600"
		palette.violetTonalPalette.getValue(70) -> "violet_700"
		palette.violetTonalPalette.getValue(80) -> "violet_800"
		palette.violetTonalPalette.getValue(90) -> "violet_900"
		palette.violetTonalPalette.getValue(95) -> "violet_950"
		palette.violetTonalPalette.getValue(99) -> "violet_990"
		palette.violetTonalPalette.getValue(100) -> "violet_1000"
		palette.cyanTonalPalette.getValue(0) -> "cyan_0"
		palette.cyanTonalPalette.getValue(10) -> "cyan_100"
		palette.cyanTonalPalette.getValue(20) -> "cyan_200"
		palette.cyanTonalPalette.getValue(30) -> "cyan_300"
		palette.cyanTonalPalette.getValue(40) -> "cyan_400"
		palette.cyanTonalPalette.getValue(50) -> "cyan_500"
		palette.cyanTonalPalette.getValue(60) -> "cyan_600"
		palette.cyanTonalPalette.getValue(70) -> "cyan_700"
		palette.cyanTonalPalette.getValue(80) -> "cyan_800"
		palette.cyanTonalPalette.getValue(90) -> "cyan_900"
		palette.cyanTonalPalette.getValue(95) -> "cyan_950"
		palette.cyanTonalPalette.getValue(99) -> "cyan_990"
		palette.cyanTonalPalette.getValue(100) -> "cyan_1000"
		palette.pinkTonalPalette.getValue(0) -> "pink_0"
		palette.pinkTonalPalette.getValue(10) -> "pink_100"
		palette.pinkTonalPalette.getValue(20) -> "pink_200"
		palette.pinkTonalPalette.getValue(30) -> "pink_300"
		palette.pinkTonalPalette.getValue(40) -> "pink_400"
		palette.pinkTonalPalette.getValue(50) -> "pink_500"
		palette.pinkTonalPalette.getValue(60) -> "pink_600"
		palette.pinkTonalPalette.getValue(70) -> "pink_700"
		palette.pinkTonalPalette.getValue(80) -> "pink_800"
		palette.pinkTonalPalette.getValue(90) -> "pink_900"
		palette.pinkTonalPalette.getValue(95) -> "pink_950"
		palette.pinkTonalPalette.getValue(99) -> "pink_990"
		palette.pinkTonalPalette.getValue(100) -> "pink_1000"
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
