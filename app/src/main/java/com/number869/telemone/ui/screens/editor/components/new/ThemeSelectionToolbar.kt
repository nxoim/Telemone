package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Deselect
import androidx.compose.material.icons.outlined.SelectAll
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.number869.telemone.R
import com.number869.telemone.ui.theme.blendWith

@Composable
fun ThemeSelectionToolbar(
	modifier: Modifier = Modifier,
	selectedThemeCount: Int,
	allThemesAreSelected: Boolean,
	unselectAllThemes: () -> Unit,
	selectAllThemes: () -> Unit,
	hideToolbarAction: () -> Unit,
	onRequestDeletion: () -> Unit
) {
	val noThemesAreSelected = selectedThemeCount == 0

	Box(modifier, contentAlignment = Alignment.Center) {
		Row(
			Modifier
				.clip(RoundedCornerShape(38.dp))
				.background(
					if (isSystemInDarkTheme())
						MaterialTheme.colorScheme.surfaceContainerHighest
					else
						MaterialTheme.colorScheme.surfaceContainer.blendWith(
							MaterialTheme.colorScheme.surfaceContainerLow,
							0.5f
						)
				)
				.padding(12.dp),
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically
		) {
			TextButton(
				onClick = {
					unselectAllThemes()
					hideToolbarAction()
				}
			) {
				Text(stringResource(R.string.cancel))
			}

			Spacer(modifier = Modifier.width(8.dp))

			FilledTonalIconButton(
				onClick = { unselectAllThemes() },
				enabled = !noThemesAreSelected
			) {
				Icon(Icons.Outlined.Deselect, contentDescription = "Deselect All Button")
			}

			Spacer(modifier = Modifier.height(8.dp))

			FilledTonalIconButton(
				onClick = { selectAllThemes() },
				enabled = !allThemesAreSelected
			) {
				Icon(Icons.Outlined.SelectAll, contentDescription = "Select All Button")
			}

			Spacer(modifier = Modifier.width(16.dp))

			FilledTonalIconButton(
				onClick = onRequestDeletion,
				enabled = !noThemesAreSelected,
				colors = IconButtonDefaults.filledTonalIconButtonColors(
					containerColor = MaterialTheme.colorScheme.errorContainer,
					contentColor = MaterialTheme.colorScheme.onErrorContainer
				)
			) {
				Icon(Icons.Outlined.DeleteForever, contentDescription = "Delete button")
			}
		}
	}
}