package com.dotstealab.telemone

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import com.dotstealab.telemone.ui.theme.FullPaletteList
import com.dotstealab.telemone.ui.theme.allColorTokensAsList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.UUID


// This does not take long to load but an advice with explanations
// will be greaty appreciated. *with examples* though cuz i think from top
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
	// translation:
	// <Themes<UUID, Theme<Name, DataAboutColors<ColorToken, ColorValue>>>
	// color in the list has to be int because Color() returns ulong anyway
	// anyway and so loading a theme after restarting the app causes a
	// crash
	// don't ask me why i don't keep ulong and use ints instead
	var themeList = mutableStateListOf<Map<String, Map<String, Pair<String, Int>>>>()
	var mappedValues = mutableStateMapOf<String, Pair<String, Color>>()
	var defaultCurrentTheme = mutableStateMapOf<String, Pair<String, Color>>()
	var loadedFromFileTheme = mutableStateMapOf<String, Pair<String, Color>>()
	// will need this to update source theme files or something
	val differencesBetweenFileAndCurrent = loadedFromFileTheme - mappedValues

	init {
		val savedThemeList = preferences.getString(themeListKey, "[]")
		val type = object : TypeToken<List<Map<String, Map<String, Pair<String, Int>>>>>() {}.type
		val list = Gson().fromJson<List<Map<String, Map<String, Pair<String, Int>>>>>(savedThemeList, type)
		themeList.addAll(
			list.map { map ->
				map.mapValues { entry ->
					entry.value.mapValues { (_, value) -> Pair(value.first, value.second) }
				}
			}
		)
	}

	fun saveCurrentTheme() {
		val mapWithUuid = mutableMapOf<String, Map<String, Pair<String, Int>>>()
		val uuid = UUID.randomUUID().toString()
		val map = mutableMapOf<String, Pair<String, Int>>()

		mappedValues.forEach { (themeTokenName, colorPair) ->
			val colorMap = Pair(colorPair.first, colorPair.second.toArgb())

			map[themeTokenName] = colorMap
			mapWithUuid.putIfAbsent(uuid, map)
		}
		themeList.add(mapWithUuid)

		val contents = Gson().toJson(themeList)
		preferences.edit().putString(themeListKey, contents).apply()
	}

	fun deleteTheme(uuid: String) {
		val mapToRemove = themeList.find { it.containsKey(uuid) }
		themeList.remove(mapToRemove)

		val contents = Gson().toJson(themeList)
		preferences.edit().clear().putString(themeListKey, contents).apply()
	}

	fun setCurrentMapTo(
		uuid: String,
		loadTokens: Boolean,
		palette: FullPaletteList,
		clearCurrentTheme: Boolean
	) {
		val loadedMap = themeList.firstOrNull { it.containsKey(uuid) }?.get(uuid)

		if (loadedMap != null) {
			if (loadTokens) {
				if (clearCurrentTheme) { mappedValues.clear() }
				loadedMap.let {
					it.forEach { colorPair ->
						val colorToken = colorPair.value.first
						val colorValue = getColorValueFromColorToken(colorToken, palette)

						mappedValues.putIfAbsent(
							colorPair.key,
							Pair(colorToken, colorValue)
						)
					}
				}
			} else {
				if (clearCurrentTheme) { mappedValues.clear() }
				loadedMap.let {
					it.forEach { colorPair ->
						mappedValues.putIfAbsent(
							colorPair.key,
							Pair(colorPair.value.first, Color(colorPair.value.second))
						)
					}
				}
			}
		}
	}

	// idk if im dum but i dont think this is able to properly load telegrams
	// stock themes, like "Day".
	fun loadThemeFromFile(
		context: Context,
		uri: Uri,
		palette: FullPaletteList,
		clearCurrentTheme: Boolean
	) {
		try {
			val loadedMap = mutableStateMapOf<String, Pair<String, Color>>()

			context.contentResolver.openInputStream(uri)?.bufferedReader()?.use { reader ->
				reader.forEachLine { line ->
					if (line.isNotEmpty()) {
						val splitLine = line.split("=")
						val key = splitLine[0]
						val value = splitLine[1]

						try {
							loadedMap[key] = Pair("", Color(value.toLong().toInt()))
						} catch (e: NumberFormatException) {
							val colorValue = getColorValueFromColorToken(value, palette)
							val isUnsupported = colorValue == Color.Red
							val pair = Pair(
								if (isUnsupported) "INCOMPATIBLE VALUE" else value,
								colorValue
							)

							loadedMap.put(key, pair).apply {
								// my brain not brainin. sory
								Toast.makeText(
									context,
									"Some colors are incompatible and were marked as such.",
									Toast.LENGTH_LONG
								).show()
							}
						}
					}
				}

				if (clearCurrentTheme) mappedValues.clear()

				loadedFromFileTheme.clear()
				mappedValues.putAll(loadedMap)
				loadedFromFileTheme.putAll(loadedMap)
			}
		} catch (e: IndexOutOfBoundsException) {
			Toast.makeText(
				context,
				"Chosen file isn't a Telegram (not Telegram X) theme.",
				Toast.LENGTH_LONG
			).show()
		}
	}

	fun resetCurrentTheme() {
		mappedValues.clear()
		mappedValues.putAll(defaultCurrentTheme)
	}

	fun overwriteCurrentLightTheme(uuid: String, palette: FullPaletteList) {
		val newDefaultTheme = themeList.find { it.containsKey(uuid) }?.getValue(uuid) ?: return

		val index = themeList.indexOfFirst { it.containsKey("defaultLightThemeUUID") }

		val defaultTheme = if (index != -1) {
			themeList[index].getValue("defaultLightThemeUUID").toMutableMap()
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

		themeList[index] = mapOf("defaultLightThemeUUID" to defaultTheme)

		val contents = Gson().toJson(themeList)
		preferences.edit().putString(themeListKey, contents).apply()
	}

	fun overwriteCurrentDarkTheme(uuid: String, palette: FullPaletteList) {
		val newDefaultTheme = themeList.find { it.containsKey(uuid) }?.getValue(uuid) ?: return

		val index = themeList.indexOfFirst { it.containsKey("defaultDarkThemeUUID") }

		val defaultTheme = if (index != -1) {
			themeList[index].getValue("defaultDarkThemeUUID").toMutableMap()
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

		themeList[index] = mapOf("defaultDarkThemeUUID" to defaultTheme)

		val contents = Gson().toJson(themeList)
		preferences.edit().putString(themeListKey, contents).apply()
	}

	fun changeValue(key: String, colorValue: Color, colorToken: String) {
		mappedValues[key] = Pair(colorToken, colorValue)
	}

	fun startupConfigProcess(palette: FullPaletteList, isDarkMode: Boolean, context: Context) {
		val darkTheme = defaultDarkTheme(palette, context)
		val lightTheme = defaultLightTheme(palette, context)

		// check if default themes are put in the list
		if (!themeList.any { it.containsKey("defaultDarkThemeUUID") }) {
			themeList.add(mapOf("defaultDarkThemeUUID" to darkTheme))
		}
		// dont do if else or when here because it will only check the first one
		if (!themeList.any { it.containsKey("defaultLightThemeUUID") }) {
			themeList.add(mapOf("defaultLightThemeUUID" to lightTheme))
		}

		// this will also fill in the missing values
		if (isDarkMode) {
			themeList.find { it.containsKey("defaultDarkThemeUUID") }
				?.getValue("defaultDarkThemeUUID")?.map {
					val uiItemName = it.key
					val colorToken = it.value.first
					val colorValueAsItWasSaved = Color(it.value.second)
					val colorValueFromToken = getColorValueFromColorToken(colorToken, palette)

					// if color token is something that is in the palette
					// list - load it. if not - load what was saved
					if (allColorTokensAsList.contains(colorToken)) {
						mappedValues[uiItemName] = Pair(colorToken, colorValueFromToken)
						defaultCurrentTheme[uiItemName] = Pair(colorToken, colorValueFromToken)
					} else {
						mappedValues[uiItemName] = Pair(colorToken, colorValueAsItWasSaved)
						defaultCurrentTheme[uiItemName] = Pair(colorToken, colorValueAsItWasSaved)
					}
				}
		} else {
			themeList.find { it.containsKey("defaultLightThemeUUID") }
				?.getValue("defaultLightThemeUUID")?.map {
					val uiItemName = it.key
					val colorToken = it.value.first
					val colorValueAsItWasSaved = Color(it.value.second)
					val colorValueFromToken = getColorValueFromColorToken(colorToken, palette)

					if (allColorTokensAsList.contains(colorToken)) {
						mappedValues[uiItemName] = Pair(colorToken, colorValueFromToken)
						defaultCurrentTheme[uiItemName] = Pair(colorToken, colorValueFromToken)
					} else {
						mappedValues[uiItemName] = Pair(colorToken, colorValueAsItWasSaved)
						defaultCurrentTheme[uiItemName] = Pair(colorToken, colorValueAsItWasSaved)
					}
				}
		}
	}

	fun loadDefaultDarkTheme(palette: FullPaletteList) {
		themeList.find { it.containsKey("defaultDarkThemeUUID") }
			?.getValue("defaultDarkThemeUUID")?.map {
				val uiItemName = it.key
				val colorToken = it.value.first
				val colorValue = getColorValueFromColorToken(colorToken, palette)

				mappedValues[uiItemName] = Pair(it.value.first, colorValue)
				defaultCurrentTheme.put(uiItemName, Pair(colorToken, colorValue))
			}
	}

	fun loadDefaultLightTheme(palette: FullPaletteList) {
		themeList.find { it.containsKey("defaultLightThemeUUID") }
			?.getValue("defaultLightThemeUUID")?.map {
				val uiItemName = it.key
				val colorToken = it.value.first
				val colorValue = getColorValueFromColorToken(colorToken, palette)

				mappedValues[uiItemName] = Pair(colorToken, colorValue)
				defaultCurrentTheme.put(uiItemName, Pair(colorToken, colorValue))
			}
	}

	fun loadStockDarkTheme(palette: FullPaletteList, context: Context) {
		mappedValues.clear()

		defaultDarkTheme(palette, context).map {
			val uiItemName = it.key
			val colorToken = it.value.first
			val colorValue = Color(it.value.second)

			mappedValues[uiItemName] = Pair(colorToken, colorValue)
			defaultCurrentTheme[uiItemName] = Pair(colorToken, colorValue)
		}
	}

	fun loadStockLightTheme(palette: FullPaletteList, context: Context) {
		mappedValues.clear()

		defaultLightTheme(palette, context).map {
			val uiItemName = it.key
			val colorToken = it.value.first
			val colorValue = Color(it.value.second)

			mappedValues[uiItemName] = Pair(colorToken, colorValue)
			defaultCurrentTheme[uiItemName] = Pair(colorToken, colorValue)
		}
	}

	fun shareCustomTheme(context: Context) {
		val map = mappedValues.mapValues {
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
		val source = themeList.find { it.containsKey("defaultLightThemeUUID") }
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
		val source = themeList.find { it.containsKey("defaultDarkThemeUUID") }
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
					val splitLine = line.split("=")
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
					val splitLine = line.split("=")
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
		else -> Color.Red
	}
}

// maybe we'll still need this in the future
fun assignColorToken(palette: FullPaletteList, color: Color): String {
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
		else -> "unspecified"
	}
}