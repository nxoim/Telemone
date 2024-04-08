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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
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
import com.number869.telemone.shared.utils.LocalBooleanProvider
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

@Composable
private fun FirstRowLight(
    changeValue: (String, String, Color) -> Unit,
    uiElementName: String
) {
    Row(horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.PrimaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SecondaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.TertiaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.ErrorContainer.dataAboutColors,
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
            dataAboutColors = ColorRolesLight.OnPrimaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnSecondaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnTertiaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnErrorContainer.dataAboutColors,
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
            dataAboutColors = ColorRolesLight.PrimaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SecondaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.TertiaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.ErrorContainer.dataAboutColors,
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
            dataAboutColors = ColorRolesLight.OnPrimaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnSecondaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnTertiaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnErrorContainer.dataAboutColors,
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
    Row(Modifier.height(64.dp), horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            dataAboutColors = ColorRolesLight.SurfaceDim.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            dataAboutColors = ColorRolesLight.Surface.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            dataAboutColors = ColorRolesLight.SurfaceBright.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        Column(
            Modifier
                .weight(1f)
                .height(64.dp)) {
            ColorRoleItem(
                modifier = Modifier.weight(1f),
                dataAboutColors = ColorRolesLight.InverseSurface.dataAboutColors,
                uiElementName = uiElementName,
                changeValue = changeValue,
                enabled = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            ColorRoleItem(
                modifier = Modifier.weight(1f),
                dataAboutColors = ColorRolesLight.InverseOnSurface.dataAboutColors,
                uiElementName = uiElementName,
                changeValue = changeValue,
                enabled = true
            )
        }
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
            dataAboutColors = ColorRolesLight.SurfaceContainerLowest.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SurfaceContainerLow.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SurfaceContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SurfaceContainerHigh.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.SurfaceContainerHighest.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.InversePrimary.dataAboutColors,
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
            dataAboutColors = ColorRolesLight.OnSurface.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OnSurfaceVariant.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.Outline.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.OutlineVariant.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.Scrim.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesLight.Shadow.dataAboutColors,
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
            dataAboutColors = ColorRolesDark.PrimaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SecondaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.TertiaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.ErrorContainer.dataAboutColors,
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
            dataAboutColors = ColorRolesDark.OnPrimaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnSecondaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnTertiaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnErrorContainer.dataAboutColors,
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
            dataAboutColors = ColorRolesDark.PrimaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SecondaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.TertiaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.ErrorContainer.dataAboutColors,
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
            dataAboutColors = ColorRolesDark.OnPrimaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnSecondaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnTertiaryContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnErrorContainer.dataAboutColors,
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
    Row(Modifier.height(64.dp), horizontalArrangement = spacedBy(8.dp)) {
        ColorRoleItem(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            dataAboutColors = ColorRolesDark.SurfaceDim.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            dataAboutColors = ColorRolesDark.Surface.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            dataAboutColors = ColorRolesDark.SurfaceBright.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        Column(
            Modifier
                .weight(1f)
                .height(64.dp)) {
            ColorRoleItem(
                modifier = Modifier.weight(1f),
                dataAboutColors = ColorRolesDark.InverseSurface.dataAboutColors,
                uiElementName = uiElementName,
                changeValue = changeValue,
                enabled = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            ColorRoleItem(
                modifier = Modifier.weight(1f),
                dataAboutColors = ColorRolesDark.InverseOnSurface.dataAboutColors,
                uiElementName = uiElementName,
                changeValue = changeValue,
                enabled = true
            )
        }
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
            dataAboutColors = ColorRolesDark.SurfaceContainerLowest.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SurfaceContainerLow.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SurfaceContainer.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SurfaceContainerHigh.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.SurfaceContainerHighest.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.InversePrimary.dataAboutColors,
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
            dataAboutColors = ColorRolesDark.OnSurface.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OnSurfaceVariant.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.Outline.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.OutlineVariant.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.Scrim.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )

        ColorRoleItem(
            modifier = Modifier.weight(1f),
            dataAboutColors = ColorRolesDark.Shadow.dataAboutColors,
            uiElementName = uiElementName,
            changeValue = changeValue,
            enabled = true
        )
    }
}