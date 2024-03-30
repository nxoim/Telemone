package com.number869.telemone.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.number869.telemone.MainViewModel
import com.number869.telemone.data.AppSettings
import com.number869.telemone.defaultDarkThemeUUID
import com.number869.telemone.defaultLightThemeUUID
import com.number869.telemone.getColorValueFromColorToken
import com.number869.telemone.ui.screens.about.AboutScreen
import com.number869.telemone.ui.screens.about.components.PrivacyPolicyDialog
import com.number869.telemone.ui.screens.about.components.TosDialog
import com.number869.telemone.ui.screens.editor.EditorScreen
import com.number869.telemone.ui.screens.editor.components.new.ClearBeforeLoadDialog
import com.number869.telemone.ui.screens.editor.components.new.DeleteSelectedThemesDialog
import com.number869.telemone.ui.screens.editor.components.new.DeleteThemeDialog
import com.number869.telemone.ui.screens.editor.components.new.LoadWithOptionsDialog
import com.number869.telemone.ui.screens.editor.components.new.OverwriteChoiceDialog
import com.number869.telemone.ui.screens.editor.components.new.OverwriteDefaultsDialog
import com.number869.telemone.ui.screens.editor.components.new.SavedThemeItemDisplayTypeChooserDialog
import com.number869.telemone.ui.screens.main.MainScreen
import com.number869.telemone.ui.screens.main.components.ThemeUpdateAvailableDialog
import com.number869.telemone.ui.screens.themeValues.ThemeValuesScreen
import com.number869.telemone.ui.screens.welcome.WelcomeScreen
import com.nxoim.decomposite.core.common.navigation.NavHost
import com.nxoim.decomposite.core.common.navigation.getExistingNavController
import com.nxoim.decomposite.core.common.navigation.navController
import com.nxoim.decomposite.core.common.ultils.ContentType
import com.nxoim.decomposite.core.common.viewModel.getExistingViewModel

