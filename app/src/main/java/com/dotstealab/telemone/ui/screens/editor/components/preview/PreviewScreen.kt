package com.dotstealab.telemone.ui.screens.editor.components.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.dotstealab.telemone.MainViewModel

val PreviewScreens = mapOf(
    "Chat Screen" to "ChatScreenPreview",
    "List Of Chats Screen" to "ListOfChatsScreenPreview",
    "New Chat Screen" to "NewChatScreenPreview",
    "Settings Screen" to "SettingsScreenPreview",
    "Attachment Screen" to "AttachmentScreenPreview",
    "Group Info Screen" to "GroupInfoScreenPreview",
)

@Composable
fun ThemePreviewScreen(navController: NavHostController, vm: MainViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PreviewScreens.forEach {
            Button(onClick = { navController.navigate(it.value) }) {
                Text(text = it.key)
            }
        }
    }
}
