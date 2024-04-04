package com.number869.telemone.ui.screens.editor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.number869.telemone.data.ThemeData
import com.number869.telemone.data.defaultDarkThemeUUID
import com.number869.telemone.data.defaultLightThemeUUID
import com.number869.telemone.shared.utils.ThemeStorageType
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
import com.nxoim.decomposite.core.common.navigation.navController
import com.nxoim.decomposite.core.common.ultils.ContentType
import com.nxoim.decomposite.core.common.viewModel.viewModel
import kotlinx.serialization.Serializable

@Composable
fun EditorNavigator() {
    val vm = viewModel { EditorViewModel() }
    val editorNavController = navController<EditorDestinations>(EditorDestinations.Editor)

    NavHost(editorNavController) {
        when (it) {
            EditorDestinations.ThemeValues -> ThemeValuesScreen()

            EditorDestinations.Editor -> EditorScreen()

            else -> Dialogs(destination = it, vm, editorNavController)
        }
    }
}

@Composable
private fun Dialogs(
    destination: EditorDestinations,
    vm: EditorViewModel,
    editorNavController: NavController<EditorDestinations>
) = when(destination) {
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
                editorNavController.navigateBack()
            }
        }
        // this one is used when pressing the "leave as is" button
        val launcherThatDoesnt = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { result ->
            pickedFileUriState.value = result

            result?.let { uri ->
                vm.loadSavedTheme(ThemeStorageType.ExternalFile(uri, false))
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
            theme = destination.theme,
            deleteTheme = { vm.deleteTheme(destination.theme.uuid) },
        )
    }
    is EditorDestinations.Dialogs.DeleteSelectedThemes -> {
        DeleteSelectedThemesDialog(
            hideToolbar = { vm.hideThemeSelectionModeToolbar() },
            hideDialog = { editorNavController.navigateBack() },
            deleteSelectedThemes = vm::deleteSelectedThemes,
            unselectAllThemes = vm::unselectAllThemes,
            selectedThemeCount = destination.selectedThemeCount,
        )
    }
    is EditorDestinations.Dialogs.LoadThemeWithOptions -> {
        LoadWithOptionsDialog(
            close = { editorNavController.navigateBack() },
            loadSavedTheme = vm::loadSavedTheme,
            uuid = destination.uuid
        )
    }
    is EditorDestinations.Dialogs.OverwriteDefaultThemeConfirmation -> {
        OverwriteDefaultsDialog(
            close = { editorNavController.navigateBack() },
            overwrite = {
                vm.overwriteTheme(destination.withTheme.uuid, isLightTheme = !destination.overwriteDark)
                editorNavController.navigateBack()
            },
            overwriteDark = destination.overwriteDark,
            lightTheme = vm.getThemeByUUID(defaultLightThemeUUID)!!,
            darkTheme = vm.getThemeByUUID(defaultDarkThemeUUID)!!,
            overwriteWith = destination.withTheme,
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
                        withTheme = destination.withTheme
                    ),
                    ContentType.Overlay
                )
            },
            chooseDark = {
                editorNavController.navigateBack()
                editorNavController.navigate(
                    EditorDestinations.Dialogs.OverwriteDefaultThemeConfirmation(
                        overwriteDark = true,
                        withTheme = destination.withTheme
                    ),
                    ContentType.Overlay
                )
            },
            lightTheme = vm.getThemeByUUID(defaultLightThemeUUID)!!,
            darkTheme = vm.getThemeByUUID(defaultDarkThemeUUID)!!,
        )
    }
    EditorDestinations.Dialogs.SavedThemeTypeSelection -> {
        SavedThemeItemDisplayTypeChooserDialog { editorNavController.navigateBack() }
    }
    else -> { }
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
        data class DeleteSelectedThemes(val selectedThemeCount: Int) : Dialogs

        @Serializable
        data object ClearThemeBeforeLoadingFromFile : Dialogs
    }
}