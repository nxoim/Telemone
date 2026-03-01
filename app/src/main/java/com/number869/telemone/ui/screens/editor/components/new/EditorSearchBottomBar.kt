package com.number869.telemone.ui.screens.editor.components.new

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.number869.telemone.App.Companion.paletteState
import com.number869.telemone.R
import com.number869.telemone.data.UiElementColorData
import com.number869.telemone.shared.ui.LocalScrollVisualFactor
import com.number869.telemone.shared.utils.CombineSharedTransitionAndAnimatedVisibility
import com.number869.telemone.shared.utils.CombinedSharedTransitionScope
import com.number869.telemone.ui.theme.scaleInWithFade
import com.number869.telemone.ui.theme.scaleOutWithFade
import com.nxoim.blean.ui.composeUiCommons.modifiers.swipeable.SwipeConstraint
import com.nxoim.blean.ui.composeUiCommons.modifiers.swipeable.swipeable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun EditorSearchBottomBar(
    mappedValuesAsList: List<UiElementColorData>,
    changeValue: (String, String, Color) -> Unit,
    exportCustomTheme: () -> Unit,
    saveCurrentTheme: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollVisualFactor = LocalScrollVisualFactor.current

    SharedTransitionLayout(modifier) {
        var expanded by remember { mutableStateOf(false) }
        val sharedContentKey = rememberSharedContentState(0)

        AnimatedContent(expanded) {
            CombineSharedTransitionAndAnimatedVisibility(this) {
                Box(Modifier.fillMaxWidth()) {
                    if (!it) {
                        AnimatedVisibility(
                            scrollVisualFactor.fraction.value <= 0.2f,
                            enter = scaleInWithFade(),
                            exit = scaleOutWithFade(),
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .navigationBarsPadding()
                                .padding(bottom = 8.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.End) {
                                var menuIsOpen by remember { mutableStateOf(false) }

                                FloatingActionButtonMenu(
                                    menuIsOpen,
                                    button = {
                                        SmallFloatingActionButton(
                                            onClick = { menuIsOpen = !menuIsOpen },
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                        ) {
                                            AnimatedContent(menuIsOpen) { isOpen ->
                                                if (isOpen) Icon(
                                                    Icons.Default.Close,
                                                    contentDescription = "Save current theme"
                                                ) else Icon(
                                                    Icons.Default.Add,
                                                    contentDescription = "Save current theme"
                                                )
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .offset(y = 8.dp)
                                        .padding(end = 4.dp)
                                ) {
                                    FloatingActionButtonMenuItem(
                                        onClick = { exportCustomTheme() },
                                        icon = {
                                            Icon(
                                                Icons.Default.Upload,
                                                contentDescription = "Export current"
                                            )
                                        },
                                        text = { Text("Export current") }
                                    )

                                    FloatingActionButtonMenuItem(
                                        onClick = { saveCurrentTheme() },
                                        icon = {
                                            Icon(
                                                Icons.Default.Save,
                                                contentDescription = "Save current theme"
                                            )
                                        },
                                        text = {
                                            Text("Save current theme")
                                        }
                                    )
                                }

                                FloatingActionButton(
                                    onClick = { expanded = true },
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .sharedBounds(
                                            sharedContentKey,
                                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = stringResource(R.string.search_title)
                                    )
                                }
                            }
                        }
                    } else {
                        EditorSearchBar(
                            modifier = Modifier.align(Alignment.Center),
                            mappedValuesAsList = mappedValuesAsList,
                            sharedContentKey = sharedContentKey,
                            changeValue = changeValue,
                            onDismiss = { expanded = false },
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CombinedSharedTransitionScope.EditorSearchBar(
    mappedValuesAsList: List<UiElementColorData>,
    sharedContentKey: SharedTransitionScope.SharedContentState,
    changeValue: (String, String, Color) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var fullscreen by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val searchQueryIsEmpty by remember { derivedStateOf { searchQuery.isEmpty() } }

    val searchedThings by produceState(emptyList<UiElementColorData>()) {
        snapshotFlow { searchQuery }
            .flowOn(Dispatchers.Default)
            .collect { query ->
                value = mappedValuesAsList.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.colorToken.contains(query, ignoreCase = true)
                }
            }
    }

    var gestureOffset by remember { mutableStateOf(Offset.Zero) }
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val bottomPadding by animateDpAsState(
        if (fullscreen) Dp.Hairline
        else WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    )

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
    }

    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = searchQuery,
                onQueryChange = {
                    searchQuery = it
                    fullscreen = true
                },
                onSearch = { },
                expanded = fullscreen,
                onExpandedChange = { },
                enabled = true,
                placeholder = { Text(stringResource(R.string.search_in_current_theme)) },
                leadingIcon = {
                    Icon(Icons.Default.Search, stringResource(R.string.search_title))
                },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = searchQueryIsEmpty && !fullscreen,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(
                            onClick = {
                                keyboardController?.hide()
                                onDismiss()
                            }
                        ) {
                            Icon(
                                Icons.Default.Close,
                                stringResource(R.string.hide_searchbar_action)
                            )
                        }
                    }

                    AnimatedVisibility(
                        visible = !searchQueryIsEmpty,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, stringResource(R.string.clear_search_action))
                        }
                    }
                },
                interactionSource = null,
                modifier = Modifier.focusRequester(focusRequester)
            )
        },
        expanded = fullscreen,
        onExpandedChange = { },
        modifier = modifier
            .imePadding()
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                layout(placeable.width, placeable.height) {
                    withMotionFrameOfReferencePlacement {
                        placeable.placeRelative(gestureOffset.x.roundToInt(), 0)
                    }
                }
            }
            .sharedBounds(sharedContentKey)
            .swipeable(
                SwipeConstraint.end(),
                activationRequireUnconsumed = false,
                isEnabled = { fullscreen },
                onStart = { gestureOffset = Offset.Zero },
                onProgress = { delta, _, _ -> gestureOffset += delta },
                onCancel = {
                    coroutineScope.launch {
                        animate(
                            typeConverter = Offset.VectorConverter,
                            initialValue = gestureOffset,
                            targetValue = Offset.Zero,
                        ) { value, _ -> gestureOffset = value }
                    }
                },
                onConfirm = { velocity, _ ->
                    sharedContentKey.prepareTransitionWithInitialVelocity(velocity)
                    focusRequester.freeFocus()
                    keyboardController?.hide()
                    onDismiss()
                }
            )
            .graphicsLayer {
                translationY = -bottomPadding.toPx()
            },
        content = {
            BackHandler(fullscreen) { fullscreen = false }

            Box {
                this@SearchBar.AnimatedVisibility(
                    visible = searchQueryIsEmpty,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Box(
                        Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            ) { fullscreen = false }
                            .imePadding()
                            .fillMaxSize()
                    ) {
                        Text(
                            stringResource(R.string.search_is_empty_tap_this_to_close_notice),
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                this@SearchBar.AnimatedVisibility(
                    visible = !searchQueryIsEmpty,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(
                            top = 8.dp,
                            bottom = 4.dp + WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding()
                        ),
                        verticalArrangement = spacedBy(4.dp)
                    ) {
                        itemsIndexed(searchedThings) { index, uiElementData ->
                            ElementColorItem(
                                Modifier
                                    .padding(horizontal = 16.dp)
                                    .animateItem(),
                                paletteState = paletteState,
                                uiElementData = uiElementData,
                                index = index,
                                changeValue = changeValue,
                                lastIndexInList = mappedValuesAsList.lastIndex
                            )
                        }
                    }
                }
            }
        }
    )
}