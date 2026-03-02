package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import com.number869.telemone.data.UiElementColorData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditorSearchComponentImpl(
    private val mappedValues: StateFlow<Map<String, UiElementColorData>>,
    private val scope: CoroutineScope,
) : EditorSearchComponent {
    override val queryState = TextFieldState()

    private val matchedNames = MutableStateFlow<List<String>>(emptyList())

    override val searchResults: StateFlow<List<UiElementColorData>> =
        combine(matchedNames, mappedValues) { names, values ->
            names.mapNotNull { values[it] }
        }
            .flowOn(Dispatchers.Default)
            .stateIn(scope, SharingStarted.WhileSubscribed(), emptyList())

    init {
        scope.launch {
            snapshotFlow { queryState.text.toString() }
                .flowOn(Dispatchers.Default)
                .collect { query ->
                    matchedNames.value = if (query.isBlank()) emptyList()
                    else mappedValues.value.values
                        .filter {
                            it.name.contains(query, ignoreCase = true) ||
                                    it.colorToken.contains(query, ignoreCase = true)
                        }
                        .map { it.name }
                }
        }
    }
}