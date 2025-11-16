package com.number869.telemone.ui.screens.about.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.number869.telemone.R

@Composable
fun VersionItem() {
	val context = LocalContext.current

	AboutCard(label = stringResource(R.string.version_label)) {
		val versionName = context.packageManager.getPackageInfo(
			context.packageName,
			0
		).versionName

		Text(
			text = versionName ?: stringResource(R.string.cant_get_version_name_for_some_reason),
			style = MaterialTheme.typography.bodyLarge
		)
	}
}