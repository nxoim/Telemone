package com.number869.telemone.ui.screens.about.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.number869.telemone.R

@Composable
fun VersionItem(version: String) {
	AboutCard(label = stringResource(R.string.version_label)) {
		Text(
			text = version,
			style = MaterialTheme.typography.bodyLarge
		)
	}
}