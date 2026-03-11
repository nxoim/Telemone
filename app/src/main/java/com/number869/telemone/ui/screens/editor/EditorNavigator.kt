package com.number869.telemone.ui.screens.editor

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatableV2
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.arkivanov.decompose.router.stack.ChildStack
import com.number869.telemone.ui.screens.editor.components.new.ClearBeforeLoadDialog
import com.number869.telemone.ui.screens.editor.components.new.DeleteSelectedThemesDialog
import com.number869.telemone.ui.screens.editor.components.new.DeleteThemeDialog
import com.number869.telemone.ui.screens.editor.components.new.LoadWithOptionsDialog
import com.number869.telemone.ui.screens.editor.components.new.OverwriteChoiceDialog
import com.number869.telemone.ui.screens.editor.components.new.OverwriteDefaultsDialog
import com.number869.telemone.ui.screens.editor.components.new.SavedThemeItemDisplayTypeChooserDialog
import com.number869.telemone.ui.screens.themeValues.ThemeValuesScreen
import com.number869.telemone.ui.shared.theme.cleanSlideAndFadeAnimator
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun EditorNavigator(component: EditorComponent) {
    val stack by component.stack.subscribeAsState()
    val dialogsStack by component.dialogsStack.subscribeAsState()

    Children(
        stack,
        animation = predictiveBackAnimation(
            backHandler = component.backHandler,
            fallbackAnimation = stackAnimation(cleanSlideAndFadeAnimator()),
            selector = { initialEvent, _, _ ->
                androidPredictiveBackAnimatableV2(initialEvent)
            },
            onBack = { component.model.navigation.navigateBack() }
        )
    ) {
        Box {
            when (it.instance) {
                EditorDestinationsInstance.Editor -> EditorScreen(component.model)
                EditorDestinationsInstance.ThemeValues -> ThemeValuesScreen(
                    component.model.mappedValuesAsList,
                    component.model.paletteState.collectAsState().value
                )
            }
        }
    }

    DialogsHost(
        stack = dialogsStack,
        model = component.model
    )
}

@OptIn(ExperimentalCoroutinesApi::class)
@Composable
private fun DialogsHost(
    model: EditorModel,
    stack: ChildStack<*, EditorDestinations.Dialogs>
) {
    val paletteState by model.paletteState.collectAsState()
    when (val destination = stack.active.instance) {
        EditorDestinations.Dialogs.ClearThemeBeforeLoadingFromFile -> {
            ClearBeforeLoadDialog(
                close = { model.navigation.navigateBackDialog() },
                clear = {
                    model.saveCurrentTheme()
                    model.pickAndLoadTheme(clearCurrentTheme = true)
                },
                leaveAsIs = {
                    model.saveCurrentTheme()
                    model.pickAndLoadTheme(clearCurrentTheme = false)
                }
            )
        }

        is EditorDestinations.Dialogs.DeleteOneTheme -> {
            DeleteThemeDialog(
                close = { model.navigation.navigateBackDialog() },
                theme = destination.theme,
                deleteTheme = { model.deleteTheme(destination.theme.uuid) },
                entirePaletteAsMap = paletteState.entirePaletteAsMap
            )
        }

        is EditorDestinations.Dialogs.DeleteSelectedThemes -> {
            DeleteSelectedThemesDialog(
                hideToolbar = { model.hideThemeSelectionModeToolbar() },
                hideDialog = { model.navigation.navigateBackDialog() },
                deleteSelectedThemes = model::deleteSelectedThemes,
                unselectAllThemes = model::unselectAllThemes,
                selectedThemeCount = destination.selectedThemeCount,
            )
        }

        is EditorDestinations.Dialogs.LoadThemeWithOptions -> {
            LoadWithOptionsDialog(
                close = { model.navigation.navigateBackDialog() },
                loadSavedTheme = model::loadSavedTheme,
                uuid = destination.uuid
            )
        }

        is EditorDestinations.Dialogs.OverwriteDefaultThemeConfirmation -> {
            val themes by model.defaultThemes.collectAsState()

            LaunchedEffect(themes) {
                if (themes == null) model.navigation.navigateBackDialog()
            }

            themes?.let { (lightTheme, darkTheme) ->
                OverwriteDefaultsDialog(
                    close = { model.navigation.navigateBackDialog() },
                    overwrite = {
                        model.overwriteTheme(
                            destination.withTheme.uuid,
                            isLightTheme = !destination.overwriteDark
                        )
                        model.navigation.navigateBackDialog()
                    },
                    overwriteDark = destination.overwriteDark,
                    lightTheme = lightTheme,
                    darkTheme = darkTheme,
                    overwriteWith = destination.withTheme,
                    entirePaletteAsMap = paletteState.entirePaletteAsMap
                )
            }
        }

        is EditorDestinations.Dialogs.OverwriteDefaultThemeChoice -> {
            val themes by model.defaultThemes.collectAsState()

            LaunchedEffect(themes) {
                if (themes == null) model.navigation.navigateBackDialog()
            }

            themes?.let { (lightTheme, darkTheme) ->
                OverwriteChoiceDialog(
                    close = { model.navigation.navigateBackDialog() },
                    chooseLight = {
                        model.navigation.navigateBackDialog()
                        model.navigation.navigateToDialog(
                            EditorDestinations.Dialogs.OverwriteDefaultThemeConfirmation(
                                overwriteDark = false,
                                withTheme = destination.withTheme
                            )
                        )
                    },
                    chooseDark = {
                        model.navigation.navigateBackDialog()
                        model.navigation.navigateToDialog(
                            EditorDestinations.Dialogs.OverwriteDefaultThemeConfirmation(
                                overwriteDark = true,
                                withTheme = destination.withTheme
                            )
                        )
                    },
                    lightTheme = lightTheme,
                    darkTheme = darkTheme,
                    entirePaletteAsMap = paletteState.entirePaletteAsMap
                )
            }
        }

        EditorDestinations.Dialogs.SavedThemeTypeSelection -> {
            val displayType by model.displayType.collectAsState()
            SavedThemeItemDisplayTypeChooserDialog(
                savedThemeItemDisplayType = displayType,
                displayTypeSettings = model.displayTypeSettings,
                hideDialog = { model.navigation.navigateBackDialog() }
            )
        }

        else -> {}
    }
}