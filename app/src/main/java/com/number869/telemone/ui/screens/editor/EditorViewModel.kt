package com.number869.telemone.ui.screens.editor

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.number869.telemone.data.ThemeColorDataType
import com.number869.telemone.data.ThemeManager
import com.number869.telemone.data.ThemeStorageType
import com.number869.telemone.data.defaultDarkThemeUUID
import com.number869.telemone.data.defaultLightThemeUUID
import com.number869.telemone.inject
import com.nxoim.decomposite.core.common.viewModel.ViewModel

@Stable
// funny of you to actually expect some sort of documentation in the
// comments
class EditorViewModel(
	private val themeManager: ThemeManager = inject()
) : ViewModel() {
	val themeList get() = themeManager.themeList
		.filterNot { it.uuid == defaultLightThemeUUID || it.uuid == defaultDarkThemeUUID }
		.reversed()
	val mappedValues get() = themeManager.mappedValues.sortedBy { it.name }
	private val defaultCurrentTheme get() = themeManager.defaultCurrentTheme
	val newUiElements get() = mappedValues.filter {
		it.name !in defaultCurrentTheme.map { it.name }
	}
	val incompatibleValues get() = mappedValues.filter {
		it.colorToken == "INCOMPATIBLE VALUE"
	}

	val selectedThemes = mutableStateListOf<String>()
	var themeSelectionToolbarIsVisible by mutableStateOf(false)

	override fun onDestroy(removeFromViewModelStore: () -> Unit) {
		// do nothing
	}

	fun exportCustomTheme() = themeManager.exportCustomTheme()

	fun saveCurrentTheme() {
		themeManager.saveCurrentTheme()
		showToast("Theme has been saved successfully.",)
	}

	fun resetCurrentTheme() {
		themeManager.resetCurrentTheme()
		showToast("Reset completed.")
	}

	fun loadSavedTheme(themeStorageType: ThemeStorageType) =
		themeManager.loadSavedTheme(
			themeStorageType,
			onSuccess = { storageTypeText, appearanceTypeText ->
				showToast(
					"$storageTypeText ${appearanceTypeText}theme has been loaded successfully."
				)
			},
			onIncompatibleValuesFound = {
				showToast("Some colors are incompatible and were marked as such.")
			},
			onIncompatibleFileType = {
				showToast("Chosen file isn't a Telegram (not Telegram X) theme.")
			}
		)

	fun changeValue(uiElementName: String, colorToken: String, colorValue: Color) =
		themeManager.changeValue(uiElementName, colorToken, colorValue)

	fun colorFromCurrentTheme(uiElementName: String): Color = mappedValues
		.find { it.name == uiElementName }
		?.let { Color(it.colorValue) }
		?: Color.Red

	fun selectOrUnselectSavedTheme(uuid: String) = if (selectedThemes.contains(uuid))
		selectedThemes.remove(uuid)
	else
		selectedThemes.add(uuid)


	fun exportTheme(uuid: String, dataType: ThemeColorDataType) =
		themeManager.exportTheme(uuid, dataType)

	fun selectAllThemes() {
		selectedThemes.clear()
		themeList.forEach {
			if (
				!selectedThemes.contains(it.uuid)
				&&
				it.uuid != defaultLightThemeUUID
				&&
				it.uuid != defaultDarkThemeUUID
			) {
				selectedThemes.add(it.uuid)
			}
		}
	}

	fun unselectAllThemes() = selectedThemes.clear()

	fun deleteSelectedThemes(selectedThemeCount: Int) {
		selectedThemes.forEach { themeManager.deleteTheme(it) }

		showToast("Themes ($selectedThemeCount) have been successfully deleted.")
	}

	fun hideThemeSelectionModeToolbar() {
		themeSelectionToolbarIsVisible = false
	}

	fun toggleThemeSelectionModeToolbar() {
		themeSelectionToolbarIsVisible = !themeSelectionToolbarIsVisible
	}

	fun getThemeByUUID(uuid: String) = themeManager.getThemeByUUID(uuid)

	fun deleteTheme(uuid: String) {
		themeManager.deleteTheme(uuid)

		showToast("Theme has been deleted successfully.",)
	}

	fun overwriteTheme(uuid: String, isLightTheme: Boolean) {
		themeManager.overwriteTheme(uuid, isLightTheme)

		val themeType = if (isLightTheme) "light" else "dark"
		showToast("Default $themeType theme has been overwritten successfully.")
	}
}

enum class ThemeColorPreviewDisplayType(val id: String) {
	SavedColorValues("1"),
	CurrentColorSchemeWithFallback("2"),
	CurrentColorScheme("3")
}

fun showToast(text: String, context: Context = inject()) {
	Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}

