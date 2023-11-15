package com.number869.telemone

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.FileProvider
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.number869.telemone.ui.theme.PaletteState
import java.io.File
import java.util.UUID

// no im not making a data class
typealias ThemeUUID = String
typealias UiElementName = String
typealias ColorToken = String
typealias ColorValue = Int
typealias DataAboutColors = Pair<ColorToken, ColorValue>
typealias UiElementData = Map<UiElementName, DataAboutColors>
typealias Theme = Map<ThemeUUID, UiElementData>
typealias ThemeList = SnapshotStateList<Theme>
typealias LoadedTheme = SnapshotStateMap<String, Pair<String, Color>>


class MainViewModelFactory(
	private val context: Application,
	private  val paletteState: PaletteState
) : ViewModelProvider.NewInstanceFactory() {
	override fun <T : ViewModel> create(
		modelClass: Class<T>,
		extras: CreationExtras
	): T = MainViewModel(context, paletteState) as T
}

// i will refactor all of this. someday. maybe. probably not.

// funny of you to actually expect some sort of documentation in the
// comments
class MainViewModel(val context: Application, val paletteState: PaletteState) : ViewModel() {
	private val themeListKey = "AppPreferences.ThemeList"
	private val preferences = context.getSharedPreferences(
		"AppPreferences",
		Context.MODE_PRIVATE
	)

	// god bless your eyes and brain that has to process this
	// color in the list has to be int because Color() returns ulong
	// anyway and so loading a theme after restarting the app causes a
	// crash.
	// don't ask me why i don't keep ulong and use ints instead
	// i tried savedStateHandle with state flows and i couldnt figure out
	// how to save the maps
	private var _themeList: ThemeList = mutableStateListOf()
	val palette = paletteState.entirePaletteAsMap.value
	val themeList: ThemeList get() = _themeList
	private var _mappedValues: LoadedTheme = mutableStateMapOf()
	val mappedValues: LoadedTheme get() = _mappedValues
	var defaultCurrentTheme: LoadedTheme = mutableStateMapOf()
	val selectedThemes = mutableStateListOf<String>()
	private var loadedFromFileTheme: LoadedTheme = mutableStateMapOf()

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

	fun selectOrUnselectSavedTheme(uuid: String) {
		if (selectedThemes.contains(uuid))
			selectedThemes.remove(uuid)
		else
			selectedThemes.add(uuid)
	}

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
	fun unselectAllThemes() {
		selectedThemes.clear()
	}

	fun deleteSelectedThemes() {
		_themeList.removeIf {
			selectedThemes.contains(it.keys.first())
		}

		// save changes locally
		val contents = Gson().toJson(_themeList)
		preferences.edit().putString(themeListKey, contents).apply()
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

		Toast.makeText(
			context,
			"Theme has been saved successfully.",
			Toast.LENGTH_LONG
		).show()
	}

