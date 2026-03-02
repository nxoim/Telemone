package com.number869.telemone.ui.screens.about.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.number869.telemone.R

@Composable
fun DescriptionItem() {
	AboutCard(label = stringResource(R.string.about_title)) {
		Text(
			text = stringResource(R.string.about_app),
			style = MaterialTheme.typography.bodyLarge
		)
	}
}