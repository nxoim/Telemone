package com.number869.telemone.ui.screens.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.number869.telemone.data.PredefinedTheme
import com.number869.telemone.data.ThemeData
import com.number869.telemone.shared.utils.ThemeStorageType
import com.number869.telemone.ui.RootDestinations
import com.number869.telemone.ui.screens.editor.components.new.ClearBeforeLoadDialog
import com.number869.telemone.ui.screens.editor.components.new.DeleteSelectedThemesDialog
import com.number869.telemone.ui.screens.editor.components.new.DeleteThemeDialog
import com.number869.telemone.ui.screens.editor.components.new.LoadWithOptionsDialog
import com.number869.telemone.ui.screens.editor.components.new.OverwriteChoiceDialog
import com.number869.telemone.ui.screens.editor.components.new.OverwriteDefaultsDialog
import com.number869.telemone.ui.screens.editor.components.new.SavedThemeItemDisplayTypeChooserDialog
import com.number869.telemone.ui.screens.themeValues.ThemeValuesScreen
import com.nxoim.decomposite.core.common.navigation.NavController
import com.nxoim.decomposite.core.common.navigation.NavHost
import kotlinx.serialization.Serializable

@Composable
fun EditorNavigator(
    vm: EditorViewModel,
    rootNavController: NavController<RootDestinations>,
    editorNavController: NavController<EditorDestinations>,
    dialogsNavController: NavController<EditorDestinations.Dialogs>
) {
    NavHost(editorNavController) {
        when (it) {
            EditorDestinations.ThemeValues -> ThemeValuesScreen(
                vm.mappedValuesAsList,
                vm.paletteState
            )

            EditorDestinations.Editor -> EditorScreen(
                rootNavController,
                editorNavController,
                dialogsNavController,
                vm
            )
        }
    }

    DialogsHost(vm, dialogsNavController)
}

@Composable
private fun DialogsHost(
    vm: EditorViewModel,
    navController: NavController<EditorDestinations.Dialogs>
) {
    NavHost(navController) { destination ->
        when(destination) {
            EditorDestinations.Dialogs.ClearThemeBeforeLoadingFromFile -> {
                val pickedFileUriState = remember { mutableStateOf<Uri?>(null) }

                // stuff for loadingMappedValues files.
                // this one is used when pressing the "clear" button
                val launcherThatClears = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.OpenDocument()
                ) { result ->
                    pickedFileUriState.value = result

                    result?.let { uri ->
                        vm.loadSavedTheme(ThemeStorageType.ExternalFile(uri, true))
                        navController.navigateBack()
                    }
                }
                // this one is used when pressing the "leave as is" button
                val launcherThatDoesnt = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.OpenDocument()
                ) { result ->
                    pickedFileUriState.value = result

                    result?.let { uri ->
                        vm.loadSavedTheme(ThemeStorageType.ExternalFile(uri, false))
                        navController.navigateBack()
                    }
                }

                ClearBeforeLoadDialog(
                    close = { navController.navigateBack() },
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
                    close = { navController.navigateBack() },
                    theme = destination.theme,
                    deleteTheme = { vm.deleteTheme(destination.theme.uuid) },
                    entirePaletteAsMap = vm.paletteState.entirePaletteAsMap
                )
            }
            is EditorDestinations.Dialogs.DeleteSelectedThemes -> {
                DeleteSelectedThemesDialog(
                    hideToolbar = { vm.hideThemeSelectionModeToolbar() },
                    hideDialog = { navController.navigateBack() },
                    deleteSelectedThemes = vm::deleteSelectedThemes,
                    unselectAllThemes = vm::unselectAllThemes,
                    selectedThemeCount = destination.selectedThemeCount,
                )
            }
            is EditorDestinations.Dialogs.LoadThemeWithOptions -> {
                LoadWithOptionsDialog(
                    close = { navController.navigateBack() },
                    loadSavedTheme = vm::loadSavedTheme,
                    uuid = destination.uuid
                )
            }
            is EditorDestinations.Dialogs.OverwriteDefaultThemeConfirmation -> {
                OverwriteDefaultsDialog(
                    close = { navController.navigateBack() },
                    overwrite = {
                        vm.overwriteTheme(destination.withTheme.uuid, isLightTheme = !destination.overwriteDark)
                        navController.navigateBack()
                    },
                    overwriteDark = destination.overwriteDark,
                    lightTheme = vm.getThemeByUUID(PredefinedTheme.Default(true).uuid)!!,
                    darkTheme = vm.getThemeByUUID(PredefinedTheme.Default(false).uuid)!!,
                    overwriteWith = destination.withTheme,
                    entirePaletteAsMap = vm.paletteState.entirePaletteAsMap
                )
            }
            is EditorDestinations.Dialogs.OverwriteDefaultThemeChoice -> {
                OverwriteChoiceDialog(
                    close = { navController.navigateBack() },
                    chooseLight = {
                        navController.navigateBack() // close this
                        navController.navigate(
                            EditorDestinations.Dialogs.OverwriteDefaultThemeConfirmation(
                                overwriteDark = false,
                                withTheme = destination.withTheme
                            ),
                        )
                    },
                    chooseDark = {
                        navController.navigateBack()
                        navController.navigate(
                            EditorDestinations.Dialogs.OverwriteDefaultThemeConfirmation(
                                overwriteDark = true,
                                withTheme = destination.withTheme
                            ),
                        )
                    },
                    lightTheme = vm.getThemeByUUID(PredefinedTheme.Default(true).uuid)!!,
                    darkTheme = vm.getThemeByUUID(PredefinedTheme.Default(false).uuid)!!,
                    entirePaletteAsMap = vm.paletteState.entirePaletteAsMap
                )
            }
            EditorDestinations.Dialogs.SavedThemeTypeSelection -> {
                SavedThemeItemDisplayTypeChooserDialog { navController.navigateBack() }
            }
            else -> { }
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
    sealed interface Dialogs {
        @Serializable
        data object Empty : Dialogs

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
        data class DeleteSelectedThemes(val selectedThemeCount: Int) : Dialogs

        @Serializable
        data object ClearThemeBeforeLoadingFromFile : Dialogs
    }
}