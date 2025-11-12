package com.number869.telemone.ui.screens.editor

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.number869.telemone.data.ThemeManager
import com.number869.telemone.shared.utils.ThemeColorDataType
import com.number869.telemone.shared.utils.ThemeStorageType
import com.number869.telemone.shared.utils.color
import com.number869.telemone.shared.utils.showToast
import com.nxoim.decomposite.core.common.viewModel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@Stable
// funny of you to actually expect some sort of documentation in the
// comments
class EditorViewModel(
	private val themeManager: ThemeManager
) : ViewModel() {
    val paletteState get() = themeManager.paletteState
	var loadingMappedValues by mutableStateOf(true)
	var loadingThemes by mutableStateOf(true)

	val themeList = themeManager.themeList.onEach { loadingThemes = false }
	private val mappedValues = themeManager.mappedValues
	var mappedValuesAsList = mappedValues
		.map {
			it.values
				.sortedBy { it.name }
				.also { loadingMappedValues = false }
		}
		.stateIn(
			scope = CoroutineScope(Dispatchers.Default),
			started = SharingStarted.WhileSubscribed(),
			initialValue = mappedValues.value.values.toList()
		)
	private val defaultCurrentTheme = themeManager.defaultCurrentTheme
	var newUiElements = mappedValues
		.map {
			it.values
				.asSequence()
				.filter { it.name !in defaultCurrentTheme.map { it.name } }
				.sortedBy { it.name }
				.toList()
	}

	var incompatibleValues = mappedValues
		.map {
			it.values
				.asSequence()
				.filter { it.colorToken == "INCOMPATIBLE VALUE" }
				.sortedByDescending { it.name }
				.toList()
	}

	val selectedThemes = mutableStateListOf<String>()
	var themeSelectionToolbarIsVisible by mutableStateOf(false)

	override fun onDestroy(removeFromViewModelStore: () -> Unit) {
		loadingMappedValues = true
		loadingThemes = true
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

	fun loadSavedTheme(themeStorageType: ThemeStorageType) = themeManager.loadSavedTheme(
		storedTheme = themeStorageType,
		onSuccess = { storageTypeText, appearanceTypeText ->
			viewModelScope.launch {
				showToast(
					"$storageTypeText ${appearanceTypeText}theme has been loaded successfully."
				)
			}
		},
		onIncompatibleValuesFound = {
			viewModelScope.launch {
				showToast("Some colors are incompatible and were marked as such.")
			}
		},
		onIncompatibleFileType = {
			viewModelScope.launch {
				showToast("Chosen file isn't a Telegram (not Telegram X) theme.")
			}
		}
	)

	fun changeValue(uiElementName: String, colorToken: String, colorValue: Color) =
		themeManager.changeValue(uiElementName, colorToken, colorValue)

	@OptIn(ExperimentalCoroutinesApi::class)
	fun colorFromCurrentTheme(uiElementName: String) = mappedValues
		.mapLatest { it[uiElementName]?.color ?: Color.Red }
		.stateIn(
			scope = CoroutineScope(Dispatchers.Default),
			started = SharingStarted.WhileSubscribed(),
			initialValue = mappedValues.value[uiElementName]?.color ?: Color.Red
		)

	fun selectOrUnselectSavedTheme(uuid: String) = if (selectedThemes.contains(uuid))
		selectedThemes.remove(uuid)
	else
		selectedThemes.add(uuid)


	fun exportTheme(uuid: String, dataType: ThemeColorDataType) =
		themeManager.exportTheme(uuid, dataType)

	fun selectAllThemes() {
		selectedThemes.clear()
		viewModelScope.launch {
			themeList.firstOrNull()?.forEach {
				if (!selectedThemes.contains(it.uuid)) {
					selectedThemes.add(it.uuid)
				}
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
