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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.util.UUID


// This takes does not take long to load but An advice with explanations
// will be greaty appreciated. *with examples* though cuz i think from top
// to bottom cuz i mainly look for patterns cuz attention span
// comparable to that one of a chimpanzee if not less

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

	fun setCurrentMapTo(uuid: String) {
		val loadedMap = themeList.firstOrNull { it.containsKey(uuid) }?.get(uuid)

		if (loadedMap != null) {
			mappedValues.clear()
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

	// idk if im dum but i dont think this is able to properly load telegrams
	// stock themes, like "Day".
	fun loadThemeFromFile(
		context: Context,
		uri: Uri,
		palette: FullPaletteList
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
							val colorValue = when(value) {
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

				// TODO Implement prompt "Clear the current theme before
				//  importing this one?"
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

	fun changeValue(key: String, colorValue: Color, colorToken: String) {
		mappedValues[key] = Pair(colorToken, colorValue)
	}

	fun startupConfigProcess(palette: FullPaletteList, isDarkMode: Boolean) {
		val theme = if (isDarkMode) defaultDarkTheme(palette) else defaultLightTheme(palette)

		theme.map {
			val colorToken = assignColorToken(palette, Color(it.value))
			mappedValues[it.key] = Pair(colorToken, Color(it.value))
			defaultCurrentTheme.put(it.key, Pair(colorToken, Color(it.value)))
		}
	}

	fun loadDarkTheme(palette: FullPaletteList) {
		mappedValues.clear()

		defaultDarkTheme(palette).map {
			val colorToken = assignColorToken(palette, Color(it.value))

			mappedValues[it.key] = Pair(colorToken, Color(it.value))
			defaultCurrentTheme[it.key] = Pair(colorToken, Color(it.value))
		}
	}

	fun loadLightTheme(palette: FullPaletteList) {
		mappedValues.clear()

		defaultLightTheme(palette).map {
			val colorToken = assignColorToken(palette, Color(it.value))

			mappedValues[it.key] = Pair(colorToken, Color(it.value))
			defaultCurrentTheme[it.key] = Pair(colorToken, Color(it.value))
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
		val result = "${
			defaultLightTheme(palette).entries.joinToString("\n")
				.replace(")", "")
				.replace("(", "")
				.replace(", ", "=")
		}\n"

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
		val result = "${
			defaultDarkTheme(palette).entries.joinToString("\n")
				.replace(")", "")
				.replace("(", "")
				.replace(", ", "=")
		}\n"

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

// pretend these are consts
private fun defaultLightTheme(palette: FullPaletteList): Map<String, Int> {
	val themeMap = mapOf(
		"actionBarActionModeDefault" to palette.n1_50.toArgb(),
		"actionBarActionModeDefaultIcon" to palette.a1_600.toArgb(),
		"actionBarActionModeDefaultSelector" to palette.n1_50.toArgb(),
		"actionBarActionModeDefaultTop" to palette.a1_600.toArgb(),
		"actionBarBrowser" to palette.n1_50.toArgb(),
		"actionBarDefault" to palette.n1_50.toArgb(),
		"actionBarDefaultArchived" to palette.n1_50.toArgb(),
		"actionBarDefaultArchivedIcon" to palette.a1_600.toArgb(),
		"actionBarDefaultArchivedSearch" to palette.a2_200.toArgb(),
		"actionBarDefaultArchivedSelector" to palette.n2_800.toArgb(),
		"actionBarDefaultArchivedTitle" to palette.a1_600.toArgb(),
		"actionBarDefaultIcon" to palette.a1_600.toArgb(),
		"actionBarDefaultSearch" to palette.a1_1000.toArgb(),
		"actionBarDefaultSearchArchivedPlaceholder" to palette.a1_0.toArgb(),
		"actionBarDefaultSearchPlaceholder" to palette.a1_600.toArgb(),
		"actionBarDefaultSelector" to palette.n2_800.toArgb(),
		"actionBarDefaultSubmenuBackground" to palette.n1_50.toArgb(),
		"actionBarDefaultSubmenuItem" to palette.a1_1000.toArgb(),
		"actionBarDefaultSubmenuItemIcon" to palette.a1_600.toArgb(),
		"actionBarDefaultSubmenuSeparator" to palette.n1_50.toArgb(),
		"actionBarDefaultSubtitle" to palette.a1_1000.toArgb(),
		"actionBarDefaultTitle" to palette.a1_1000.toArgb(),
		"actionBarTabActiveText" to palette.a1_600.toArgb(),
		"actionBarTabLine" to palette.a1_600.toArgb(),
		"actionBarTabSelector" to palette.a1_700.toArgb(),
		"actionBarTabUnactiveText" to palette.a2_800.toArgb(),
		"actionBarTipBackground" to palette.n1_100.toArgb(),
		"actionBarWhiteSelector" to palette.n2_800.toArgb(),
		"avatar_actionBarIconBlue" to palette.a1_600.toArgb(),
		"avatar_actionBarSelectorBlue" to palette.a1_600.toArgb(),
		"avatar_backgroundActionBarBlue" to palette.n1_50.toArgb(),
		"avatar_backgroundArchived" to palette.a1_600.toArgb(),
		"avatar_backgroundArchivedHidden" to palette.a1_600.toArgb(),
		"avatar_backgroundBlue" to palette.a1_500.toArgb(),
		"avatar_backgroundCyan" to palette.a1_500.toArgb(),
		"avatar_backgroundGreen" to palette.a1_500.toArgb(),
		"avatar_backgroundInProfileBlue" to palette.a1_600.toArgb(),
		"avatar_backgroundOrange" to palette.a1_500.toArgb(),
		"avatar_backgroundPink" to palette.a1_500.toArgb(),
		"avatar_backgroundRed" to palette.a1_500.toArgb(),
		"avatar_backgroundSaved" to palette.a1_500.toArgb(),
		"avatar_backgroundViolet" to palette.a1_500.toArgb(),
		"avatar_background2Blue" to palette.a1_600.toArgb(),
		"avatar_background2Cyan" to palette.a1_600.toArgb(),
		"avatar_background2Green" to palette.a1_600.toArgb(),
		"avatar_background2Orange" to palette.a1_600.toArgb(),
		"avatar_background2Pink" to palette.a1_600.toArgb(),
		"avatar_background2Red" to palette.a1_600.toArgb(),
		"avatar_background2Saved" to palette.a1_600.toArgb(),
		"avatar_background2Violet" to palette.a1_600.toArgb(),
		"avatar_nameInMessageBlue" to palette.a1_400.toArgb(),
		"avatar_nameInMessageCyan" to palette.a1_400.toArgb(),
		"avatar_nameInMessageGreen" to palette.a1_400.toArgb(),
		"avatar_nameInMessageOrange" to palette.a1_400.toArgb(),
		"avatar_nameInMessagePink" to palette.a1_400.toArgb(),
		"avatar_nameInMessageRed" to palette.a1_400.toArgb(),
		"avatar_nameInMessageViolet" to palette.a1_400.toArgb(),
		"avatar_subtitleInProfileBlue" to palette.a1_1000.toArgb(),
		"avatar_text" to palette.n1_50.toArgb(),
		"calls_callReceivedGreenIcon" to 0xFFFFFFFF,
		"calls_callReceivedRedIcon" to 0xFFFFFFFF,
		"changephoneinfo_image" to palette.a1_600.toArgb(),
		"changephoneinfo_image2" to palette.a1_600.toArgb(),
		"chats_actionBackground" to palette.a1_400.toArgb(),
		"chats_actionIcon" to palette.n1_1000.toArgb(),
		"chats_actionMessage" to palette.a1_800.toArgb(),
		"chats_actionPressedBackground" to palette.a1_200.toArgb(),
		"chats_actionUnreadBackground" to palette.a1_600.toArgb(),
		"chats_actionUnreadIcon" to palette.n1_50.toArgb(),
		"chats_actionUnreadPressedBackground" to palette.a1_200.toArgb(),
		"chats_archiveBackground" to palette.a1_600.toArgb(),
		"chats_archiveIcon" to palette.a3_200.toArgb(),
		"chats_archivePinBackground" to palette.a2_600.toArgb(),
		"chats_archivePullDownBackground" to palette.n1_400.toArgb(),
		"chats_archivePullDownBackgroundActive" to palette.a2_300.toArgb(),
		"chats_archiveText" to palette.a3_200.toArgb(),
		"chats_attachMessage" to palette.a1_1000.toArgb(),
		"chats_date" to palette.a1_600.toArgb(),
		"chats_draft" to palette.a1_600.toArgb(),
		"chats_mentionIcon" to palette.n1_50.toArgb(),
		"chats_menuBackground" to palette.n1_50.toArgb(),
		"chats_menuCloud" to palette.n1_900.toArgb(),
		"chats_menuCloudBackgroundCats" to palette.n1_50.toArgb(),
		"chats_menuItemCheck" to palette.a3_200.toArgb(),
		"chats_menuItemIcon" to palette.a1_600.toArgb(),
		"chats_menuItemText" to palette.a1_1000.toArgb(),
		"chats_menuName" to palette.a1_600.toArgb(),
		"chats_menuPhone" to palette.a1_0.toArgb(),
		"chats_menuPhoneCats" to palette.a1_600.toArgb(),
		"chats_menuTopBackgroundCats" to palette.n1_50.toArgb(),
		"chats_menuTopShadow" to palette.n1_800.toArgb(),
		"chats_menuTopShadowCats" to palette.n1_50.toArgb(),
		"chats_message" to palette.a1_1000.toArgb(),
		"chats_messageArchived" to palette.a1_1000.toArgb(),
		"chats_message_threeLines" to palette.a1_1000.toArgb(),
		"chats_muteIcon" to palette.a1_600.toArgb(),
		"chats_name" to palette.a1_600.toArgb(),
		"chats_nameArchived" to palette.a1_600.toArgb(),
		"chats_nameIcon" to palette.n1_50.toArgb(),
		"chats_nameMessage" to palette.a1_600.toArgb(),
		"chats_nameMessageArchived" to palette.a1_600.toArgb(),
		"chats_nameMessageArchived_threeLines" to palette.a1_600.toArgb(),
		"chats_nameMessage_threeLines" to palette.a1_600.toArgb(),
		"chats_onlineCircle" to palette.a1_600.toArgb(),
		"chats_pinnedIcon" to palette.a1_600.toArgb(),
		"chats_pinnedOverlay" to palette.n1_50.toArgb(),
		"chats_secretIcon" to palette.a1_600.toArgb(),
		"chats_secretName" to palette.a1_600.toArgb(),
		"chats_sentCheck" to palette.a1_600.toArgb(),
		"chats_sentClock" to palette.a1_600.toArgb(),
		"chats_sentError" to 0xFFFFFFFF,
		"chats_sentErrorIcon" to 0xFFFFFFFF,
		"chats_sentReadCheck" to palette.a1_600.toArgb(),
		"chats_tabletSelectedOverlay" to palette.n1_100.toArgb(),
		"chats_tabUnreadActiveBackground" to palette.a1_600.toArgb(),
		"chats_tabUnreadUnactiveBackground" to palette.a2_800.toArgb(),
		"chats_unreadCounter" to palette.a1_600.toArgb(),
		"chats_unreadCounterMuted" to palette.n1_400.toArgb(),
		"chats_unreadCounterText" to palette.n1_50.toArgb(),
		"chats_verifiedBackground" to palette.a1_600.toArgb(),
		"chats_verifiedCheck" to palette.n1_50.toArgb(),
		"chat_addContact" to palette.a1_600.toArgb(),
		"chat_adminSelectedText" to palette.a1_1000.toArgb(),
		"chat_adminText" to palette.a1_1000.toArgb(),
		"chat_attachActiveTab" to palette.a1_600.toArgb(),
		"chat_attachAudioBackground" to palette.a1_600.toArgb(),
		"chat_attachAudioIcon" to palette.n1_50.toArgb(),
		"chat_attachAudioText" to palette.a1_1000.toArgb(),
		"chat_attachCheckBoxBackground" to palette.a1_600.toArgb(),
		"chat_attachCheckBoxCheck" to palette.n1_50.toArgb(),
		"chat_attachContactBackground" to palette.a1_600.toArgb(),
		"chat_attachContactIcon" to palette.n1_50.toArgb(),
		"chat_attachContactText" to palette.a1_1000.toArgb(),
		"chat_attachEmptyImage" to palette.a1_600.toArgb(),
		"chat_attachFileBackground" to palette.a1_600.toArgb(),
		"chat_attachFileIcon" to palette.n1_50.toArgb(),
		"chat_attachFileText" to palette.a1_1000.toArgb(),
		"chat_attachGalleryBackground" to palette.a1_600.toArgb(),
		"chat_attachGalleryIcon" to palette.n1_50.toArgb(),
		"chat_attachGalleryText" to palette.a1_1000.toArgb(),
		"chat_attachLocationBackground" to palette.a1_600.toArgb(),
		"chat_attachLocationIcon" to palette.n1_50.toArgb(),
		"chat_attachLocationText" to palette.a1_1000.toArgb(),
		"chat_attachMediaBanBackground" to palette.n1_50.toArgb(),
		"chat_attachMediaBanText" to palette.a1_1000.toArgb(),
		"chat_attachPermissionImage" to palette.a1_500.toArgb(),
		"chat_attachPermissionMark" to 0xFFFFFFFF,
		"chat_attachPermissionText" to palette.a1_500.toArgb(),
		"chat_attachPhotoBackground" to 0xFFFFFFFF,
		"chat_attachPollBackground" to palette.a1_600.toArgb(),
		"chat_attachPollIcon" to palette.n1_50.toArgb(),
		"chat_attachPollText" to palette.a1_1000.toArgb(),
		"chat_attachUnactiveTab" to palette.n1_300.toArgb(),
		"chat_BlurAlpha" to 0xFFFFFFFF,
		"chat_botButtonText" to palette.n1_50.toArgb(),
		"chat_botKeyboardButtonBackground" to palette.a1_600.toArgb(),
		"chat_botKeyboardButtonBackgroundPressed" to palette.n1_50.toArgb(),
		"chat_botKeyboardButtonText" to palette.n1_50.toArgb(),
		"chat_botProgress" to palette.a3_50.toArgb(),
		"chat_botSwitchToInlineText" to palette.n1_50.toArgb(),
		"chat_emojiBottomPanelIcon" to palette.a2_300.toArgb(),
		"chat_emojiPanelBackground" to palette.n1_50.toArgb(),
		"chat_emojiPanelBackspace" to palette.a2_700.toArgb(),
		"chat_emojiPanelBadgeBackground" to palette.n1_50.toArgb(),
		"chat_emojiPanelBadgeText" to palette.n1_900.toArgb(),
		"chat_emojiPanelEmptyText" to palette.a1_600.toArgb(),
		"chat_emojiPanelIcon" to palette.a2_300.toArgb(),
		"chat_emojiPanelIconSelected" to palette.a1_600.toArgb(),
		"chat_emojiPanelIconSelector" to palette.a1_600.toArgb(),
		"chat_emojiPanelMasksIcon" to palette.a1_600.toArgb(),
		"chat_emojiPanelMasksIconSelected" to palette.a1_400.toArgb(),
		"chat_emojiPanelNewTrending" to palette.a1_600.toArgb(),
		"chat_emojiPanelShadowLine" to palette.n1_50.toArgb(),
		"chat_emojiPanelStickerPackSelector" to palette.n2_800.toArgb(),
		"chat_emojiPanelStickerPackSelectorLine" to palette.a1_600.toArgb(),
		"chat_emojiPanelStickerSetName" to palette.a2_500.toArgb(),
		"chat_emojiPanelStickerSetNameHighlight" to palette.a3_300.toArgb(),
		"chat_emojiPanelStickerSetNameIcon" to palette.a1_600.toArgb(),
		"chat_emojiPanelTrendingDescription" to palette.a1_600.toArgb(),
		"chat_emojiPanelTrendingTitle" to palette.a1_600.toArgb(),
		"chat_emojiSearchBackground" to palette.a1_100.toArgb(),
		"chat_emojiSearchIcon" to palette.a1_600.toArgb(),
		"chat_fieldOverlayText" to palette.a1_600.toArgb(),
		"chat_gifSaveHintBackground" to palette.a1_500.toArgb(),
		"chat_gifSaveHintText" to palette.n1_50.toArgb(),
		"chat_goDownButton" to palette.a1_400.toArgb(),
		"chat_goDownButtonCounter" to palette.n1_50.toArgb(),
		"chat_goDownButtonCounterBackground" to palette.a1_400.toArgb(),
		"chat_goDownButtonIcon" to palette.n1_50.toArgb(),
		"chat_goDownButtonShadow" to palette.a1_600.toArgb(),
		"chat_inAudioCacheSeekbar" to palette.a1_600.toArgb(),
		"chat_inAudioDurationSelectedText" to palette.a1_1000.toArgb(),
		"chat_inAudioDurationText" to palette.a1_600.toArgb(),
		"chat_inAudioPerfomerSelectedText" to palette.a1_1000.toArgb(),
		"chat_inAudioPerfomerText" to palette.a1_1000.toArgb(),
		"chat_inAudioProgress" to palette.a1_600.toArgb(),
		"chat_inAudioSeekbar" to palette.a2_300.toArgb(),
		"chat_inAudioSeekbarFill" to palette.a1_600.toArgb(),
		"chat_inAudioSeekbarSelected" to palette.a1_600.toArgb(),
		"chat_inAudioSelectedProgress" to palette.n1_300.toArgb(),
		"chat_inAudioTitleText" to palette.a1_1000.toArgb(),
		"chat_inBubble" to palette.a2_50.toArgb(),
		"chat_inBubbleSelected" to palette.a2_50.toArgb(),
		"chat_inBubbleShadow" to palette.a1_300.toArgb(),
		"chat_inContactBackground" to palette.a1_600.toArgb(),
		"chat_inContactIcon" to palette.a1_600.toArgb(),
		"chat_inContactNameText" to palette.a1_600.toArgb(),
		"chat_inContactPhoneSelectedText" to palette.n1_50.toArgb(),
		"chat_inContactPhoneText" to palette.a1_1000.toArgb(),
		"chat_inDownCall" to palette.a1_600.toArgb(),
		"chat_inFileBackground" to palette.a1_200.toArgb(),
		"chat_inFileBackgroundSelected" to palette.a1_200.toArgb(),
		"chat_inFileIcon" to palette.a1_600.toArgb(),
		"chat_inFileInfoSelectedText" to palette.a1_1000.toArgb(),
		"chat_inFileInfoText" to palette.a1_1000.toArgb(),
		"chat_inFileNameText" to palette.a1_600.toArgb(),
		"chat_inFileProgress" to palette.a1_600.toArgb(),
		"chat_inFileProgressSelected" to palette.a1_600.toArgb(),
		"chat_inFileSelectedIcon" to palette.a1_600.toArgb(),
		"chat_inForwardedNameText" to palette.a1_1000.toArgb(),
		"chat_inInstant" to palette.a1_600.toArgb(),
		"chat_inInstantSelected" to palette.a1_600.toArgb(),
		"chat_inlineResultIcon" to palette.a1_600.toArgb(),
		"chat_inLoader" to palette.a1_600.toArgb(),
		"chat_inLoaderPhoto" to palette.a1_600.toArgb(),
		"chat_inLoaderPhotoIcon" to palette.n1_50.toArgb(),
		"chat_inLoaderPhotoIconSelected" to palette.n1_50.toArgb(),
		"chat_inLoaderPhotoSelected" to palette.a1_600.toArgb(),
		"chat_inLoaderSelected" to palette.a1_600.toArgb(),
		"chat_inLocationBackground" to palette.n1_50.toArgb(),
		"chat_inLocationIcon" to palette.a1_600.toArgb(),
		"chat_inMediaIcon" to palette.n1_50.toArgb(),
		"chat_inMediaIconSelected" to palette.n1_50.toArgb(),
		"chat_inMenu" to palette.a1_600.toArgb(),
		"chat_inMenuSelected" to palette.a1_600.toArgb(),
		"chat_inPollCorrectAnswer" to palette.a3_600.toArgb(),
		"chat_inPollWrongAnswer" to palette.a2_500.toArgb(),
		"chat_inPreviewInstantSelectedText" to palette.a1_1000.toArgb(),
		"chat_inPreviewInstantText" to palette.n1_1000.toArgb(),
		"chat_inPreviewLine" to palette.a1_600.toArgb(),
		"chat_inPsaNameText" to palette.n1_900.toArgb(),
		"chat_inReactionButtonBackground" to palette.a1_600.toArgb(),
		"chat_inReactionButtonText" to palette.a1_1000.toArgb(),
		"chat_inReactionButtonTextSelected" to palette.n1_50.toArgb(),
		"chat_inReplyLine" to palette.a1_600.toArgb(),
		"chat_inReplyMediaMessageSelectedText" to palette.a1_1000.toArgb(),
		"chat_inReplyMediaMessageText" to palette.a1_1000.toArgb(),
		"chat_inReplyMessageText" to palette.a1_1000.toArgb(),
		"chat_inReplyNameText" to palette.a1_600.toArgb(),
		"chat_inSentClock" to palette.a1_1000.toArgb(),
		"chat_inSentClockSelected" to palette.a1_1000.toArgb(),
		"chat_inSiteNameText" to palette.a1_1000.toArgb(),
		"chat_inTextSelectionHighlight" to 0xFFFFFFFF,
		"chat_inTimeSelectedText" to palette.a1_1000.toArgb(),
		"chat_inTimeText" to palette.a1_1000.toArgb(),
		"chat_inUpCall" to 0xFFFFFFFF,
		"chat_inVenueInfoSelectedText" to palette.a1_1000.toArgb(),
		"chat_inVenueInfoText" to palette.n1_50.toArgb(),
		"chat_inViaBotNameText" to palette.a1_600.toArgb(),
		"chat_inViews" to palette.a1_1000.toArgb(),
		"chat_inViewsSelected" to palette.a1_1000.toArgb(),
		"chat_inVoiceSeekbar" to palette.n1_300.toArgb(),
		"chat_inVoiceSeekbarFill" to palette.a1_600.toArgb(),
		"chat_inVoiceSeekbarSelected" to palette.n1_300.toArgb(),
		"chat_linkSelectBackground" to palette.n1_400.toArgb(),
		"chat_lockIcon" to palette.a1_600.toArgb(),
		"chat_mediaInfoText" to palette.n1_50.toArgb(),
		"chat_mediaLoaderPhoto" to palette.a1_600.toArgb(),
		"chat_mediaLoaderPhotoIcon" to palette.n1_50.toArgb(),
		"chat_mediaLoaderPhotoIconSelected" to palette.n1_50.toArgb(),
		"chat_mediaLoaderPhotoSelected" to palette.a1_600.toArgb(),
		"chat_mediaMenu" to palette.a1_600.toArgb(),
		"chat_mediaProgress" to palette.a1_600.toArgb(),
		"chat_mediaSentCheck" to palette.n1_50.toArgb(),
		"chat_mediaSentClock" to palette.n1_50.toArgb(),
		"chat_mediaTimeBackground" to palette.a1_600.toArgb(),
		"chat_mediaTimeText" to palette.n1_50.toArgb(),
		"chat_mediaViews" to palette.n1_50.toArgb(),
		"chat_messageLinkIn" to palette.a3_500.toArgb(),
		"chat_messageLinkOut" to palette.a3_200.toArgb(),
		"chat_messagePanelBackground" to palette.n1_50.toArgb(),
		"chat_messagePanelCancelInlineBot" to palette.a1_600.toArgb(),
		"chat_messagePanelCursor" to palette.a1_600.toArgb(),
		"chat_messagePanelHint" to palette.n1_300.toArgb(),
		"chat_messagePanelIcons" to palette.a1_600.toArgb(),
		"chat_messagePanelSend" to palette.a1_600.toArgb(),
		"chat_messagePanelShadow" to palette.n1_50.toArgb(),
		"chat_messagePanelText" to palette.a1_1000.toArgb(),
		"chat_messagePanelVideoFrame" to palette.a1_600.toArgb(),
		"chat_messagePanelVoiceBackground" to palette.a1_600.toArgb(),
		"chat_messagePanelVoiceDelete" to palette.a1_600.toArgb(),
		"chat_messagePanelVoiceDuration" to palette.n1_50.toArgb(),
		"chat_messagePanelVoicePressed" to palette.n1_50.toArgb(),
		"chat_messageTextIn" to palette.a1_1000.toArgb(),
		"chat_messageTextOut" to palette.n1_50.toArgb(),
		"chat_muteIcon" to palette.a1_600.toArgb(),
		"chat_outAdminSelectedText" to palette.n1_0.toArgb(),
		"chat_outAdminText" to palette.n1_0.toArgb(),
		"chat_outAudioCacheSeekbar" to palette.n1_50.toArgb(),
		"chat_outAudioDurationSelectedText" to palette.n1_50.toArgb(),
		"chat_outAudioDurationText" to palette.n1_50.toArgb(),
		"chat_outAudioPerfomerSelectedText" to palette.n1_50.toArgb(),
		"chat_outAudioPerfomerText" to palette.n1_50.toArgb(),
		"chat_outAudioProgress" to palette.a1_200.toArgb(),
		"chat_outAudioSeekbar" to palette.n1_300.toArgb(),
		"chat_outAudioSeekbarFill" to palette.a1_300.toArgb(),
		"chat_outAudioSeekbarSelected" to palette.n1_300.toArgb(),
		"chat_outAudioSelectedProgress" to palette.a1_200.toArgb(),
		"chat_outAudioTitleText" to palette.n1_50.toArgb(),
		"chat_outBubble" to palette.a1_600.toArgb(),
		"chat_outBubbleGradient" to palette.a1_600.toArgb(),
		"chat_outBubbleGradient2" to palette.a1_600.toArgb(),
		"chat_outBubbleGradient3" to palette.a1_700.toArgb(),
		"chat_outBubbleGradientAnimated" to 0xFFFFFFFF,
		"chat_outBubbleGradientSelectedOverlay" to palette.a1_600.toArgb(),
		"chat_outBubbleSelected" to palette.a1_600.toArgb(),
		"chat_outBubbleShadow" to palette.n2_800.toArgb(),
		"chat_outContactBackground" to palette.n1_50.toArgb(),
		"chat_outContactIcon" to palette.n1_50.toArgb(),
		"chat_outContactNameText" to palette.n1_50.toArgb(),
		"chat_outContactPhoneSelectedText" to palette.n1_50.toArgb(),
		"chat_outContactPhoneText" to palette.n1_50.toArgb(),
		"chat_outFileBackground" to palette.n1_50.toArgb(),
		"chat_outFileBackgroundSelected" to palette.a1_600.toArgb(),
		"chat_outFileIcon" to palette.n1_50.toArgb(),
		"chat_outFileInfoSelectedText" to palette.n1_50.toArgb(),
		"chat_outFileInfoText" to palette.n1_50.toArgb(),
		"chat_outFileNameText" to palette.n1_50.toArgb(),
		"chat_outFileProgress" to palette.n1_50.toArgb(),
		"chat_outFileProgressSelected" to palette.n1_50.toArgb(),
		"chat_outFileSelectedIcon" to palette.n1_50.toArgb(),
		"chat_outForwardedNameText" to palette.n1_50.toArgb(),
		"chat_outInstant" to palette.a1_10.toArgb(),
		"chat_outInstantSelected" to palette.a1_10.toArgb(),
		"chat_outLinkSelectBackground" to palette.n1_400.toArgb(),
		"chat_outLoader" to palette.n1_50.toArgb(),
		"chat_outLoaderPhoto" to palette.a1_600.toArgb(),
		"chat_outLoaderPhotoIcon" to palette.a1_600.toArgb(),
		"chat_outLoaderPhotoIconSelected" to palette.n1_50.toArgb(),
		"chat_outLoaderPhotoSelected" to palette.n1_50.toArgb(),
		"chat_outLoaderSelected" to palette.n1_50.toArgb(),
		"chat_outLocationBackground" to palette.n1_50.toArgb(),
		"chat_outLocationIcon" to palette.n1_50.toArgb(),
		"chat_outMediaIcon" to palette.a1_600.toArgb(),
		"chat_outMediaIconSelected" to palette.a1_600.toArgb(),
		"chat_outMenu" to palette.n1_50.toArgb(),
		"chat_outMenuSelected" to palette.n1_50.toArgb(),
		"chat_outPollCorrectAnswer" to palette.a3_300.toArgb(),
		"chat_outPollWrongAnswer" to palette.a2_300.toArgb(),
		"chat_outPreviewInstantSelectedText" to palette.a1_1000.toArgb(),
		"chat_outPreviewInstantText" to palette.n1_50.toArgb(),
		"chat_outPreviewLine" to palette.n1_50.toArgb(),
		"chat_outPsaNameText" to palette.n1_50.toArgb(),
		"chat_outReactionButtonBackground" to palette.a1_200.toArgb(),
		"chat_outReactionButtonText" to palette.n1_0.toArgb(),
		"chat_outReactionButtonTextSelected" to palette.n1_1000.toArgb(),
		"chat_outReplyLine" to palette.n1_50.toArgb(),
		"chat_outReplyMediaMessageSelectedText" to palette.n1_50.toArgb(),
		"chat_outReplyMediaMessageText" to palette.n1_50.toArgb(),
		"chat_outReplyMessageText" to palette.n1_50.toArgb(),
		"chat_outReplyNameText" to palette.n1_50.toArgb(),
		"chat_outSentCheck" to palette.a2_200.toArgb(),
		"chat_outSentCheckRead" to palette.a2_200.toArgb(),
		"chat_outSentCheckReadSelected" to palette.a2_200.toArgb(),
		"chat_outSentCheckSelected" to palette.a2_200.toArgb(),
		"chat_outSentClock" to palette.a2_200.toArgb(),
		"chat_outSentClockSelected" to palette.a2_200.toArgb(),
		"chat_outSiteNameText" to palette.n1_50.toArgb(),
		"chat_outTextSelectionCursor" to palette.a2_300.toArgb(),
		"chat_outTextSelectionHighlight" to palette.n1_700.toArgb(),
		"chat_outTimeSelectedText" to palette.n1_50.toArgb(),
		"chat_outTimeText" to palette.n1_50.toArgb(),
		"chat_outUpCall" to palette.a1_300.toArgb(),
		"chat_outVenueInfoSelectedText" to palette.n1_50.toArgb(),
		"chat_outVenueInfoText" to palette.n1_50.toArgb(),
		"chat_outViaBotNameText" to palette.n1_50.toArgb(),
		"chat_outViews" to palette.n1_50.toArgb(),
		"chat_outViewsSelected" to palette.n1_50.toArgb(),
		"chat_outVoiceSeekbar" to palette.n1_300.toArgb(),
		"chat_outVoiceSeekbarFill" to palette.n1_50.toArgb(),
		"chat_outVoiceSeekbarSelected" to palette.n1_300.toArgb(),
		"chat_previewDurationText" to palette.n1_50.toArgb(),
		"chat_previewGameText" to palette.n1_50.toArgb(),
		"chat_recordedVoiceBackground" to palette.a1_600.toArgb(),
		"chat_recordedVoiceDot" to palette.a1_600.toArgb(),
		"chat_recordedVoicePlayPause" to palette.a1_100.toArgb(),
		"chat_recordedVoiceProgress" to palette.n1_300.toArgb(),
		"chat_recordedVoiceProgressInner" to palette.n1_50.toArgb(),
		"chat_recordTime" to palette.a1_600.toArgb(),
		"chat_recordVoiceCancel" to palette.a1_600.toArgb(),
		"chat_replyPanelClose" to palette.a1_600.toArgb(),
		"chat_replyPanelIcons" to palette.a1_600.toArgb(),
		"chat_replyPanelLine" to palette.n1_50.toArgb(),
		"chat_replyPanelMessage" to palette.n1_50.toArgb(),
		"chat_replyPanelName" to palette.a1_1000.toArgb(),
		"chat_reportSpam" to palette.a3_600.toArgb(),
		"chat_searchPanelIcons" to palette.a1_600.toArgb(),
		"chat_searchPanelText" to palette.a1_1000.toArgb(),
		"chat_secretChatStatusText" to palette.a1_600.toArgb(),
		"chat_secretTimerBackground" to palette.n1_50.toArgb(),
		"chat_secretTimerText" to palette.a1_0.toArgb(),
		"chat_secretTimeText" to palette.a1_0.toArgb(),
		"chat_selectedBackground" to palette.n2_100.toArgb(),
		"chat_sentError" to 0xFFFFFFFF,
		"chat_sentErrorIcon" to 0xFFFFFFFF,
		"chat_serviceBackground" to palette.a1_600.toArgb(),
		"chat_serviceBackgroundSelected" to palette.a1_600.toArgb(),
		"chat_serviceBackgroundSelector" to palette.a1_700.toArgb(),
		"chat_serviceIcon" to palette.n1_50.toArgb(),
		"chat_serviceLink" to palette.n1_50.toArgb(),
		"chat_serviceText" to palette.n1_50.toArgb(),
		"chat_status" to palette.a1_600.toArgb(),
		"chat_stickerNameText" to palette.n1_50.toArgb(),
		"chat_stickerReplyLine" to palette.n1_50.toArgb(),
		"chat_stickerReplyMessageText" to palette.n1_50.toArgb(),
		"chat_stickerReplyNameText" to palette.n1_50.toArgb(),
		"chat_stickersHintPanel" to palette.n1_50.toArgb(),
		"chat_stickerViaBotNameText" to palette.a1_0.toArgb(),
		"chat_textSelectBackground" to palette.a1_200.toArgb(),
		"chat_TextSelectionCursor" to palette.a3_500.toArgb(),
		"chat_topPanelBackground" to palette.n1_50.toArgb(),
		"chat_topPanelClose" to palette.a1_600.toArgb(),
		"chat_topPanelLine" to palette.a1_600.toArgb(),
		"chat_topPanelMessage" to palette.a1_1000.toArgb(),
		"chat_topPanelTitle" to palette.a1_600.toArgb(),
		"chat_unreadMessagesStartArrowIcon" to palette.a1_600.toArgb(),
		"chat_unreadMessagesStartBackground" to palette.n1_50.toArgb(),
		"chat_unreadMessagesStartText" to palette.a1_1000.toArgb(),
		"chat_wallpaper" to palette.n1_50.toArgb(),
		"checkbox" to palette.a1_600.toArgb(),
		"checkboxCheck" to palette.a1_50.toArgb(),
		"checkboxDisabled" to palette.a2_200.toArgb(),
		"checkboxSquareBackground" to palette.a1_600.toArgb(),
		"checkboxSquareCheck" to palette.n1_50.toArgb(),
		"checkboxSquareDisabled" to palette.a1_600.toArgb(),
		"checkboxSquareUnchecked" to palette.n2_500.toArgb(),
		"contacts_inviteBackground" to palette.n2_800.toArgb(),
		"contacts_inviteText" to palette.n1_50.toArgb(),
		"contextProgressInner1" to palette.a1_1000.toArgb(),
		"contextProgressInner2" to palette.n1_100.toArgb(),
		"contextProgressInner3" to palette.n1_600.toArgb(),
		"contextProgressInner4" to palette.a1_1000.toArgb(),
		"contextProgressOuter1" to palette.n1_50.toArgb(),
		"contextProgressOuter2" to palette.a1_400.toArgb(),
		"contextProgressOuter3" to palette.a1_200.toArgb(),
		"contextProgressOuter4" to palette.n1_50.toArgb(),
		"dialogBackground" to palette.n1_50.toArgb(),
		"dialogBackgroundGray" to palette.n1_50.toArgb(),
		"dialogBadgeBackground" to palette.a1_600.toArgb(),
		"dialogBadgeText" to palette.a1_600.toArgb(),
		"dialogButton" to palette.a1_600.toArgb(),
		"dialogButtonSelector" to palette.a1_600.toArgb(),
		"dialogCameraIcon" to palette.a1_600.toArgb(),
		"dialogCheckboxSquareBackground" to palette.a1_600.toArgb(),
		"dialogCheckboxSquareCheck" to palette.n1_50.toArgb(),
		"dialogCheckboxSquareDisabled" to palette.a1_700.toArgb(),
		"dialogCheckboxSquareUnchecked" to palette.a1_600.toArgb(),
		"dialogEmptyImage" to palette.a1_900.toArgb(),
		"dialogEmptyText" to palette.a1_900.toArgb(),
		"dialogFloatingButton" to palette.a1_600.toArgb(),
		"dialogFloatingButtonPressed" to palette.a2_600.toArgb(),
		"dialogFloatingIcon" to palette.n1_50.toArgb(),
		"dialogGrayLine" to palette.n1_50.toArgb(),
		"dialogIcon" to palette.a1_600.toArgb(),
		"dialogInputField" to palette.n1_50.toArgb(),
		"dialogInputFieldActivated" to palette.a1_600.toArgb(),
		"dialogLineProgress" to palette.a1_600.toArgb(),
		"dialogLineProgressBackground" to palette.a1_200.toArgb(),
		"dialogLinkSelection" to palette.a1_600.toArgb(),
		"dialogProgressCircle" to palette.a1_600.toArgb(),
		"dialogRadioBackground" to palette.a1_600.toArgb(),
		"dialogRadioBackgroundChecked" to palette.a1_600.toArgb(),
		"dialogReactionMentionBackground" to palette.a3_700.toArgb(),
		"dialogRedIcon" to 0xFFFFFFFF,
		"dialogRoundCheckBox" to palette.a1_600.toArgb(),
		"dialogRoundCheckBoxCheck" to palette.n1_50.toArgb(),
		"dialogScrollGlow" to palette.n1_100.toArgb(),
		"dialogSearchBackground" to palette.n1_50.toArgb(),
		"dialogSearchHint" to palette.a1_600.toArgb(),
		"dialogSearchIcon" to palette.a1_600.toArgb(),
		"dialogSearchText" to palette.n1_900.toArgb(),
		"dialogShadowLine" to palette.n1_50.toArgb(),
		"dialogSwipeRemove" to palette.a3_700.toArgb(),
		"dialogTextBlack" to palette.a1_1000.toArgb(),
		"dialogTextBlue" to palette.a1_1000.toArgb(),
		"dialogTextBlue2" to palette.a1_600.toArgb(),
		"dialogTextBlue3" to palette.a1_600.toArgb(),
		"dialogTextBlue4" to palette.a1_600.toArgb(),
		"dialogTextGray" to palette.a1_600.toArgb(),
		"dialogTextGray2" to palette.a1_600.toArgb(),
		"dialogTextGray3" to palette.a1_600.toArgb(),
		"dialogTextGray4" to palette.a1_600.toArgb(),
		"dialogTextHint" to palette.n1_300.toArgb(),
		"dialogTextLink" to palette.a1_600.toArgb(),
		"dialogTextRed" to 0xFFFFFFFF,
		"dialogTextRed2" to 0xFFFFFFFF,
		"dialogTopBackground" to palette.n1_500.toArgb(),
		"dialog_inlineProgress" to palette.a1_600.toArgb(),
		"dialog_inlineProgressBackground" to palette.n1_50.toArgb(),
		"dialog_liveLocationProgress" to palette.a1_600.toArgb(),
		"divider" to palette.n1_50.toArgb(),
		"emptyListPlaceholder" to palette.n2_800.toArgb(),
		"fastScrollActive" to palette.a1_200.toArgb(),
		"fastScrollInactive" to palette.a1_600.toArgb(),
		"fastScrollText" to palette.n1_800.toArgb(),
		"featuredStickers_addButton" to palette.a1_600.toArgb(),
		"featuredStickers_addButtonPressed" to palette.a1_600.toArgb(),
		"featuredStickers_addedIcon" to palette.a1_600.toArgb(),
		"featuredStickers_buttonProgress" to palette.a1_600.toArgb(),
		"featuredStickers_buttonText" to palette.n1_50.toArgb(),
		"featuredStickers_removeButtonText" to palette.a3_500.toArgb(),
		"featuredStickers_unread" to palette.a1_600.toArgb(),
		"files_folderIcon" to palette.a1_0.toArgb(),
		"files_folderIconBackground" to palette.a1_600.toArgb(),
		"files_iconText" to palette.a1_1000.toArgb(),
		"graySection" to palette.n1_50.toArgb(),
		"groupcreate_cursor" to palette.a1_600.toArgb(),
		"groupcreate_hintText" to palette.a1_700.toArgb(),
		"groupcreate_sectionShadow" to palette.n1_0.toArgb(),
		"groupcreate_sectionText" to palette.n1_50.toArgb(),
		"groupcreate_spanBackground" to palette.a2_200.toArgb(),
		"groupcreate_spanDelete" to palette.a1_700.toArgb(),
		"groupcreate_spanText" to palette.a1_0.toArgb(),
		"inappPlayerBackground" to palette.n1_50.toArgb(),
		"inappPlayerClose" to palette.a1_1000.toArgb(),
		"inappPlayerPerformer" to palette.a1_600.toArgb(),
		"inappPlayerPlayPause" to palette.a1_600.toArgb(),
		"inappPlayerTitle" to palette.a1_1000.toArgb(),
		"key_chat_messagePanelVoiceLock" to palette.n1_50.toArgb(),
		"key_chat_messagePanelVoiceLockBackground" to palette.a1_500.toArgb(),
		"key_chat_messagePanelVoiceLockShadow" to palette.a2_600.toArgb(),
		"key_chat_recordedVoiceHighlight" to palette.a2_500.toArgb(),
		"key_graySectionText" to palette.a1_600.toArgb(),
		"key_player_progressCachedBackground" to palette.a2_300.toArgb(),
		"key_sheet_other" to palette.a1_600.toArgb(),
		"key_sheet_scrollUp" to palette.a1_600.toArgb(),
		"kvoipgroup_overlayAlertMutedByAdmin2" to palette.a3_100.toArgb(),
		"listSelectorSDK21" to palette.a1_100.toArgb(),
		"location_actionActiveIcon" to palette.a1_600.toArgb(),
		"location_actionBackground" to palette.n1_50.toArgb(),
		"location_actionIcon" to palette.a1_600.toArgb(),
		"location_actionPressedBackground" to palette.n1_50.toArgb(),
		"location_liveLocationProgress" to palette.a1_600.toArgb(),
		"location_placeLocationBackground" to palette.n1_50.toArgb(),
		"location_sendLiveLocationBackground" to palette.a1_600.toArgb(),
		"location_sendLiveLocationIcon" to palette.a1_0.toArgb(),
		"location_sendLiveLocationText" to palette.a1_600.toArgb(),
		"location_sendLocationBackground" to palette.a1_600.toArgb(),
		"location_sendLocationIcon" to palette.a1_0.toArgb(),
		"location_sendLocationText" to palette.a1_600.toArgb(),
		"login_progressInner" to palette.a1_100.toArgb(),
		"login_progressOuter" to palette.a1_700.toArgb(),
		"musicPicker_buttonBackground" to palette.a1_600.toArgb(),
		"musicPicker_buttonIcon" to palette.a1_50.toArgb(),
		"musicPicker_checkbox" to palette.a1_600.toArgb(),
		"musicPicker_checkboxCheck" to palette.n1_900.toArgb(),
		"passport_authorizeBackground" to palette.n1_50.toArgb(),
		"passport_authorizeBackgroundSelected" to palette.n1_50.toArgb(),
		"passport_authorizeText" to palette.n1_900.toArgb(),
		"picker_badge" to palette.n1_50.toArgb(),
		"picker_badgeText" to palette.n1_900.toArgb(),
		"picker_disabledButton" to palette.a1_600.toArgb(),
		"picker_enabledButton" to palette.a1_600.toArgb(),
		"player_actionBar" to palette.n1_50.toArgb(),
		"player_actionBarItems" to palette.a1_1000.toArgb(),
		"player_actionBarSelector" to palette.a1_600.toArgb(),
		"player_actionBarSubtitle" to palette.a1_600.toArgb(),
		"player_actionBarTitle" to palette.a1_1000.toArgb(),
		"player_actionBarTop" to palette.a1_600.toArgb(),
		"player_background" to palette.n1_50.toArgb(),
		"player_button" to palette.a1_600.toArgb(),
		"player_buttonActive" to palette.a1_600.toArgb(),
		"player_progress" to palette.a1_600.toArgb(),
		"player_progressBackground" to palette.a2_300.toArgb(),
		"player_progressBackground2" to palette.n1_500.toArgb(),
		"player_time" to palette.a1_600.toArgb(),
		"premiumGradient0" to palette.a1_400.toArgb(),
		"premiumGradient1" to palette.a1_400.toArgb(),
		"premiumGradient2" to palette.a1_400.toArgb(),
		"premiumGradient3" to palette.a3_400.toArgb(),
		"premiumGradient4" to palette.a3_400.toArgb(),
		"premiumGradientBackground1" to palette.a3_400.toArgb(),
		"premiumGradientBackground2" to palette.a1_400.toArgb(),
		"premiumGradientBackground3" to palette.a3_400.toArgb(),
		"premiumGradientBackground4" to palette.a3_400.toArgb(),
		"premiumGradientBackgroundOverlay" to palette.n1_0.toArgb(),
		"premiumGradientBottomSheet1" to palette.a3_200.toArgb(),
		"premiumGradientBottomSheet2" to palette.a2_200.toArgb(),
		"premiumGradientBottomSheet3" to palette.a1_200.toArgb(),
		"premiumStarGradient1" to palette.n1_0.toArgb(),
		"premiumStarGradient2" to palette.n1_50.toArgb(),
		"premiumStartSmallStarsColor" to palette.n1_0.toArgb(),
		"premiumStartSmallStarsColor2" to palette.n1_50.toArgb(),
		"profile_actionBackground" to palette.a1_600.toArgb(),
		"profile_actionIcon" to palette.n1_50.toArgb(),
		"profile_actionPressedBackground" to palette.a1_200.toArgb(),
		"profile_creatorIcon" to palette.a1_600.toArgb(),
		"profile_status" to palette.a1_1000.toArgb(),
		"profile_tabSelectedLine" to palette.a1_700.toArgb(),
		"profile_tabSelectedText" to palette.a1_700.toArgb(),
		"profile_tabSelector" to palette.a1_600.toArgb(),
		"profile_tabText" to palette.n1_500.toArgb(),
		"profile_title" to palette.a1_1000.toArgb(),
		"profile_verifiedBackground" to palette.a1_600.toArgb(),
		"profile_verifiedCheck" to palette.a1_0.toArgb(),
		"progressCircle" to palette.a1_600.toArgb(),
		"radioBackground" to palette.a1_600.toArgb(),
		"radioBackgroundChecked" to palette.a1_600.toArgb(),
		"returnToCallBackground" to palette.n1_50.toArgb(),
		"returnToCallMutedBackground" to palette.n1_600.toArgb(),
		"returnToCallText" to palette.a1_1000.toArgb(),
		"sessions_devicesImage" to palette.a1_100.toArgb(),
		"sharedMedia_actionMode" to palette.a3_600.toArgb(),
		"sharedMedia_linkPlaceholder" to palette.a1_600.toArgb(),
		"sharedMedia_linkPlaceholderText" to palette.n1_50.toArgb(),
		"sharedMedia_photoPlaceholder" to palette.a1_600.toArgb(),
		"sharedMedia_startStopLoadIcon" to palette.a1_600.toArgb(),
		"statisticChartActiveLine" to palette.n1_50.toArgb(),
		"statisticChartActivePickerChart" to palette.a1_600.toArgb(),
		"statisticChartBackZoomColor" to palette.a1_600.toArgb(),
		"statisticChartCheckboxInactive" to palette.n1_50.toArgb(),
		"statisticChartChevronColor" to palette.a1_600.toArgb(),
		"statisticChartHighlightColor" to palette.a1_600.toArgb(),
		"statisticChartHintLine" to palette.a1_600.toArgb(),
		"statisticChartInactivePickerChart" to 0xFFFFFFFF,
		"statisticChartLineEmpty" to palette.n1_50.toArgb(),
		"statisticChartLine_blue" to palette.a1_300.toArgb(),
		"statisticChartLine_cyan" to palette.a2_600.toArgb(),
		"statisticChartLine_golden" to palette.a1_500.toArgb(),
		"statisticChartLine_green" to palette.a1_700.toArgb(),
		"statisticChartLine_indigo" to palette.a2_300.toArgb(),
		"statisticChartLine_lightblue" to palette.a3_300.toArgb(),
		"statisticChartLine_lightgreen" to palette.a3_500.toArgb(),
		"statisticChartLine_orange" to palette.a3_700.toArgb(),
		"statisticChartLine_purple" to palette.a2_400.toArgb(),
		"statisticChartLine_red" to palette.a2_500.toArgb(),
		"statisticChartRipple" to palette.a1_300.toArgb(),
		"statisticChartSignature" to palette.a1_600.toArgb(),
		"statisticChartSignatureAlpha" to palette.a1_600.toArgb(),
		"stickers_menu" to palette.a1_600.toArgb(),
		"stickers_menuSelector" to palette.n2_200.toArgb(),
		"switch2Track" to palette.n2_500.toArgb(),
		"switch2TrackChecked" to palette.a1_700.toArgb(),
		"switchTrack" to palette.n2_500.toArgb(),
		"switchTrackBlue" to palette.a1_600.toArgb(),
		"switchTrackBlueChecked" to palette.a1_600.toArgb(),
		"switchTrackBlueSelector" to palette.a1_600.toArgb(),
		"switchTrackBlueSelectorChecked" to palette.a1_600.toArgb(),
		"switchTrackBlueThumb" to palette.n1_50.toArgb(),
		"switchTrackBlueThumbChecked" to palette.n1_50.toArgb(),
		"switchTrackChecked" to palette.a1_700.toArgb(),
		"topics_unreadCounter" to palette.a1_600.toArgb(),
		"topics_unreadCounterMuted" to palette.n1_50.toArgb(),
		"undo_background" to palette.a1_600.toArgb(),
		"undo_cancelColor" to palette.a1_200.toArgb(),
		"undo_infoColor" to palette.n1_50.toArgb(),
		"voipgroup_actionBar" to palette.n1_900.toArgb(),
		"voipgroup_actionBarItems" to palette.n1_0.toArgb(),
		"voipgroup_actionBarItemsSelector" to palette.n1_700.toArgb(),
		"voipgroup_actionBarSubtitle" to palette.n1_0.toArgb(),
		"voipgroup_actionBarUnscrolled" to palette.n1_900.toArgb(),
		"voipgroup_blueText" to palette.a3_200.toArgb(),
		"voipgroup_checkMenu" to palette.a1_200.toArgb(),
		"voipgroup_connectingProgress" to palette.a2_100.toArgb(),
		"voipgroup_dialogBackground" to palette.n1_900.toArgb(),
		"voipgroup_disabledButton" to palette.n2_800.toArgb(),
		"voipgroup_disabledButtonActive" to palette.n2_800.toArgb(),
		"voipgroup_disabledButtonActiveScrolled" to palette.n2_800.toArgb(),
		"voipgroup_emptyView" to palette.n1_900.toArgb(),
		"voipgroup_inviteMembersBackground" to palette.n1_900.toArgb(),
		"voipgroup_lastSeenText" to palette.n1_200.toArgb(),
		"voipgroup_lastSeenTextUnscrolled" to palette.n1_200.toArgb(),
		"voipgroup_leaveButton" to palette.a3_300.toArgb(),
		"voipgroup_leaveButtonScrolled" to palette.a3_200.toArgb(),
		"voipgroup_leaveCallMenu" to palette.a1_100.toArgb(),
		"voipgroup_listeningText" to palette.a2_300.toArgb(),
		"voipgroup_listSelector" to palette.n1_700.toArgb(),
		"voipgroup_listViewBackground" to palette.n2_800.toArgb(),
		"voipgroup_listViewBackgroundUnscrolled" to palette.n2_800.toArgb(),
		"voipgroup_muteButton" to palette.a1_500.toArgb(),
		"voipgroup_muteButton2" to palette.a2_300.toArgb(),
		"voipgroup_muteButton3" to palette.a1_300.toArgb(),
		"voipgroup_mutedByAdminGradient" to palette.a3_200.toArgb(),
		"voipgroup_mutedByAdminGradient2" to palette.a3_200.toArgb(),
		"voipgroup_mutedByAdminGradient3" to palette.a3_300.toArgb(),
		"voipgroup_mutedByAdminIcon" to palette.a3_400.toArgb(),
		"voipgroup_mutedByAdminMuteButton" to palette.a3_300.toArgb(),
		"voipgroup_mutedByAdminMuteButtonDisabled" to palette.a3_200.toArgb(),
		"voipgroup_mutedIcon" to palette.a2_400.toArgb(),
		"voipgroup_mutedIconUnscrolled" to palette.a2_400.toArgb(),
		"voipgroup_nameText" to palette.n1_0.toArgb(),
		"voipgroup_overlayAlertGradientMuted" to palette.a2_400.toArgb(),
		"voipgroup_overlayAlertGradientMuted2" to palette.a2_200.toArgb(),
		"voipgroup_overlayAlertGradientUnmuted" to palette.a1_400.toArgb(),
		"voipgroup_overlayAlertGradientUnmuted2" to palette.a1_200.toArgb(),
		"voipgroup_overlayAlertMutedByAdmin" to palette.a3_300.toArgb(),
		"voipgroup_overlayBlue1" to palette.a2_400.toArgb(),
		"voipgroup_overlayBlue2" to palette.a2_200.toArgb(),
		"voipgroup_overlayGreen1" to palette.a1_400.toArgb(),
		"voipgroup_overlayGreen2" to palette.a1_200.toArgb(),
		"voipgroup_scrollUp" to palette.a1_400.toArgb(),
		"voipgroup_searchBackground" to palette.n2_700.toArgb(),
		"voipgroup_searchPlaceholder" to palette.n2_200.toArgb(),
		"voipgroup_searchText" to palette.n1_0.toArgb(),
		"voipgroup_soundButton" to palette.a3_400.toArgb(),
		"voipgroup_soundButton2" to palette.a1_400.toArgb(),
		"voipgroup_soundButtonActive" to palette.a3_400.toArgb(),
		"voipgroup_soundButtonActive2" to palette.a1_400.toArgb(),
		"voipgroup_soundButtonActive2Scrolled" to palette.a1_400.toArgb(),
		"voipgroup_soundButtonActiveScrolled" to palette.a3_400.toArgb(),
		"voipgroup_speakingText" to palette.a1_100.toArgb(),
		"voipgroup_topPanelBlue1" to palette.n2_50.toArgb(),
		"voipgroup_topPanelBlue2" to palette.n2_200.toArgb(),
		"voipgroup_topPanelGray" to palette.n2_50.toArgb(),
		"voipgroup_topPanelGreen1" to palette.a1_300.toArgb(),
		"voipgroup_topPanelGreen2" to palette.a1_500.toArgb(),
		"voipgroup_unmuteButton" to palette.a2_600.toArgb(),
		"voipgroup_unmuteButton2" to palette.a2_500.toArgb(),
		"voipgroup_windowBackgroundWhiteInputField" to palette.n1_0.toArgb(),
		"voipgroup_windowBackgroundWhiteInputFieldActivated" to palette.n1_0.toArgb(),
		"wallet_blackBackground" to palette.n1_1000.toArgb(),
		"wallet_greenText" to 0xFFFFFFFF,
		"windowBackgroundChecked" to palette.n1_50.toArgb(),
		"windowBackgroundCheckText" to palette.a1_600.toArgb(),
		"windowBackgroundGray" to palette.n1_50.toArgb(),
		"windowBackgroundGrayShadow" to palette.n1_50.toArgb(),
		"windowBackgroundUnchecked" to palette.n1_50.toArgb(),
		"windowBackgroundWhite" to palette.n1_50.toArgb(),
		"windowBackgroundWhiteBlackText" to palette.a1_1000.toArgb(),
		"windowBackgroundWhiteBlueButton" to palette.a1_600.toArgb(),
		"windowBackgroundWhiteBlueHeader" to palette.a1_600.toArgb(),
		"windowBackgroundWhiteBlueIcon" to palette.a1_600.toArgb(),
		"windowBackgroundWhiteBlueText" to palette.a1_600.toArgb(),
		"windowBackgroundWhiteBlueText2" to palette.a1_700.toArgb(),
		"windowBackgroundWhiteBlueText3" to palette.a1_700.toArgb(),
		"windowBackgroundWhiteBlueText4" to palette.a1_700.toArgb(),
		"windowBackgroundWhiteBlueText5" to palette.a1_700.toArgb(),
		"windowBackgroundWhiteBlueText6" to palette.a1_700.toArgb(),
		"windowBackgroundWhiteBlueText7" to palette.a1_700.toArgb(),
		"windowBackgroundWhiteGrayIcon" to palette.a1_600.toArgb(),
		"windowBackgroundWhiteGrayLine" to palette.a1_600.toArgb(),
		"windowBackgroundWhiteGrayText" to palette.n1_600.toArgb(),
		"windowBackgroundWhiteGrayText2" to palette.n1_600.toArgb(),
		"windowBackgroundWhiteGrayText3" to palette.n1_600.toArgb(),
		"windowBackgroundWhiteGrayText4" to palette.n1_600.toArgb(),
		"windowBackgroundWhiteGrayText5" to palette.n1_600.toArgb(),
		"windowBackgroundWhiteGrayText6" to palette.n1_600.toArgb(),
		"windowBackgroundWhiteGrayText7" to palette.n1_600.toArgb(),
		"windowBackgroundWhiteGrayText8" to palette.n1_600.toArgb(),
		"windowBackgroundWhiteGreenText" to palette.a1_1000.toArgb(),
		"windowBackgroundWhiteGreenText2" to palette.a1_1000.toArgb(),
		"windowBackgroundWhiteHintText" to palette.a1_1000.toArgb(),
		"windowBackgroundWhiteInputField" to palette.a1_300.toArgb(),
		"windowBackgroundWhiteInputFieldActivated" to palette.a1_600.toArgb(),
		"windowBackgroundWhiteLinkSelection" to palette.n1_200.toArgb(),
		"windowBackgroundWhiteLinkText" to palette.a3_500.toArgb(),
		"windowBackgroundWhiteRedText" to 0xFFFFFFFF,
		"windowBackgroundWhiteRedText2" to 0xFFFFFFFF,
		"windowBackgroundWhiteRedText3" to 0xFFFFFFFF,
		"windowBackgroundWhiteRedText4" to 0xFFFFFFFF,
		"windowBackgroundWhiteRedText5" to 0xFFFFFFFF,
		"windowBackgroundWhiteRedText6" to 0xFFFFFFFF,
		"windowBackgroundWhiteYellowText" to palette.a1_600.toArgb(),
		"windowBackgroundWhiteValueText" to palette.a1_600.toArgb()
	)

	return themeMap as Map<String, Int>
}

// also at least pretend to like these funny little silly haha comments
private fun defaultDarkTheme(palette: FullPaletteList): Map<String, Int>  {
	val themeMap = mapOf(
		"actionBarActionModeDefault" to palette.n1_900.toArgb(),
		"actionBarActionModeDefaultIcon" to palette.a1_100.toArgb(),
		"actionBarActionModeDefaultSelector" to palette.n1_50.toArgb(),
		"actionBarActionModeDefaultTop" to palette.a1_100.toArgb(),
		"actionBarBrowser" to palette.n1_900.toArgb(),
		"actionBarDefault" to palette.n1_900.toArgb(),
		"actionBarDefaultArchived" to palette.n1_900.toArgb(),
		"actionBarDefaultArchivedIcon" to palette.a1_200.toArgb(),
		"actionBarDefaultArchivedSearch" to palette.a2_400.toArgb(),
		"actionBarDefaultArchivedSelector" to palette.n2_800.toArgb(),
		"actionBarDefaultArchivedTitle" to palette.a1_100.toArgb(),
		"actionBarDefaultIcon" to palette.a1_100.toArgb(),
		"actionBarDefaultSearch" to palette.n1_50.toArgb(),
		"actionBarDefaultSearchArchivedPlaceholder" to palette.a1_1000.toArgb(),
		"actionBarDefaultSearchPlaceholder" to palette.a1_100.toArgb(),
		"actionBarDefaultSelector" to palette.n2_800.toArgb(),
		"actionBarDefaultSubmenuBackground" to palette.n1_900.toArgb(),
		"actionBarDefaultSubmenuItem" to palette.n1_50.toArgb(),
		"actionBarDefaultSubmenuItemIcon" to palette.a1_100.toArgb(),
		"actionBarDefaultSubmenuSeparator" to palette.n1_900.toArgb(),
		"actionBarDefaultSubtitle" to palette.n1_50.toArgb(),
		"actionBarDefaultTitle" to palette.a2_0.toArgb(),
		"actionBarTabActiveText" to palette.a1_100.toArgb(),
		"actionBarTabLine" to palette.a1_100.toArgb(),
		"actionBarTabSelector" to palette.a1_100.toArgb(),
		"actionBarTabUnactiveText" to palette.n1_600.toArgb(),
		"actionBarTipBackground" to palette.n1_800.toArgb(),
		"actionBarWhiteSelector" to palette.n2_800.toArgb(),
		"avatar_actionBarIconBlue" to palette.a1_100.toArgb(),
		"avatar_actionBarSelectorBlue" to palette.a1_100.toArgb(),
		"avatar_backgroundActionBarBlue" to palette.n1_900.toArgb(),
		"avatar_backgroundArchived" to palette.n2_800.toArgb(),
		"avatar_backgroundArchivedHidden" to palette.n2_800.toArgb(),
		"avatar_backgroundBlue" to palette.n2_700.toArgb(),
		"avatar_backgroundCyan" to palette.n2_700.toArgb(),
		"avatar_backgroundGreen" to palette.n2_700.toArgb(),
		"avatar_backgroundInProfileBlue" to palette.n2_800.toArgb(),
		"avatar_backgroundOrange" to palette.n2_700.toArgb(),
		"avatar_backgroundPink" to palette.n2_700.toArgb(),
		"avatar_backgroundRed" to palette.n2_700.toArgb(),
		"avatar_backgroundSaved" to palette.n2_700.toArgb(),
		"avatar_backgroundViolet" to palette.n2_700.toArgb(),
		"avatar_background2Blue" to palette.n2_800.toArgb(),
		"avatar_background2Cyan" to palette.n2_800.toArgb(),
		"avatar_background2Green" to palette.n2_800.toArgb(),
		"avatar_background2Orange" to palette.n2_800.toArgb(),
		"avatar_background2Pink" to palette.n2_800.toArgb(),
		"avatar_background2Red" to palette.n2_800.toArgb(),
		"avatar_background2Saved" to palette.n2_800.toArgb(),
		"avatar_background2Violet" to palette.n2_800.toArgb(),
		"avatar_nameInMessageBlue" to palette.a1_400.toArgb(),
		"avatar_nameInMessageCyan" to palette.a1_400.toArgb(),
		"avatar_nameInMessageGreen" to palette.a1_400.toArgb(),
		"avatar_nameInMessageOrange" to palette.a1_400.toArgb(),
		"avatar_nameInMessagePink" to palette.a1_400.toArgb(),
		"avatar_nameInMessageRed" to palette.a1_400.toArgb(),
		"avatar_nameInMessageViolet" to palette.a1_400.toArgb(),
		"avatar_subtitleInProfileBlue" to palette.n1_50.toArgb(),
		"avatar_text" to palette.a1_100.toArgb(),
		"calls_callReceivedGreenIcon" to 0xFFFFFFFF,
		"calls_callReceivedRedIcon" to 0xFFFFFFFF,
		"changephoneinfo_image" to palette.a1_100.toArgb(),
		"changephoneinfo_image2" to palette.a1_100.toArgb(),
		"chats_actionBackground" to palette.a1_400.toArgb(),
		"chats_actionIcon" to palette.a1_100.toArgb(),
		"chats_actionMessage" to palette.a1_200.toArgb(),
		"chats_actionPressedBackground" to palette.a2_700.toArgb(),
		"chats_actionUnreadBackground" to palette.n2_800.toArgb(),
		"chats_actionUnreadIcon" to palette.a1_100.toArgb(),
		"chats_actionUnreadPressedBackground" to palette.a1_100.toArgb(),
		"chats_archiveBackground" to palette.n2_800.toArgb(),
		"chats_archiveIcon" to palette.a3_200.toArgb(),
		"chats_archivePinBackground" to palette.a2_600.toArgb(),
		"chats_archivePullDownBackground" to palette.a2_800.toArgb(),
		"chats_archivePullDownBackgroundActive" to palette.n1_800.toArgb(),
		"chats_archiveText" to palette.a3_200.toArgb(),
		"chats_attachMessage" to palette.a1_100.toArgb(),
		"chats_date" to palette.n1_50.toArgb(),
		"chats_draft" to palette.a1_400.toArgb(),
		"chats_mentionIcon" to palette.n1_900.toArgb(),
		"chats_menuBackground" to palette.n1_900.toArgb(),
		"chats_menuCloud" to palette.n1_50.toArgb(),
		"chats_menuCloudBackgroundCats" to palette.n1_900.toArgb(),
		"chats_menuItemCheck" to palette.a3_200.toArgb(),
		"chats_menuItemIcon" to palette.a1_100.toArgb(),
		"chats_menuItemText" to palette.n1_50.toArgb(),
		"chats_menuName" to palette.a1_100.toArgb(),
		"chats_menuPhone" to palette.n1_50.toArgb(),
		"chats_menuPhoneCats" to palette.n1_50.toArgb(),
		"chats_menuTopBackgroundCats" to palette.n1_900.toArgb(),
		"chats_menuTopShadow" to palette.n1_700.toArgb(),
		"chats_menuTopShadowCats" to palette.n1_900.toArgb(),
		"chats_message" to palette.n1_300.toArgb(),
		"chats_messageArchived" to palette.n1_300.toArgb(),
		"chats_message_threeLines" to palette.n1_300.toArgb(),
		"chats_muteIcon" to palette.a1_100.toArgb(),
		"chats_name" to palette.n1_50.toArgb(),
		"chats_nameArchived" to palette.a2_300.toArgb(),
		"chats_nameIcon" to palette.n1_900.toArgb(),
		"chats_nameMessage" to palette.a1_100.toArgb(),
		"chats_nameMessageArchived" to palette.a1_100.toArgb(),
		"chats_nameMessageArchived_threeLines" to palette.n1_200.toArgb(),
		"chats_nameMessage_threeLines" to palette.n1_200.toArgb(),
		"chats_onlineCircle" to palette.a1_200.toArgb(),
		"chats_pinnedIcon" to palette.a1_500.toArgb(),
		"chats_pinnedOverlay" to palette.n1_900.toArgb(),
		"chats_secretIcon" to palette.a1_100.toArgb(),
		"chats_secretName" to palette.a1_100.toArgb(),
		"chats_sentCheck" to palette.a2_100.toArgb(),
		"chats_sentClock" to palette.a2_100.toArgb(),
		"chats_sentError" to 0xFFFFFFFF,
		"chats_sentErrorIcon" to 0xFFFFFFFF,
		"chats_sentReadCheck" to palette.a2_100.toArgb(),
		"chats_tabletSelectedOverlay" to 0xFFFFFFFF,
		"chats_tabUnreadActiveBackground" to palette.a1_100.toArgb(),
		"chats_tabUnreadUnactiveBackground" to palette.n1_600.toArgb(),
		"chats_unreadCounter" to palette.a1_100.toArgb(),
		"chats_unreadCounterMuted" to palette.a2_600.toArgb(),
		"chats_unreadCounterText" to palette.n1_900.toArgb(),
		"chats_verifiedBackground" to palette.a1_100.toArgb(),
		"chats_verifiedCheck" to palette.n1_900.toArgb(),
		"chat_addContact" to palette.a1_100.toArgb(),
		"chat_adminSelectedText" to palette.n1_50.toArgb(),
		"chat_adminText" to palette.n2_50.toArgb(),
		"chat_attachActiveTab" to palette.a1_100.toArgb(),
		"chat_attachAudioBackground" to palette.a1_500.toArgb(),
		"chat_attachAudioIcon" to palette.a1_50.toArgb(),
		"chat_attachAudioText" to palette.a1_100.toArgb(),
		"chat_attachCheckBoxBackground" to palette.n2_800.toArgb(),
		"chat_attachCheckBoxCheck" to palette.a1_100.toArgb(),
		"chat_attachContactBackground" to palette.a1_500.toArgb(),
		"chat_attachContactIcon" to palette.a1_50.toArgb(),
		"chat_attachContactText" to palette.a1_100.toArgb(),
		"chat_attachEmptyImage" to palette.a1_100.toArgb(),
		"chat_attachFileBackground" to palette.a1_500.toArgb(),
		"chat_attachFileIcon" to palette.a1_50.toArgb(),
		"chat_attachFileText" to palette.a1_100.toArgb(),
		"chat_attachGalleryBackground" to palette.a1_500.toArgb(),
		"chat_attachGalleryIcon" to palette.a1_50.toArgb(),
		"chat_attachGalleryText" to palette.a1_100.toArgb(),
		"chat_attachLocationBackground" to palette.a1_500.toArgb(),
		"chat_attachLocationIcon" to palette.a1_50.toArgb(),
		"chat_attachLocationText" to palette.a1_100.toArgb(),
		"chat_attachMediaBanBackground" to palette.a1_100.toArgb(),
		"chat_attachMediaBanText" to palette.n1_50.toArgb(),
		"chat_attachPermissionImage" to palette.a1_200.toArgb(),
		"chat_attachPermissionMark" to 0xFFFFFFFF,
		"chat_attachPermissionText" to palette.a1_200.toArgb(),
		"chat_attachPhotoBackground" to 0xFFFFFFFF,
		"chat_attachPollBackground" to palette.a1_500.toArgb(),
		"chat_attachPollIcon" to palette.a1_50.toArgb(),
		"chat_attachPollText" to palette.a1_100.toArgb(),
		"chat_attachUnactiveTab" to palette.n1_600.toArgb(),
		"chat_BlurAlpha" to 0xFFFFFFFF,
		"chat_botButtonText" to palette.a1_100.toArgb(),
		"chat_botKeyboardButtonBackground" to palette.n1_500.toArgb(),
		"chat_botKeyboardButtonBackgroundPressed" to palette.n1_800.toArgb(),
		"chat_botKeyboardButtonText" to palette.n1_50.toArgb(),
		"chat_botProgress" to palette.a3_50.toArgb(),
		"chat_botSwitchToInlineText" to palette.n1_50.toArgb(),
		"chat_emojiBottomPanelIcon" to palette.a2_400.toArgb(),
		"chat_emojiPanelBackground" to palette.n1_900.toArgb(),
		"chat_emojiPanelBackspace" to palette.a2_300.toArgb(),
		"chat_emojiPanelBadgeBackground" to palette.n1_900.toArgb(),
		"chat_emojiPanelBadgeText" to palette.n1_50.toArgb(),
		"chat_emojiPanelEmptyText" to palette.n1_50.toArgb(),
		"chat_emojiPanelIcon" to palette.a2_400.toArgb(),
		"chat_emojiPanelIconSelected" to palette.a1_100.toArgb(),
		"chat_emojiPanelIconSelector" to palette.a1_100.toArgb(),
		"chat_emojiPanelMasksIcon" to palette.a1_100.toArgb(),
		"chat_emojiPanelMasksIconSelected" to palette.a1_50.toArgb(),
		"chat_emojiPanelNewTrending" to palette.a1_100.toArgb(),
		"chat_emojiPanelShadowLine" to palette.n1_900.toArgb(),
		"chat_emojiPanelStickerPackSelector" to palette.n2_800.toArgb(),
		"chat_emojiPanelStickerPackSelectorLine" to palette.a1_100.toArgb(),
		"chat_emojiPanelStickerSetName" to palette.a2_100.toArgb(),
		"chat_emojiPanelStickerSetNameHighlight" to palette.a3_200.toArgb(),
		"chat_emojiPanelStickerSetNameIcon" to palette.a1_100.toArgb(),
		"chat_emojiPanelTrendingDescription" to palette.n1_900.toArgb(),
		"chat_emojiPanelTrendingTitle" to palette.n1_50.toArgb(),
		"chat_emojiSearchBackground" to palette.n2_800.toArgb(),
		"chat_emojiSearchIcon" to palette.a1_200.toArgb(),
		"chat_fieldOverlayText" to palette.a1_100.toArgb(),
		"chat_gifSaveHintBackground" to palette.n2_900.toArgb(),
		"chat_gifSaveHintText" to palette.a1_100.toArgb(),
		"chat_goDownButton" to palette.n2_800.toArgb(),
		"chat_goDownButtonCounter" to palette.n1_50.toArgb(),
		"chat_goDownButtonCounterBackground" to palette.n2_800.toArgb(),
		"chat_goDownButtonIcon" to palette.n1_50.toArgb(),
		"chat_goDownButtonShadow" to palette.a1_100.toArgb(),
		"chat_inAudioCacheSeekbar" to palette.a1_100.toArgb(),
		"chat_inAudioDurationSelectedText" to palette.n1_50.toArgb(),
		"chat_inAudioDurationText" to palette.n1_50.toArgb(),
		"chat_inAudioPerfomerSelectedText" to palette.n1_500.toArgb(),
		"chat_inAudioPerfomerText" to palette.n1_500.toArgb(),
		"chat_inAudioProgress" to palette.a1_100.toArgb(),
		"chat_inAudioSeekbar" to palette.a2_300.toArgb(),
		"chat_inAudioSeekbarFill" to palette.a1_300.toArgb(),
		"chat_inAudioSeekbarSelected" to palette.a1_100.toArgb(),
		"chat_inAudioSelectedProgress" to palette.a1_100.toArgb(),
		"chat_inAudioTitleText" to palette.n1_50.toArgb(),
		"chat_inBubble" to palette.n2_800.toArgb(),
		"chat_inBubbleSelected" to palette.n2_800.toArgb(),
		"chat_inBubbleShadow" to palette.a1_100.toArgb(),
		"chat_inContactBackground" to palette.n1_900.toArgb(),
		"chat_inContactIcon" to palette.a1_100.toArgb(),
		"chat_inContactNameText" to palette.n1_50.toArgb(),
		"chat_inContactPhoneSelectedText" to palette.n1_50.toArgb(),
		"chat_inContactPhoneText" to palette.n1_50.toArgb(),
		"chat_inDownCall" to 0xFFFFFFFF,
		"chat_inFileBackground" to palette.n1_900.toArgb(),
		"chat_inFileBackgroundSelected" to palette.n1_900.toArgb(),
		"chat_inFileIcon" to palette.a1_100.toArgb(),
		"chat_inFileInfoSelectedText" to palette.n1_50.toArgb(),
		"chat_inFileInfoText" to palette.n1_50.toArgb(),
		"chat_inFileNameText" to palette.n1_50.toArgb(),
		"chat_inFileProgress" to palette.a1_100.toArgb(),
		"chat_inFileProgressSelected" to palette.a1_100.toArgb(),
		"chat_inFileSelectedIcon" to palette.n1_900.toArgb(),
		"chat_inForwardedNameText" to palette.n1_50.toArgb(),
		"chat_inInstant" to palette.a1_100.toArgb(),
		"chat_inInstantSelected" to palette.a1_100.toArgb(),
		"chat_inlineResultIcon" to palette.a1_100.toArgb(),
		"chat_inLoader" to palette.a1_100.toArgb(),
		"chat_inLoaderPhoto" to palette.a1_100.toArgb(),
		"chat_inLoaderPhotoIcon" to palette.a1_100.toArgb(),
		"chat_inLoaderPhotoIconSelected" to palette.a1_100.toArgb(),
		"chat_inLoaderPhotoSelected" to palette.a1_100.toArgb(),
		"chat_inLoaderSelected" to palette.a1_100.toArgb(),
		"chat_inLocationBackground" to palette.n1_900.toArgb(),
		"chat_inLocationIcon" to palette.a1_100.toArgb(),
		"chat_inMediaIcon" to palette.n1_900.toArgb(),
		"chat_inMediaIconSelected" to palette.n1_900.toArgb(),
		"chat_inMenu" to palette.a1_100.toArgb(),
		"chat_inMenuSelected" to palette.a1_100.toArgb(),
		"chat_inPollCorrectAnswer" to palette.a3_500.toArgb(),
		"chat_inPollWrongAnswer" to palette.a2_500.toArgb(),
		"chat_inPreviewInstantSelectedText" to palette.n1_50.toArgb(),
		"chat_inPreviewInstantText" to palette.a1_100.toArgb(),
		"chat_inPreviewLine" to palette.n1_900.toArgb(),
		"chat_inPsaNameText" to palette.n1_50.toArgb(),
		"chat_inReactionButtonBackground" to palette.a1_300.toArgb(),
		"chat_inReactionButtonText" to palette.n1_50.toArgb(),
		"chat_inReactionButtonTextSelected" to palette.n1_900.toArgb(),
		"chat_inReplyLine" to palette.a1_100.toArgb(),
		"chat_inReplyMediaMessageSelectedText" to palette.n1_50.toArgb(),
		"chat_inReplyMediaMessageText" to palette.n1_50.toArgb(),
		"chat_inReplyMessageText" to palette.n1_50.toArgb(),
		"chat_inReplyNameText" to palette.n1_50.toArgb(),
		"chat_inSentClock" to palette.n1_50.toArgb(),
		"chat_inSentClockSelected" to palette.n1_50.toArgb(),
		"chat_inSiteNameText" to palette.n1_50.toArgb(),
		"chat_inTextSelectionHighlight" to 0xFFFFFFFF,
		"chat_inTimeSelectedText" to palette.n1_50.toArgb(),
		"chat_inTimeText" to palette.n1_50.toArgb(),
		"chat_inUpCall" to 0xFFFFFFFF,
		"chat_inVenueInfoSelectedText" to palette.a1_100.toArgb(),
		"chat_inVenueInfoText" to palette.n1_50.toArgb(),
		"chat_inViaBotNameText" to palette.a1_100.toArgb(),
		"chat_inViews" to palette.n1_50.toArgb(),
		"chat_inViewsSelected" to palette.n1_50.toArgb(),
		"chat_inVoiceSeekbar" to palette.a2_200.toArgb(),
		"chat_inVoiceSeekbarFill" to palette.a1_100.toArgb(),
		"chat_inVoiceSeekbarSelected" to palette.a2_300.toArgb(),
		"chat_linkSelectBackground" to palette.n1_300.toArgb(),
		"chat_lockIcon" to palette.a1_100.toArgb(),
		"chat_mediaInfoText" to palette.a1_100.toArgb(),
		"chat_mediaLoaderPhoto" to palette.a1_700.toArgb(),
		"chat_mediaLoaderPhotoIcon" to palette.a1_100.toArgb(),
		"chat_mediaLoaderPhotoIconSelected" to palette.a1_100.toArgb(),
		"chat_mediaLoaderPhotoSelected" to palette.a1_700.toArgb(),
		"chat_mediaMenu" to palette.a1_100.toArgb(),
		"chat_mediaProgress" to palette.a1_100.toArgb(),
		"chat_mediaSentCheck" to palette.n1_50.toArgb(),
		"chat_mediaSentClock" to palette.n1_50.toArgb(),
		"chat_mediaTimeBackground" to palette.a1_600.toArgb(),
		"chat_mediaTimeText" to palette.n1_50.toArgb(),
		"chat_mediaViews" to palette.n1_50.toArgb(),
		"chat_messageLinkIn" to palette.a3_100.toArgb(),
		"chat_messageLinkOut" to palette.a3_500.toArgb(),
		"chat_messagePanelBackground" to palette.n1_900.toArgb(),
		"chat_messagePanelCancelInlineBot" to palette.a1_100.toArgb(),
		"chat_messagePanelCursor" to palette.a1_100.toArgb(),
		"chat_messagePanelHint" to palette.n1_300.toArgb(),
		"chat_messagePanelIcons" to palette.a2_200.toArgb(),
		"chat_messagePanelSend" to palette.a2_200.toArgb(),
		"chat_messagePanelShadow" to palette.n1_900.toArgb(),
		"chat_messagePanelText" to palette.n1_50.toArgb(),
		"chat_messagePanelVideoFrame" to palette.a1_100.toArgb(),
		"chat_messagePanelVoiceBackground" to palette.a1_400.toArgb(),
		"chat_messagePanelVoiceDelete" to palette.a1_100.toArgb(),
		"chat_messagePanelVoiceDuration" to palette.a1_100.toArgb(),
		"chat_messagePanelVoicePressed" to palette.n1_0.toArgb(),
		"chat_messageTextIn" to palette.n1_50.toArgb(),
		"chat_messageTextOut" to palette.n1_900.toArgb(),
		"chat_muteIcon" to palette.a1_100.toArgb(),
		"chat_outAdminSelectedText" to palette.n1_500.toArgb(),
		"chat_outAdminText" to palette.n1_500.toArgb(),
		"chat_outAudioCacheSeekbar" to palette.n1_900.toArgb(),
		"chat_outAudioDurationSelectedText" to palette.n1_900.toArgb(),
		"chat_outAudioDurationText" to palette.n1_900.toArgb(),
		"chat_outAudioPerfomerSelectedText" to palette.n1_900.toArgb(),
		"chat_outAudioPerfomerText" to palette.n1_900.toArgb(),
		"chat_outAudioProgress" to palette.n1_900.toArgb(),
		"chat_outAudioSeekbar" to palette.n1_400.toArgb(),
		"chat_outAudioSeekbarFill" to palette.a1_600.toArgb(),
		"chat_outAudioSeekbarSelected" to palette.n1_900.toArgb(),
		"chat_outAudioSelectedProgress" to palette.n1_900.toArgb(),
		"chat_outAudioTitleText" to palette.n1_900.toArgb(),
		"chat_outBubble" to palette.a1_100.toArgb(),
		"chat_outBubbleGradient" to palette.a1_100.toArgb(),
		"chat_outBubbleGradient2" to palette.a1_100.toArgb(),
		"chat_outBubbleGradient3" to palette.a1_200.toArgb(),
		"chat_outBubbleGradientAnimated" to 0xFFFFFFFF,
		"chat_outBubbleGradientSelectedOverlay" to palette.a1_100.toArgb(),
		"chat_outBubbleSelected" to palette.a1_100.toArgb(),
		"chat_outBubbleShadow" to palette.n2_800.toArgb(),
		"chat_outContactBackground" to palette.n1_900.toArgb(),
		"chat_outContactIcon" to palette.n1_900.toArgb(),
		"chat_outContactNameText" to palette.n1_900.toArgb(),
		"chat_outContactPhoneSelectedText" to palette.n1_900.toArgb(),
		"chat_outContactPhoneText" to palette.n1_900.toArgb(),
		"chat_outFileBackground" to palette.n1_900.toArgb(),
		"chat_outFileBackgroundSelected" to palette.a1_100.toArgb(),
		"chat_outFileIcon" to palette.n1_900.toArgb(),
		"chat_outFileInfoSelectedText" to palette.n1_900.toArgb(),
		"chat_outFileInfoText" to palette.n1_900.toArgb(),
		"chat_outFileNameText" to palette.n1_900.toArgb(),
		"chat_outFileProgress" to palette.n1_900.toArgb(),
		"chat_outFileProgressSelected" to palette.n1_900.toArgb(),
		"chat_outFileSelectedIcon" to palette.n1_900.toArgb(),
		"chat_outForwardedNameText" to palette.n1_900.toArgb(),
		"chat_outInstant" to palette.n1_900.toArgb(),
		"chat_outInstantSelected" to palette.n1_900.toArgb(),
		"chat_outLinkSelectBackground" to palette.n1_400.toArgb(),
		"chat_outLoader" to palette.n1_900.toArgb(),
		"chat_outLoaderPhoto" to palette.a1_100.toArgb(),
		"chat_outLoaderPhotoIcon" to palette.a1_100.toArgb(),
		"chat_outLoaderPhotoIconSelected" to palette.n1_900.toArgb(),
		"chat_outLoaderPhotoSelected" to palette.n1_900.toArgb(),
		"chat_outLoaderSelected" to palette.n1_900.toArgb(),
		"chat_outLocationBackground" to palette.n1_900.toArgb(),
		"chat_outLocationIcon" to palette.n1_900.toArgb(),
		"chat_outMediaIcon" to palette.a1_100.toArgb(),
		"chat_outMediaIconSelected" to palette.a1_100.toArgb(),
		"chat_outMenu" to palette.n1_900.toArgb(),
		"chat_outMenuSelected" to palette.n1_900.toArgb(),
		"chat_outPollCorrectAnswer" to palette.a3_500.toArgb(),
		"chat_outPollWrongAnswer" to palette.a2_500.toArgb(),
		"chat_outPreviewInstantSelectedText" to palette.n1_900.toArgb(),
		"chat_outPreviewInstantText" to palette.n1_900.toArgb(),
		"chat_outPreviewLine" to palette.n1_900.toArgb(),
		"chat_outPsaNameText" to palette.n1_900.toArgb(),
		"chat_outReactionButtonBackground" to palette.a1_500.toArgb(),
		"chat_outReactionButtonText" to palette.n1_1000.toArgb(),
		"chat_outReactionButtonTextSelected" to palette.n1_0.toArgb(),
		"chat_outReplyLine" to palette.n1_900.toArgb(),
		"chat_outReplyMediaMessageSelectedText" to palette.n1_900.toArgb(),
		"chat_outReplyMediaMessageText" to palette.n1_900.toArgb(),
		"chat_outReplyMessageText" to palette.n1_900.toArgb(),
		"chat_outReplyNameText" to palette.n1_900.toArgb(),
		"chat_outSentCheck" to palette.a1_600.toArgb(),
		"chat_outSentCheckRead" to palette.a1_600.toArgb(),
		"chat_outSentCheckReadSelected" to palette.a1_600.toArgb(),
		"chat_outSentCheckSelected" to palette.a1_600.toArgb(),
		"chat_outSentClock" to palette.a1_600.toArgb(),
		"chat_outSentClockSelected" to palette.a1_600.toArgb(),
		"chat_outSiteNameText" to palette.n1_900.toArgb(),
		"chat_outTextSelectionCursor" to palette.a2_500.toArgb(),
		"chat_outTextSelectionHighlight" to palette.n2_400.toArgb(),
		"chat_outTimeSelectedText" to palette.n1_900.toArgb(),
		"chat_outTimeText" to palette.n1_900.toArgb(),
		"chat_outUpCall" to palette.a1_600.toArgb(),
		"chat_outVenueInfoSelectedText" to palette.n1_900.toArgb(),
		"chat_outVenueInfoText" to palette.n1_900.toArgb(),
		"chat_outViaBotNameText" to palette.n1_900.toArgb(),
		"chat_outViews" to palette.n1_900.toArgb(),
		"chat_outViewsSelected" to palette.n1_900.toArgb(),
		"chat_outVoiceSeekbar" to palette.n2_600.toArgb(),
		"chat_outVoiceSeekbarFill" to palette.n1_900.toArgb(),
		"chat_outVoiceSeekbarSelected" to palette.n1_900.toArgb(),
		"chat_previewDurationText" to palette.n1_50.toArgb(),
		"chat_previewGameText" to palette.n1_50.toArgb(),
		"chat_recordedVoiceBackground" to palette.n2_800.toArgb(),
		"chat_recordedVoiceDot" to palette.a1_100.toArgb(),
		"chat_recordedVoicePlayPause" to palette.a1_100.toArgb(),
		"chat_recordedVoiceProgress" to palette.n2_100.toArgb(),
		"chat_recordedVoiceProgressInner" to palette.a1_100.toArgb(),
		"chat_recordTime" to palette.n1_50.toArgb(),
		"chat_recordVoiceCancel" to palette.a1_100.toArgb(),
		"chat_replyPanelClose" to palette.a1_100.toArgb(),
		"chat_replyPanelIcons" to palette.a1_100.toArgb(),
		"chat_replyPanelLine" to palette.n1_900.toArgb(),
		"chat_replyPanelMessage" to palette.n1_50.toArgb(),
		"chat_replyPanelName" to palette.a1_100.toArgb(),
		"chat_reportSpam" to palette.a3_200.toArgb(),
		"chat_searchPanelIcons" to palette.a1_100.toArgb(),
		"chat_searchPanelText" to palette.n1_50.toArgb(),
		"chat_secretChatStatusText" to palette.n1_50.toArgb(),
		"chat_secretTimerBackground" to palette.a1_100.toArgb(),
		"chat_secretTimerText" to palette.n1_900.toArgb(),
		"chat_secretTimeText" to palette.a1_0.toArgb(),
		"chat_selectedBackground" to palette.a2_700.toArgb(),
		"chat_sentError" to 0xFFFFFFFF,
		"chat_sentErrorIcon" to 0xFFFFFFFF,
		"chat_serviceBackground" to palette.n1_700.toArgb(),
		"chat_serviceBackgroundSelected" to palette.n1_900.toArgb(),
		"chat_serviceBackgroundSelector" to palette.n1_800.toArgb(),
		"chat_serviceIcon" to palette.a1_100.toArgb(),
		"chat_serviceLink" to palette.a1_100.toArgb(),
		"chat_serviceText" to palette.a1_100.toArgb(),
		"chat_status" to palette.n1_50.toArgb(),
		"chat_stickerNameText" to palette.n1_50.toArgb(),
		"chat_stickerReplyLine" to palette.a2_200.toArgb(),
		"chat_stickerReplyMessageText" to palette.n1_50.toArgb(),
		"chat_stickerReplyNameText" to palette.a2_200.toArgb(),
		"chat_stickersHintPanel" to palette.n1_900.toArgb(),
		"chat_stickerViaBotNameText" to palette.a1_100.toArgb(),
		"chat_textSelectBackground" to palette.n2_200.toArgb(),
		"chat_TextSelectionCursor" to palette.a3_400.toArgb(),
		"chat_topPanelBackground" to palette.n1_900.toArgb(),
		"chat_topPanelClose" to palette.a1_100.toArgb(),
		"chat_topPanelLine" to palette.a1_100.toArgb(),
		"chat_topPanelMessage" to palette.a1_100.toArgb(),
		"chat_topPanelTitle" to palette.a2_200.toArgb(),
		"chat_unreadMessagesStartArrowIcon" to palette.a1_100.toArgb(),
		"chat_unreadMessagesStartBackground" to palette.n1_900.toArgb(),
		"chat_unreadMessagesStartText" to palette.a1_100.toArgb(),
		"chat_wallpaper" to palette.n1_900.toArgb(),
		"checkbox" to palette.a1_100.toArgb(),
		"checkboxCheck" to palette.n1_900.toArgb(),
		"checkboxDisabled" to palette.a2_400.toArgb(),
		"checkboxSquareBackground" to palette.a1_100.toArgb(),
		"checkboxSquareCheck" to palette.n1_900.toArgb(),
		"checkboxSquareDisabled" to palette.a1_100.toArgb(),
		"checkboxSquareUnchecked" to palette.n2_500.toArgb(),
		"contacts_inviteBackground" to palette.a1_100.toArgb(),
		"contacts_inviteText" to palette.a1_1000.toArgb(),
		"contextProgressInner1" to palette.a1_100.toArgb(),
		"contextProgressInner2" to palette.n1_700.toArgb(),
		"contextProgressInner3" to palette.n1_600.toArgb(),
		"contextProgressInner4" to palette.a1_100.toArgb(),
		"contextProgressOuter1" to palette.n1_900.toArgb(),
		"contextProgressOuter2" to palette.a1_200.toArgb(),
		"contextProgressOuter3" to palette.a1_200.toArgb(),
		"contextProgressOuter4" to palette.n1_900.toArgb(),
		"dialogBackground" to palette.n1_900.toArgb(),
		"dialogBackgroundGray" to palette.n1_900.toArgb(),
		"dialogBadgeBackground" to palette.n1_900.toArgb(),
		"dialogBadgeText" to palette.a1_600.toArgb(),
		"dialogButton" to palette.a1_100.toArgb(),
		"dialogButtonSelector" to palette.a1_100.toArgb(),
		"dialogCameraIcon" to palette.a1_100.toArgb(),
		"dialogCheckboxSquareBackground" to palette.n1_900.toArgb(),
		"dialogCheckboxSquareCheck" to palette.n1_900.toArgb(),
		"dialogCheckboxSquareDisabled" to palette.a1_100.toArgb(),
		"dialogCheckboxSquareUnchecked" to palette.a1_100.toArgb(),
		"dialogEmptyImage" to palette.a1_50.toArgb(),
		"dialogEmptyText" to palette.a1_50.toArgb(),
		"dialogFloatingButton" to palette.a1_200.toArgb(),
		"dialogFloatingButtonPressed" to palette.a2_200.toArgb(),
		"dialogFloatingIcon" to palette.n1_900.toArgb(),
		"dialogGrayLine" to palette.n1_900.toArgb(),
		"dialogIcon" to palette.a1_100.toArgb(),
		"dialogInputField" to palette.n1_900.toArgb(),
		"dialogInputFieldActivated" to palette.a1_100.toArgb(),
		"dialogLineProgress" to palette.a1_100.toArgb(),
		"dialogLineProgressBackground" to palette.n2_800.toArgb(),
		"dialogLinkSelection" to palette.a1_200.toArgb(),
		"dialogProgressCircle" to palette.a1_100.toArgb(),
		"dialogRadioBackground" to palette.a1_300.toArgb(),
		"dialogRadioBackgroundChecked" to palette.a1_100.toArgb(),
		"dialogReactionMentionBackground" to palette.a3_300.toArgb(),
		"dialogRedIcon" to 0xFFFFFFFF,
		"dialogRoundCheckBox" to palette.a1_200.toArgb(),
		"dialogRoundCheckBoxCheck" to palette.n1_900.toArgb(),
		"dialogScrollGlow" to palette.n1_800.toArgb(),
		"dialogSearchBackground" to palette.n1_900.toArgb(),
		"dialogSearchHint" to palette.a1_100.toArgb(),
		"dialogSearchIcon" to palette.a1_100.toArgb(),
		"dialogSearchText" to palette.n1_50.toArgb(),
		"dialogShadowLine" to palette.n1_900.toArgb(),
		"dialogSwipeRemove" to palette.a3_700.toArgb(),
		"dialogTextBlack" to palette.n1_50.toArgb(),
		"dialogTextBlue" to palette.a1_100.toArgb(),
		"dialogTextBlue2" to palette.n1_50.toArgb(),
		"dialogTextBlue3" to palette.n1_50.toArgb(),
		"dialogTextBlue4" to palette.n1_50.toArgb(),
		"dialogTextGray" to palette.n1_50.toArgb(),
		"dialogTextGray2" to palette.a1_100.toArgb(),
		"dialogTextGray3" to palette.a1_100.toArgb(),
		"dialogTextGray4" to palette.a1_100.toArgb(),
		"dialogTextHint" to palette.n1_300.toArgb(),
		"dialogTextLink" to palette.n1_50.toArgb(),
		"dialogTextRed" to 0xFFFFFFFF,
		"dialogTextRed2" to 0xFFFFFFFF,
		"dialogTopBackground" to palette.n1_900.toArgb(),
		"dialog_inlineProgress" to palette.a1_100.toArgb(),
		"dialog_inlineProgressBackground" to palette.n1_900.toArgb(),
		"dialog_liveLocationProgress" to palette.a1_100.toArgb(),
		"divider" to palette.n1_900.toArgb(),
		"emptyListPlaceholder" to palette.n2_100.toArgb(),
		"fastScrollActive" to palette.a1_600.toArgb(),
		"fastScrollInactive" to palette.a1_200.toArgb(),
		"fastScrollText" to palette.n1_50.toArgb(),
		"featuredStickers_addButton" to palette.a1_100.toArgb(),
		"featuredStickers_addButtonPressed" to palette.n2_800.toArgb(),
		"featuredStickers_addedIcon" to palette.a1_100.toArgb(),
		"featuredStickers_buttonProgress" to palette.a1_200.toArgb(),
		"featuredStickers_buttonText" to palette.n1_900.toArgb(),
		"featuredStickers_removeButtonText" to palette.a3_500.toArgb(),
		"featuredStickers_unread" to palette.a1_600.toArgb(),
		"files_folderIcon" to palette.a1_50.toArgb(),
		"files_folderIconBackground" to palette.a1_500.toArgb(),
		"files_iconText" to palette.a1_100.toArgb(),
		"graySection" to palette.n1_900.toArgb(),
		"groupcreate_cursor" to palette.a1_100.toArgb(),
		"groupcreate_hintText" to palette.n1_50.toArgb(),
		"groupcreate_sectionShadow" to palette.n1_0.toArgb(),
		"groupcreate_sectionText" to palette.n1_50.toArgb(),
		"groupcreate_spanBackground" to palette.a2_400.toArgb(),
		"groupcreate_spanDelete" to palette.n1_700.toArgb(),
		"groupcreate_spanText" to palette.n1_900.toArgb(),
		"inappPlayerBackground" to palette.n1_900.toArgb(),
		"inappPlayerClose" to palette.n1_50.toArgb(),
		"inappPlayerPerformer" to palette.n1_0.toArgb(),
		"inappPlayerPlayPause" to palette.a1_300.toArgb(),
		"inappPlayerTitle" to palette.n1_50.toArgb(),
		"key_chat_messagePanelVoiceLock" to palette.a1_100.toArgb(),
		"key_chat_messagePanelVoiceLockBackground" to palette.n1_800.toArgb(),
		"key_chat_messagePanelVoiceLockShadow" to palette.n2_900.toArgb(),
		"key_chat_recordedVoiceHighlight" to palette.a2_500.toArgb(),
		"key_graySectionText" to palette.a1_200.toArgb(),
		"key_player_progressCachedBackground" to palette.a2_300.toArgb(),
		"key_sheet_other" to palette.a1_100.toArgb(),
		"key_sheet_scrollUp" to palette.a1_100.toArgb(),
		"kvoipgroup_overlayAlertMutedByAdmin2" to palette.a3_100.toArgb(),
		"listSelectorSDK21" to palette.n2_800.toArgb(),
		"location_actionActiveIcon" to palette.a1_300.toArgb(),
		"location_actionBackground" to palette.n1_900.toArgb(),
		"location_actionIcon" to palette.a1_300.toArgb(),
		"location_actionPressedBackground" to palette.n1_900.toArgb(),
		"location_liveLocationProgress" to palette.a1_100.toArgb(),
		"location_placeLocationBackground" to palette.n1_900.toArgb(),
		"location_sendLiveLocationBackground" to palette.a1_300.toArgb(),
		"location_sendLiveLocationIcon" to palette.a1_0.toArgb(),
		"location_sendLiveLocationText" to palette.a2_200.toArgb(),
		"location_sendLocationBackground" to palette.a1_300.toArgb(),
		"location_sendLocationIcon" to palette.a1_0.toArgb(),
		"location_sendLocationText" to palette.a2_200.toArgb(),
		"login_progressInner" to palette.a1_700.toArgb(),
		"login_progressOuter" to palette.a1_300.toArgb(),
		"musicPicker_buttonBackground" to palette.a1_200.toArgb(),
		"musicPicker_buttonIcon" to palette.a1_700.toArgb(),
		"musicPicker_checkbox" to palette.a1_200.toArgb(),
		"musicPicker_checkboxCheck" to palette.a1_900.toArgb(),
		"passport_authorizeBackground" to palette.n1_900.toArgb(),
		"passport_authorizeBackgroundSelected" to palette.n1_900.toArgb(),
		"passport_authorizeText" to palette.n1_50.toArgb(),
		"picker_badge" to palette.n1_50.toArgb(),
		"picker_badgeText" to palette.n1_900.toArgb(),
		"picker_disabledButton" to palette.a1_100.toArgb(),
		"picker_enabledButton" to palette.a1_100.toArgb(),
		"player_actionBar" to palette.n1_900.toArgb(),
		"player_actionBarItems" to palette.a1_100.toArgb(),
		"player_actionBarSelector" to palette.n2_800.toArgb(),
		"player_actionBarSubtitle" to palette.n1_50.toArgb(),
		"player_actionBarTitle" to palette.n1_50.toArgb(),
		"player_actionBarTop" to palette.n1_900.toArgb(),
		"player_background" to palette.n1_900.toArgb(),
		"player_button" to palette.a1_100.toArgb(),
		"player_buttonActive" to palette.a1_100.toArgb(),
		"player_progress" to palette.a1_100.toArgb(),
		"player_progressBackground" to palette.a2_300.toArgb(),
		"player_progressBackground2" to palette.n1_700.toArgb(),
		"player_time" to palette.a1_100.toArgb(),
		"premiumGradient0" to palette.a1_300.toArgb(),
		"premiumGradient1" to palette.a1_300.toArgb(),
		"premiumGradient2" to palette.a1_300.toArgb(),
		"premiumGradient3" to palette.a3_300.toArgb(),
		"premiumGradient4" to palette.a3_300.toArgb(),
		"premiumGradientBackground1" to palette.a3_400.toArgb(),
		"premiumGradientBackground2" to palette.a1_400.toArgb(),
		"premiumGradientBackground3" to palette.a3_400.toArgb(),
		"premiumGradientBackground4" to palette.a3_400.toArgb(),
		"premiumGradientBackgroundOverlay" to palette.n1_0.toArgb(),
		"premiumGradientBottomSheet1" to palette.a3_500.toArgb(),
		"premiumGradientBottomSheet2" to palette.a2_500.toArgb(),
		"premiumGradientBottomSheet3" to palette.a1_500.toArgb(),
		"premiumStarGradient1" to palette.n1_0.toArgb(),
		"premiumStarGradient2" to palette.n1_50.toArgb(),
		"premiumStartSmallStarsColor" to palette.n1_0.toArgb(),
		"premiumStartSmallStarsColor2" to palette.n1_50.toArgb(),
		"profile_actionBackground" to palette.n2_800.toArgb(),
		"profile_actionIcon" to palette.a1_100.toArgb(),
		"profile_actionPressedBackground" to palette.a1_100.toArgb(),
		"profile_creatorIcon" to palette.a1_100.toArgb(),
		"profile_status" to palette.n1_50.toArgb(),
		"profile_tabSelectedLine" to palette.a1_200.toArgb(),
		"profile_tabSelectedText" to palette.a1_200.toArgb(),
		"profile_tabSelector" to palette.n1_900.toArgb(),
		"profile_tabText" to palette.n1_300.toArgb(),
		"profile_title" to palette.n1_50.toArgb(),
		"profile_verifiedBackground" to palette.n1_50.toArgb(),
		"profile_verifiedCheck" to palette.n1_900.toArgb(),
		"progressCircle" to palette.a1_100.toArgb(),
		"radioBackground" to palette.a1_100.toArgb(),
		"radioBackgroundChecked" to palette.a1_100.toArgb(),
		"returnToCallBackground" to palette.n1_900.toArgb(),
		"returnToCallMutedBackground" to palette.n1_300.toArgb(),
		"returnToCallText" to palette.a1_100.toArgb(),
		"sessions_devicesImage" to palette.a1_600.toArgb(),
		"sharedMedia_actionMode" to palette.a3_200.toArgb(),
		"sharedMedia_linkPlaceholder" to palette.a1_100.toArgb(),
		"sharedMedia_linkPlaceholderText" to palette.n1_900.toArgb(),
		"sharedMedia_photoPlaceholder" to palette.n2_800.toArgb(),
		"sharedMedia_startStopLoadIcon" to palette.a1_100.toArgb(),
		"statisticChartActiveLine" to palette.a1_400.toArgb(),
		"statisticChartActivePickerChart" to palette.a1_400.toArgb(),
		"statisticChartBackZoomColor" to palette.a1_100.toArgb(),
		"statisticChartCheckboxInactive" to palette.n1_900.toArgb(),
		"statisticChartChevronColor" to palette.a1_100.toArgb(),
		"statisticChartHighlightColor" to palette.a1_100.toArgb(),
		"statisticChartHintLine" to palette.a1_100.toArgb(),
		"statisticChartInactivePickerChart" to 0xFFFFFFFF,
		"statisticChartLineEmpty" to palette.n1_800.toArgb(),
		"statisticChartLine_blue" to palette.a1_400.toArgb(),
		"statisticChartLine_cyan" to palette.a2_600.toArgb(),
		"statisticChartLine_golden" to palette.a1_500.toArgb(),
		"statisticChartLine_green" to palette.a1_700.toArgb(),
		"statisticChartLine_indigo" to palette.a1_200.toArgb(),
		"statisticChartLine_lightblue" to palette.a3_200.toArgb(),
		"statisticChartLine_lightgreen" to palette.a3_700.toArgb(),
		"statisticChartLine_orange" to palette.a3_500.toArgb(),
		"statisticChartLine_purple" to palette.a2_400.toArgb(),
		"statisticChartLine_red" to palette.a3_300.toArgb(),
		"statisticChartRipple" to palette.a1_800.toArgb(),
		"statisticChartSignature" to palette.a1_100.toArgb(),
		"statisticChartSignatureAlpha" to palette.a1_100.toArgb(),
		"stickers_menu" to palette.a1_100.toArgb(),
		"stickers_menuSelector" to palette.n2_700.toArgb(),
		"switch2Track" to palette.n2_500.toArgb(),
		"switch2TrackChecked" to palette.a1_100.toArgb(),
		"switchTrack" to palette.n2_500.toArgb(),
		"switchTrackBlue" to palette.a1_100.toArgb(),
		"switchTrackBlueChecked" to palette.a1_100.toArgb(),
		"switchTrackBlueSelector" to palette.a1_100.toArgb(),
		"switchTrackBlueSelectorChecked" to palette.a1_100.toArgb(),
		"switchTrackBlueThumb" to palette.a1_100.toArgb(),
		"switchTrackBlueThumbChecked" to palette.n1_900.toArgb(),
		"switchTrackChecked" to palette.a1_300.toArgb(),
		"topics_unreadCounter" to palette.a1_100.toArgb(),
		"topics_unreadCounterMuted" to palette.a2_600.toArgb(),
		"undo_background" to palette.n2_800.toArgb(),
		"undo_cancelColor" to palette.a1_100.toArgb(),
		"undo_infoColor" to palette.a1_100.toArgb(),
		"voipgroup_actionBar" to palette.n1_900.toArgb(),
		"voipgroup_actionBarItems" to palette.n1_0.toArgb(),
		"voipgroup_actionBarItemsSelector" to palette.n1_700.toArgb(),
		"voipgroup_actionBarSubtitle" to palette.n1_0.toArgb(),
		"voipgroup_actionBarUnscrolled" to palette.n1_900.toArgb(),
		"voipgroup_blueText" to palette.a3_200.toArgb(),
		"voipgroup_checkMenu" to palette.a1_200.toArgb(),
		"voipgroup_connectingProgress" to palette.a2_100.toArgb(),
		"voipgroup_dialogBackground" to palette.n1_900.toArgb(),
		"voipgroup_disabledButton" to palette.n2_800.toArgb(),
		"voipgroup_disabledButtonActive" to palette.n2_800.toArgb(),
		"voipgroup_disabledButtonActiveScrolled" to palette.n2_800.toArgb(),
		"voipgroup_emptyView" to palette.n1_900.toArgb(),
		"voipgroup_inviteMembersBackground" to palette.n1_900.toArgb(),
		"voipgroup_lastSeenText" to palette.n1_200.toArgb(),
		"voipgroup_lastSeenTextUnscrolled" to palette.n1_200.toArgb(),
		"voipgroup_leaveButton" to palette.a3_300.toArgb(),
		"voipgroup_leaveButtonScrolled" to palette.a3_200.toArgb(),
		"voipgroup_leaveCallMenu" to palette.a1_100.toArgb(),
		"voipgroup_listeningText" to palette.a2_300.toArgb(),
		"voipgroup_listSelector" to palette.n1_700.toArgb(),
		"voipgroup_listViewBackground" to palette.n2_800.toArgb(),
		"voipgroup_listViewBackgroundUnscrolled" to palette.n2_800.toArgb(),
		"voipgroup_muteButton" to palette.a1_500.toArgb(),
		"voipgroup_muteButton2" to palette.a2_300.toArgb(),
		"voipgroup_muteButton3" to palette.a1_300.toArgb(),
		"voipgroup_mutedByAdminGradient" to palette.a3_300.toArgb(),
		"voipgroup_mutedByAdminGradient2" to palette.a3_200.toArgb(),
		"voipgroup_mutedByAdminGradient3" to palette.a3_400.toArgb(),
		"voipgroup_mutedByAdminIcon" to palette.a3_400.toArgb(),
		"voipgroup_mutedByAdminMuteButton" to palette.a3_300.toArgb(),
		"voipgroup_mutedByAdminMuteButtonDisabled" to palette.a3_600.toArgb(),
		"voipgroup_mutedIcon" to palette.a2_400.toArgb(),
		"voipgroup_mutedIconUnscrolled" to palette.a2_400.toArgb(),
		"voipgroup_nameText" to palette.n1_0.toArgb(),
		"voipgroup_overlayAlertGradientMuted" to palette.a2_400.toArgb(),
		"voipgroup_overlayAlertGradientMuted2" to palette.a2_200.toArgb(),
		"voipgroup_overlayAlertGradientUnmuted" to palette.a1_400.toArgb(),
		"voipgroup_overlayAlertGradientUnmuted2" to palette.a1_200.toArgb(),
		"voipgroup_overlayAlertMutedByAdmin" to palette.a3_300.toArgb(),
		"voipgroup_overlayBlue1" to palette.a2_400.toArgb(),
		"voipgroup_overlayBlue2" to palette.a2_200.toArgb(),
		"voipgroup_overlayGreen1" to palette.a1_400.toArgb(),
		"voipgroup_overlayGreen2" to palette.a1_200.toArgb(),
		"voipgroup_scrollUp" to palette.a1_400.toArgb(),
		"voipgroup_searchBackground" to palette.n2_700.toArgb(),
		"voipgroup_searchPlaceholder" to palette.n2_200.toArgb(),
		"voipgroup_searchText" to palette.n1_0.toArgb(),
		"voipgroup_soundButton" to palette.a3_400.toArgb(),
		"voipgroup_soundButton2" to palette.a1_400.toArgb(),
		"voipgroup_soundButtonActive" to palette.a3_400.toArgb(),
		"voipgroup_soundButtonActive2" to palette.a1_400.toArgb(),
		"voipgroup_soundButtonActive2Scrolled" to palette.a1_400.toArgb(),
		"voipgroup_soundButtonActiveScrolled" to palette.a3_400.toArgb(),
		"voipgroup_speakingText" to palette.a1_100.toArgb(),
		"voipgroup_topPanelBlue1" to palette.n2_600.toArgb(),
		"voipgroup_topPanelBlue2" to palette.n2_300.toArgb(),
		"voipgroup_topPanelGray" to palette.n1_900.toArgb(),
		"voipgroup_topPanelGreen1" to palette.a1_400.toArgb(),
		"voipgroup_topPanelGreen2" to palette.a1_600.toArgb(),
		"voipgroup_unmuteButton" to palette.a2_500.toArgb(),
		"voipgroup_unmuteButton2" to palette.a2_600.toArgb(),
		"voipgroup_windowBackgroundWhiteInputField" to palette.n1_0.toArgb(),
		"voipgroup_windowBackgroundWhiteInputFieldActivated" to palette.n1_0.toArgb(),
		"wallet_blackBackground" to palette.n1_1000.toArgb(),
		"wallet_greenText" to 0xFFFFFFFF,
		"windowBackgroundChecked" to palette.n1_900.toArgb(),
		"windowBackgroundCheckText" to palette.n1_50.toArgb(),
		"windowBackgroundGray" to palette.n1_900.toArgb(),
		"windowBackgroundGrayShadow" to palette.n1_900.toArgb(),
		"windowBackgroundUnchecked" to palette.n1_900.toArgb(),
		"windowBackgroundWhite" to palette.n1_900.toArgb(),
		"windowBackgroundWhiteBlackText" to palette.n1_50.toArgb(),
		"windowBackgroundWhiteBlueButton" to palette.a1_100.toArgb(),
		"windowBackgroundWhiteBlueHeader" to palette.a1_100.toArgb(),
		"windowBackgroundWhiteBlueIcon" to palette.a1_100.toArgb(),
		"windowBackgroundWhiteBlueText" to palette.n1_50.toArgb(),
		"windowBackgroundWhiteBlueText2" to palette.a2_200.toArgb(),
		"windowBackgroundWhiteBlueText3" to palette.a2_200.toArgb(),
		"windowBackgroundWhiteBlueText4" to palette.a2_200.toArgb(),
		"windowBackgroundWhiteBlueText5" to palette.a2_200.toArgb(),
		"windowBackgroundWhiteBlueText6" to palette.a2_200.toArgb(),
		"windowBackgroundWhiteBlueText7" to palette.a2_200.toArgb(),
		"windowBackgroundWhiteGrayIcon" to palette.a1_100.toArgb(),
		"windowBackgroundWhiteGrayLine" to palette.a1_200.toArgb(),
		"windowBackgroundWhiteGrayText" to palette.n1_200.toArgb(),
		"windowBackgroundWhiteGrayText2" to palette.n1_200.toArgb(),
		"windowBackgroundWhiteGrayText3" to palette.n1_200.toArgb(),
		"windowBackgroundWhiteGrayText4" to palette.n1_200.toArgb(),
		"windowBackgroundWhiteGrayText5" to palette.n1_200.toArgb(),
		"windowBackgroundWhiteGrayText6" to palette.n1_200.toArgb(),
		"windowBackgroundWhiteGrayText7" to palette.n1_200.toArgb(),
		"windowBackgroundWhiteGrayText8" to palette.n1_200.toArgb(),
		"windowBackgroundWhiteGreenText" to palette.n1_50.toArgb(),
		"windowBackgroundWhiteGreenText2" to palette.n1_50.toArgb(),
		"windowBackgroundWhiteHintText" to palette.n1_50.toArgb(),
		"windowBackgroundWhiteInputField" to palette.a1_200.toArgb(),
		"windowBackgroundWhiteInputFieldActivated" to palette.a1_100.toArgb(),
		"windowBackgroundWhiteLinkSelection" to palette.n1_600.toArgb(),
		"windowBackgroundWhiteLinkText" to palette.a3_100.toArgb(),
		"windowBackgroundWhiteRedText" to 0xFFFFFFFF,
		"windowBackgroundWhiteRedText2" to 0xFFFFFFFF,
		"windowBackgroundWhiteRedText3" to 0xFFFFFFFF,
		"windowBackgroundWhiteRedText4" to 0xFFFFFFFF,
		"windowBackgroundWhiteRedText5" to 0xFFFFFFFF,
		"windowBackgroundWhiteRedText6" to 0xFFFFFFFF,
		"windowBackgroundWhiteYellowText" to palette.a1_100.toArgb(),
		"windowBackgroundWhiteValueText" to palette.a1_100.toArgb()
	)

	return themeMap as Map<String, Int>
}

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