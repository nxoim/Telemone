package com.dotstealab.telemone.ui.screens.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.dotstealab.telemone.MainViewModel
import com.dotstealab.telemone.ui.theme.SolarSet
import com.dotstealab.telemone.ui.theme.fullPalette

@Composable
fun DefaultThemesButtons(vm: MainViewModel) {
	val context = LocalContext.current
	val palette = fullPalette()
	Column(Modifier.fillMaxWidth()) {
		LightThemeButton { vm.saveLightModeTheme(context, palette) }
		DarkThemeButton(Modifier.align(Alignment.End).padding(top = 16.dp)) { vm.saveDarkModeTheme(context, palette) }
	}
}

@Composable
private fun LightThemeButton(modifier: Modifier = Modifier, save: () -> Unit) {
	OutlinedCard(
		modifier
			.size(198.dp)
			.clip(RoundedCornerShape(32.dp))
			.clickable { save() },
		shape = RoundedCornerShape(32.dp)
	) {
		Column(
			Modifier.padding(24.dp).fillMaxSize(),
			verticalArrangement = Arrangement.SpaceEvenly
		) {
			Icon(
				SolarSet.Sun,
				contentDescription = "",
				Modifier.size(64.dp).align(Alignment.End)
			)
			Text(
				text = "Save Light",
				style = MaterialTheme.typography.displaySmall.plus(
					// this removes default padding around text
					TextStyle(platformStyle = PlatformTextStyle(false))
				)
			)
		}
	}
}

@Composable
private fun DarkThemeButton(modifier: Modifier = Modifier, save: () -> Unit) {
	OutlinedCard(
		modifier
			.size(198.dp)
			.clip(RoundedCornerShape(32.dp))
			.clickable { save() },
		shape = RoundedCornerShape(32.dp)
	) {
		Column(
			Modifier.padding(24.dp).fillMaxSize(),
			verticalArrangement = Arrangement.SpaceEvenly
		) {
			Text(
				text = "Save Dark",
				style = MaterialTheme.typography.displaySmall .plus(
					// this removes default padding around text
					TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
				),
				modifier = Modifier.width(78.dp).align(Alignment.End)
			)
			Icon(
				SolarSet.Moon,
				contentDescription = "",
				Modifier.size(64.dp)
			)
		}
	}
}