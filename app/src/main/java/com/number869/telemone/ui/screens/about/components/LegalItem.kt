package com.number869.telemone.ui.screens.about.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.number869.decomposite.core.common.navigation.navController
import com.number869.decomposite.core.common.ultils.ContentType
import com.number869.telemone.ui.Destinations

@Composable
fun LegalItem() {
	val navController = navController<Destinations>()

	AboutCard(label = "Legal") {
		Column(
			Modifier
				.clickable {
					navController.navigate(
						Destinations.AboutScreen.Dialogs.PrivacyPolicyDialog,
						ContentType.Overlay
					)
				}
				.fillMaxWidth()
		) {
			Text(
				"Privacy Policy",
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.titleLarge),
			)
			Text(
				"View Privacy Policy",
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.bodyMedium),
			)
		}

		Spacer(modifier = Modifier.height(16.dp))

		Column(
			Modifier
				.clickable {
					navController.navigate(
						Destinations.AboutScreen.Dialogs.TosDialog,
						ContentType.Overlay
					)
				}
				.fillMaxWidth()
		) {
			Text(
				"Terms of Service",
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.titleLarge),
			)
			Text(
				"View Terms of Service",
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.bodyMedium),
			)
		}
	}
}