package com.number869.telemone.ui.screens.editor.components.new

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.number869.telemone.data.ThemeData
import com.number869.telemone.shared.utils.ThemeColorDataType
import com.number869.telemone.shared.utils.ThemeStorageType
import com.number869.telemone.shared.utils.showToast

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedThemeItem(
	modifier: Modifier,
	colorOf: @Composable (String) -> Color,
	overlay: (@Composable () -> Unit)? = null
) {
	Box {
		OutlinedCard(
			Modifier
				.clip(RoundedCornerShape(16.dp))
				.then(modifier)
				.width(150.dp)
				.height(180.dp),
			shape = RoundedCornerShape(16.dp)
		) {
			Box {
				Column {
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

				overlay?.invoke()
			}
		}
	}
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

