package com.number869.telemone.ui.screens.editor.components.new

import android.content.Context
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.number869.telemone.data.AppSettings
import com.number869.telemone.shared.ui.SelectionDialog
import com.number869.telemone.shared.ui.SelectionDialogItem

@Composable
fun SavedThemeItemDisplayTypeChooserDialog(
	showSavedThemeTypeDialog: Boolean,
	hideDialog: () -> Unit
) {
	val screenHeight = LocalConfiguration.current.screenHeightDp.dp +
			WindowInsets.statusBars.asPaddingValues().calculateTopPadding() +
			WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

	val preferences = LocalContext.current.getSharedPreferences(
		"AppPreferences.Settings",
		Context.MODE_PRIVATE
	)

	// 1 - Saved color values
	// 2 - Current color scheme (fallback to saved colors)
	// 3 - Current color scheme
	val savedThemeItemDisplayType = preferences.getString(
		AppSettings.SavedThemeItemDisplayType.id,
		"1"
	)


	if (showSavedThemeTypeDialog) {
		var displayTheDialog by remember { mutableStateOf(false) }
		val animatedDialogHeight by animateDpAsState(
			if (displayTheDialog) 308.dp else 0.dp,
			animationSpec = if (displayTheDialog)
				tween(400, easing = EaseOutQuart)
			else
				tween(150, easing = EaseOutQuart),
			label = ""
		)

		val animatedAlpha by animateFloatAsState(
			if (displayTheDialog) 1f else 0f,
			animationSpec = if (displayTheDialog)
				tween(400, easing = EaseOutQuart)
			else
				tween(150, easing = EaseOutQuart),
			label = ""
		)

		LaunchedEffect(Unit) {
			displayTheDialog = true
		}

		Popup(
			onDismissRequest = { displayTheDialog = false },
			properties = PopupProperties(
				focusable = displayTheDialog,
				dismissOnBackPress = true
			)
		) {
			Box {
				Box(
					Modifier
						.fillMaxSize()
						.alpha(animatedAlpha)
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							enabled = displayTheDialog,
							onClick = { displayTheDialog = false }
						)
				)

				if (animatedDialogHeight != 0.dp) Box(
					Modifier
						.align(Alignment.TopCenter)
						.height(animatedDialogHeight)
						.offset(y = (screenHeight / 2) - (154.dp + ((1f - animatedAlpha) * 32).dp))
				) {
					DisposableEffect(Unit) {
						onDispose {
							hideDialog()
						}
					}

					SelectionDialog(
						title = "Choose display type",
						contentAlpha = { animatedAlpha }
					) {
						SelectionDialogItem(
							text = "Saved color values",
							selectThisItem = {
								preferences.edit().putString(
									AppSettings.SavedThemeItemDisplayType.id,
									"1"
								).apply()
								displayTheDialog = false
							},
							selected = savedThemeItemDisplayType == "1"
						)

						SelectionDialogItem(
							text = "Current color scheme (fallback to saved colors)",
							selectThisItem = {
								preferences.edit().putString(
									AppSettings.SavedThemeItemDisplayType.id,
									"2"
								).apply()
								displayTheDialog = false
							},
							selected = savedThemeItemDisplayType == "2"
						)

						SelectionDialogItem(
							text = "Current color scheme",
							selectThisItem = {
								preferences.edit().putString(
									AppSettings.SavedThemeItemDisplayType.id,
									"3"
								).apply()
								displayTheDialog = false
							},
							selected = savedThemeItemDisplayType == "3"
						)
					}
				}
			}
		}
	}
}