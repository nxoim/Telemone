@file:OptIn(ExperimentalFoundationApi::class)

package com.number869.telemone.ui.screens.editor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.number869.telemone.R
import com.number869.telemone.data.PaletteState
import com.number869.telemone.data.UiElementColorData
import com.number869.telemone.ui.screens.editor.components.new.ChangeThemeValue
import com.number869.telemone.ui.screens.editor.components.new.CheckboxSelectionOverlay
import com.number869.telemone.ui.screens.editor.components.new.CurrentThemePreview
import com.number869.telemone.ui.screens.editor.components.new.EditorSearchBottomBar
import com.number869.telemone.ui.screens.editor.components.new.EditorTopAppBar
import com.number869.telemone.ui.screens.editor.components.new.ElementColorItem
import com.number869.telemone.ui.screens.editor.components.new.SavedThemeDropdownMenu
import com.number869.telemone.ui.screens.editor.components.new.SavedThemeItem
import com.number869.telemone.ui.screens.editor.components.new.ThemeSelectionToolbar
import com.number869.telemone.ui.shared.LocalScrollVisualFactor
import com.number869.telemone.ui.shared.SmallTintedLabel
import com.number869.telemone.ui.shared.modifiers.edgeToEdgeSystemBarsAlphaMaskFade
import com.number869.telemone.ui.shared.rememberScrollVisualFactorRoot
import com.number869.telemone.utils.ThemeColorPreviewDisplayType
import com.number869.telemone.utils.ThemeStorageType
import com.number869.telemone.utils.colorOf
import com.number869.telemone.utils.incompatibleUiElementColorData
import com.number869.telemone.utils.showToast
import com.nxoim.evolpagink.compose.items
import com.nxoim.evolpagink.compose.toState
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import my.nanihadesuka.compose.InternalLazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSelectionActionable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(model: EditorModel) {
    val topAppBarState = TopAppBarDefaults.pinnedScrollBehavior()
    val wholeThingListState = rememberLazyListState()

    val colorDisplayType by model.displayType.collectAsState()
    val paletteState by model.paletteState.collectAsState()

    val mappedValuesAsList by model.mappedValuesAsList.collectAsState()
    val newUiElements by model.newUiElements.collectAsState(listOf())
    val incompatibleValues by model.incompatibleValues.collectAsState(listOf())

    val scrollConnection = rememberScrollVisualFactorRoot()

    CompositionLocalProvider(LocalScrollVisualFactor provides scrollConnection) {
        Scaffold(
            Modifier
                .nestedScroll(topAppBarState.nestedScrollConnection)
                .nestedScroll(scrollConnection),
            topBar = {
                EditorTopAppBar(
                    topAppBarState,
                    loadSavedTheme = model::loadSavedTheme,
                    onNavigateBack = model.navigation::navigateBack,
                    onNavigateToThemeValues = model.navigation::navigateToThemeValues,
                    onNavigateToDialog = model.navigation::navigateToDialog
                )
            },
            bottomBar = {
                EditorSearchBottomBar(
                    paletteState = paletteState,
                    searchComponent = model.searchComponent,
                    changeValue = model::changeValue,
                    exportCustomTheme = { model.exportCustomTheme() },
                    saveCurrentTheme = model::saveCurrentTheme,
                )
            }
        ) { scaffoldPadding ->
            Box {
                AnimatedVisibility(
                    visible = model.loadingMappedValues,
                    enter = fadeIn(tween(300)),
                    exit = fadeOut(tween(300)),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    CircularProgressIndicator()
                }

                AnimatedVisibility(
                    visible = !model.loadingMappedValues,
                    enter = fadeIn(tween(300)),
                    exit = fadeOut(tween(300))
                ) {
                    LazyColumn(
                        state = wholeThingListState,
                        verticalArrangement = Arrangement.Absolute.spacedBy(4.dp),
                        contentPadding = scaffoldPadding + PaddingValues(
                            bottom = WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding() + 8.dp
                        ),
                        modifier = Modifier.edgeToEdgeSystemBarsAlphaMaskFade()
                    ) {
                        item {
                            CurrentThemeSection(
                                colorOf = {
                                    val color by model.colorFromCurrentTheme(it).collectAsState()
                                    animateColorAsState(color).value
                                }
                            )
                            SavedThemesSection(model, paletteState, colorDisplayType)
                        }

                        NewValuesSection(
                            model::changeValue,
                            paletteState,
                            newUiElements
                        )
                        IncompatibleValuesSection(
                            paletteState,
                            model::changeValue,
                            incompatibleValues,
                            mappedValuesAsList
                        )

                        item {
                            SmallTintedLabel(
                                Modifier.padding(start = 16.dp),
                                labelText = stringResource(R.string.all_colors_label)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        AllColorsSection(
                            paletteState,
                            model::changeValue,
                            mappedValuesAsList
                        )
                    }

                    InternalLazyColumnScrollbar(
                        listState = wholeThingListState,
                        thumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        thumbSelectedColor = MaterialTheme.colorScheme.primary,
                        selectionActionable = ScrollbarSelectionActionable.WhenVisible,
                        modifier = Modifier.padding(
                            top = scaffoldPadding.calculateTopPadding() + 8.dp,
                            bottom = WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding() + 8.dp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun CurrentThemeSection(
    modifier: Modifier = Modifier,
    colorOf: @Composable (String) -> Color
) = Column(modifier) {
    SmallTintedLabel(
        Modifier.padding(start = 16.dp),
        labelText = stringResource(R.string.current_theme_label)
    )
    CurrentThemePreview(colorOf)
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun SavedThemesSection(
    model: EditorModel,
    paletteState: PaletteState,
    colorDisplayType: ThemeColorPreviewDisplayType
) {
    val lazyThemeListState = rememberLazyListState()
    val themePageable = model.themesPageable.toState(
        lazyThemeListState,
        key = { it.uuid }
    )
    val displayingSavedThemes by remember {
        derivedStateOf { themePageable.items.isNotEmpty() }
    }

    LaunchedEffect(Unit) {
        model.themeCount.drop(1).collect {
            launch { lazyThemeListState.animateScrollToItem(0) }
            model.themesPageable.jumpTo(0)
        }
    }

    Row(
        Modifier
            .clip(CircleShape)
            .clickable { model.navigation.navigateToDialog(EditorDestinations.Dialogs.SavedThemeTypeSelection) },
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = spacedBy(8.dp)
    ) {
        SmallTintedLabel(
            Modifier.padding(start = 16.dp),
            labelText = stringResource(R.string.saved_themes_label)
        )

        FilledTonalIconButton(
            onClick = { model.navigation.navigateToDialog(EditorDestinations.Dialogs.SavedThemeTypeSelection) },
            modifier = Modifier.size(18.dp)
        ) {
            Icon(Icons.Default.MoreVert, contentDescription = stringResource(R.string.choose_display_type_label))
        }
    }

    Box {
        AnimatedVisibility(
            visible = model.loadingThemes,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200))
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(64.dp)) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }

        AnimatedVisibility(
            visible = !model.loadingThemes && !displayingSavedThemes,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            NoSavedThemesPlaceholder()
        }

        AnimatedVisibility(
            visible = displayingSavedThemes,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                LazyRow(
                    state = lazyThemeListState,
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp),
                    horizontalArrangement = spacedBy(16.dp),
                    modifier = Modifier.animateContentSize()
                ) {
                    items(themePageable) { theme ->
                        var showDropDown by rememberSaveable { mutableStateOf(false) }

                        Box {
                            SavedThemeItem(
                                Modifier
                                    .animateItem()
                                    .let {
                                        return@let if (!model.themeSelectionToolbarIsVisible)
                                            it.combinedClickable(
                                                onClick = {
                                                    model.loadSavedTheme(
                                                        ThemeStorageType.ByUuid(
                                                            theme.uuid,
                                                            withTokens = false,
                                                            clearCurrentTheme = true
                                                        )
                                                    )
                                                    showToast(R.string.theme_loaded)
                                                },
                                                onLongClick = { showDropDown = true }
                                            )
                                        else
                                            it.clickable { model.selectOrUnselectSavedTheme(theme.uuid) }
                                    },
                                colorOf = { targetUiElement ->
                                    colorOf(
                                        theme.values.find { it.name == targetUiElement }
                                            ?: incompatibleUiElementColorData(targetUiElement),
                                        colorDisplayType,
                                        palette = paletteState.entirePaletteAsMap
                                    )
                                },
                                overlay = {
                                    CheckboxSelectionOverlay(
                                        selected = model.selectedThemes.contains(theme.uuid),
                                        onCheckedChange = { model.selectOrUnselectSavedTheme(theme.uuid) }
                                    )
                                }
                            )

                            SavedThemeDropdownMenu(
                                showDropDown,
                                onDismissRequest = { showDropDown = false },
                                onLoadThemeWithOptionsRequest = {
                                    model.navigation.navigateToDialog(
                                        EditorDestinations.Dialogs.LoadThemeWithOptions(theme.uuid)
                                    )
                                },
                                onExportTheme = { model.exportTheme(theme.uuid, it) },
                                onOverwriteDefaultThemeChoiceRequest = {
                                    showDropDown = false
                                    model.navigation.navigateToDialog(
                                        EditorDestinations.Dialogs.OverwriteDefaultThemeChoice(theme)
                                    )
                                },
                                onDeleteRequest = {
                                    showDropDown = false
                                    model.navigation.navigateToDialog(
                                        EditorDestinations.Dialogs.DeleteOneTheme(theme)
                                    )
                                },
                                onSelectRequest = {
                                    showDropDown = false
                                    model.toggleThemeSelectionModeToolbar()
                                    model.selectOrUnselectSavedTheme(theme.uuid)
                                }
                            )
                        }
                    }
                }

                ThemeSelectionToolbarSection(model)
            }
        }
    }
}

@Composable
private fun NoSavedThemesPlaceholder() {
    Box(
        Modifier
            .fillMaxWidth(1f)
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
            .animateContentSize()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "No saved themes",
                style = TextStyle(
                    platformStyle = PlatformTextStyle(includeFontPadding = false)
                ).plus(MaterialTheme.typography.headlineSmall),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ThemeSelectionToolbarSection(model: EditorModel) {
    val count by model.themeCount.collectAsStateWithLifecycle()

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        AnimatedVisibility(
            model.themeSelectionToolbarIsVisible,
            enter = expandVertically(expandFrom = Alignment.CenterVertically) + fadeIn(),
            exit = shrinkVertically(shrinkTowards = Alignment.CenterVertically) + fadeOut(),
        ) {
            ThemeSelectionToolbar(
                Modifier.padding(top = 16.dp),
                selectedThemeCount = model.selectedThemes.count(),
                allThemesAreSelected = model.selectedThemes.count() == count,
                unselectAllThemes = model::unselectAllThemes,
                selectAllThemes = model::selectAllThemes,
                hideToolbarAction = { model.hideThemeSelectionModeToolbar() },
                onRequestDeletion = {
                    model.navigation.navigateToDialog(
                        EditorDestinations.Dialogs.DeleteSelectedThemes(model.selectedThemes.count())
                    )
                }
            )
        }
    }

    Spacer(
        Modifier
            .animateContentSize()
            .height(if (model.themeSelectionToolbarIsVisible) 12.dp else 24.dp)
    )
}

private fun LazyListScope.NewValuesSection(
    changeValue: ChangeThemeValue,
    paletteState: PaletteState,
    newUiElements: List<UiElementColorData>
) {
    if (newUiElements.isNotEmpty()) {
        item {
            SmallTintedLabel(
                Modifier.padding(start = 16.dp).animateItem(),
                labelText = stringResource(R.string.new_values_label)
            )
            Spacer(modifier = Modifier.height(16.dp).animateItem())
        }

        itemsIndexed(newUiElements) { index, uiElementData ->
            ElementColorItem(
                Modifier.padding(horizontal = 16.dp).animateItem(),
                paletteState = paletteState,
                uiElementData = uiElementData,
                index = index,
                changeValue = changeValue,
                lastIndexInList = newUiElements.lastIndex
            )
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

private fun LazyListScope.IncompatibleValuesSection(
    paletteState: PaletteState,
    changeValue: ChangeThemeValue,
    incompatibleValues: List<UiElementColorData>,
    mappedValuesAsList: List<UiElementColorData>
) {
    if (incompatibleValues.isNotEmpty()) {
        item {
            SmallTintedLabel(
                Modifier.padding(start = 16.dp).animateItem(),
                labelText = stringResource(R.string.incompatible_values_label)
            )
            Spacer(modifier = Modifier.height(16.dp).animateItem())
        }

        itemsIndexed(incompatibleValues) { index, uiElementData ->
            ElementColorItem(
                Modifier.padding(horizontal = 16.dp).animateItem(),
                paletteState = paletteState,
                uiElementData = uiElementData,
                index = index,
                changeValue = changeValue,
                lastIndexInList = mappedValuesAsList.lastIndex
            )
        }
    }
}

private fun LazyListScope.AllColorsSection(
    paletteState: PaletteState,
    changeValue: ChangeThemeValue,
    mappedValuesAsList: List<UiElementColorData>
) {
    itemsIndexed(mappedValuesAsList, key = { _, it -> it.name }) { index, uiElementColorData ->
        ElementColorItem(
            Modifier.padding(horizontal = 16.dp).animateItem(),
            paletteState = paletteState,
            uiElementData = uiElementColorData,
            index = index,
            changeValue = changeValue,
            lastIndexInList = mappedValuesAsList.size
        )
    }
}