package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CurrentThemePreview(colorOf: @Composable (String) -> Color) {
	Row(
		Modifier
			.padding(16.dp)
			.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceEvenly
	) {
		PreviewHomeScreen(colorOf)
		PreviewChat(colorOf)
	}
}

@Composable
private fun PreviewHomeScreen(colorOf: @Composable (String) -> Color) {
	OutlinedCard(
		Modifier
			.width(150.dp)
			.height(180.dp),
		shape = RoundedCornerShape(16.dp),
		colors = CardDefaults.outlinedCardColors(colorOf("windowBackgroundWhite")),
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.background(colorOf("windowBackgroundWhite"))
		) {
			ListPreviewHeader(
				backgroundColor = colorOf("actionBarDefault"),
				iconsColor = colorOf("actionBarDefaultIcon"),
				titleColor = colorOf("actionBarDefaultTitle"),
				folderUnderlineColor = colorOf("actionBarTabLine"),
				selectedFolderItemColor = colorOf("actionBarActiveText"),
				unselectedFolderItemColor = colorOf("actionBarUnactiveText")
			)

			Spacer(modifier = Modifier.height(2.dp))

			ChatItem(pinned = true, colorOf = colorOf)
			ChatItem(unread = true, colorOf = colorOf)
			ChatItem(secret = true, sent = true, colorOf = colorOf)
			ChatItem(muted = true, unread = true, colorOf = colorOf)
			ChatItem(verified = true, colorOf = colorOf)
		}
	}
}

@Composable
private fun PreviewChat(colorOf: @Composable (String) -> Color) {
	OutlinedCard(
		Modifier
			.width(150.dp)
			.height(180.dp),
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
		Messages(colorOf("windowBackgroundWhite"))
		ChatBottomAppBar(
			colorOf("chat_messagePanelBackground"),
//				colorOf("chats_attachMessage"),
			colorOf("chat_messagePanelIcons"),
			colorOf("chat_messagePanelHint")
		)
	}
}

// Preview components

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
private fun Messages(backgroundColor: Color) {
	Column(
		Modifier
			.fillMaxWidth()
			.height(122.dp)
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

@Composable
private fun ListPreviewHeader(
	backgroundColor: Color,
	iconsColor: Color,
	titleColor: Color,
	folderUnderlineColor: Color,
	selectedFolderItemColor: Color,
	unselectedFolderItemColor: Color
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
				Icons.Default.Menu,
				contentDescription = "",
				Modifier.size(12.dp),
				tint = iconsColor
			)

			Spacer(modifier = Modifier.width(2.dp))

			Text(
				"Telemone",
				fontSize = 8.sp,
				fontWeight = FontWeight.Medium,
				style = TextStyle(
					platformStyle = PlatformTextStyle(false)
				),
				color = titleColor
			)
		}

		Row(Modifier.padding(horizontal = 8.dp)) {
			Icon(
				Icons.Default.Search,
				contentDescription = "",
				Modifier.size(12.dp),
				tint = iconsColor
			)
		}
	}
}

