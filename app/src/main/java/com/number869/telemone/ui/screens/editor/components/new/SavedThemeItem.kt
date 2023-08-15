package com.number869.telemone.ui.screens.editor.components.new

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.number869.telemone.MainViewModel
import com.number869.telemone.data.AppSettings
import com.number869.telemone.getColorValueFromColorToken
import com.number869.telemone.ui.theme.FullPaletteList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedThemeItem(
	modifier: Modifier,
	vm: MainViewModel,
	uuid: String,
	palette: FullPaletteList,
	context: Context,
	isInSavedThemesRow: Boolean = false,
	colorDisplayTypeOverwrite: String? = null,
	changeSelectionMode: () -> Unit = { },
	themeSelectionModeIsActive: Boolean = false
) {
	var showMenu by remember { mutableStateOf(false) }
	var showDeleteDialog by remember { mutableStateOf(false) }
	var showLoadWithOptionsDialog by remember { mutableStateOf(false) }
	var showOverwriteChoiceDialog by remember { mutableStateOf(false) }
	var showOverwriteLightThemeDialog by remember { mutableStateOf(false) }
	var showOverwriteDarkThemeDialog by remember { mutableStateOf(false) }

	val selected by remember { derivedStateOf { vm.selectedThemes.contains(uuid) } }
	val selectedOverlayColor by animateColorAsState(
		if (themeSelectionModeIsActive && selected)
			MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
		else
			Color.Transparent,
		label = ""
	)

	val preferences = LocalContext.current.getSharedPreferences(
		"AppPreferences.Settings",
		Context.MODE_PRIVATE
	)

	// 1 - Saved color values
	// 2 - Current color scheme (fallback to saved colors)
	// 3 - Current color scheme
	val colorDisplayType = preferences.getString(
		AppSettings.SavedThemeItemDisplayType.id,
		"1"
	)

	// cry about it being here
	// tho this whole file could be prettier, yes
	@Composable
	fun colorOf(colorValueOf: String): Color {
		return animateColorAsState(
			when (
				if (colorDisplayTypeOverwrite == null)
					colorDisplayType
				else
					colorDisplayTypeOverwrite
			) {
				"1" -> {
					vm.themeList.find { it.containsKey(uuid) }
						?.get(uuid)
						?.get(colorValueOf)
						?.let { Color(it.second) } ?: Color.Red
				}
				"2" -> { // in case theres a need to show monet colors only when available
					vm.themeList.find { it.containsKey(uuid) }
						?.get(uuid)
						?.get(colorValueOf)
						?.let {
							val colorFromToken = getColorValueFromColorToken(it.first, palette)
							val colorAsSaved = Color(it.second)

							// colorFromToken prob should be Color? so it can
							// be null
							if (colorFromToken == Color.Red) colorAsSaved else colorFromToken
						} ?: Color.Red
				}
				else -> {
					vm.themeList.find { it.containsKey(uuid) }
						?.get(uuid)
						?.get(colorValueOf)
						?.let { getColorValueFromColorToken(it.first, palette) } ?: Color.Red
				}
			},
			label = "i hate these labels"
		).value
	}

	Box {
		OutlinedCard(
			modifier
				.clip(RoundedCornerShape(16.dp))
				.width(150.dp)
				.height(180.dp)
				.let {
					return@let if (isInSavedThemesRow && !themeSelectionModeIsActive)
						it.combinedClickable(
							onClick = {
								vm.loadTheme(
									uuid,
									withTokens = false,
									palette,
									clearCurrentTheme = true,
									context
								)

								Toast
									.makeText(
										context,
										"Theme loaded",
										Toast.LENGTH_SHORT
									)
									.show()
							},
							onLongClick = { showMenu = true }
						)
					else if (themeSelectionModeIsActive)
						it.clickable { vm.selectOrUnselectSavedTheme(uuid) }
					else
						it
				}
				.drawWithContent {
					drawContent()
					drawRect(selectedOverlayColor)
				},
			shape = RoundedCornerShape(16.dp)
		) {
			ChatTopAppBar(
				colorOf("actionBarDefault"),
				colorOf("actionBarDefaultIcon"),
				colorOf("avatar_backgroundOrange"),
				colorOf("avatar_text"),
				colorOf("actionBarDefaultTitle"),
				colorOf("actionBarDefaultSubtitle")
			)
			Messages(Modifier.weight(1f), colorOf("windowBackgroundWhite"))
			ChatBottomAppBar(
				colorOf("chat_messagePanelBackground"),
				colorOf("chat_messagePanelIcons"),
				colorOf("chat_messagePanelHint")
			)
		}

		AnimatedVisibility(
			visible = themeSelectionModeIsActive,
			enter = scaleIn(),
			exit = scaleOut()
		) {
			Checkbox(
				checked = selected,
				onCheckedChange = { vm.selectOrUnselectSavedTheme(uuid) },
				colors = CheckboxDefaults.colors(
					uncheckedColor = Color.Transparent
				),
				modifier = Modifier
					.padding(8.dp)
					.clip(CircleShape)
					.border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
					.size(24.dp)
			)
		}

		DropdownMenu(
			expanded = showMenu,
			onDismissRequest = { showMenu = false }
		) {
			DropdownMenuItem(
				text = { Text("Load theme with options") },
				onClick = { showLoadWithOptionsDialog = true }
			)
			DropdownMenuItem(
				text = { Text("Export this theme")},
				onClick = {
					showMenu = false
					vm.exportThemeWithColorValues(uuid, context)
				}
			)
			DropdownMenuItem(
				text = { Text("Export this theme in Telemone format")},
				onClick = {
					showMenu = false
					vm.exportThemeWithColorTokens(uuid, context)
				}
			)
			DropdownMenuItem(
				text = { Text("Overwrite a default theme")},
				onClick = {
					showMenu = false
					showOverwriteChoiceDialog = true
				}
			)
			DropdownMenuItem(
				text = { Text("Delete theme") },
				onClick = {
					showMenu = false
					showDeleteDialog = true
				}
			)
			DropdownMenuItem(
				text = { Text("Select") },
				onClick = {
					showMenu = false
					changeSelectionMode()
					vm.selectOrUnselectSavedTheme(uuid)
				}
			)
		}
	}

	// i dont know what to do with all these dialogs

	LoadWithOptionsDialog(
		{ showLoadWithOptionsDialog = false },
		showLoadWithOptionsDialog,
		vm,
		palette,
		uuid
	)

	DeleteThemeDialog(
		close = { showDeleteDialog = false },
		showDeleteDialog,
		vm,
		uuid,
		palette,
		context
	)

	OverwriteChoiceDialog(
		{ showOverwriteChoiceDialog = false },
		showOverwriteChoiceDialog,
		{ showOverwriteChoiceDialog = false; showOverwriteLightThemeDialog = true },
		{ showOverwriteChoiceDialog = false; showOverwriteDarkThemeDialog = true },
		vm,
		palette,
		context
	)

	OverwriteDefaultsDialog(
		close = { showOverwriteDarkThemeDialog = false },
		showOverwriteDarkThemeDialog,
		overwrite = {
			showOverwriteDarkThemeDialog = false
			vm.overwriteDefaultDarkTheme(uuid, palette, context)
		},
		vm = vm,
		overwriteDark = true,
		uuid,
		palette,
		context
	)

	OverwriteDefaultsDialog(
		close = { showOverwriteLightThemeDialog = false },
		showOverwriteLightThemeDialog,
		overwrite = {
			showOverwriteLightThemeDialog = false
			vm.overwriteDefaultLightTheme(uuid, palette, context)
		},
		vm = vm,
		overwriteDark = false,
		uuid,
		palette,
		context
	)
}

