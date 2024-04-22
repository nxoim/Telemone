package com.number869.telemone.shared.ui

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.number869.telemone.ui.theme.TelemoneTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectionDialog(
	modifier: Modifier = Modifier,
	title: String,
	contentAlpha: () -> Float = { 1f },
	content: LazyListScope.() -> Unit
) {
	val backgroundColor = if (isSystemInDarkTheme())
		MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
	else
		MaterialTheme.colorScheme.surface

	Box(
		modifier
			.widthIn(min = 280.dp, max = 560.dp)
			.heightIn(max = 640.dp)
			.padding(horizontal = 48.dp)
			.shadow(
				8.dp,
				RoundedCornerShape(24.dp)
			)
			.clip(RoundedCornerShape(24.dp))
			.background(backgroundColor)
	) {
		Column(Modifier.padding(24.dp)) {
			Text(
				title,
				style = MaterialTheme.typography.headlineSmall,
				modifier = Modifier
					.align(Alignment.CenterHorizontally).basicMarquee()
					.graphicsLayer { alpha = contentAlpha() },
				color = MaterialTheme.colorScheme.onSurface,
				maxLines = 1
			)

			Spacer(modifier = Modifier.height(18.dp))

			LazyColumn(
				modifier = Modifier.graphicsLayer { alpha = contentAlpha() },
				content = content,
				verticalArrangement = spacedBy(4.dp)
			)
		}

	}
}

fun LazyListScope.SelectionDialogItem(
	modifier: Modifier = Modifier,
	text: String,
	selectThisItem: () -> Unit,
	selected: Boolean
) {
	item {
		val selectedContainerColor = if (isSystemInDarkTheme())
			MaterialTheme.colorScheme.onPrimaryContainer
		else
			MaterialTheme.colorScheme.primaryContainer

		val selectedContentColor = if (isSystemInDarkTheme())
			MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
		else
			MaterialTheme.colorScheme.onPrimaryContainer

		val animatedBackgroundColor by animateColorAsState(
			if (selected) selectedContainerColor else Color.Transparent,
			label = ""
		)

		val animatedTextColor by animateColorAsState(
			if (selected)
				selectedContentColor
			else
				MaterialTheme.colorScheme.onSurfaceVariant,
			label = ""
		)

		val animatedIconColor by animateColorAsState(
			if (selected) selectedContentColor else Color.Transparent,
			label = ""
		)

		Box(
			modifier
				.clip(RoundedCornerShape(28.dp))
				.background(animatedBackgroundColor)
				.clickable { selectThisItem() }
		) {
			Row(
				Modifier
					.fillMaxWidth()
					.heightIn(70.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				Spacer(modifier = Modifier.width(16.dp))

				Box(Modifier.size(26.dp)) {
					Icon(
						Icons.Default.Check,
						"Selected",
						modifier = Modifier.size(26.dp),
						tint = animatedIconColor
					)
				}

				Spacer(modifier = Modifier.width(12.dp))

				Text(
					text = text,
					fontSize = 16.sp,
					color = animatedTextColor
				)
			}
		}
	}
}

@Preview(
	device = "spec:width=411dp,height=891dp", showBackground = true,
	backgroundColor = 0xFFFFFFFF,
	uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
	wallpaper = Wallpapers.YELLOW_DOMINATED_EXAMPLE, showSystemUi = false
)
@Composable
fun SelectionDialoguePreview() {
	TelemoneTheme {
		SelectionDialog(title = "Choose display type",) {
			SelectionDialogItem(
				text = "Saved color values",
				selectThisItem = {},
				selected = true
			)

			SelectionDialogItem(
				text = "From current color scheme (fallback to saved colors)",
				selectThisItem = {},
				selected = false
			)


			SelectionDialogItem(
				text = "From current color scheme",
				selectThisItem = {},
				selected = false
			)
		}
	}
}