package com.number869.telemone.shared.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit

@Composable
fun TextWithFixedSize(
	text: String,
	fontSize: TextUnit,
	style: TextStyle,
	modifier: Modifier = Modifier,
) {
	CompositionLocalProvider(
		LocalDensity provides Density(LocalDensity.current.density)
	) {
		Text(
			text = text,
			fontSize = fontSize,
			style = style,
			modifier = modifier
		)
	}
}