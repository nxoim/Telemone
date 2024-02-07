package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.number869.telemone.data.LoadedTheme
import kotlinx.coroutines.delay

@Composable
fun ElementColorItem(
	modifier: Modifier = Modifier,
	uiElementData: Pair<String, Pair<String, Color>>,
	index: Int,
	themeMap: LoadedTheme,
	lastIndexInList: Int
) {
	val backgroundColor by animateColorAsState(
		when (themeMap.containsKey(uiElementData.first)) {
			true -> themeMap[uiElementData.first]!!.second
			else -> Color.Red
		}, label = ""
	)

	val roundedCornerShape = if (index == 0)
		RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
	else if (index == lastIndexInList)
		RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 32.dp, bottomEnd = 32.dp)
	else
		RoundedCornerShape(4.dp)

	Box(contentAlignment = Alignment.Center) {
		var showPopUp by remember { mutableStateOf(false) }

		Column(
			modifier
				.clip(roundedCornerShape)
				.background(backgroundColor)
				.height(64.dp)
				.fillMaxWidth()
				.clickable { showPopUp = !showPopUp },
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center,
		) {
			// name
			Text(
				text = uiElementData.first,
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
				color = Color.White,
				modifier = Modifier
					.clip(CircleShape)
					.background(Color(0x4D000000))
					.padding(horizontal = 8.dp, vertical = 2.dp)
			)

			Spacer(modifier = Modifier.height(8.dp))

			// material you palette color token
			Text(
				text = uiElementData.second.first,
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
				color = Color.White,
				modifier = Modifier
					.clip(CircleShape)
					.background(Color(0x4D000000))
					.padding(horizontal = 8.dp, vertical = 2.dp)
			)
		}

		if (showPopUp) {
			// so that the performance does not suffer
			var startPopupAnimation by remember { mutableStateOf(false) }

			Popup(alignment = Alignment.TopCenter) {
				Box(
					Modifier.fillMaxSize(),
					contentAlignment = BiasAlignment(0f, 0.8f)
				) {
					// helps avoid accidental input like accidentally opening
					// another popup when clicking outside of the popup,
					// accidentally closing the popup when using gestures
					Box(
						Modifier
							.fillMaxSize()
							.clickable(
								interactionSource = remember { MutableInteractionSource() },
								indication = null,
								onClick = { startPopupAnimation = false }
							)
					)

					PalettePopup(
						uiElementData.first,
						backgroundColor,
						uiElementData.second.first,
						startPopupAnimation
					) { startPopupAnimation = false }
				}
			}

			LaunchedEffect(Unit) {
				startPopupAnimation = true
			}

			LaunchedEffect(startPopupAnimation) {
				delay(300)

				if (!startPopupAnimation) {
					showPopUp = false
				}
			}
		}
	}
}