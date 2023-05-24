package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.number869.telemone.LoadedTheme
import com.number869.telemone.MainViewModel
import com.number869.telemone.ui.screens.editor.components.old.PalettePopup
import com.number869.telemone.ui.theme.FullPaletteList

@Composable
fun ElementColorItem(
	modifier: Modifier = Modifier,
	uiElementData: Pair<String, Pair<String, Color>>,
	vm: MainViewModel,
	index: Int,
	themeMap: LoadedTheme,
	lastIndexInList: Int,
	palette: FullPaletteList
) {
	var showPopUp by remember { mutableStateOf(false) }
	val backgroundColor = when (themeMap.containsKey(uiElementData.first)) {
		true -> themeMap[uiElementData.first]!!.second
		else -> Color.Red
	}

	val roundedCornerShape = if (index == 0)
		RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = 4.dp, bottomEnd = 4.dp)
	else if (index == lastIndexInList)
		RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 32.dp, bottomEnd = 32.dp)
	else
		RoundedCornerShape(4.dp)

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
		Popup(onDismissRequest = { showPopUp = false }) {
			PalettePopup(uiElementData.first, vm, palette)
		}
	}
}