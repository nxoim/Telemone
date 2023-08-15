package com.number869.telemone.ui.screens.editor.components.new

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.number869.telemone.MainViewModel
import com.number869.telemone.ui.theme.FullPaletteList

@Composable
fun DeleteThemeDialog(
	close: () -> Unit,
	isShowingDeleteDialog: Boolean,
	vm: MainViewModel,
	uuid: String,
	palette: FullPaletteList,
	context: Context
) {
	AnimatedVisibility(
		visible = isShowingDeleteDialog
	) {
		AlertDialog(
			onDismissRequest = { close() },
			title = { Text("Delete this theme?") },
			icon = {
				SavedThemeItem(
					Modifier
						.width(150.dp)
						.height(180.dp)
						.clip(RoundedCornerShape(16.dp)),
					vm,
					uuid,
					palette,
					context
				)
			},
			text = {
				Text(text = "Are you sure you want to delete this theme? You will not be able to recover this theme if you delete it.",)
			},
			confirmButton = {
				FilledTonalButton(
					onClick = { vm.deleteTheme(uuid, context) },
					colors = ButtonDefaults.filledTonalButtonColors(
						containerColor = MaterialTheme.colorScheme.errorContainer,
						contentColor = MaterialTheme.colorScheme.onErrorContainer
					)
				) {
					Text("Delete")
				}
			},
			dismissButton = {
				TextButton(onClick = { close() },) {
					Text("Cancel")
				}
			}
		)
	}
}