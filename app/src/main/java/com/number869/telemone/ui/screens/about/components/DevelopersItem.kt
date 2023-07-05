package com.number869.telemone.ui.screens.about.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.number869.telemone.R

@Composable
fun DevelopersItem() {
	val context = LocalContext.current

	AboutCard(label = "Developers") {
		Column(verticalArrangement = spacedBy(16.dp)) {
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.clickable {
						val intent = Intent(
							Intent.ACTION_VIEW,
							Uri.parse("https://github.com/Number869")
						)
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
					text = "Number869",
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
					contentDescription = "GitHub",
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