@Composable
private fun ChatTopAppBar(
	backgroundColor: Color,
	iconColor: Color,
	emptyAvatarPreviewBackgroundColor: Color,
	emptyAvatarPreviewLetterColor: Color,
	titleTextColor: Color,
	membersTextColor: Color
) {
	Row(
		Modifier
			.fillMaxWidth()
			.height(34.dp)
			.background(backgroundColor),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Row(
			Modifier.padding(horizontal = 8.dp),
			horizontalArrangement = Arrangement.spacedBy(4.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				Icons.Default.ArrowBack,
				contentDescription = "Chat Preview Arrow Back",
				Modifier.size(12.dp),
				tint = iconColor
			)

			Spacer(modifier = Modifier.width(2.dp))

			Box(
				Modifier
					.size(16.dp)
					.clip(CircleShape)
					.background(emptyAvatarPreviewBackgroundColor),
				contentAlignment = Alignment.Center
			) {
				Text(
					"P",
					fontSize = 8.sp,
					style = TextStyle(
						platformStyle = PlatformTextStyle(false)
					),
					color = emptyAvatarPreviewLetterColor
				)
			}

			Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
				Text(
					"Preview",
					fontSize = 7.sp,
					fontWeight = FontWeight.Medium,
					style = TextStyle(
						platformStyle = PlatformTextStyle(false)
					),
					color = titleTextColor
				)
				Text("48463 whatever",
					fontSize = 5.sp,
					style = TextStyle(
						platformStyle = PlatformTextStyle(false)
					),
					color = membersTextColor
				)
			}
		}

		Row(Modifier.padding(horizontal = 8.dp)) {
			Icon(
				Icons.Default.MoreVert,
				contentDescription = "Chat Preview Arrow Back",
				Modifier.size(12.dp),
				tint = iconColor
			)
		}
	}
}

@Composable
private fun Messages(modifier: Modifier = Modifier, backgroundColor: Color) {
	Column(
		modifier
			.fillMaxSize()
			.background(backgroundColor)
	) {

	}
}

@Composable
private fun ChatBottomAppBar(
	backgroundColor: Color,
	iconColor: Color,
	textHintColor: Color
) {
	Row(
		Modifier
			.fillMaxWidth()
			.height(23.dp)
			.background(backgroundColor),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Row(
			Modifier.padding(horizontal = 8.dp),
			horizontalArrangement = Arrangement.SpaceBetween
		) {
			Row(
				horizontalArrangement = Arrangement.spacedBy(6.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Icon(
					Icons.Outlined.Face,
					contentDescription = "Emoji icon in chat preview",
					Modifier.size(10.dp),
					tint = iconColor
				)

				Text(
					"Preview",
					fontSize = 7.sp,
					style = TextStyle(
						platformStyle = PlatformTextStyle(false)
					),
					color = textHintColor
				)
			}
		}
		Row(Modifier.padding(6.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
			Icon(
				Icons.Outlined.Add,
				contentDescription = "Emoji icon in chat preview",
				Modifier.size(10.dp),
				tint = iconColor
			)

			Icon(
				Icons.Default.Create,
				contentDescription = "Emoji icon in chat preview",
				Modifier.size(10.dp),
				tint = iconColor
			)
		}
	}
}