@Composable
fun ChatItem(
	pinned: Boolean = false,
	unread: Boolean = false,
	sent: Boolean = false,
	secret: Boolean = false,
	muted: Boolean = false,
	verified: Boolean = false,
	colorOf: @Composable (String) -> Color
) {
	val avatarBackgroundNumber by remember {
		derivedStateOf {
			(1..7).random()
		}
	}

	val backgroundColor = colorOf("windowBackgroundWhite")
	val unreadCounterColor = colorOf("chats_unreadCounter")
	val unreadMutedCounterColor = colorOf("chats_unreadCounterMuted")
	val unreadCounterNumberColor = colorOf("chats_unreadCounterText")
	val muteIconColor = colorOf("chats_muteIcon")
	val dividerColor = colorOf("divider")
	val messageTextColor = colorOf("chats_message")
	val messageAttachementColor = colorOf("chats_actionMessage")
	val pinIconColor = colorOf("chats_pinnedIcon")
	val dateColor = colorOf("chats_date")
	val readCheckColor = colorOf("chats_sentReadCheck")
	val avatarService1Color = colorOf("avatar_backgroundSaved")
	val avatarService2Color = colorOf("avatar_background2Saved")
	val chatSecretIconColor = colorOf("chats_secretIcon")
	val chatSecretNameColor = colorOf("chats_secretName")
	val chatNameColor = colorOf("chats_name")
//    val mutedIconColor literally doesnt show in app
	val verifiedIconBackgroundColor = colorOf("chats_verifiedBackground")
	val verifiedIconColor = colorOf("chats_verifiedCheck")
	val avatarTextColor = colorOf("avatar_text")

	val avatarColor1 = when (avatarBackgroundNumber) {
		1 -> colorOf("avatar_backgroundBlue")
		2 -> colorOf("avatar_backgroundGreen")
		3 -> colorOf("avatar_backgroundOrange")
		4 -> colorOf("avatar_backgroundPink")
		5 -> colorOf("avatar_backgroundRed")
		6 -> colorOf("avatar_backgroundViolet")
		7 -> colorOf("avatar_backgroundCyan")
		else -> colorOf("avatar_backgroundBlue")
	}



	val avatarColor2 = when (avatarBackgroundNumber) {
		1 -> colorOf("avatar_background2Blue")
		2 -> colorOf("avatar_background2Green")
		3 -> colorOf("avatar_background2Orange")
		4 -> colorOf("avatar_background2Pink")
		5 -> colorOf("avatar_background2Red")
		6 -> colorOf("avatar_background2Violet")
		7 -> colorOf("avatar_background2Cyan")
		else -> colorOf("avatar_background2Blue")
	}


	Column {
		Row(
			modifier = Modifier
				.padding(start = 6.dp, end = 6.dp, top = 3.dp, bottom = 3.dp)
				.fillMaxWidth()
				.background(backgroundColor),
			horizontalArrangement = Arrangement.Start,
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(
				Modifier
					.size(18.dp)
					.clip(CircleShape)
					.background(
						brush = Brush.verticalGradient(
							listOf(avatarColor1, avatarColor2)
						)
					)
			) {
				Column(
					modifier = Modifier.fillMaxSize(),
					verticalArrangement = Arrangement.Center,
					horizontalAlignment = Alignment.CenterHorizontally
				) {
					Text(
						text = "A",
						color = avatarTextColor,
						fontSize = 6.sp,
						style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
					)
				}
			}
			
			Spacer(modifier = Modifier.width(4.dp))
			
			Column(verticalArrangement = Arrangement.Center) {
				Row(verticalAlignment = Alignment.CenterVertically) {
					if (secret) {
						Icon(
							imageVector = Icons.Default.Lock,
							contentDescription = "Secret Chat",
							modifier = Modifier.size(6.dp),
							tint = chatSecretIconColor
						)
						Spacer(modifier = Modifier.width(2.dp))
					}
					Text(
						maxLines = 1,
						text = "Chat Name",
						fontWeight = FontWeight.Bold,
						fontSize = 6.sp,
						color = if (secret) chatSecretNameColor else chatNameColor,
						style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
					)
					if (muted) {
						Spacer(modifier = Modifier.width(2.dp))
						Icon(
							imageVector = Icons.Default.VolumeOff,
							contentDescription = "Muted",
							modifier = Modifier.size(4.dp),
							tint = muteIconColor
						)
					} else if (verified) {
						Spacer(modifier = Modifier.width(2.dp))
						Icon(
							imageVector = Icons.Filled.Verified,
							contentDescription = "Verified",
							modifier = Modifier.size(6.dp),
							tint = verifiedIconColor
						)
					}
				}
				
				Spacer(modifier = Modifier.height(2.dp))
				
				val message = "Lorem ipsum dolor"
				Text(
					text = if (sent) "You: $message" else message,
					fontSize = 6.sp,
					maxLines = 1,
					color = messageTextColor,
					modifier = Modifier.weight(1f, false),
					style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
				)
			}
			
			Spacer(modifier = Modifier.weight(1f))
			
			Column(
				horizontalAlignment = Alignment.End,
				verticalArrangement = Arrangement.Top
			) {
				Row(horizontalArrangement = Arrangement.End) {
					if (sent) {
						Icon(
							imageVector = Icons.Default.DoneAll,
							contentDescription = "Sent",
							modifier = Modifier
								.size(8.dp)
								.weight(1f, false),
							tint = readCheckColor
						)
						Spacer(modifier = Modifier.width(2.dp))
					}
					Text(
						maxLines = 1,
						text = "12:00",
						fontSize = 5.sp,
						color = dateColor,
						style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
					)
				}
				if (pinned) {
					Spacer(modifier = Modifier.height(2.dp))
					Icon(
						imageVector = Icons.Default.PushPin,
						contentDescription = "Pinned",
						modifier = Modifier
							.size(6.dp)
							.rotate(45f),
						tint = pinIconColor
					)
				} else if (unread) {
					Spacer(modifier = Modifier.height(2.dp))
					Column(
						modifier = Modifier
							.size(8.dp)
							.clip(CircleShape)
							.background(unreadCounterColor),
						verticalArrangement = Arrangement.Center,
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						Text(
							text = "1",
							color = unreadCounterNumberColor,
							fontSize = 5.sp,
							fontWeight = FontWeight.Bold,
							style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
						)
					}
				}
			}
		}

		Divider(Modifier.padding(horizontal = 8.dp), color = dividerColor)
	}
}