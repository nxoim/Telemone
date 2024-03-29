package com.number869.telemone.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.number869.decomposite.common.cleanFadeAndSlide
import com.number869.decomposite.common.scaleFadePredictiveBackAnimation
import com.number869.decomposite.core.common.navigation.NavHost
import com.number869.decomposite.core.common.navigation.navController
import com.number869.decomposite.core.common.ultils.ContentType
import com.number869.decomposite.core.common.ultils.animation.OverlayStackNavigationAnimation
import com.number869.decomposite.core.common.viewModel.viewModel
import com.number869.telemone.MainViewModel
import com.number869.telemone.data.AppSettings
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

@Composable
fun Navigator() {
	val vm = viewModel<MainViewModel>()
	val screenWidth = LocalConfiguration.current.screenWidthDp
	val easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs = CubicBezierEasing(0.48f,0.19f,0.05f,1.03f)
	val preferences = LocalContext.current.getSharedPreferences(
		"AppPreferences.Settings",
		Context.MODE_PRIVATE
	)
	val skipWelcomeScreen = preferences.getBoolean(AppSettings.AgreedToPpAndTos.id, false)
	val startDestination = if (skipWelcomeScreen) Destinations.MainScreen else Destinations.WelcomeScreen

	NavHost(
		startingDestination = startDestination,
		containedContentAnimation = {
			scaleFadePredictiveBackAnimation(
				fallbackAnimation = stackAnimation { _ ->
					cleanFadeAndSlide(
						tween(
							600,
							easing = easingMaybeLikeTheOneThatGoogleUsesInMockupsButDoesntGiveTheSpecs
						),
						targetOffsetDp = (screenWidth * 0.2).toInt()
					)
				}
			)
		},
		overlayingContentAnimation = {
			// no animations for overlays because that is handled inside
			// the overlays themselves
			OverlayStackNavigationAnimation { null }
		}
	) {
		when (it) {
			Destinations.EditorScreen.ThemeValuesScreen -> ThemeValuesScreen()
			Destinations.MainScreen -> MainScreen()
			Destinations.WelcomeScreen -> WelcomeScreen()
			Destinations.EditorScreen.Editor -> EditorScreen()
			Destinations.AboutScreen.About -> AboutScreen()

			Destinations.AboutScreen.Dialogs.PrivacyPolicyDialog -> PrivacyPolicyDialog()
			Destinations.AboutScreen.Dialogs.TosDialog -> TosDialog()
			Destinations.EditorScreen.Dialogs.ClearThemeBeforeLoadingFromFile -> {
				val navController = navController<Destinations>()
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
			is Destinations.EditorScreen.Dialogs.DeleteOneTheme -> {
				val navController = navController<Destinations>()

				DeleteThemeDialog(
					close = { navController.navigateBack() },
					uuid = it.uuid,
					deleteTheme = { vm.deleteTheme(it.uuid) }
				)
			}
			Destinations.EditorScreen.Dialogs.DeleteSelectedThemes -> {
				val navController = navController<Destinations>()

				DeleteSelectedThemesDialog(
					hideToolbar = { vm.hideThemeSelectionModeToolbar() },
					hideDialog = { navController.navigateBack() },
					deleteSelectedThemes = vm::deleteSelectedThemes,
					unselectAllThemes = vm::unselectAllThemes,
					selectedThemeCount = vm.selectedThemes.size,
					context = LocalContext.current
				)
			}
			is Destinations.EditorScreen.Dialogs.LoadThemeWithOptions -> {
				val navController = navController<Destinations>()

				LoadWithOptionsDialog(
					close = { navController.navigateBack() },
					loadSavedTheme = vm::loadSavedTheme,
					uuid = it.uuid
				)
			}
			is Destinations.EditorScreen.Dialogs.OverwriteDefaultThemeConfirmation -> {
				val navController = navController<Destinations>()

				OverwriteDefaultsDialog(
					close = { navController.navigateBack() },
					overwrite = {
						vm.overwriteTheme(it.withThemeUuid, isLightTheme = !it.overwriteDark)
						navController.navigateBack()
					},
					overwriteDark = it.overwriteDark,
					overwriteWith = it.withThemeUuid
				)
			}
			is Destinations.EditorScreen.Dialogs.OverwriteDefaultThemeChoice -> {
				val navController = navController<Destinations>()

				OverwriteChoiceDialog(
					close = { navController.navigateBack() },
					chooseLight = {
						navController.navigateBack() // close this
						navController.navigate(
							Destinations.EditorScreen.Dialogs.OverwriteDefaultThemeConfirmation(
								overwriteDark = false,
								withThemeUuid = it.withThemeUuid
							),
							ContentType.Overlay
						)
					},
					chooseDark = {
						navController.navigateBack()
						navController.navigate(
							Destinations.EditorScreen.Dialogs.OverwriteDefaultThemeConfirmation(
								overwriteDark = true,
								withThemeUuid = it.withThemeUuid
							),
							ContentType.Overlay
						)
					}
				)
			}
			Destinations.EditorScreen.Dialogs.SavedThemeTypeSelection -> {
				val navController = navController<Destinations>()

				SavedThemeItemDisplayTypeChooserDialog { navController.navigateBack() }
			}

			is Destinations.GlobalDialogs.ThemeUpdateAvailable -> {
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