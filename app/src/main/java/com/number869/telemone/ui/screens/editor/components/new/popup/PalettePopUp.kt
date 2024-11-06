package com.number869.telemone.ui.screens.editor.components.new.popup

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastFilter
import com.number869.telemone.data.UiElementColorData
import com.number869.telemone.shared.utils.CombineSharedTransitionAndAnimatedVisibility
import com.number869.telemone.shared.utils.color
import com.number869.telemone.ui.theme.ColorRolesLight
import com.number869.telemone.ui.theme.ColorRolesShared
import com.number869.telemone.ui.theme.ToneInfo
import com.number869.telemone.ui.theme.blueTones
import com.number869.telemone.ui.theme.cyanTones
import com.number869.telemone.ui.theme.greenTones
import com.number869.telemone.ui.theme.neutralTones
import com.number869.telemone.ui.theme.neutralVariantTones
import com.number869.telemone.ui.theme.orangeTones
import com.number869.telemone.ui.theme.pinkTones
import com.number869.telemone.ui.theme.primaryTones
import com.number869.telemone.ui.theme.redTones
import com.number869.telemone.ui.theme.secondaryTones
import com.number869.telemone.ui.theme.tertiaryTones
import com.number869.telemone.ui.theme.violetTones

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PalettePopup(
    uiElementColorData: UiElementColorData,
    changeValue: (String, String, Color) -> Unit,
    onDismissRequest: () -> Unit
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    var targetContent by rememberSaveable { mutableStateOf(PaletteMenuCategories.Home) }
    val isOnHomePage = targetContent == PaletteMenuCategories.Home

    val primaryTones = primaryTones.filterOutBlackAndWhite()
    val secondaryTones = secondaryTones.filterOutBlackAndWhite()
    val tertiaryTones = tertiaryTones.filterOutBlackAndWhite()
    val neutralTones = neutralTones.filterOutBlackAndWhite()
    val neutralVariantTones = neutralVariantTones.filterOutBlackAndWhite()
    val blueTones = blueTones.filterOutBlackAndWhite()
    val redTones = redTones.filterOutBlackAndWhite()
    val greenTones = greenTones.filterOutBlackAndWhite()
    val orangeTones = orangeTones.filterOutBlackAndWhite()
    val violetTones = violetTones.filterOutBlackAndWhite()
    val pinkTones = pinkTones.filterOutBlackAndWhite()
    val cyanTones = cyanTones.filterOutBlackAndWhite()

    LaunchedEffect(Unit) { visible = true }

    Box(
        Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { visible = false }
            )
    )

    AnimatedVisibility(
        visible = visible,
        enter = expandVertically(),
        exit = shrinkVertically() + fadeOut(tween(150, 100))
    ) {
        DisposableEffect(Unit) { onDispose(onDismissRequest) }

        Box(
            Modifier
                .clickable(false) {} // prevents from clicking through this container
                .widthIn(max = 364.dp)
                .clip(RoundedCornerShape(32.dp))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(32.dp)
                )
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column {
                PalettePopupAppBar(
                    isOnHomePage,
                    visible,
                    uiElementColorData.name,
                    animateColorAsState(uiElementColorData.color),
                    uiElementColorData.colorToken,
                    hidePopup = { visible = false },
                    openHome = { targetContent = PaletteMenuCategories.Home }
                )

                Box(
                    Modifier.padding(
                        top = 8.dp,
                        bottom = 16.dp
                    )
                ) {
                    SharedTransitionLayout {
                        AnimatedContent(
                            targetContent,
                            Modifier.animateContentSize(), // weird jumps without this modifier
                            transitionSpec = {
                                (fadeIn(softSpring) + scaleIn(softSpring, 0.85f)) togetherWith
                                        (fadeOut(softSpring) + scaleOut(softSpring, 0.95f))
                            },
                            label = ""
                        ) { currentContentType ->
                            CombineSharedTransitionAndAnimatedVisibility(this) {
                                when (currentContentType) {
                                    PaletteMenuCategories.Home -> Column(
                                        Modifier
                                            .height(420.dp)
                                            .padding(horizontal = 16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = spacedBy(8.dp)
                                    ) {
                                        Row(
                                            Modifier.weight(1f, false),
                                            horizontalArrangement = spacedBy(8.dp)
                                        ) {
                                            Row(
                                                Modifier.weight(1f, false),
                                                horizontalArrangement = spacedBy(8.dp)
                                            ) {
                                                TonalPaletteCategoryButton(
                                                    Modifier.weight(1f),
                                                    expand = {
                                                        targetContent =
                                                            PaletteMenuCategories.Primary
                                                    },
                                                    label = "Primary",
                                                    listOfColors = primaryTones
                                                )

                                                TonalPaletteCategoryButton(
                                                    Modifier.weight(1f),
                                                    expand = {
                                                        targetContent =
                                                            PaletteMenuCategories.Secondary
                                                    },
                                                    label = "Secondary",
                                                    listOfColors = secondaryTones
                                                )

                                                TonalPaletteCategoryButton(
                                                    Modifier.weight(1f),
                                                    expand = {
                                                        targetContent =
                                                            PaletteMenuCategories.Tertiary
                                                    },
                                                    label = "Tertiary",
                                                    listOfColors = tertiaryTones
                                                )
                                            }
                                        }

                                        Row(
                                            Modifier.weight(1f, false),
                                            horizontalArrangement = spacedBy(8.dp)
                                        ) {
                                            TonalPaletteCategoryButton(
                                                Modifier.weight(1f),
                                                expand = {
                                                    targetContent = PaletteMenuCategories.Neutral
                                                },
                                                label = "Neutral",
                                                listOfColors = neutralTones
                                            )

                                            TonalPaletteCategoryButton(
                                                Modifier.weight(1f),
                                                expand = {
                                                    targetContent =
                                                        PaletteMenuCategories.NeutralVariant
                                                },
                                                label = "Neutral Variant",
                                                listOfColors = neutralVariantTones
                                            )
                                        }

                                        Row(
                                            Modifier.weight(1f, false),
                                            horizontalArrangement = spacedBy(8.dp)
                                        ) {
                                            TonalPaletteCategoryButton(
                                                Modifier.weight(1f),
                                                expand = {
                                                    targetContent = PaletteMenuCategories.Red
                                                },
                                                label = "Red",
                                                listOfColors = redTones
                                            )

                                            TonalPaletteCategoryButton(
                                                Modifier.weight(1f),
                                                expand = {
                                                    targetContent = PaletteMenuCategories.Green
                                                },
                                                label = "Green",
                                                listOfColors = greenTones
                                            )

                                            TonalPaletteCategoryButton(
                                                Modifier.weight(1f),
                                                expand = {
                                                    targetContent = PaletteMenuCategories.Blue
                                                },
                                                label = "Blue",
                                                listOfColors = blueTones
                                            )
                                        }

                                        Row(
                                            Modifier.weight(1f, false),
                                            horizontalArrangement = spacedBy(8.dp)
                                        ) {
                                            TonalPaletteCategoryButton(
                                                Modifier.weight(1f),
                                                expand = {
                                                    targetContent = PaletteMenuCategories.Orange
                                                },
                                                label = "Orange",
                                                listOfColors = orangeTones
                                            )

                                            TonalPaletteCategoryButton(
                                                Modifier.weight(1f),
                                                expand = {
                                                    targetContent = PaletteMenuCategories.Violet
                                                },
                                                label = "Violet",
                                                listOfColors = violetTones
                                            )

                                            TonalPaletteCategoryButton(
                                                Modifier.weight(1f),
                                                expand = {
                                                    targetContent = PaletteMenuCategories.Pink
                                                },
                                                label = "Pink",
                                                listOfColors = pinkTones
                                            )

                                            TonalPaletteCategoryButton(
                                                Modifier.weight(1f),
                                                expand = {
                                                    targetContent = PaletteMenuCategories.Cyan
                                                },
                                                label = "Cyan",
                                                listOfColors = cyanTones
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Row(Modifier.weight(1f, false)) {
                                            val allColors = remember {
                                                ColorRolesLight.entries.map { it.dataAboutColors } +
                                                        ColorRolesShared.entries.map { it.dataAboutColors }
                                            }

                                            AnimatedVisibility(
                                                targetContent != PaletteMenuCategories.ColorRoles,
                                                enter = EnterTransition.None,
                                                exit = ExitTransition.None,
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                ColorRolesCategoryButton(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .sharedBounds(
                                                            rememberSharedContentState(
                                                                PaletteMenuCategories.ColorRoles
                                                            ),
                                                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                                                        ),
                                                    expand = {
                                                        targetContent =
                                                            PaletteMenuCategories.ColorRoles
                                                    },
                                                    isOnHomePage = isOnHomePage,
                                                    label = "Color Roles",
                                                    enabled =
                                                    targetContent != PaletteMenuCategories.ColorRoles,
                                                    listOfColors = allColors,
                                                    changeValue = changeValue,
                                                    key = uiElementColorData.name
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(8.dp))

                                            AnimatedVisibility(
                                                targetContent != PaletteMenuCategories.AdditionalColors,
                                                enter = EnterTransition.None,
                                                exit = ExitTransition.None
                                            ) {
                                                AdditionalColorsCategoryButton(
                                                    modifier = Modifier
                                                        .sharedBounds(
                                                            rememberSharedContentState("additionalSection"),
                                                            resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds
                                                        )
                                                        .width(44.dp),
                                                    expand = {
                                                        targetContent =
                                                            PaletteMenuCategories.AdditionalColors
                                                    },
                                                )
                                            }

                                        }
                                    }

                                    PaletteMenuCategories.ColorRoles ->
                                        ColorRolesContent(
                                            Modifier
                                                .sharedBounds(
                                                    rememberSharedContentState(PaletteMenuCategories.ColorRoles),
                                                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(
                                                        ContentScale.FillBounds
                                                    )
                                                ),
                                            changeValue,
                                            uiElementName = uiElementColorData.name
                                        )


                                    PaletteMenuCategories.AdditionalColors -> AdditionalColorsContent(
                                        Modifier
                                            .sharedBounds(
                                                rememberSharedContentState("additionalSection"),
                                                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(
                                                    ContentScale.FillBounds
                                                )
                                            )
                                            .padding(horizontal = 16.dp),
                                        changeValue = changeValue,
                                        uiElementName = uiElementColorData.name
                                    )

                                    else -> {
                                        val palette = when (currentContentType) {
                                            PaletteMenuCategories.Primary -> primaryTones
                                            PaletteMenuCategories.Secondary -> secondaryTones
                                            PaletteMenuCategories.Tertiary -> tertiaryTones
                                            PaletteMenuCategories.Neutral -> neutralTones
                                            PaletteMenuCategories.NeutralVariant -> neutralVariantTones
                                            PaletteMenuCategories.Blue -> blueTones
                                            PaletteMenuCategories.Red -> redTones
                                            PaletteMenuCategories.Green -> greenTones
                                            PaletteMenuCategories.Orange -> orangeTones
                                            PaletteMenuCategories.Violet -> violetTones
                                            PaletteMenuCategories.Pink -> pinkTones
                                            PaletteMenuCategories.Cyan -> cyanTones
                                            else -> null
                                        }

                                        if (palette != null) {
                                            TonalPalette(
                                                Modifier.padding(horizontal = 16.dp),
                                                tones = palette,
                                                changeValue = changeValue,
                                                touchActionEnabled = true,
                                                uiElementColorData.name,
                                            )
                                        }
                                    }
                                }
                            }

                            val isSelected = targetContent == currentContentType
                            if (!isSelected && targetContent != PaletteMenuCategories.Home) Spacer(
                                Modifier
                                    .fillMaxSize()
                                    .pointerInput(Unit) { // block input on unselected layers
                                        awaitPointerEventScope {
                                            while (true) {
                                                val event = awaitPointerEvent()
                                                event.changes.forEach { it.consume() }
                                            }
                                        }
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun List<ToneInfo>.filterOutBlackAndWhite() = fastFilter { it.tone != 0 && it.tone != 100 }

private val softSpring = spring<Float>(1.8f, 2500f)
