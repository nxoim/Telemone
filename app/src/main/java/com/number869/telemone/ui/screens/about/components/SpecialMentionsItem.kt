package com.number869.telemone.ui.screens.about.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun SpecialMentionsItem() {
	val context = LocalContext.current

	AboutCard("Special Mentions") {
		val tgMonetMention = buildAnnotatedString {
			append("Everyone involved in the ")
			pushStringAnnotation(tag = "URL", annotation = "https://github.com/c3r5b8/Telegram-Monet")
			withStyle(
				style = SpanStyle(
					color = MaterialTheme.colorScheme.tertiary,
					textDecoration = TextDecoration.Underline
				)
			) {
				append("Telegram Monet project")
			}
			pop()
			append(" as it is what pushed me to start this project.")
		}

		val fourHundredEightyMention = buildAnnotatedString {
			pushStringAnnotation(tag = "URL", annotation = "https://t.me/Design480")
			withStyle(
				style = SpanStyle(
					color = MaterialTheme.colorScheme.tertiary,
					textDecoration = TextDecoration.Underline
				)
			) {
				append("480 Design")
			}
			pop()

			append(" for the ")
			pushStringAnnotation(tag = "URL", annotation = "https://github.com/480-Design/Solar-Icon-Set")
			withStyle(
				style = SpanStyle(
					color = MaterialTheme.colorScheme.tertiary,
					textDecoration = TextDecoration.Underline
				)
			) {
				append("Solar Icon Set")
			}
			pop()

			append(".")
		}

		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(8.dp)
		) {
			Box(
				Modifier
					.size(38.dp)
					.clip(CircleShape)
					.background(MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)),
				contentAlignment = Alignment.Center
			) {
				Icon(
					Icons.Default.StarBorder,
					contentDescription = "Star"
				)
			}

			ClickableText(
				text = tgMonetMention,
				style = MaterialTheme.typography.bodyLarge.plus(
					TextStyle(MaterialTheme.colorScheme.onSurface)
				),
				onClick = {
					tgMonetMention.getStringAnnotations("URL", it, it).firstOrNull()
						?.let { annotation ->
							val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
							context.startActivity(intent)
						}
				}
			)
		}

		Spacer(modifier = Modifier.height(8.dp))

		Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
			Box(
				Modifier
					.size(38.dp)
					.clip(CircleShape)
					.background(MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp)),
				contentAlignment = Alignment.Center
			) {
				Icon(
					Icons.Default.StarBorder,
					contentDescription = "Star"
				)
			}

			ClickableText(
				text = fourHundredEightyMention,
				style = MaterialTheme.typography.bodyLarge.plus(
					TextStyle(MaterialTheme.colorScheme.onSurface)
				),
				onClick = {
					fourHundredEightyMention.getStringAnnotations("URL", it, it).forEach { annotation ->
						val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
						context.startActivity(intent)
					}
				}
			)
		}

	}
}