	fun deleteTheme(uuid: String) {
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
		clearCurrentTheme: Boolean
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

	fun overwriteDefaultLightTheme(uuid: String, ) {
		val newDefaultTheme = _themeList.find { it.containsKey(uuid) }?.getValue(uuid) ?: return

		val index = _themeList.indexOfFirst { it.containsKey(defaultLightThemeUUID) }

		val defaultTheme = if (index != -1) {
			_themeList[index].getValue(defaultLightThemeUUID).toMutableMap()
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

		_themeList[index] = mapOf(defaultLightThemeUUID to defaultTheme)

		val contents = Gson().toJson(_themeList)
		preferences.edit().putString(themeListKey, contents).apply()

		Toast.makeText(
			context,
			"Default light theme has been overwritten successfully.",
			Toast.LENGTH_LONG
		).show()
	}

	fun overwriteDefaultDarkTheme(uuid: String, ) {
		val newDefaultTheme = _themeList.find { it.containsKey(uuid) }?.getValue(uuid) ?: return

		val index = _themeList.indexOfFirst { it.containsKey(defaultDarkThemeUUID) }

		val defaultTheme = if (index != -1) {
			_themeList[index].getValue(defaultDarkThemeUUID).toMutableMap()
		} else {
			mutableMapOf()
		}

		newDefaultTheme.forEach { (key, value) ->
			val colorToken = value.first
			val colorValueAsItWasLoaded = value.second
			val colorValueFromToken = getColorValueFromColorToken(colorToken, paletteState.entirePaletteAsMap.value)

			if (paletteState.allPossibleColorTokensAsList.contains(colorToken)) {
				defaultTheme[key] = Pair(colorToken, colorValueFromToken.toArgb())
			} else {
				defaultTheme[key] = Pair(colorToken, colorValueAsItWasLoaded)
			}
		}

		_themeList[index] = mapOf(defaultDarkThemeUUID to defaultTheme)

		val contents = Gson().toJson(_themeList)
		preferences.edit().putString(themeListKey, contents).apply()

		Toast.makeText(
			context,
			"Default dark theme has been overwritten successfully.",
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
		val darkTheme = stockDarkTheme(paletteState.entirePaletteAsMap.value, context)
		val lightTheme = stockLightTheme(paletteState.entirePaletteAsMap.value, context)
		val defaultThemeKey = if (isDarkMode)
			defaultDarkThemeUUID
		else
			defaultLightThemeUUID

		// check if default themes are put in the list
		if (!_themeList.any { it.containsKey(defaultDarkThemeUUID) }) {
			_themeList.add(mapOf(defaultDarkThemeUUID to darkTheme))
		}
		// dont do if else or when here because it will only check the first one
		if (!_themeList.any { it.containsKey(defaultLightThemeUUID) }) {
			_themeList.add(mapOf(defaultLightThemeUUID to lightTheme))
		}

		// this will also fill in the missing values
		_themeList.find { it.containsKey(defaultThemeKey) }
			?.getValue(defaultThemeKey)?.map {
				val uiItemName = it.key
				val colorToken = it.value.first
				val colorValueAsItWasSaved = Color(it.value.second)
				val colorValueFromToken = getColorValueFromColorToken(colorToken, paletteState.entirePaletteAsMap.value)

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

	fun exportThemeWithColorValues(uuid: String) {
		val theme = getThemeAsStringByUUID(
			_themeList,
			uuid
		)

		File(context.cacheDir, "Telemone Export.attheme").writeText(theme)

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

	fun exportThemeWithColorTokens(uuid: String) {
		val theme = getThemeAsStringByUUID(
			_themeList,
			uuid,
			ThemeAsStringType.ColorTokens
		)

		File(
			context.cacheDir,
			"Telemone Export (Telemone Format).attheme"
		).writeText(theme)

		val uri = FileProvider.getUriForFile(
			context,
			"${context.packageName}.provider",
			File(context.cacheDir, "Telemone Export (Telemone Format).attheme")
		)

		val intent = Intent(Intent.ACTION_SEND)
		intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
		intent.type = "*/attheme"
		intent.putExtra(Intent.EXTRA_STREAM, uri)
		context.startActivity(
			Intent.createChooser(
				intent,
				"Telemone Export (Telemone Format)"
			)
		)
	}

	fun loadDefaultDarkTheme() {
		_themeList.find { it.containsKey(defaultDarkThemeUUID) }
			?.getValue(defaultDarkThemeUUID)?.map {
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

	fun loadDefaultLightTheme() {
		_themeList.find { it.containsKey(defaultLightThemeUUID) }
			?.getValue(defaultLightThemeUUID)?.map {
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

	fun loadStockDarkTheme() {
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

	fun loadStockLightTheme() {
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
	fun exportCustomTheme() {
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

	fun saveLightTheme() {
		val theme = getThemeAsStringByUUID(
			_themeList,
			defaultLightThemeUUID,
			ThemeAsStringType.ColorValuesFromDevicesColorScheme,
			palette = palette
		)

		File(context.cacheDir, "Telemone Light.attheme").writeText(theme)

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

	fun saveDarkTheme() {
		val theme = getThemeAsStringByUUID(
			_themeList,
			defaultDarkThemeUUID,
			ThemeAsStringType.ColorValuesFromDevicesColorScheme,
			palette = palette
		)

		File(context.cacheDir, "Telemone Dark.attheme").writeText(theme)

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

const val defaultLightThemeUUID = "defaultLightThemeUUID"
const val defaultDarkThemeUUID = "defaultDarkThemeUUID"

private fun stockLightTheme(
	palette: Map<String, Color>,
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
	palette: Map<String, Color>,
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

val defaultShadowTokens = mapOf(
	"windowBackgroundGrayShadow" to Pair("transparent", Color.Transparent),
	"chat_inBubbleShadow" to Pair("transparent", Color.Transparent),
	"chat_outBubbleShadow" to Pair("transparent", Color.Transparent),
	"chats_menuTopShadow" to Pair("transparent", Color.Transparent),
	"chats_menuTopShadowCats" to Pair("transparent", Color.Transparent),
	"dialogShadowLine" to Pair("transparent", Color.Transparent),
	"key_chat_messagePanelVoiceLockShadow" to Pair("transparent", Color.Transparent),
	"chat_emojiPanelShadowLine" to Pair("transparent", Color.Transparent),
	"chat_messagePanelShadow" to Pair("transparent", Color.Transparent),
	// TODO choose which shadows you want to be gone
//	"chat_goDownButtonShadow" to Pair("transparent", Color.Transparent)
)

private fun getThemeAsStringByUUID(
	themeList: ThemeList,
	uuid: String,
	loadThemeUsing: ThemeAsStringType = ThemeAsStringType.ColorValues,
	palette: Map<String, Color>? = null
): String {
	val chosenTheme = themeList.find { it.containsKey(uuid) }
		?.getValue(uuid)
		?.mapValues {
			when (loadThemeUsing) {
				ThemeAsStringType.ColorValues -> Color(it.value.second).toArgb()
				ThemeAsStringType.ColorTokens -> it.value.first
				ThemeAsStringType.ColorValuesFromDevicesColorScheme -> {
					if (palette != null) {
						getColorValueFromColorToken(it.value.first, palette).toArgb()
					} else {
						throw Exception("palette is null in getThemeAsStringByUUID(). pass palette to the function where getThemeAsStringByUUID() was called.")
					}
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

private enum class ThemeAsStringType {
	ColorValues,
	ColorTokens,
	ColorValuesFromDevicesColorScheme
}