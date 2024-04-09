package com.number869.telemone.ui.screens.editor.components.new.popup

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.number869.telemone.shared.utils.LocalBooleanProvider
import com.number869.telemone.shared.utils.sharedElement
import com.number869.telemone.ui.theme.ColorRolesDark
import com.number869.telemone.ui.theme.ColorRolesLight
import com.number869.telemone.ui.theme.ColorRolesShared
import com.number869.telemone.ui.theme.DataAboutColors
import com.number869.telemone.ui.theme.SolarSet

@Composable
fun ColorRolesContent(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String,
) {
    val notPagerScrollState = rememberScrollState()

    val indicatorHorizontalAlignment by remember {
        derivedStateOf {
            -1f + ((1 + notPagerScrollState.value.toFloat()) / (1 + notPagerScrollState.maxValue.toFloat())) * 2
        }
    }

    CompositionLocalProvider(
        // "visible" color item's shared element parameter
        LocalBooleanProvider provides true
    ) {
        Column {
            Box(
                contentAlignment = BiasAlignment(indicatorHorizontalAlignment, 0f)
            ) {
                HorizontalDivider()
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(0.5f),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            BoxWithConstraints {
                val maxSize = DpSize(maxWidth, maxHeight)

                Row(Modifier.horizontalScroll(notPagerScrollState)) {
                    Box(Modifier.size(maxSize)) {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                SolarSet.Sun,
                                contentDescription = "Light theme colorValue roles",
                                Modifier
                                    .size(32.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Column(verticalArrangement = spacedBy(8.dp)) {
                                ItemRow(
                                    items = ColorRoleItems.firstRowLightItems,
                                    uiElementName = uiElementName,
                                    changeValue = changeValue
                                )

                                ItemRow(
                                    items = ColorRoleItems.secondRowLightItems,
                                    uiElementName = uiElementName,
                                    changeValue = changeValue
                                )

                                ItemRow(
                                    items = ColorRoleItems.thirdRowLightItems,
                                    uiElementName = uiElementName,
                                    changeValue = changeValue
                                )

                                ItemRow(
                                    items = ColorRoleItems.fourthRowLightItems,
                                    uiElementName = uiElementName,
                                    changeValue = changeValue
                                )

                                Box(Modifier.height(136.dp)) // space for shared tokens

                                ItemRow(
                                    Modifier.height(64.dp),
                                    items = ColorRoleItems.fifthRowLightItems,
                                    additionalItems = ColorRoleItems.fifthRowLightItemsInverse,
                                    uiElementName = uiElementName,
                                    changeValue = changeValue
                                )

                                ItemRow(
                                    items = ColorRoleItems.sixthRowLightItems,
                                    uiElementName = uiElementName,
                                    changeValue = changeValue
                                )

                                ItemRow(
                                    items = ColorRoleItems.seventhRowLightItems,
                                    uiElementName = uiElementName,
                                    changeValue = changeValue
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Box(Modifier.size(maxSize)) {
                        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                SolarSet.Moon,
                                contentDescription = "Dark theme colorValue roles",
                                Modifier
                                    .size(32.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Column(verticalArrangement = spacedBy(8.dp)) {
                                ItemRow(
                                    items = ColorRoleItems.firstRowDarkItems,
                                    uiElementName = uiElementName,
                                    changeValue = changeValue
                                )

                                ItemRow(
                                    items = ColorRoleItems.secondRowDarkItems,
                                    uiElementName = uiElementName,
                                    changeValue = changeValue
                                )

                                ItemRow(
                                    items = ColorRoleItems.thirdRowDarkItems,
                                    uiElementName = uiElementName,
                                    changeValue = changeValue
                                )

                                ItemRow(
                                    items = ColorRoleItems.fourthRowDarkItems,
                                    uiElementName = uiElementName,
                                    changeValue = changeValue
                                )

                                Box(Modifier.height(136.dp)) // space for shared tokens

                                ItemRow(
                                    Modifier.height(64.dp),
                                    items = ColorRoleItems.fifthRowDarkItems,
                                    additionalItems = ColorRoleItems.fifthRowDarkItemsInverse,
                                    uiElementName = uiElementName,
                                    changeValue = changeValue
                                )

                                ItemRow(
                                    items = ColorRoleItems.sixthRowDarkItems,
                                    uiElementName = uiElementName,
                                    changeValue = changeValue
                                )

                                ItemRow(
                                    items = ColorRoleItems.seventhRowDarkItems,
                                    uiElementName = uiElementName,
                                    changeValue = changeValue
                                )
                            }
                        }
                    }
                }

                Column(
                    Modifier.padding(top = 240.dp),
                    verticalArrangement = spacedBy(8.dp)
                ) {
                    ItemRow(
                        items = ColorRoleItems.firstSharedRowItems,
                        uiElementName = uiElementName,
                        changeValue = changeValue
                    )

                    ItemRow(
                        items = ColorRoleItems.secondSharedRowItems,
                        uiElementName = uiElementName,
                        changeValue = changeValue
                    )

                    ItemRow(
                        items = ColorRoleItems.thirdSharedRowItems,
                        uiElementName = uiElementName,
                        changeValue = changeValue
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@NonRestartableComposable
@Composable
fun ColorRoleItem(
    modifier: Modifier = Modifier,
    dataAboutColors: DataAboutColors,
    uiElementName: String,
    changeValue: (String, String, Color) -> Unit,
    enabled: Boolean,
) {
    val colorToken = dataAboutColors.colorToken
    val colorValue = dataAboutColors.colorValue()

    val outlineAlpha by animateFloatAsState(if (enabled) 1f else 0f, label = "")
    val zIndex = when (dataAboutColors) {
        ColorRolesLight.PrimaryContainer.dataAboutColors -> 1f
        ColorRolesLight.SecondaryContainer.dataAboutColors -> 2f
        ColorRolesLight.TertiaryContainer.dataAboutColors -> 3f
        else -> 0f
    }

    Box(
        modifier
            .sharedElement(
                dataAboutColors.toString() + "colorRoleItem",
                visible = LocalBooleanProvider.current,
                zIndexInOverlay = zIndex
            )
            .height(40.dp)
            .fillMaxWidth()
            .clip(CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(outlineAlpha), CircleShape)
            .background(colorValue)
            .let {
                return@let if (enabled)
                    it.clickable { changeValue(uiElementName, colorToken, colorValue) }
                else
                    it
            }
    )
}

@Stable
@Composable
private fun ItemRow(
    modifier: Modifier = Modifier.height(40.dp),
    items: List<DataAboutColors>,
    additionalItems: List<DataAboutColors>? = null,
    uiElementName: String,
    changeValue: (String, String, Color) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.fastForEach { item ->
            ColorRoleItem(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                dataAboutColors = item,
                uiElementName = uiElementName,
                changeValue = changeValue,
                enabled = true
            )
        }

        additionalItems?.let {
            Column(Modifier.weight(1f), verticalArrangement = spacedBy(8.dp)) {
                it.fastForEach { item  ->
                    ColorRoleItem(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize(),
                        dataAboutColors = item,
                        uiElementName = uiElementName,
                        changeValue = changeValue,
                        enabled = true
                    )
                }
            }
        }
    }
}

private object ColorRoleItems {
    val firstSharedRowItems = listOf(
        ColorRolesShared.PrimaryFixed.dataAboutColors,
        ColorRolesShared.PrimaryFixedDim.dataAboutColors,
        ColorRolesShared.SecondaryFixed.dataAboutColors,
        ColorRolesShared.SecondaryFixedDim.dataAboutColors,
        ColorRolesShared.TertiaryFixed.dataAboutColors,
        ColorRolesShared.TertiaryFixedDim.dataAboutColors
    )

    val secondSharedRowItems = listOf(
        ColorRolesShared.OnPrimaryFixed.dataAboutColors,
        ColorRolesShared.OnSecondaryFixed.dataAboutColors,
        ColorRolesShared.OnTertiaryFixed.dataAboutColors
    )

    val thirdSharedRowItems = listOf(
        ColorRolesShared.OnPrimaryFixedVariant.dataAboutColors,
        ColorRolesShared.OnSecondaryFixedVariant.dataAboutColors,
        ColorRolesShared.OnTertiaryFixedVariant.dataAboutColors
    )

    val firstRowLightItems = listOf(
        ColorRolesLight.PrimaryContainer.dataAboutColors,
        ColorRolesLight.SecondaryContainer.dataAboutColors,
        ColorRolesLight.TertiaryContainer.dataAboutColors,
        ColorRolesLight.ErrorContainer.dataAboutColors
    )

    val secondRowLightItems = listOf(
        ColorRolesLight.OnPrimaryContainer.dataAboutColors,
        ColorRolesLight.OnSecondaryContainer.dataAboutColors,
        ColorRolesLight.OnTertiaryContainer.dataAboutColors,
        ColorRolesLight.OnErrorContainer.dataAboutColors
    )

    val thirdRowLightItems = listOf(
        ColorRolesLight.PrimaryContainer.dataAboutColors,
        ColorRolesLight.SecondaryContainer.dataAboutColors,
        ColorRolesLight.TertiaryContainer.dataAboutColors,
        ColorRolesLight.ErrorContainer.dataAboutColors
    )

    val fourthRowLightItems = listOf(
        ColorRolesLight.OnPrimaryContainer.dataAboutColors,
        ColorRolesLight.OnSecondaryContainer.dataAboutColors,
        ColorRolesLight.OnTertiaryContainer.dataAboutColors,
        ColorRolesLight.OnErrorContainer.dataAboutColors
    )

    val fifthRowLightItems = listOf(
        ColorRolesLight.SurfaceDim.dataAboutColors,
        ColorRolesLight.Surface.dataAboutColors,
        ColorRolesLight.SurfaceBright.dataAboutColors,
    )

    val fifthRowLightItemsInverse = listOf(
        ColorRolesLight.InverseSurface.dataAboutColors,
        ColorRolesLight.InverseOnSurface.dataAboutColors
    )

    val sixthRowLightItems = listOf(
        ColorRolesLight.SurfaceContainerLowest.dataAboutColors,
        ColorRolesLight.SurfaceContainerLow.dataAboutColors,
        ColorRolesLight.SurfaceContainer.dataAboutColors,
        ColorRolesLight.SurfaceContainerHigh.dataAboutColors,
        ColorRolesLight.SurfaceContainerHighest.dataAboutColors,
        ColorRolesLight.InversePrimary.dataAboutColors
    )

    val seventhRowLightItems = listOf(
        ColorRolesLight.OnSurface.dataAboutColors,
        ColorRolesLight.OnSurfaceVariant.dataAboutColors,
        ColorRolesLight.Outline.dataAboutColors,
        ColorRolesLight.OutlineVariant.dataAboutColors,
        ColorRolesLight.Scrim.dataAboutColors,
        ColorRolesLight.Shadow.dataAboutColors
    )

    val firstRowDarkItems = listOf(
        ColorRolesDark.PrimaryContainer.dataAboutColors,
        ColorRolesDark.SecondaryContainer.dataAboutColors,
        ColorRolesDark.TertiaryContainer.dataAboutColors,
        ColorRolesDark.ErrorContainer.dataAboutColors
    )

    val secondRowDarkItems = listOf(
        ColorRolesDark.OnPrimaryContainer.dataAboutColors,
        ColorRolesDark.OnSecondaryContainer.dataAboutColors,
        ColorRolesDark.OnTertiaryContainer.dataAboutColors,
        ColorRolesDark.OnErrorContainer.dataAboutColors
    )

    val thirdRowDarkItems = listOf(
        ColorRolesDark.PrimaryContainer.dataAboutColors,
        ColorRolesDark.SecondaryContainer.dataAboutColors,
        ColorRolesDark.TertiaryContainer.dataAboutColors,
        ColorRolesDark.ErrorContainer.dataAboutColors
    )

    val fourthRowDarkItems = listOf(
        ColorRolesDark.OnPrimaryContainer.dataAboutColors,
        ColorRolesDark.OnSecondaryContainer.dataAboutColors,
        ColorRolesDark.OnTertiaryContainer.dataAboutColors,
        ColorRolesDark.OnErrorContainer.dataAboutColors
    )

    val fifthRowDarkItems = listOf(
        ColorRolesDark.InverseSurface.dataAboutColors,
        ColorRolesDark.InverseOnSurface.dataAboutColors
    )

    val fifthRowDarkItemsInverse = listOf(
        ColorRolesDark.InverseSurface.dataAboutColors,
        ColorRolesDark.InverseOnSurface.dataAboutColors
    )

    val sixthRowDarkItems = listOf(
        ColorRolesDark.SurfaceContainerLowest.dataAboutColors,
        ColorRolesDark.SurfaceContainerLow.dataAboutColors,
        ColorRolesDark.SurfaceContainer.dataAboutColors,
        ColorRolesDark.SurfaceContainerHigh.dataAboutColors,
        ColorRolesDark.SurfaceContainerHighest.dataAboutColors,
        ColorRolesDark.InversePrimary.dataAboutColors
    )

    val seventhRowDarkItems = listOf(
        ColorRolesDark.OnSurface.dataAboutColors,
        ColorRolesDark.OnSurfaceVariant.dataAboutColors,
        ColorRolesDark.Outline.dataAboutColors,
        ColorRolesDark.OutlineVariant.dataAboutColors,
        ColorRolesDark.Scrim.dataAboutColors,
        ColorRolesDark.Shadow.dataAboutColors
    )
}