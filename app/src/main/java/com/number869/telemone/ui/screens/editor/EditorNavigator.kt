package com.number869.telemone.ui.screens.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.number869.telemone.data.ThemeData
import com.number869.telemone.data.defaultDarkThemeUUID
import com.number869.telemone.data.defaultLightThemeUUID
import com.number869.telemone.data.getColorValueFromColorToken
import com.number869.telemone.inject
import com.number869.telemone.ui.screens.editor.components.new.ClearBeforeLoadDialog
import com.number869.telemone.ui.screens.editor.components.new.DeleteSelectedThemesDialog
import com.number869.telemone.ui.screens.editor.components.new.DeleteThemeDialog
import com.number869.telemone.ui.screens.editor.components.new.LoadWithOptionsDialog
import com.number869.telemone.ui.screens.editor.components.new.OverwriteChoiceDialog
import com.number869.telemone.ui.screens.editor.components.new.OverwriteDefaultsDialog
import com.number869.telemone.ui.screens.editor.components.new.SavedThemeItemDisplayTypeChooserDialog
import com.number869.telemone.ui.screens.themeValues.ThemeValuesScreen
import com.number869.telemone.ui.theme.PaletteState
import com.nxoim.decomposite.core.common.navigation.NavHost
import com.nxoim.decomposite.core.common.navigation.navController
import com.nxoim.decomposite.core.common.ultils.ContentType
import com.nxoim.decomposite.core.common.viewModel.viewModel
import kotlinx.serialization.Serializable

@Composable
fun EditorNavigator() {
    val vm = viewModel { EditorViewModel() }
    val palette = remember {
        inject<PaletteState>().entirePaletteAsMap.value
    }
    val editorNavController = navController<EditorDestinations>(EditorDestinations.Editor)

    NavHost(editorNavController) {
        when (it) {
            EditorDestinations.ThemeValues -> ThemeValuesScreen()

            EditorDestinations.Editor -> EditorScreen()

            EditorDestinations.Dialogs.ClearThemeBeforeLoadingFromFile -> {
                val pickedFileUriState = remember { mutableStateOf<Uri?>(null) }

                // stuff for loading files.
                // this one is used when pressing the "clear" button
                val launcherThatClears = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.OpenDocument()
                ) { result ->
                    pickedFileUriState.value = result

                    result?.let { uri ->
                        vm.loadThemeFromFile(uri,true)
                        editorNavController.navigateBack()
                    }
                }
                // this one is used when pressing the "leave as is" button
                val launcherThatDoesnt = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.OpenDocument()
                ) { result ->
                    pickedFileUriState.value = result

                    result?.let { uri ->
                        vm.loadThemeFromFile(uri, false)
                        editorNavController.navigateBack()
                    }
                }

                ClearBeforeLoadDialog(
                    close = { editorNavController.navigateBack() },
                    clear = {
                        vm.saveCurrentTheme()
                        launcherThatClears.launch(arrayOf("*/*"))
                    },
                    leaveAsIs = {
                        vm.saveCurrentTheme()
                        launcherThatDoesnt.launch(arrayOf("*/*"))
                    }
                )
            }
            is EditorDestinations.Dialogs.DeleteOneTheme -> {
                DeleteThemeDialog(
                    close = { editorNavController.navigateBack() },
                    theme = it.theme,
                    deleteTheme = { vm.deleteTheme(it.theme.uuid) },
                    getColorValueFromColorToken = {
                        getColorValueFromColorToken(it, palette)
                    }
                )
            }
            EditorDestinations.Dialogs.DeleteSelectedThemes -> {
                DeleteSelectedThemesDialog(
                    hideToolbar = { vm.hideThemeSelectionModeToolbar() },
                    hideDialog = { editorNavController.navigateBack() },
                    deleteSelectedThemes = vm::deleteSelectedThemes,
                    unselectAllThemes = vm::unselectAllThemes,
                    selectedThemeCount = vm.selectedThemes.size,
                    context = LocalContext.current
                )
            }
            is EditorDestinations.Dialogs.LoadThemeWithOptions -> {
                LoadWithOptionsDialog(
                    close = { editorNavController.navigateBack() },
                    loadSavedTheme = vm::loadSavedTheme,
                    uuid = it.uuid
                )
            }
            is EditorDestinations.Dialogs.OverwriteDefaultThemeConfirmation -> {
                OverwriteDefaultsDialog(
                    close = { editorNavController.navigateBack() },
                    overwrite = {
                        vm.overwriteTheme(it.withTheme.uuid, isLightTheme = !it.overwriteDark)
                        editorNavController.navigateBack()
                    },
                    overwriteDark = it.overwriteDark,
                    lightTheme = vm.themeList.find { it.uuid == defaultLightThemeUUID }!!,
                    darkTheme = vm.themeList.find { it.uuid == defaultDarkThemeUUID }!!,
                    overwriteWith = it.withTheme,
                    getColorValueFromColorToken = {
                        getColorValueFromColorToken(it, palette)
                    }
                )
            }
            is EditorDestinations.Dialogs.OverwriteDefaultThemeChoice -> {
                OverwriteChoiceDialog(
                    close = { editorNavController.navigateBack() },
                    chooseLight = {
                        editorNavController.navigateBack() // close this
                        editorNavController.navigate(
                            EditorDestinations.Dialogs.OverwriteDefaultThemeConfirmation(
                                overwriteDark = false,
                                withTheme = it.withTheme
                            ),
                            ContentType.Overlay
                        )
                    },
                    chooseDark = {
                        editorNavController.navigateBack()
                        editorNavController.navigate(
                            EditorDestinations.Dialogs.OverwriteDefaultThemeConfirmation(
                                overwriteDark = true,
                                withTheme = it.withTheme
                            ),
                            ContentType.Overlay
                        )
                    },
                    lightTheme = vm.themeList.find { it.uuid == defaultLightThemeUUID }!!,
                    darkTheme = vm.themeList.find { it.uuid == defaultDarkThemeUUID }!!,
                    getColorValueFromColorToken = {
                        getColorValueFromColorToken(it, palette)
                    }
                )
            }
            EditorDestinations.Dialogs.SavedThemeTypeSelection -> {
                SavedThemeItemDisplayTypeChooserDialog { editorNavController.navigateBack() }
            }
        }
    }
}

@Serializable
sealed interface EditorDestinations {
    @Serializable
    data object Editor : EditorDestinations
    @Serializable
    data object ThemeValues : EditorDestinations

    @Serializable
    sealed interface Dialogs : EditorDestinations {
        @Serializable
        data object SavedThemeTypeSelection : Dialogs

        @Serializable
        data class LoadThemeWithOptions(val uuid: String) : Dialogs

        @Serializable
        data class OverwriteDefaultThemeChoice(val withTheme: ThemeData) : Dialogs

        @Serializable
        data class OverwriteDefaultThemeConfirmation(
            val overwriteDark: Boolean,
            val withTheme: ThemeData
        ) : Dialogs

        @Serializable
        data class DeleteOneTheme(val theme: ThemeData) : Dialogs

        @Serializable
        data object DeleteSelectedThemes : Dialogs

        @Serializable
        data object ClearThemeBeforeLoadingFromFile : Dialogs
    }
}