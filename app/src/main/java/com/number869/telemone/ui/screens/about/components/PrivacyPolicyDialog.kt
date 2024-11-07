package com.number869.telemone.ui.screens.about.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.number869.telemone.R
import com.number869.telemone.ui.screens.about.AboutDestinations
import com.nxoim.decomposite.core.common.navigation.NavController

@Composable
fun PrivacyPolicyDialog(
	onNavigateBackRequest: () -> Unit,
) {
	Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
		Card(
			colors = CardDefaults.elevatedCardColors(MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)),
			shape = RoundedCornerShape(32.dp)
		) {
			Column {
				Column(
					Modifier
						.verticalScroll(rememberScrollState())
						.weight(1f, false)) {
					Text(
						stringResource(id = R.string.privacy_policy_text),
						modifier = Modifier.padding(24.dp)
					)
				}

				TextButton(
					onClick = onNavigateBackRequest,
					modifier = Modifier
						.align(Alignment.End)
						.padding(top = 20.dp, bottom = 20.dp, end = 24.dp)
						.weight(1f, false)
				) {
					Text(text = "Close")
				}
			}
		}
	}
}