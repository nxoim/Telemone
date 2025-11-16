package com.number869.telemone.ui.screens.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.number869.telemone.R

@Composable
fun ThemeUpdateAvailableDialog(
	ofLight: Boolean,
	decline: () -> Unit,
	acceptStockThemeUpdate: () -> Unit,
) {
	val typeText = if (ofLight) "light" else "dark"

	AlertDialog(
		onDismissRequest = {  },
		title = {
			Text(stringResource(R.string.update_default_theme, typeText))
		},
		text = {
			Text(text = stringResource(
                R.string.stock_theme_update_notice_content,
                typeText,
                typeText,
                typeText,
                typeText
            ).trimMargin())
		},
		icon = {
			Icon(Icons.Default.TipsAndUpdates, contentDescription = null)
		},
		confirmButton = {
			Button(onClick = acceptStockThemeUpdate) { Text(text = stringResource(R.string.apply)) }
		},
		dismissButton = {
			OutlinedButton(onClick = decline) {
				Text(text = stringResource(R.string.decline))
			}
		}
	)
}