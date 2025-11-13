package com.number869.telemone.ui.screens.editor

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.number869.telemone.R
import com.number869.telemone.data.ThemeManager
import com.number869.telemone.shared.utils.ThemeColorDataType
import com.number869.telemone.shared.utils.ThemeStorageType
import com.number869.telemone.shared.utils.color
import com.number869.telemone.shared.utils.showToast
import com.nxoim.decomposite.core.common.viewModel.ViewModel
import com.nxoim.evolpagink.core.pageable
import com.nxoim.evolpagink.core.prefetchMinimumItemAmount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val pageSize = 10

// funny of you to actually expect some sort of documentation in the
// comments
class EditorViewModel(
    private val themeManager: ThemeManager
) : ViewModel() {
    val paletteState get() = themeManager.paletteState
    var loadingMappedValues by mutableStateOf(true)
    var loadingThemes by mutableStateOf(true)

    val themesPageable = pageable(
        viewModelScope,
        onPage = {
            themeManager
                .getThemes((pageSize * it) until (pageSize * (it + 1)))
                .onEach { loadingThemes = false }
        },
        strategy = prefetchMinimumItemAmount()
    )
    val themeCount = themeManager.countSavedThemes()
        .stateIn(viewModelScope, WhileSubscribed(), 0)

    private val mappedValues = themeManager.mappedValues
    var mappedValuesAsList = mappedValues
        .map {
            it.values
                .sortedBy { it.name }
                .also { loadingMappedValues = false }
        }
        .stateIn(
            scope = viewModelScope,
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
        showToast { getString(R.string.theme_has_been_saved_successfully) }
    }

    fun resetCurrentTheme() {
        themeManager.resetCurrentTheme()
        showToast { getString(R.string.reset_completed) }
    }

    fun loadSavedTheme(themeStorageType: ThemeStorageType) = themeManager.loadSavedTheme(
        storedTheme = themeStorageType,
        onSuccess = { storageTypeText, appearanceTypeText ->
            viewModelScope.launch {
                showToast {
                    getString(
                        R.string.theme_has_been_loaded_successfully,
                        storageTypeText,
                        appearanceTypeText
                    )
                }
            }
        },
        onIncompatibleValuesFound = {
            viewModelScope.launch {
                showToast { getString(R.string.some_colors_are_incompatible_and_were_marked_as_such) }
            }
        },
        onIncompatibleFileType = {
            viewModelScope.launch {
                showToast { getString(R.string.chosen_file_isn_t_a_telegram_not_telegram_x_theme) }
            }
        }
    )

    fun changeValue(uiElementName: String, colorToken: String, colorValue: Color) =
        themeManager.changeValue(uiElementName, colorToken, colorValue)

    @OptIn(ExperimentalCoroutinesApi::class)
    fun colorFromCurrentTheme(uiElementName: String) = mappedValues
        .mapLatest { it[uiElementName]?.color ?: Color.Red }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = mappedValues.value[uiElementName]?.color ?: Color.Red
        )

    fun selectOrUnselectSavedTheme(uuid: String) = if (selectedThemes.contains(uuid))
        selectedThemes.remove(uuid)
    else
        selectedThemes.add(uuid)


    fun exportTheme(uuid: String, dataType: ThemeColorDataType, activityContext: Context) =
        themeManager.exportTheme(uuid, dataType, activityContext)

    fun selectAllThemes() {
        selectedThemes.clear()
        viewModelScope.launch(Dispatchers.Default) {
            themeManager.getThemes(0..<Int.MAX_VALUE)
                .firstOrNull()
                ?.let { themeList ->
                    themeList.forEach {
                        if (!selectedThemes.contains(it.uuid)) {
                            selectedThemes.add(it.uuid)
                        }
                    }
                }
        }
    }

    fun unselectAllThemes() = selectedThemes.clear()

    fun deleteSelectedThemes(selectedThemeCount: Int) {
        selectedThemes.forEach { themeManager.deleteTheme(it) }

        showToast { getString(R.string.themes_have_been_successfully_deleted, selectedThemeCount) }
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

        showToast { getString(R.string.theme_has_been_deleted_successfully) }
    }

    fun overwriteTheme(uuid: String, isLightTheme: Boolean) {
        themeManager.overwriteTheme(uuid, isLightTheme)

        val themeType = if (isLightTheme) "light" else "dark"
        showToast { getString(R.string.default_theme_has_been_overwritten_successfully, themeType) }
    }
}
