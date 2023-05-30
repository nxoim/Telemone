package com.number869.telemone.ui.screens.about.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun VersionItem() {
	val context = LocalContext.current

	AboutCard(label = "Version") {
		val versionName = context.packageManager.getPackageInfo(
			context.packageName,
			0
		).versionName

		Text(
			text = versionName,
			style = MaterialTheme.typography.bodyLarge
		)
	}
}