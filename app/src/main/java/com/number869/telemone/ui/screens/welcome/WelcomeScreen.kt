package com.number869.telemone.ui.screens.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.number869.telemone.R
import com.number869.telemone.data.AppSettings
import com.number869.telemone.shared.ui.LargeTonalButton
import com.number869.telemone.ui.RootDestinations
import com.nxoim.decomposite.core.common.navigation.NavController
import com.nxoim.decomposite.core.common.navigation.getExistingNavController

@Composable
fun WelcomeScreen(
	navController: NavController<RootDestinations> = getExistingNavController()
) {
	val privacyPolicyText = stringResource(R.string.privacy_policy_text)
	val tosText = stringResource(R.string.tos_text)

	Column(
		Modifier
			.systemBarsPadding()
			.fillMaxSize()
			.padding(horizontal = 16.dp),
		verticalArrangement = Arrangement.SpaceBetween
	) {
		Column(
			Modifier
				.fillMaxWidth()
		) {
			Text(
				text = "Welcome to Telemone",
				textAlign = TextAlign.Center,
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.headlineLarge),
				modifier = Modifier
					.align(Alignment.CenterHorizontally)
					.padding(vertical = 8.dp)
			)

			Spacer(modifier = Modifier.height(16.dp))

			Text(
				text = "To use this app - please agree to the following Privacy Policy and Terms of Service.",
				textAlign = TextAlign.Center,
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(MaterialTheme.typography.titleMedium),
				modifier = Modifier.align(Alignment.CenterHorizontally))
		}

		Column(
			Modifier
				.fillMaxSize()
				.weight(4f)) {
			OutlinedCard(
				Modifier
					.padding(vertical = 16.dp)
					.weight(1f)
			) {
				Column(Modifier.verticalScroll(rememberScrollState())) {
					Text(privacyPolicyText, modifier = Modifier.padding(16.dp))
				}
			}

			OutlinedCard(
				Modifier
					.padding(vertical = 16.dp)
					.weight(1f)
			) {
				Column(Modifier.verticalScroll(rememberScrollState())) {
					Text(tosText, modifier = Modifier.padding(16.dp))
				}
			}
		}

		LargeTonalButton(
			onClick = {
				AppSettings.agreedToConditions.set(true)
				navController.replaceAll(RootDestinations.Main)
			},
			label = "I Agree",
			modifier = Modifier.weight(1f, false)
		)
	}
}