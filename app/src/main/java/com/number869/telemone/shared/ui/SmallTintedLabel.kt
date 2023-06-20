package com.number869.telemone.shared.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun SmallTintedLabel(modifier: Modifier = Modifier, labelText: String) {
	Text(
		text = labelText,
		style = TextStyle(
			platformStyle = PlatformTextStyle(
				includeFontPadding = false)
		).plus(MaterialTheme.typography.labelLarge),
		modifier = modifier,
		color = MaterialTheme.colorScheme.onPrimaryContainer
	)
}