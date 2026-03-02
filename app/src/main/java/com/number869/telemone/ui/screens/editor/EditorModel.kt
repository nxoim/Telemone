package com.number869.telemone.ui.screens.editor

import androidx.collection.LruCache
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import com.number869.telemone.R
import com.number869.telemone.data.PredefinedTheme
import com.number869.telemone.data.ThemeLoadError
import com.number869.telemone.data.ThemeManager
import com.number869.telemone.ui.screens.common.ThemeExporter
import com.number869.telemone.ui.screens.common.ThemeFilePicker
import com.number869.telemone.ui.screens.editor.components.new.EditorSearchComponent
import com.number869.telemone.ui.screens.editor.components.new.EditorSearchComponentImpl
import com.number869.telemone.utils.ThemeColorDataType
import com.number869.telemone.utils.ThemeColorPreviewDisplayType
import com.number869.telemone.utils.ThemeStorageType
import com.number869.telemone.utils.color
import com.number869.telemone.utils.showToast
import com.nxoim.evolpagink.core.pageable
import com.nxoim.evolpagink.core.prefetchMinimumItemAmount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val pageSize = 10

class EditorModel(
    private val themeManager: ThemeManager,
    val navigation: EditorNavigation,
    private val coroutineScope: CoroutineScope,
    private val themeExporter: ThemeExporter,
    private val themePicker: ThemeFilePicker,
    val displayTypeSettings: ThemeDisplaySettings
) {
    private val colorFlowCache = LruCache<String, StateFlow<Color>>(maxSize = 100)

    val paletteState = themeManager.paletteState

    var loadingMappedValues by mutableStateOf(true)
        private set
    var loadingThemes by mutableStateOf(true)
        private set

    val defaultThemes= combine(
        themeManager.getThemeByUUID(PredefinedTheme.Default(true).uuid),
        themeManager.getThemeByUUID(PredefinedTheme.Default(false).uuid)
    ) { light, dark ->
        if (light != null && dark != null) light to dark else null
    }.stateIn(coroutineScope, SharingStarted.Eagerly, null)

    val themesPageable = pageable(
        coroutineScope,
        onPage = {
            themeManager
                .getThemes((pageSize * it) until (pageSize * (it + 1)))
                .onEach { loadingThemes = false }
        },
        strategy = prefetchMinimumItemAmount()
    )

    val themeCount = themeManager.countSavedThemes()
        .stateIn(coroutineScope, WhileSubscribed(), 0)

    private val mappedValues = themeManager.mappedValues
    val mappedValuesAsList = mappedValues
        .map {
            it.values
                .sortedBy { it.name }
                .also { loadingMappedValues = false }
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = mappedValues.value.values.toList()
        )

    private val defaultCurrentTheme = themeManager.defaultCurrentTheme
    val displayType = displayTypeSettings.displayType()
        .map { ThemeColorPreviewDisplayType.fromId(it) }
        .stateIn(
            coroutineScope,
            WhileSubscribed(),
            ThemeColorPreviewDisplayType.SavedColorValues
        )

    val newUiElements = mappedValues.map {
        it.values
            .asSequence()
            .filter { it.name !in defaultCurrentTheme.map { it.name } }
            .sortedBy { it.name }
            .toList()
    }

    val incompatibleValues = mappedValues.map {
        it.values
            .asSequence()
            .filter { it.colorToken == "INCOMPATIBLE VALUE" }
            .sortedByDescending { it.name }
            .toList()
    }

    val selectedThemes = mutableStateListOf<String>()
    var themeSelectionToolbarIsVisible by mutableStateOf(false)

    val searchComponent: EditorSearchComponent =
        EditorSearchComponentImpl(mappedValues, coroutineScope)

    fun changeValue(uiElementName: String, colorToken: String, colorValue: Color) {
        coroutineScope.launch {
            themeManager.changeValue(uiElementName, colorToken, colorValue)
        }
    }

    fun saveCurrentTheme() = coroutineScope.launch {
        themeManager.saveCurrentTheme()
        showToast(R.string.theme_has_been_saved_successfully)
    }

    fun loadSavedTheme(themeStorageType: ThemeStorageType) = coroutineScope.launch {
        themeManager.loadSavedThemeForResult(themeStorageType)
            .onSuccess {
                showToast(
                    R.string.theme_has_been_loaded_successfully,
                    it.storageTypeText,
                    it.appearanceTypeText
                )
            }
            .onFailure { error ->
                showToast(
                    when (error) {
                        ThemeLoadError.IncompatibleValues ->
                            R.string.some_colors_are_incompatible_and_were_marked_as_such

                        ThemeLoadError.IncompatibleFileType ->
                            R.string.chosen_file_isn_t_a_telegram_not_telegram_x_theme
                    }
                )
            }
    }

    fun pickAndLoadTheme(clearCurrentTheme: Boolean) = coroutineScope.launch {
        val source = themePicker.pick() ?: return@launch
        loadSavedTheme(ThemeStorageType.ExternalFile(source, clearCurrentTheme))
        navigation.navigateBackDialog()
    }

    fun exportCustomTheme() = coroutineScope.launch {
        val (fileName, content) = themeManager.backUpAndPackageCustomTheme()
        themeExporter.export(fileName, content)
    }

    fun exportTheme(uuid: String, dataType: ThemeColorDataType) = coroutineScope.launch {
        val (fileName, content) = themeManager.backUpAndPackageTheme(uuid, dataType)
        themeExporter.export(fileName, content)
    }

    fun selectOrUnselectSavedTheme(uuid: String) =
        if (selectedThemes.contains(uuid))
            selectedThemes.remove(uuid)
        else
            selectedThemes.add(uuid)

    fun selectAllThemes() {
        selectedThemes.clear()
        coroutineScope.launch(Dispatchers.Default) {
            themeManager.getThemes(0..<Int.MAX_VALUE)
                .firstOrNull()
                ?.forEach { if (!selectedThemes.contains(it.uuid)) selectedThemes.add(it.uuid) }
        }
    }

    fun unselectAllThemes() = selectedThemes.clear()

    fun deleteSelectedThemes(selectedThemeCount: Int) {
        coroutineScope.launch {
            selectedThemes.forEach { themeManager.deleteTheme(it) }
        }
        showToast(R.string.themes_have_been_successfully_deleted, selectedThemeCount)
    }

    fun hideThemeSelectionModeToolbar() {
        themeSelectionToolbarIsVisible = false
    }

    fun toggleThemeSelectionModeToolbar() {
        themeSelectionToolbarIsVisible = !themeSelectionToolbarIsVisible
    }

    fun getThemeByUUID(uuid: String) = themeManager.getThemeByUUID(uuid)

    fun deleteTheme(uuid: String) = coroutineScope.launch {
        themeManager.deleteTheme(uuid)
        showToast(R.string.theme_has_been_deleted_successfully)
    }

    fun overwriteTheme(uuid: String, isLightTheme: Boolean) = coroutineScope.launch {
        themeManager.overwriteTheme(uuid, isLightTheme)
        val themeType = if (isLightTheme) "light" else "dark"
        showToast(
            R.string.default_theme_has_been_overwritten_successfully,
            themeType
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun colorFromCurrentTheme(uiElementName: String) = colorFlowCache.getOrPut(uiElementName) {
        mappedValues
            .mapLatest { it[uiElementName]?.color ?: Color.Red }
            .stateIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = mappedValues.value[uiElementName]?.color ?: Color.Red
            )
    }
}

private inline fun <K : Any, V : Any> LruCache<K, V>.getOrPut(key: K, block: () -> V): V =
    get(key) ?: block().also { put(key, it) }

interface EditorNavigation {
    fun navigateBack()
    fun navigateToThemeValues()
    fun navigateToDialog(destination: EditorDestinations.Dialogs)
    fun navigateBackDialog()
}

interface ThemeDisplaySettings {
    fun displayType(): Flow<Int>
    suspend fun setDisplayType(id: Int)
}