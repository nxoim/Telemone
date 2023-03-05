package com.dotstealab.telemone.ui.screens.editor.components.preview

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ListOfChatsScreenPreview() {
    Column(
        modifier = Modifier
            .systemBarsPadding()
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        TgHeader()
        ChatItem(pinned = true)
        ChatItem(unread = true)
        ChatItem(secret = true, sent = true)
        ChatItem(muted = true, unread = true)
        ChatItem(verified = true)
        ChatItem(secret = true)
    }
}

@Composable
fun TgHeader() {
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
                .padding(16.dp)
                .size(24.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Text(
            text = "TeleMone",
            modifier = Modifier
                .padding(16.dp),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            modifier = Modifier
                .padding(16.dp)
                .size(24.dp),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
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
) {
    Row(
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 8.dp,
            )
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Card(
            modifier = Modifier
                .size(50.dp),
            shape = RoundedCornerShape(100),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "A",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
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
                            .size(14.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }
                Text(
                    text = "Chat Name",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color =
                        if(secret) MaterialTheme.colorScheme.onPrimaryContainer
                        else if(isSystemInDarkTheme()) Color.White
                        else Color.Black,
                )
                if(muted) {
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Default.VolumeOff,
                        contentDescription = "Muted",
                        modifier = Modifier
                            .size(14.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
                else if(verified) {
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(
                        imageVector = Icons.Filled.Verified,
                        contentDescription = "Verified",
                        modifier = Modifier
                            .size(14.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            val message = "Lorem ipsum dolor sit amet"
            Text(
                text =
                    if(sent) "You: $message"
                    else "$message",
                fontSize = 15.sp,
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
                            .size(20.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = "12:00",
                    fontSize = 11.sp,
                )
            }
            if(pinned) {
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    imageVector = Icons.Default.PushPin,
                    contentDescription = "Pinned",
                    modifier = Modifier
                        .size(18.dp)
                        .rotate(45f),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
            else if(unread) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier
                        .size(18.dp),
                    shape = RoundedCornerShape(100),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "1",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 8.sp,
                        )
                    }
                }
            }
        }
    }
}

@Preview (showSystemUi = true, device = "spec:width=1080px,height=2400px,dpi=440")
@Composable
fun ListOfChatsScreenPreviewPreview() {
    ListOfChatsScreenPreview()
}