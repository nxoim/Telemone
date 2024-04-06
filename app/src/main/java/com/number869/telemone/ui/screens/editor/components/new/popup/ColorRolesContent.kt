package com.number869.telemone.ui.screens.editor.components.new.popup

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import com.number869.telemone.shared.utils.sharedElement
import com.number869.telemone.ui.theme.ColorRolesDark
import com.number869.telemone.ui.theme.ColorRolesLight
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
                            FirstRowLight(changeValue, uiElementName)
                            SecondRowLight(changeValue, uiElementName)
                            ThirdRowLight(changeValue, uiElementName)
                            FourthRowLight(changeValue, uiElementName)
                            FifthRowLight(changeValue, uiElementName)
                            SixthRowLight(changeValue, uiElementName)
                            SeventhRowLight(changeValue, uiElementName)
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
                            FirstRowDark(changeValue, uiElementName)
                            SecondRowDark(changeValue, uiElementName)
                            ThirdRowDark(changeValue, uiElementName)
                            FourthRowDark(changeValue, uiElementName)
                            FifthRowDark(changeValue, uiElementName)
                            SixthRowDark(changeValue, uiElementName)
                            SeventhRowDark(changeValue, uiElementName)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
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

    Box(
        modifier
            .sharedElement(colorToken + "colorRoleItem", enabled, renderInOverlay = false)
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

@Composable
private fun FirstRowLight(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Row(horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.PrimaryContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SecondaryContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.TertiaryContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.ErrorContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )
    }
}

@Composable
private fun SecondRowLight(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Row(horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnPrimaryContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnSecondaryContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnTertiaryContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnErrorContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )
    }
}

@Composable
private fun ThirdRowLight(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Row(horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.PrimaryContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SecondaryContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.TertiaryContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.ErrorContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )
    }
}

@Composable
private fun FourthRowLight(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Row(horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnPrimaryContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnSecondaryContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnTertiaryContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnErrorContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )
    }
}

@Composable
private fun FifthRowLight(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Row(horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SurfaceDimLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SurfaceLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SurfaceBrightLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )
    }
}

@Composable
private fun SixthRowLight(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Row(horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SurfaceContainerLowestLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SurfaceContainerLowLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SurfaceContainerLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SurfaceContainerHighLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SurfaceContainerHighestLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )
    }
}

@Composable
private fun SeventhRowLight(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Row(horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnSurfaceLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnSurfaceVariantLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OutlineLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OutlineVariantLight.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )
    }
}
@Composable
private fun FirstRowDark(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Row(horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.PrimaryContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SecondaryContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.TertiaryContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.ErrorContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )
    }
}

@Composable
private fun SecondRowDark(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Row(horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnPrimaryContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnSecondaryContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnTertiaryContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnErrorContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )
    }
}

@Composable
private fun ThirdRowDark(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Row(horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.PrimaryContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SecondaryContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.TertiaryContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.ErrorContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )
    }
}

@Composable
private fun FourthRowDark(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Row(horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnPrimaryContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnSecondaryContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnTertiaryContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnErrorContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )
    }
}

@Composable
private fun FifthRowDark(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Row(horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SurfaceDimDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SurfaceDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SurfaceBrightDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )
    }
}

@Composable
private fun SixthRowDark(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Row(horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SurfaceContainerLowestDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SurfaceContainerLowDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SurfaceContainerDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SurfaceContainerHighDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SurfaceContainerHighestDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )
    }
}

@Composable
private fun SeventhRowDark(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Row(horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnSurfaceDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnSurfaceVariantDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OutlineDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OutlineVariantDark.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )
    }
}