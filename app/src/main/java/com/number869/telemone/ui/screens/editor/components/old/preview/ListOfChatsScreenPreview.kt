package com.number869.telemone.ui.screens.editor.components.old.preview

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material3.Card
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
import com.number869.telemone.MainViewModel

@Composable
fun ListOfChatsScreenPreview(vm: MainViewModel, animatedValue: Float = 1f) {
//    val scale = min(0.5f + animatedValue, 1f)
//    fun colorOf(colorValueOf: String): Color {
//        return try {
//            vm.mappedValues.getOrElse(colorValueOf) { Pair("", Color.Red) }.second
//        } catch (e: NoSuchElementException) {
//            Color.Red
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(colorOf("windowBackgroundWhite"))
//    ) {
//        Header(
//            scale,
//            backgroundColor = colorOf("actionBarDefault"),
//            iconsColor = colorOf("actionBarDefaultIcon"),
//            titleColor = colorOf("actionBarDefaultTitle"),
//            folderUnderlineColor = colorOf("actionBarTabLine"),
//            selectedFolderItemColor = colorOf("actionBarActiveText"),
//            unselectedFolderItemColor = colorOf("actionBarUnactiveText")
//        )
//        ChatItem(scale, pinned = true, vm = vm)
//        ChatItem(scale, unread = true, vm = vm)
//        ChatItem(scale, secret = true, sent = true, vm = vm)
//        ChatItem(scale, muted = true, unread = true, vm = vm)
//        ChatItem(scale, verified = true, vm = vm)
//        ChatItem(scale, secret = true, vm = vm)
//    }
}

@Composable
fun Header(
    animatedValue: Float,
    backgroundColor: Color,
    iconsColor: Color,
    titleColor: Color,
    folderUnderlineColor: Color,
    selectedFolderItemColor: Color,
    unselectedFolderItemColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = "Menu",
            modifier = Modifier
                .padding((16 * animatedValue).dp)
                .size((24 * animatedValue).dp),
            tint = iconsColor,
        )
        Text(maxLines = 1,
            text = "TeleMone",
            modifier = Modifier
                .padding((16 * animatedValue).dp),
            fontSize = (20 * animatedValue).sp,
            fontWeight = FontWeight.Bold,
            color = titleColor,
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            modifier = Modifier
                .padding((16 * animatedValue).dp)
                .size((24 * animatedValue).dp),
            tint = iconsColor,
        )
    }
}

@Composable
fun ChatItem(
    animatedValue: Float,
    pinned: Boolean = false,
    unread: Boolean = false,
    sent: Boolean = false,
    secret: Boolean = false,
    muted: Boolean = false,
    verified: Boolean = false,
    vm: MainViewModel
) {
    fun colorOf(colorValueOf: String): Color {
        return try {
            vm.mappedValues.getOrElse(colorValueOf) { Pair("", Color.Red) }.second
        } catch (e: NoSuchElementException) {
            Color.Red
        }
    }
    val backgroundColor = colorOf("windowBackgroundWhite")
    val unreadCounterColor = colorOf("chats_unreadCounter")
    val unreadMutedCounterColor = colorOf("chats_unreadCounterMuted")
    val unreadCounterNumberColor = colorOf("chats_unreadCounterText")
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

    // TODO: put all of the colors
    val avatarCyan1BackgroundColor = colorOf("avatar_backgroundCyan")
    val avatarCyan2BackgroundColor = colorOf("avatar_backgroundCyan")
    val avatarTextColor = colorOf("avatar_text")

    Row(
        modifier = Modifier
            .padding(
                start = (16 * animatedValue).dp,
                end = (16 * animatedValue).dp,
                top = (8 * animatedValue).dp,
                bottom = (8 * animatedValue).dp,
            )
            .fillMaxWidth()
            .background(backgroundColor),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Card(
            modifier = Modifier
                .size((50 * animatedValue).dp),
            shape = RoundedCornerShape(100),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(maxLines = 1,
                    text = "A",
                    color = avatarTextColor,
                    fontSize = (18  * animatedValue).sp
                )
            }
        }
        Spacer(modifier = Modifier.width((12 * animatedValue).dp))
        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if(secret) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Secret Chat",
                        modifier = Modifier
                            .size((14 * animatedValue).dp),
                        tint = chatSecretIconColor,
                    )
                    Spacer(modifier = Modifier.width((2 * animatedValue).dp))
                }
                Text(maxLines = 1,
                    text = "Chat Name",
                    fontWeight = FontWeight.Bold,
                    fontSize = (15 * animatedValue).sp,
                    color =
                        if (secret) chatSecretNameColor
                        else chatNameColor,
                )
                if(muted) {
                    Spacer(modifier = Modifier.width((2 * animatedValue).dp))
                    Icon(
                        imageVector = Icons.Default.VolumeOff,
                        contentDescription = "Muted",
                        modifier = Modifier
                            .size((14 * animatedValue).dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                else if(verified) {
                    Spacer(modifier = Modifier.width((2 * animatedValue).dp))
                    Icon(
                        imageVector = Icons.Filled.Verified,
                        contentDescription = "Verified",
                        modifier = Modifier.size((14 * animatedValue).dp),
                        tint = verifiedIconColor,
                    )
                }
            }
            Spacer(modifier = Modifier.height((2 * animatedValue).dp))
            val message = "Lorem ipsum dolor sit amet"
            Text(maxLines = 1,
                text =
                    if(sent) "You: $message"
                    else "$message",
                fontSize = (15 * animatedValue).sp,
                color = messageTextColor,
                modifier = Modifier.weight(1f, false)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
            ) {
                if(sent) {
                    Icon(
                        imageVector = Icons.Default.DoneAll,
                        contentDescription = "Sent",
                        modifier = Modifier
                            .size((20 * animatedValue).dp)
                            .weight(1f, false),
                        tint = readCheckColor,
                    )
                    Spacer(modifier = Modifier.width((8 * animatedValue).dp))
                }
                Text(maxLines = 1,
                    text = "12:00",
                    fontSize = (11 * animatedValue).sp,
                    color = dateColor
                )
            }
            if(pinned) {
                Spacer(modifier = Modifier.height((8 * animatedValue).dp))
                Icon(
                    imageVector = Icons.Default.PushPin,
                    contentDescription = "Pinned",
                    modifier = Modifier
                        .size((18 * animatedValue).dp)
                        .rotate(45f),
                    tint = pinIconColor
                )
            }
            else if(unread) {
                Spacer(modifier = Modifier.height((8 * animatedValue).dp))
                Column(
                    modifier = Modifier
                        .size((18 * animatedValue).dp)
                        .clip(RoundedCornerShape(100))
                        .background(unreadCounterColor),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(maxLines = 1,
                            text = "1",
                            color = unreadCounterNumberColor,
                            fontSize = (14 * animatedValue).sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, device = "spec:width=1080px,height=2400px,dpi=440",
    showBackground = true,
    wallpaper = Wallpapers.BLUE_DOMINATED_EXAMPLE
)
@Composable
fun ListOfChatsScreenPreviewPreview() {
    ListOfChatsScreenPreview(MainViewModel(Application()))
}