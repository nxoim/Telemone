package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOutExpo
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.number869.telemone.data.AppSettings
import com.number869.telemone.shared.ui.SelectionDialog
import com.number869.telemone.shared.ui.SelectionDialogItem
import com.number869.telemone.shared.utils.ThemeColorPreviewDisplayType
import com.number869.telemone.shared.utils.getColorDisplayType

@Composable
fun SavedThemeItemDisplayTypeChooserDialog(hideDialog: () -> Unit) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp +
            WindowInsets.statusBars.asPaddingValues().calculateTopPadding() +
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val savedThemeItemDisplayType = getColorDisplayType()

    var displayTheDialog by remember { mutableStateOf(false) }

    val animatedAlpha by animateFloatAsState(
        if (displayTheDialog) 1f else 0f,
        animationSpec = if (displayTheDialog)
            tween(400, easing = EaseOutQuart)
        else
            tween(150, easing = EaseOutQuart),
        label = ""
    )

    LaunchedEffect(Unit) { displayTheDialog = true }

    Popup(
        onDismissRequest = { displayTheDialog = false },
        properties = PopupProperties(
            focusable = displayTheDialog,
        )
    ) {
        Box(Modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        enabled = displayTheDialog,
                        onClick = { displayTheDialog = false }
                    )
            )

            Box(
                Modifier.graphicsLayer {
                    translationY =
                        ((screenHeight.toPx() / 2) - (154.dp.toPx() + ((1f - animatedAlpha) * 32.dp.toPx())))
                },
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedVisibility(
                    visible = displayTheDialog,
                    enter = fadeIn(tween(50))
                            + expandVertically(expandFrom = Alignment.Top),
                    exit = fadeOut(tween(70, 20, EaseInOutExpo))
                            + shrinkVertically(shrinkTowards = Alignment.Top),
                ) {
                    DisposableEffect(Unit) { onDispose { hideDialog() } }

                    SelectionDialog(
                        title = "Choose display type",
                        contentAlpha = { animatedAlpha }
                    ) {
                        SelectionDialogItem(
                            text = "Saved color values",
                            selectThisItem = {
                                AppSettings.savedThemeDisplayType.set(
                                    ThemeColorPreviewDisplayType.SavedColorValues.id
                                )
                                displayTheDialog = false
                            },
                            selected = savedThemeItemDisplayType == ThemeColorPreviewDisplayType.SavedColorValues
                        )

                        SelectionDialogItem(
                            text = "Current color scheme (fallback to saved colors)",
                            selectThisItem = {
                                AppSettings.savedThemeDisplayType.set(
                                    ThemeColorPreviewDisplayType.CurrentColorSchemeWithFallback.id
                                )
                                displayTheDialog = false
                            },
                            selected = savedThemeItemDisplayType
                                    == ThemeColorPreviewDisplayType.CurrentColorSchemeWithFallback
                        )

                        SelectionDialogItem(
                            text = "Current color scheme",
                            selectThisItem = {
                                AppSettings.savedThemeDisplayType.set(
                                    ThemeColorPreviewDisplayType.CurrentColorScheme.id
                                )
                                displayTheDialog = false
                            },
                            selected = savedThemeItemDisplayType == ThemeColorPreviewDisplayType.CurrentColorScheme
                        )
                    }

                }
            }

        }
    }
}