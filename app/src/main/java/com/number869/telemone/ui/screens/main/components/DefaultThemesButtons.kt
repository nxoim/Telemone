package com.number869.telemone.ui.screens.main.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.number869.decomposite.core.common.viewModel.viewModel
import com.number869.telemone.MainViewModel
import com.number869.telemone.shared.ui.TextWithFixedSize
import com.number869.telemone.ui.theme.SolarSet

@Composable
fun DefaultThemesButtons() {
	val vm = viewModel<MainViewModel>()

	val buttons = remember {
		movableContentOf<Pair<Modifier, Modifier>> {
			LightThemeButton(it.first) { vm.exportTheme(light = true) }

			DarkThemeButton(it.second) { vm.exportTheme(light = false) }
		}
	}

	BoxWithConstraints {
		if (maxHeight < 420.dp) {
			Row(
				Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceAround
			) {
				buttons(
					Pair(
						Modifier,
						Modifier
					)
				)
			}
		} else {
			Column(Modifier.fillMaxWidth()) {
				buttons(
					Pair(
						Modifier,
						Modifier.padding(top = 16.dp).align(Alignment.End)
					)
				)
			}
		}
	}
}

@Composable
private fun LightThemeButton(modifier: Modifier = Modifier, save: () -> Unit) {
	RoundedOutlinedButtonBox(modifier, onClick = save) {
		Column(
			Modifier
				.padding(24.dp)
				.fillMaxSize(),
			verticalArrangement = Arrangement.SpaceEvenly
		) {
			Icon(
				SolarSet.Sun,
				contentDescription = "",
				Modifier
					.size(64.dp)
					.align(Alignment.End)
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
	RoundedOutlinedButtonBox(modifier, onClick = save) {
		Column(
			Modifier
				.padding(24.dp)
				.fillMaxSize(),
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
				modifier = Modifier
					.width(78.dp)
					.align(Alignment.End)
			)
			Icon(
				SolarSet.Moon,
				contentDescription = "",
				Modifier.size(64.dp)
			)
		}
	}
}

@Composable
private fun RoundedOutlinedButtonBox(
	modifier: Modifier = Modifier,
	onClick: () -> Unit,
	content: @Composable BoxScope.() -> Unit
) = Box(
	modifier
		.size(198.dp)
		.border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(32.dp))
		.clip(RoundedCornerShape(32.dp))
		.clickable { onClick() },
	content = content
)
