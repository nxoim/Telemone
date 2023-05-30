package com.number869.telemone.ui.screens.about.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.number869.telemone.R

@Composable
fun SourceAndLinksItem() {
	val context = LocalContext.current

	AboutCard(label = "Source and Links") {
		val sourceCodeLink = "https://github.com/Number869/TeleMone"
		val supportLink = "https://t.me/number869community"

		Spacer(modifier = Modifier.height(4.dp))

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.clickable {
					val intent =
						Intent(Intent.ACTION_VIEW, Uri.parse(sourceCodeLink))
					context.startActivity(intent)
				},
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				painter = painterResource(R.drawable.github_mark_white),
				contentDescription = "GitHub",
				modifier = Modifier.size(32.dp)
			)
			Spacer(modifier = Modifier.width(8.dp))
			Text(
				text = "View Source Code",
			)
		}

		Spacer(modifier = Modifier.height(8.dp))

		Row(
			modifier = Modifier
				.fillMaxWidth()
				.clickable {
					val intent = Intent(Intent.ACTION_VIEW, Uri.parse(supportLink))
					context.startActivity(intent)
				},
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				painter = painterResource(R.drawable.telegram_app_white),
				contentDescription = "Telegram",
				modifier = Modifier.size(32.dp)
			)
			Spacer(modifier = Modifier.width(8.dp))
			Text(
				text = "Telegram Channel",
			)
		}

		Spacer(modifier = Modifier.height(8.dp))

		Text(
			text = "License: GPLv3"
		)
	}

}