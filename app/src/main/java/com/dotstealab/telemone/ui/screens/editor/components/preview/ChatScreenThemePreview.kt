package com.dotstealab.telemone.ui.screens.editor.components.preview

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dotstealab.telemone.MainViewModel


@Composable
fun ChatScreenPreview(vm: MainViewModel) {
    fun colorOf(colorValueOf: String): Color {
        return try {
            vm.mappedValues.getOrElse(colorValueOf) { Pair("", Color.Red) }.second
        } catch (e: NoSuchElementException) {
            Color.Red
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        Box() {
            Column(
                modifier = Modifier
                    .padding(8.dp),
                verticalArrangement = Arrangement.Bottom,
            ) {
                MessageWithImageReceivedBubble()
                MessageSentBubble(colorOf("chat_outBubble"))
                VoiceMessageSentBubble()
                MessageReceivedBubble()
                SongReceivedBubble()
                MessageSentBubble(colorOf("chat_outBubble"))
            }
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column() {
                    GroupInfoTopBar(colorOf("actionBarDefault"),
                        colorOf("actionBarDefaultIcon"),
                        colorOf("avatar_backgroundOrange"),
                        colorOf("avatar_text"),
                        colorOf("actionBarDefaultTitle"),
                        colorOf("actionBarDefaultSubtitle"))
                    PinnedMessages(colorOf("actionBarDefault"))
                }

                BottomBar(
                    colorOf("chat_messagePanelBackground"),
//				colorOf("chats_attachMessage"),
                    colorOf("chat_messagePanelIcons"),
                    colorOf("chat_messagePanelHint")
                )
            }

        }
    }
}

@Composable
fun GroupInfoTopBar(
    backgroundColor: Color,
    iconColor: Color,
    emptyAvatarPreviewBackgroundColor: Color,
    emptyAvatarPreviewLetterColor: Color,
    titleTextColor: Color,
    membersTextColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .padding(16.dp),
            tint = iconColor,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(100))
                .background(emptyAvatarPreviewBackgroundColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "A",
                    color = emptyAvatarPreviewLetterColor,
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "Cool Group",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = titleTextColor
            )
            Text(
                text = "30 members, 2 online",
                style = MaterialTheme.typography.bodySmall,
                color = membersTextColor
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            modifier = Modifier
                .padding(8.dp),
            tint = iconColor,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "More",
            modifier = Modifier
                .padding(8.dp),
            tint = iconColor,
        )
    }
}

@Composable
fun PinnedMessages(backgroundColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(8.dp)
            .height(40.dp),
        horizontalArrangement = Arrangement.Start,
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Card(
                modifier = Modifier
                    .width(3.dp)
                    .height(12.dp)
                    .background(MaterialTheme.colorScheme.onPrimary),
                shape = RoundedCornerShape(20),
            ) { }
            Spacer(modifier = Modifier.height(2.dp))
            Card(
                modifier = Modifier
                    .width(3.dp)
                    .height(12.dp)
                    .background(MaterialTheme.colorScheme.onPrimary),
                shape = RoundedCornerShape(20),
            ) { }
            Spacer(modifier = Modifier.height(2.dp))
            Card(
                modifier = Modifier
                    .width(3.dp)
                    .height(12.dp)
                    .background(MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(20),
            ) { }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "Pinned Messages",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
            Text(
                text = "Lorem ipsum dolor sit amet",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.PushPin,
            contentDescription = "More",
            modifier = Modifier
                .padding(8.dp)
                .size(32.dp)
                .rotate(45f),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}

@Composable
fun MessageWithImageReceivedBubble() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 80.dp),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(7),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(48.dp))
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Image",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Spacer(modifier = Modifier.height(48.dp))
                }
                Box {
                    Text(
                        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                        modifier = Modifier
                            .padding(8.dp),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.End,
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "12:00",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageSentBubble(backgroundColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 80.dp),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            )
        ) {
            Box {
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                    modifier = Modifier
                        .padding(8.dp),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End,
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "12:00",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Default.DoneAll,
                            contentDescription = "Read",
                            modifier = Modifier
                                .size(14.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VoiceMessageSentBubble() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 160.dp),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Card(
                    modifier = Modifier
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black,
                    ),
                    shape = RoundedCornerShape(100),
                ) {
                    Icon(
                        imageVector = Icons.Default.Pause,
                        contentDescription = "VoicePause",
                        modifier = Modifier
                            .padding(8.dp)
                            .size(24.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                (0..60).forEach {
                    Spacer(modifier = Modifier.width(1.dp))
                    Card(
                        modifier = Modifier
                            .width(1.dp)
                            .height(
                                if (it < 8 || it > 52) 4.dp
                                else (3..18).random().dp
                            ),
                        shape = RoundedCornerShape(20),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                        ),
                    ) { }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "12:00",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = "Read",
                        modifier = Modifier
                            .size(14.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
    }
}

@Composable
fun MessageReceivedBubble() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 80.dp),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20),
        ) {
            Box {
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                    modifier = Modifier
                        .padding(8.dp),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.End,
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "12:00",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }
    }
}

@Composable
fun SongReceivedBubble() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 120.dp),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(20),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box {
                    Card(
                        modifier = Modifier
                            .padding(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black,
                        ),
                        shape = RoundedCornerShape(100),
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play",
                            modifier = Modifier
                                .padding(8.dp)
                                .size(24.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .size(50.dp)
                            .aspectRatio(1f),
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                            ),
                            shape = RoundedCornerShape(100),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Save",
                                modifier = Modifier
                                    .padding(2.dp)
                                    .size(10.dp),
                                tint = Color.Black,
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = "Song Name",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        text = "Artist Name",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Text(
                        text = "0:00 / 3:00",
                        fontSize = 8.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = "12:00",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    backgroundColor: Color,
    iconColor: Color,
    textHintColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(backgroundColor),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Icon(
            imageVector = Icons.Default.SentimentSatisfied,
            contentDescription = "EmojisStickersGifs",
            modifier = Modifier
                .size(32.dp),
            tint = iconColor,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "Message",
            fontSize = 18.sp,
            color = textHintColor,
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Terminal,
            contentDescription = "Photo",
            modifier = Modifier
                .size(32.dp),
            tint = iconColor,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            imageVector = Icons.Default.AttachFile,
            contentDescription = "AttachFile",
            modifier = Modifier
                .rotate(225f)
                .size(32.dp),
            tint = iconColor,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Voice",
            modifier = Modifier
                .size(32.dp),
            tint = iconColor,
        )
    }
}

@Preview(
    device = "id:pixel_6_pro",
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE,
    showBackground = true, apiLevel = 33
)
@Composable
fun ChatScreenPreviewPreview() {
    ChatScreenPreview(MainViewModel(Application()))
}