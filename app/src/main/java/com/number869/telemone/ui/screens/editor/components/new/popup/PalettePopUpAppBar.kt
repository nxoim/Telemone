package com.number869.telemone.ui.screens.editor.components.new.popup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PalettePopupAppBar(
	isOnHomePage: Boolean,
	isPopupVisible: Boolean,
	currentUiElement: String,
	currentColor: Color,
	currentColorName: String,
	openHome: () -> Unit,
	hidePopup: () -> Unit,
) {
	Row(Modifier.fillMaxWidth(), horizontalArrangement = spacedBy(4.dp)) {
		Box(Modifier.padding(top = 8.dp, start = 8.dp)) {
			if (isOnHomePage) {
				IconButton(onClick = hidePopup) {
					Icon(Icons.Default.Close, contentDescription = "Close popup")
				}
			} else {
				IconButton(onClick = openHome) {
					Icon(Icons.Default.ArrowBack, contentDescription = "Back")
				}
			}
		}

		Row(
			Modifier.padding(top = 16.dp, bottom = 16.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(
				Modifier
					.clip(CircleShape)
					.background(currentColor)
					.width(24.dp)
					.border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
					.height(16.dp)
			)

			Spacer(modifier = Modifier.width(12.dp))

			Column(verticalArrangement = spacedBy(2.dp)) {
				Text(
					text = currentUiElement,
					style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
					modifier = Modifier.basicMarquee()
				)

				Text(
					text = currentColorName,
					style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					fontSize = 12.sp,
					modifier = Modifier.basicMarquee()
				)
			}
		}

		BackHandler(enabled = isPopupVisible) {
			if (isOnHomePage) hidePopup() else openHome()
		}
	}
}