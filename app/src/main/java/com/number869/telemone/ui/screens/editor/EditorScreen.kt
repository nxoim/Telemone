package com.number869.telemone.ui.screens.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.number869.seksinavigation.OverlayItemWrapper
import com.number869.seksinavigation.OverlayLayoutState
import com.number869.telemone.MainViewModel


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditorScreen(navController: NavHostController, vm: MainViewModel, state: OverlayLayoutState) {
	val topPaddingAsDp = WindowInsets.systemBars.getTop(LocalDensity.current).dp
	val bottomPaddingAsDp = WindowInsets.systemBars.getBottom(LocalDensity.current).dp

	Column(
		Modifier.fillMaxSize().systemBarsPadding(),
		verticalArrangement = Arrangement.Bottom,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		OverlayItemWrapper(
			isOriginalItemStatic = true,
			originalCornerRadius = 16.dp,
			key = "alternativeEditorScreen",
			state = state
		) {
			Column(Modifier.background(MaterialTheme.colorScheme.background)) {
				val itemState = state.itemsState["alternativeEditorScreen"] ?: state.emptyOverlayItemValues

				AnimatedVisibility(visible = !itemState.isExpanded) {
					OutlinedButton(onClick = { state.addToOverlayStack("alternativeEditorScreen") }) {
						Text(text = "Go to alternative editor")
					}
				}

				AnimatedVisibility(visible = itemState.isExpanded) {
					AlternativeEditorScreen(navController, vm)
				}
			}
		}
	}
}