package com.number869.telemone.ui.screens.editor.components.new.popup

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.number869.telemone.ui.theme.ColorRoles
import com.number869.telemone.ui.theme.ColorRolesShared
import com.number869.telemone.ui.theme.DataAboutColors
import com.number869.telemone.ui.theme.SolarSet

@Composable
fun ColorRolesContent(
    modifier: Modifier = Modifier,
    colorRolesLight: ColorRoles,
    colorRolesDark: ColorRoles,
    colorRolesShared: ColorRolesShared,
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String,
) {
    val colorRoleItems = remember {
        ColorRoleItems(colorRolesDark, colorRolesLight, colorRolesShared)
    }

    val notPagerScrollState = rememberScrollState()

    val indicatorHorizontalAlignment by remember {
        derivedStateOf {
            -1f + ((1 + notPagerScrollState.value.toFloat()) / (1 + notPagerScrollState.maxValue.toFloat())) * 2
        }
    }

    Column(modifier.height(560.dp).fillMaxWidth()) {
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
            val maxWidth = this.maxWidth - 32.dp
            val maxHeight = this.maxHeight

            Row(Modifier.horizontalScroll(notPagerScrollState)) {
                Spacer(Modifier.width(16.dp))

                Column(
                    Modifier.size(maxWidth, maxHeight),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        SolarSet.Sun,
                        contentDescription = "Light theme colorValue roles",
                        Modifier
                            .size(32.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = spacedBy(8.dp)) {
                        ItemRow(
                            items = colorRoleItems.firstRowLightItems,
                            uiElementName = uiElementName,
                            changeValue = changeValue
                        )

                        ItemRow(
                            items = colorRoleItems.secondRowLightItems,
                            uiElementName = uiElementName,
                            changeValue = changeValue
                        )

                        ItemRow(
                            items = colorRoleItems.thirdRowLightItems,
                            uiElementName = uiElementName,
                            changeValue = changeValue
                        )

                        ItemRow(
                            items = colorRoleItems.fourthRowLightItems,
                            uiElementName = uiElementName,
                            changeValue = changeValue
                        )

                        Box(Modifier.height(136.dp)) // space for shared tokens

                        ItemRow(
                            Modifier.height(64.dp),
                            items = colorRoleItems.fifthRowLightItems,
                            additionalItems = colorRoleItems.fifthRowLightItemsInverse,
                            uiElementName = uiElementName,
                            changeValue = changeValue
                        )

                        ItemRow(
                            items = colorRoleItems.sixthRowLightItems,
                            uiElementName = uiElementName,
                            changeValue = changeValue
                        )

                        ItemRow(
                            items = colorRoleItems.seventhRowLightItems,
                            uiElementName = uiElementName,
                            changeValue = changeValue
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    Modifier.size(maxWidth, maxHeight),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        SolarSet.Moon,
                        contentDescription = "Dark theme colorValue roles",
                        Modifier
                            .size(32.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(verticalArrangement = spacedBy(8.dp)) {
                        ItemRow(
                            items = colorRoleItems.firstRowDarkItems,
                            uiElementName = uiElementName,
                            changeValue = changeValue
                        )

                        ItemRow(
                            items = colorRoleItems.secondRowDarkItems,
                            uiElementName = uiElementName,
                            changeValue = changeValue
                        )

                        ItemRow(
                            items = colorRoleItems.thirdRowDarkItems,
                            uiElementName = uiElementName,
                            changeValue = changeValue
                        )

                        ItemRow(
                            items = colorRoleItems.fourthRowDarkItems,
                            uiElementName = uiElementName,
                            changeValue = changeValue
                        )

                        Box(Modifier.height(136.dp)) // space for shared tokens

                        ItemRow(
                            Modifier.height(64.dp),
                            items = colorRoleItems.fifthRowDarkItems,
                            additionalItems = colorRoleItems.fifthRowDarkItemsInverse,
                            uiElementName = uiElementName,
                            changeValue = changeValue
                        )

                        ItemRow(
                            items = colorRoleItems.sixthRowDarkItems,
                            uiElementName = uiElementName,
                            changeValue = changeValue
                        )

                        ItemRow(
                            items = colorRoleItems.seventhRowDarkItems,
                            uiElementName = uiElementName,
                            changeValue = changeValue
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))
            }

            Column(
                Modifier.padding(top = 240.dp).padding(horizontal = 16.dp),
                verticalArrangement = spacedBy(8.dp)
            ) {
                ItemRow(
                    items = colorRoleItems.firstSharedRowItems,
                    uiElementName = uiElementName,
                    changeValue = changeValue
                )

                ItemRow(
                    items = colorRoleItems.secondSharedRowItems,
                    uiElementName = uiElementName,
                    changeValue = changeValue
                )

                ItemRow(
                    items = colorRoleItems.thirdSharedRowItems,
                    uiElementName = uiElementName,
                    changeValue = changeValue
                )
            }
        }
    }
}

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
    val colorValue = dataAboutColors.colorValue

    Box(
        modifier
            .height(40.dp)
            .fillMaxWidth()
            .clip(CircleShape)
            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
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
                it.fastForEach { item ->
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

private class ColorRoleItems(
    private val colorRolesDark: ColorRoles,
    private val colorRolesLight: ColorRoles,
    private val colorRolesShared: ColorRolesShared
) {
    val firstSharedRowItems = listOf(
        colorRolesShared.primaryFixed,
        colorRolesShared.primaryFixedDim,
        colorRolesShared.secondaryFixed,
        colorRolesShared.secondaryFixedDim,
        colorRolesShared.tertiaryFixed,
        colorRolesShared.tertiaryFixedDim
    )

    val secondSharedRowItems = listOf(
        colorRolesShared.onPrimaryFixed,
        colorRolesShared.onSecondaryFixed,
        colorRolesShared.onTertiaryFixed
    )

    val thirdSharedRowItems = listOf(
        colorRolesShared.onPrimaryFixedVariant,
        colorRolesShared.onSecondaryFixedVariant,
        colorRolesShared.onTertiaryFixedVariant
    )

    val firstRowLightItems = listOf(
        colorRolesLight.primary,
        colorRolesLight.secondary,
        colorRolesLight.tertiary,
        colorRolesLight.error
    )

    val secondRowLightItems = listOf(
        colorRolesLight.onPrimary,
        colorRolesLight.onSecondary,
        colorRolesLight.onTertiary,
        colorRolesLight.onError
    )

    val thirdRowLightItems = listOf(
        colorRolesLight.primaryContainer,
        colorRolesLight.secondaryContainer,
        colorRolesLight.tertiaryContainer,
        colorRolesLight.errorContainer
    )

    val fourthRowLightItems = listOf(
        colorRolesLight.onPrimaryContainer,
        colorRolesLight.onSecondaryContainer,
        colorRolesLight.onTertiaryContainer,
        colorRolesLight.onErrorContainer
    )

    val fifthRowLightItems = listOf(
        colorRolesLight.surfaceDim,
        colorRolesLight.surface,
        colorRolesLight.surfaceBright,
    )

    val fifthRowLightItemsInverse = listOf(
        colorRolesLight.inverseSurface,
        colorRolesLight.inverseOnSurface
    )

    val sixthRowLightItems = listOf(
        colorRolesLight.surfaceContainerLowest,
        colorRolesLight.surfaceContainerLow,
        colorRolesLight.surfaceContainer,
        colorRolesLight.surfaceContainerHigh,
        colorRolesLight.surfaceContainerHighest,
        colorRolesLight.inversePrimary
    )

    val seventhRowLightItems = listOf(
        colorRolesLight.onSurface,
        colorRolesLight.onSurfaceVariant,
        colorRolesLight.outline,
        colorRolesLight.outlineVariant,
        colorRolesLight.scrim,
        colorRolesLight.shadow
    )

    val firstRowDarkItems = listOf(
        colorRolesDark.primary,
        colorRolesDark.secondary,
        colorRolesDark.tertiary,
        colorRolesDark.error
    )

    val secondRowDarkItems = listOf(
        colorRolesDark.onPrimary,
        colorRolesDark.onSecondary,
        colorRolesDark.onTertiary,
        colorRolesDark.onError
    )

    val thirdRowDarkItems = listOf(
        colorRolesDark.primaryContainer,
        colorRolesDark.secondaryContainer,
        colorRolesDark.tertiaryContainer,
        colorRolesDark.errorContainer
    )

    val fourthRowDarkItems = listOf(
        colorRolesDark.onPrimaryContainer,
        colorRolesDark.onSecondaryContainer,
        colorRolesDark.onTertiaryContainer,
        colorRolesDark.onErrorContainer
    )

    val fifthRowDarkItems = listOf(
        colorRolesDark.surfaceDim,
        colorRolesDark.surface,
        colorRolesDark.surfaceBright,
    )

    val fifthRowDarkItemsInverse = listOf(
        colorRolesDark.inverseSurface,
        colorRolesDark.inverseOnSurface
    )

    val sixthRowDarkItems = listOf(
        colorRolesDark.surfaceContainerLowest,
        colorRolesDark.surfaceContainerLow,
        colorRolesDark.surfaceContainer,
        colorRolesDark.surfaceContainerHigh,
        colorRolesDark.surfaceContainerHighest,
        colorRolesDark.inversePrimary
    )

    val seventhRowDarkItems = listOf(
        colorRolesDark.onSurface,
        colorRolesDark.onSurfaceVariant,
        colorRolesDark.outline,
        colorRolesDark.outlineVariant,
        colorRolesDark.scrim,
        colorRolesDark.shadow
    )
}