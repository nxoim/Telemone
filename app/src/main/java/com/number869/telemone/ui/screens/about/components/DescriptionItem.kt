package com.number869.telemone.ui.screens.about.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun DescriptionItem() {
	AboutCard(label = "About") {
		Text(
			text = "Telemone is an app that allows you to create custom Telegram Monet Themes.",
			style = MaterialTheme.typography.bodyLarge
		)
	}
}