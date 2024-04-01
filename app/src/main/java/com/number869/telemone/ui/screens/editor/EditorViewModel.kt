package com.number869.telemone.ui.screens.editor

import android.net.Uri
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.number869.telemone.data.ThemeColorDataType
import com.number869.telemone.data.ThemeManager
import com.number869.telemone.data.ThemeStorageType
import com.number869.telemone.data.UiElementColorData
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
	val mappedValues get() = themeManager.mappedValues
	val defaultCurrentTheme get() = themeManager.defaultCurrentTheme
	val selectedThemes = mutableStateListOf<String>()
	var themeSelectionToolbarIsVisible by mutableStateOf(false)

	override fun onDestroy(removeFromViewModelStore: () -> Unit) {
		// do nothing
	}

	fun exportCustomTheme() = themeManager.exportCustomTheme()

	fun saveCurrentTheme() = themeManager.saveCurrentTheme()

	fun resetCurrentTheme() = themeManager.resetCurrentTheme()

	fun loadSavedTheme(themeStorageType: ThemeStorageType) =
		themeManager.loadSavedTheme(themeStorageType)

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

	fun deleteSelectedThemes() = selectedThemes.forEach {
		themeManager.deleteTheme(it)
	}

	fun hideThemeSelectionModeToolbar() {
		themeSelectionToolbarIsVisible = false
	}

	fun toggleThemeSelectionModeToolbar() {
		themeSelectionToolbarIsVisible = !themeSelectionToolbarIsVisible
	}


	fun loadThemeFromFile(uri: Uri, clearCurrentTheme: Boolean) =
		themeManager.loadThemeFromFile(uri, clearCurrentTheme)

	fun deleteTheme(uuid: String) = themeManager.deleteTheme(uuid)

	fun overwriteTheme(uuid: String, isLightTheme: Boolean) =
		themeManager.overwriteTheme(uuid, isLightTheme)

	fun stringify(
		source: List<UiElementColorData>,
		using: ThemeColorDataType
	) = themeManager.stringify(source, using)
}

enum class ThemeColorPreviewDisplayType(val id: String) {
	SavedColorValues("1"),
	CurrentColorSchemeWithFallback("2"),
	CurrentColorScheme("3")
}

