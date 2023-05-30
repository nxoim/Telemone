package com.number869.telemone.ui.screens.about.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
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
		var number869Expanded by remember { mutableStateOf(false) }
		val number869Github = "Number869"
		val number869Telegram = "koorDesart"
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.clickable { number869Expanded = !number869Expanded }
				.animateContentSize(
					animationSpec = spring(
						dampingRatio = Spring.DampingRatioLowBouncy,
						stiffness = Spring.StiffnessLow
					)
				)
		) {
			Row {
				Text(
					text = "Number869",
					style = MaterialTheme.typography.bodyLarge
				)
				Spacer(modifier = Modifier.weight(1f))
				Icon(
					imageVector = if (number869Expanded) {
						Icons.Filled.ExpandLess
					} else {
						Icons.Filled.ExpandMore
					},
					contentDescription = "Expand",
				)
			}
			if (number869Expanded) {
				Spacer(modifier = Modifier.height(4.dp))
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.clickable(onClick = {
							val intent = Intent(
								Intent.ACTION_VIEW,
								Uri.parse("https://github.com/$number869Github")
							)
							context.startActivity(intent)
						})
						.padding(start = 16.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					Icon(
						painter = painterResource(R.drawable.github_mark_white),
						contentDescription = "GitHub",
						modifier = Modifier.size(32.dp)
					)
					Spacer(modifier = Modifier.width(8.dp))
					Text(
						text = number869Github,
						style = MaterialTheme.typography.bodyMedium
					)
				}
				Spacer(modifier = Modifier.height(4.dp))
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.clickable {
							val intent = Intent(
								Intent.ACTION_VIEW,
								Uri.parse("https://t.me/$number869Telegram")
							)
							context.startActivity(intent)
						}
						.padding(start = 16.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					Icon(
						painter = painterResource(R.drawable.telegram_app_white),
						contentDescription = "Telegram",
						modifier = Modifier.size(32.dp)
					)
					Spacer(modifier = Modifier.width(8.dp))
					Text(
						text = number869Telegram,
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
		}

		var Lambada10Expanded by remember { mutableStateOf(false) }
		val Lambada10Github = "Lambada10"
		val Lambada10Telegram = "Lambada10"
		Spacer(modifier = Modifier.height(8.dp))
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.clickable(onClick = { Lambada10Expanded = !Lambada10Expanded })
				.animateContentSize(
					animationSpec = spring(
						dampingRatio = Spring.DampingRatioLowBouncy,
						stiffness = Spring.StiffnessLow
					)
				)
		) {
			Row {
				Text(
					text = "Lambada10",
					style = MaterialTheme.typography.bodyLarge
				)
				Spacer(modifier = Modifier.weight(1f))
				Icon(
					imageVector = if (Lambada10Expanded) {
						Icons.Filled.ExpandLess
					} else {
						Icons.Filled.ExpandMore
					},
					contentDescription = "Expand",
				)
			}
			if (Lambada10Expanded) {
				Spacer(modifier = Modifier.height(4.dp))
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.clickable {
							val intent = Intent(
								Intent.ACTION_VIEW,
								Uri.parse("https://github.com/$Lambada10Github")
							)
							context.startActivity(intent)
						}
						.padding(start = 16.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					Icon(
						painter = painterResource(R.drawable.github_mark_white),
						contentDescription = "GitHub",
						modifier = Modifier.size(32.dp)
					)
					Spacer(modifier = Modifier.width(8.dp))
					Text(
						text = Lambada10Github,
						style = MaterialTheme.typography.bodyMedium
					)
				}
				Spacer(modifier = Modifier.height(4.dp))
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.clickable {
							val intent = Intent(
								Intent.ACTION_VIEW,
								Uri.parse("https://t.me/$Lambada10Telegram")
							)
							context.startActivity(intent)
						}
						.padding(start = 16.dp),
					verticalAlignment = Alignment.CenterVertically
				) {
					Icon(
						painter = painterResource(R.drawable.telegram_app_white),
						contentDescription = "Telegram",
						modifier = Modifier.size(32.dp)
					)
					Spacer(modifier = Modifier.width(8.dp))
					Text(
						text = Lambada10Telegram,
						style = MaterialTheme.typography.bodyMedium
					)
				}
			}
		}
	}
}