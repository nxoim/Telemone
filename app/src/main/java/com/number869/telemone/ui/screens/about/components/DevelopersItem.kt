package com.number869.telemone.ui.screens.about.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.number869.telemone.R

@Composable
fun DevelopersItem() {
	val context = LocalContext.current

	AboutCard(label = stringResource(R.string.developers_label)) {
		Column(verticalArrangement = spacedBy(16.dp)) {
			Row(
				modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/nxoim")
                        )
                        context.startActivity(intent)
                    },
				verticalAlignment = Alignment.CenterVertically
			) {
				Icon(
					painter = painterResource(R.drawable.github_mark_white),
					contentDescription = stringResource(R.string.github),
					modifier = Modifier.size(32.dp)
				)

				Spacer(modifier = Modifier.width(8.dp))

				Text(
					text = "nxoim",
					style = MaterialTheme.typography.bodyLarge
				)
			}

			Row(
				modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://github.com/Lambada10")
                        )
                        context.startActivity(intent)
                    },
				verticalAlignment = Alignment.CenterVertically
			) {
				Icon(
					painter = painterResource(R.drawable.github_mark_white),
					contentDescription = stringResource(R.string.github),
					modifier = Modifier.size(32.dp)
				)

				Spacer(modifier = Modifier.width(8.dp))

				Text(
					text = "Lambada10",
					style = MaterialTheme.typography.bodyLarge
				)
			}
		}
	}
}