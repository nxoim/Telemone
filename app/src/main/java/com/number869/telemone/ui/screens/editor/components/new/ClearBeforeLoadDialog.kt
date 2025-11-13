package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.number869.telemone.R

@Composable
fun ClearBeforeLoadDialog(
	close: () -> Unit,
	clear: () -> Unit,
	leaveAsIs: () -> Unit
) {
	AlertDialog(
		onDismissRequest = { close() },
		title = { Text(stringResource(R.string.clear_current_theme_before_loading_an_external_theme)) },
		text = { Text(stringResource(R.string.loading_a_theme_will_save_the_current_theme_to_saved_themes)) },
		confirmButton = {
			TextButton(onClick = { clear() }) {
				Text(stringResource(R.string.clear))
			}

			TextButton(onClick = { leaveAsIs() }) {
				Text(stringResource(R.string.leave_as_is))
			}
		},
		dismissButton = {
			TextButton(onClick = { close() }) {
				Text(stringResource(R.string.cancel))
			}
		}
	)
}