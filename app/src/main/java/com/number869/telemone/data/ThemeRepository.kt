package com.number869.telemone.data

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.number869.telemone.shared.utils.getColorValueFromColorToken
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import org.mongodb.kbson.ObjectId

class ThemeRepository(
	private val realm: Realm = Realm.open(
		RealmConfiguration.create(
			setOf(ThemeDataRealm::class, UiElementColorDataRealm::class)
		)
	),
	private val context: Context
) {
    fun getThemesInRange(range: IntRange) = realm.query(ThemeDataRealm::class)
        .sort("_id", Sort.DESCENDING)
        .asFlow()
        .flowOn(Dispatchers.IO)
        .map { results ->
            results.list
                .drop(range.first)
                .take(range.last - range.first + 1)
                .mapNotNull { theme ->
                    val allowed = when (theme.uuid) {
                        PredefinedTheme.LastSession.uuid,
                        PredefinedTheme.Default(true).uuid,
                        PredefinedTheme.Default(false).uuid -> false
                        else -> true
                    }

                    if (allowed) theme.toThemeData() else null
                }
        }

    fun getAllThemes() = realm.query(ThemeDataRealm::class)
        .sort("_id", Sort.DESCENDING)
		.asFlow()
		.flowOn(Dispatchers.IO)
		.map { results ->
			results.list
				.mapNotNull { theme ->
					val allowed = when(theme.uuid) {
						PredefinedTheme.LastSession.uuid,
						PredefinedTheme.Default(true).uuid,
						PredefinedTheme.Default(false).uuid -> false
						else -> true
					}

					if (allowed) theme.toThemeData() else null
				}
	}

    fun getThemeCount() = realm.query(ThemeDataRealm::class)
        .asFlow()
        .flowOn(Dispatchers.IO)
        .map { results ->
            results.list.count { theme ->
                when (theme.uuid) {
                    PredefinedTheme.LastSession.uuid,
                    PredefinedTheme.Default(true).uuid,
                    PredefinedTheme.Default(false).uuid -> false

                    else -> true
                }
            }
        }

    fun getThemeByUUID(uuid: String): ThemeData? {
		return realm.query(ThemeDataRealm::class, "uuid == $0", uuid)
			.first()
			.find()
			?.toThemeData()
	}

	fun getThemeByUUIDAsFlow(uuid: String) = realm
		.query(ThemeDataRealm::class, "uuid = $0", uuid)
		.first()
		.asFlow()
		.flowOn(Dispatchers.IO)
		.map { it.obj?.toThemeData() }

	suspend fun saveTheme(theme: ThemeData) = withContext(Dispatchers.IO) {
		realm.write {
			this.delete(this.query(ThemeDataRealm::class, "uuid == $0", theme.uuid))
			this.copyToRealm(theme.toRealmRepresentation(), UpdatePolicy.ERROR)
		}
		return@withContext
	}

	fun saveThemeBlocking(theme: ThemeData) {
		realm.writeBlocking {
			this.delete(this.query(ThemeDataRealm::class, "uuid == $0", theme.uuid))
			this.copyToRealm(theme.toRealmRepresentation(), UpdatePolicy.ERROR)
		}
	}

	suspend fun deleteTheme(uuid: String) = withContext(Dispatchers.IO) {
		realm.write {
			this.delete(this.query(ThemeDataRealm::class, "uuid == $0", uuid).first())
		}
	}

	fun deleteThemeBlocking(uuid: String) {
		realm.writeBlocking {
			this.delete(this.query(ThemeDataRealm::class, "uuid == $0", uuid).first())
		}
	}

	fun getStockTheme(
		palette: Map<String, Color>,
		light: Boolean
	) = getStockTheme(palette, context, light)

	private fun ThemeData.toRealmRepresentation() = ThemeDataRealm().apply {
		uuid = this@toRealmRepresentation.uuid
		values.addAll(this@toRealmRepresentation.values.map { it.toRealmRepresentation() })
	}

	private fun ThemeDataRealm.toThemeData() = ThemeData(
		uuid = uuid,
		values = values.map { it.toUiElementColorData() }
	)

	private fun UiElementColorData.toRealmRepresentation()= UiElementColorDataRealm().apply {
		name = this@toRealmRepresentation.name
		colorToken = this@toRealmRepresentation.colorToken
		colorValue = this@toRealmRepresentation.colorValue
	}

	private fun UiElementColorDataRealm.toUiElementColorData() = UiElementColorData(
		name = name,
		colorToken = colorToken,
		colorValue = colorValue
	)
}

private fun getStockTheme(
	palette: Map<String, Color>,
	context: Context,
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
		PredefinedTheme.Default(light).uuid,
		themeData
	)
}

fun themeRepositoryInitializer(context: Context) = ThemeRepository(context = context)


@Serializable
data class UiElementColorData(
	val name: String,
	val colorToken: String,
	val colorValue: Int
)

@Serializable
data class ThemeData(val uuid: String, val values: List<UiElementColorData>) {
    companion object {
        val Undefined = ThemeData("undefined", emptyList())
    }
}
private class ThemeDataRealm : RealmObject {
	@PrimaryKey
	var _id: ObjectId = ObjectId()
	var uuid: String = ""
	var values: RealmList<UiElementColorDataRealm> = realmListOf()
}

private class UiElementColorDataRealm : EmbeddedRealmObject {
	var name: String = ""
	var colorToken: String = ""
	var colorValue: Int = 0
}

sealed class PredefinedTheme(val uuid: String) {
	data class Default(private val light: Boolean) : PredefinedTheme(
		if (light)
			"defaultLightThemeUUID"
		else
			"defaultDarkThemeUUID"
	)

	data object LastSession : PredefinedTheme("lastSession")
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