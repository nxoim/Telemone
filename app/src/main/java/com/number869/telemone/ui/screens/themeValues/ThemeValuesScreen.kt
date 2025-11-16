package com.number869.telemone.ui.screens.themeValues

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.number869.telemone.R
import com.number869.telemone.data.UiElementColorData
import com.number869.telemone.shared.utils.ThemeColorDataType
import com.number869.telemone.shared.utils.stringify
import com.number869.telemone.ui.theme.PaletteState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ThemeValuesScreen(
    mappedValuesAsList: StateFlow<List<UiElementColorData>>,
    paletteState: PaletteState
) {
	var showValues by remember { mutableStateOf(false) }
	val text = remember(showValues) {
		stringify(
			mappedValuesAsList.value.toList(),
			if (showValues)
				ThemeColorDataType.ColorValues
			else
				ThemeColorDataType.ColorTokens,
            palette = paletteState.entirePaletteAsMap
		)
	}

	LazyColumn(
		Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
		contentPadding = PaddingValues(
			top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding(),
			bottom = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()
		)
	) {
		item {
			Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
				Text(text = stringResource(R.string.show_color_values), style = MaterialTheme.typography.headlineSmall)
				Switch(checked = showValues, onCheckedChange = { showValues = it })
			}

		}
		item {
			SelectionContainer() {
				Text(text = text)
			}
		}
	}
}