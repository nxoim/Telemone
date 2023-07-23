package com.number869.telemone.ui.screens.main.components

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.number869.telemone.MainViewModel
import com.number869.telemone.R
import com.number869.telemone.shared.ui.TextWithFixedSize
import com.number869.telemone.ui.theme.SolarSet
import com.number869.telemone.ui.theme.fullPalette

@Composable
fun DefaultThemesButtons(vm: MainViewModel) {
	val context = LocalContext.current
	val palette = fullPalette()
	Column(Modifier.fillMaxWidth()) {
		LightThemeButton { vm.saveLightTheme(context) }
		DarkThemeButton(Modifier.align(Alignment.End).padding(top = 16.dp)) { vm.saveDarkTheme(context) }
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
			TextWithFixedSize(
				text = "Save Light",
				fontSize = 35.sp,
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
			TextWithFixedSize(
				text = "Save Dark",
				fontSize = 35.sp,
				style = MaterialTheme.typography.displaySmall.plus(
					// this removes default padding around text
					TextStyle(
						platformStyle = PlatformTextStyle(includeFontPadding = false)
					)
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