@Composable
fun Navigator() {
	val vm = getExistingViewModel<MainViewModel>()
	val preferences = LocalContext.current.getSharedPreferences(
		"AppPreferences.Settings",
		Context.MODE_PRIVATE
	)
	val skipWelcomeScreen = preferences.getBoolean(AppSettings.AgreedToPpAndTos.id, false)
	val startDestination = if (skipWelcomeScreen) MainDestinations.MainScreen else MainDestinations.WelcomeScreen

	val mainNavController = navController(startingDestination = startDestination)
	NavHost(mainNavController) {
		when (it) {
			MainDestinations.EditorScreen.ThemeValuesScreen -> ThemeValuesScreen()
			MainDestinations.MainScreen -> MainScreen()
			MainDestinations.WelcomeScreen -> WelcomeScreen()
			MainDestinations.EditorScreen.Editor -> EditorScreen()
			MainDestinations.AboutScreen.About -> AboutScreen()

			MainDestinations.AboutScreen.Dialogs.PrivacyPolicyDialog -> PrivacyPolicyDialog()
			MainDestinations.AboutScreen.Dialogs.TosDialog -> TosDialog()
			MainDestinations.EditorScreen.Dialogs.ClearThemeBeforeLoadingFromFile -> {
				val navController = getExistingNavController<MainDestinations>()
				val pickedFileUriState = remember { mutableStateOf<Uri?>(null) }

				// stuff for loading files.
				// this one is used when pressing the "clear" button
				val launcherThatClears = rememberLauncherForActivityResult(
					contract = ActivityResultContracts.OpenDocument()
				) { result ->
					pickedFileUriState.value = result

					result?.let { uri ->
						vm.loadThemeFromFile(uri,true)
						navController.navigateBack()
					}
				}
				// this one is used when pressing the "leave as is" button
				val launcherThatDoesnt = rememberLauncherForActivityResult(
					contract = ActivityResultContracts.OpenDocument()
				) { result ->
					pickedFileUriState.value = result

					result?.let { uri ->
						vm.loadThemeFromFile(uri, false)
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
			is MainDestinations.EditorScreen.Dialogs.DeleteOneTheme -> {
				val navController = getExistingNavController<MainDestinations>()

				DeleteThemeDialog(
					close = { navController.navigateBack() },
					theme = it.theme,
					deleteTheme = { vm.deleteTheme(it.theme.uuid) },
					getColorValueFromColorToken = { getColorValueFromColorToken(it, vm.palette) }
				)
			}
			MainDestinations.EditorScreen.Dialogs.DeleteSelectedThemes -> {
				val navController = getExistingNavController<MainDestinations>()

				DeleteSelectedThemesDialog(
					hideToolbar = { vm.hideThemeSelectionModeToolbar() },
					hideDialog = { navController.navigateBack() },
					deleteSelectedThemes = vm::deleteSelectedThemes,
					unselectAllThemes = vm::unselectAllThemes,
					selectedThemeCount = vm.selectedThemes.size,
					context = LocalContext.current
				)
			}
			is MainDestinations.EditorScreen.Dialogs.LoadThemeWithOptions -> {
				val navController = getExistingNavController<MainDestinations>()

				LoadWithOptionsDialog(
					close = { navController.navigateBack() },
					loadSavedTheme = vm::loadSavedTheme,
					uuid = it.uuid
				)
			}
			is MainDestinations.EditorScreen.Dialogs.OverwriteDefaultThemeConfirmation -> {
				val navController = getExistingNavController<MainDestinations>()

				OverwriteDefaultsDialog(
					close = { navController.navigateBack() },
					overwrite = {
						vm.overwriteTheme(it.withTheme.uuid, isLightTheme = !it.overwriteDark)
						navController.navigateBack()
					},
					overwriteDark = it.overwriteDark,
					lightTheme = vm.themeList.find { it.uuid == defaultLightThemeUUID }!!,
					darkTheme = vm.themeList.find { it.uuid == defaultDarkThemeUUID }!!,
					overwriteWith = it.withTheme,
					getColorValueFromColorToken = { getColorValueFromColorToken(it, vm.palette) }
				)
			}
			is MainDestinations.EditorScreen.Dialogs.OverwriteDefaultThemeChoice -> {
				val navController = getExistingNavController<MainDestinations>()

				OverwriteChoiceDialog(
					close = { navController.navigateBack() },
					chooseLight = {
						navController.navigateBack() // close this
						navController.navigate(
							MainDestinations.EditorScreen.Dialogs.OverwriteDefaultThemeConfirmation(
								overwriteDark = false,
								withTheme = it.withTheme
							),
							ContentType.Overlay
						)
					},
					chooseDark = {
						navController.navigateBack()
						navController.navigate(
							MainDestinations.EditorScreen.Dialogs.OverwriteDefaultThemeConfirmation(
								overwriteDark = true,
								withTheme = it.withTheme
							),
							ContentType.Overlay
						)
					},
					lightTheme = vm.themeList.find { it.uuid == defaultLightThemeUUID }!!,
					darkTheme = vm.themeList.find { it.uuid == defaultDarkThemeUUID }!!,
					getColorValueFromColorToken = { getColorValueFromColorToken(it, vm.palette) }
				)
			}
			MainDestinations.EditorScreen.Dialogs.SavedThemeTypeSelection -> {
				val navController = getExistingNavController<MainDestinations>()

				SavedThemeItemDisplayTypeChooserDialog { navController.navigateBack() }
			}

			is MainDestinations.GlobalDialogs.ThemeUpdateAvailable -> {
				ThemeUpdateAvailableDialog(
					ofLight = it.ofLight,
					decline = { vm.declineDefaultThemeUpdate(it.ofLight) }
				) {
					vm.acceptTheStockThemeUpdate(it.ofLight)
				}
			}
		}
	}
}