package com.number869.telemone.ui.screens.about.components

import android.content.Intent
import android.net.Uri
import androidx.annotation.StringRes
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.number869.telemone.R

@Composable
fun SpecialMentionsItem() {
    val context = LocalContext.current

    AboutCard(stringResource(R.string.special_mentions_label)) {
        val tgmonetName = stringResource(R.string.telegram_monet)
        val designName = stringResource(R.string._480_design)
        val solarIconSetName = stringResource(R.string.solar_icon_set)

        val tgMonetMention = linkableTextResource(
            R.string.full_monet_mention,
            arrayOf(tgmonetName),
            listOf(tgmonetName to "https://github.com/c3r5b8/Telegram-Monet")
        )

        val fourHundredEightyMention = linkableTextResource(
            R.string.full_icon_mention,
            arrayOf(designName, solarIconSetName),
            listOf(
                designName to "https://t.me/Design480",
                solarIconSetName to "https://github.com/480-Design/Solar-Icon-Set"
            )
        )

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
                    contentDescription = null
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
                    contentDescription = null
                )
            }

            ClickableText(
                text = fourHundredEightyMention,
                style = MaterialTheme.typography.bodyLarge.plus(
                    TextStyle(MaterialTheme.colorScheme.onSurface)
                ),
                onClick = {
                    fourHundredEightyMention.getStringAnnotations("URL", it, it)
                        .forEach { annotation ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                            context.startActivity(intent)
                        }
                }
            )
        }

    }
}

@Composable
private inline fun linkableTextResource(
    @StringRes resId: Int,
    args: Array<Any>,
    links: List<Pair<String, String>>, // Pair(displayText, url),
    linkColor: Color = MaterialTheme.colorScheme.tertiary
): AnnotatedString {
    val string = stringResource(resId, *args)

    return remember(resId, args, links, linkColor) {
        linkableTextResource(string, links, linkColor)
    }
}

private fun linkableTextResource(
    full: String,
    links: List<Pair<String, String>>, // Pair(displayText, url),
    linkColor: Color
) = buildAnnotatedString {
    append(full)
    links.forEach { (text, url) ->
        val start = full.indexOf(text)
        if (start >= 0) {
            val end = start + text.length
            addStyle(
                style = SpanStyle(
                    color = linkColor,
                    textDecoration = TextDecoration.Underline
                ),
                start = start,
                end = end
            )
            addStringAnnotation(tag = "URL", annotation = url, start = start, end = end)
        }
    }
}
