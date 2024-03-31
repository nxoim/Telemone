package com.number869.telemone.data

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.Serializable

// no im not making a data class
private typealias ThemeUUID = String
private typealias UiElementName = String
private typealias ColorToken = String
private typealias ColorValue = Int
private typealias DataAboutColors = Pair<ColorToken, ColorValue>
private typealias UiElementData = Map<UiElementName, DataAboutColors>
private typealias Theme = Map<ThemeUUID, UiElementData>
private typealias ThemeList = SnapshotStateList<Theme>
private typealias LoadedTheme = SnapshotStateMap<String, Pair<String, Color>>

@Serializable
data class UiElementColorData(
	val name: String,
	val colorToken: String,
	val colorValue: Int
)
val UiElementColorData.color get() = Color(colorValue)

@Serializable
data class ThemeData(val uuid: String, val values: List<UiElementColorData>)


class ThemeRepository(private val context: Context) {
	private val oldThemeListKey = "AppPreferences.ThemeList"
	private val preferences = context.getSharedPreferences(
		"AppPreferences",
		Context.MODE_PRIVATE
	)
	private var _themeList = MutableStateFlow(getAllThemesFromStorage())

	fun getAllThemes() = _themeList.asStateFlow()

	private fun getAllThemesFromStorage(): List<ThemeData> {
		var endResult = listOf<ThemeData>()
		val oldSavedThemeList = preferences.getString(oldThemeListKey, "[]")
		val oldType = object : TypeToken<ThemeList>() {}.type
		val oldList = Gson().fromJson<ThemeList>(oldSavedThemeList, oldType)

		if (oldList.isNotEmpty()) {
			val oldThemeList = oldList.map { map ->
				map.mapValues { entry ->
					entry.value.mapValues { (_, value) -> Pair(value.first, value.second) }
				}
			}
			val convertedToNew = oldThemeList.map { it.toNew() }
			endResult = convertedToNew
		}

		return endResult
	}

	fun getThemeByUUID(uuid: String): ThemeData? = _themeList.value.find { it.uuid == uuid }

	fun saveTheme(theme: ThemeData) {
		_themeList.value += theme
		saveThemeListToStorage()
	}

	fun deleteTheme(uuid: String) {
		_themeList.value.find { it.uuid == uuid }?.let {
			_themeList.value -= it
		}
		saveThemeListToStorage()
	}

	fun replaceTheme(targetThemesUUID: String, newData: List<UiElementColorData>) {
		val newList = _themeList.value.toMutableList()

		newList.replaceAll {
			if (it.uuid == targetThemesUUID)
				ThemeData(targetThemesUUID, newData)
			else
				it
		}

		_themeList.value = newList

		if (targetThemesUUID !in _themeList.value.map { it.uuid }) {
			_themeList.value += ThemeData(targetThemesUUID, newData)
		}
		saveThemeListToStorage()
	}

	private fun saveThemeListToStorage() {
		val oldThemeList = _themeList.value.map { it.toOld() }
		val contents = Gson().toJson(oldThemeList)
		preferences.edit().putString(oldThemeListKey, contents).apply()
	}

	fun getStockTheme(
		palette: Map<String, Color>,
		light: Boolean
	): ThemeData {
		val themeData = mutableListOf<UiElementColorData>()
		val fileName = if (light) "defaultLightFile.attheme" else "defaultDarkFile.attheme"

		context.assets.open(fileName)
			.bufferedReader().use { reader ->
				reader.forEachLine { line ->
					if (line.isNotEmpty()) {
						val splitLine = line.replace(" ", "").split("=")
						val name = splitLine[0]
						val colorToken = splitLine[1]

						val colorValue = getColorValueFromColorToken(colorToken, palette)

						themeData.add(
							UiElementColorData(
								name = name,
								colorToken = colorToken,
								colorValue = colorValue.toArgb()
							)
						)
					}
				}
			}

		return ThemeData(
			if (light) defaultLightThemeUUID else defaultDarkThemeUUID,
			themeData
		)
	}

	private fun Theme.toNew(): ThemeData {
		val themeData = this.values.first().map { (elementName, pair) ->
			UiElementColorData(
				name = elementName,
				colorToken = pair.first,
				colorValue = pair.second
			)
		}

		return ThemeData(this.keys.first(), themeData)
	}

	private fun ThemeData.toOld() = mapOf(
		uuid to values.associate { data ->
			data.name to (data.colorToken to data.colorValue)
		}
	)
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