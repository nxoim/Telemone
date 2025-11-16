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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.number869.telemone.R
import com.number869.telemone.ui.screens.about.AboutDestinations
import com.nxoim.decomposite.core.common.navigation.NavController

@Composable
fun LegalItem(
	navController: NavController<AboutDestinations.Dialogs>
) {
	AboutCard(label = stringResource(R.string.legal_label)) {
		Column(
			Modifier
                .clickable {
                    navController.navigate(AboutDestinations.Dialogs.PrivacyPolicyDialog)
                }
                .fillMaxWidth()
		) {
			Text(
                stringResource(R.string.privacy_policy_label),
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.titleLarge),
			)
			Text(
                stringResource(R.string.view_privacy_policy_view_action),
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.bodyMedium),
			)
		}

		Spacer(modifier = Modifier.height(16.dp))

		Column(
			Modifier
                .clickable {
                    navController.navigate(AboutDestinations.Dialogs.TosDialog)
                }
                .fillMaxWidth()
		) {
			Text(
                stringResource(R.string.terms_of_service_label),
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.titleLarge),
			)
			Text(
                stringResource(R.string.view_terms_of_service_view_action),
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.bodyMedium),
			)
		}
	}
}