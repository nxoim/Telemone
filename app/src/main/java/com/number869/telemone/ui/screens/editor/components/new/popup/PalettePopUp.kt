package com.number869.telemone.ui.screens.editor.components.new.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.number869.telemone.data.UiElementColorData
import com.number869.telemone.shared.utils.SharedTransitionScopeProvider
import com.number869.telemone.shared.utils.color
import com.number869.telemone.shared.utils.customBoundaTransform
import com.number869.telemone.ui.theme.ColorRolesLight
import com.number869.telemone.ui.theme.ColorRolesShared
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
    var visible by remember { mutableStateOf(false) }
    var currentContent by rememberSaveable { mutableStateOf(PaletteMenuCategories.Home) }
    val isOnHomePage by remember { derivedStateOf { currentContent == PaletteMenuCategories.Home } }
    val animatedPopupHeight by animateDpAsState(
        when (currentContent) {
            PaletteMenuCategories.Home -> 516.dp
            PaletteMenuCategories.AdditionalColors -> 226.dp
            PaletteMenuCategories.ColorRoles -> 655.dp
            else -> 256.dp
        },
        animationSpec = spring(0.9f, 200f),
        label = ""
    )

    val primaryTones = primaryTones
    val secondaryTones = secondaryTones
    val tertiaryTones = tertiaryTones
    val neutralTones = neutralTones
    val neutralVariantTones = neutralVariantTones
    val blueTones = blueTones
    val redTones = redTones
    val greenTones = greenTones
    val orangeTones = orangeTones
    val violetTones = violetTones
    val pinkTones = pinkTones
    val cyanTones = cyanTones

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
                .height(animatedPopupHeight)
        ) {
            SharedTransitionScopeProvider() {
                Column {
                    PalettePopupAppBar(
                        isOnHomePage,
                        visible,
                        uiElementColorData.name,
                        animateColorAsState(uiElementColorData.color).value,
                        uiElementColorData.colorToken,
                        hidePopup = { visible = false },
                        openHome = { currentContent = PaletteMenuCategories.Home }
                    )

                    Box(
                        Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 16.dp
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = spacedBy(8.dp)
                        ) {
                            Row(
                                Modifier.weight(1f),
                                horizontalArrangement = spacedBy(8.dp)
                            ) {
                                Row(
                                    Modifier.weight(1f),
                                    horizontalArrangement = spacedBy(8.dp)
                                ) {
                                    TonalPaletteCategoryButton(
                                        Modifier.weight(1f),
                                        expand = { currentContent = PaletteMenuCategories.Primary },
                                        enabled = currentContent != PaletteMenuCategories.Primary,
                                        label = "Primary",
                                        isOnHomePage = isOnHomePage,
                                        listOfColors = primaryTones
                                    )

                                    TonalPaletteCategoryButton(
                                        Modifier.weight(1f),
                                        expand = {
                                            currentContent = PaletteMenuCategories.Secondary
                                        },
                                        enabled = currentContent != PaletteMenuCategories.Secondary,
                                        label = "Secondary",
                                        isOnHomePage = isOnHomePage,
                                        listOfColors = secondaryTones
                                    )

                                    TonalPaletteCategoryButton(
                                        Modifier.weight(1f),
                                        expand = {
                                            currentContent = PaletteMenuCategories.Tertiary
                                        },
                                        enabled = currentContent != PaletteMenuCategories.Tertiary,
                                        label = "Tertiary",
                                        isOnHomePage = isOnHomePage,
                                        listOfColors = tertiaryTones
                                    )
                                }
                            }

                            Row(
                                Modifier.weight(1f),
                                horizontalArrangement = spacedBy(8.dp)
                            ) {
                                TonalPaletteCategoryButton(
                                    Modifier.weight(1f),
                                    expand = { currentContent = PaletteMenuCategories.Neutral },
                                    enabled = currentContent != PaletteMenuCategories.Neutral,
                                    label = "Neutral",
                                    isOnHomePage = isOnHomePage,
                                    listOfColors = neutralTones
                                )

                                TonalPaletteCategoryButton(
                                    Modifier.weight(1f),
                                    expand = {
                                        currentContent = PaletteMenuCategories.NeutralVariant
                                    },
                                    enabled = currentContent != PaletteMenuCategories.NeutralVariant,
                                    label = "Neutral Variant",
                                    isOnHomePage = isOnHomePage,
                                    listOfColors = neutralVariantTones
                                )
                            }

                            Row(
                                Modifier.weight(1f),
                                horizontalArrangement = spacedBy(8.dp)
                            ) {
                                TonalPaletteCategoryButton(
                                    Modifier.weight(1f),
                                    expand = { currentContent = PaletteMenuCategories.Red },
                                    enabled = currentContent != PaletteMenuCategories.Red,
                                    label = "Red",
                                    isOnHomePage = isOnHomePage,
                                    listOfColors = redTones
                                )

                                TonalPaletteCategoryButton(
                                    Modifier.weight(1f),
                                    expand = { currentContent = PaletteMenuCategories.Green },
                                    enabled = currentContent != PaletteMenuCategories.Green,
                                    label = "Green",
                                    isOnHomePage = isOnHomePage,
                                    listOfColors = greenTones
                                )

                                TonalPaletteCategoryButton(
                                    Modifier.weight(1f),
                                    expand = { currentContent = PaletteMenuCategories.Blue },
                                    enabled = currentContent != PaletteMenuCategories.Blue,
                                    label = "Blue",
                                    isOnHomePage = isOnHomePage,
                                    listOfColors = blueTones
                                )
                            }

                            Row(
                                Modifier.weight(1f),
                                horizontalArrangement = spacedBy(8.dp)
                            ) {
                                TonalPaletteCategoryButton(
                                    Modifier.weight(1f),
                                    expand = { currentContent = PaletteMenuCategories.Orange },
                                    enabled = currentContent != PaletteMenuCategories.Orange,
                                    label = "Orange",
                                    isOnHomePage = isOnHomePage,
                                    listOfColors = orangeTones
                                )

                                TonalPaletteCategoryButton(
                                    Modifier.weight(1f),
                                    expand = { currentContent = PaletteMenuCategories.Violet },
                                    enabled = currentContent != PaletteMenuCategories.Violet,
                                    label = "Violet",
                                    isOnHomePage = isOnHomePage,
                                    listOfColors = violetTones
                                )

                                TonalPaletteCategoryButton(
                                    Modifier.weight(1f),
                                    expand = { currentContent = PaletteMenuCategories.Pink },
                                    enabled = currentContent != PaletteMenuCategories.Pink,
                                    label = "Pink",
                                    isOnHomePage = isOnHomePage,
                                    listOfColors = pinkTones
                                )

                                TonalPaletteCategoryButton(
                                    Modifier.weight(1f),
                                    expand = { currentContent = PaletteMenuCategories.Cyan },
                                    enabled = currentContent != PaletteMenuCategories.Cyan,
                                    label = "Cyan",
                                    isOnHomePage = isOnHomePage,
                                    listOfColors = cyanTones
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(Modifier.weight(1f)) {
                                val allColors = remember {
                                    ColorRolesLight.entries.map { it.dataAboutColors } +
                                            ColorRolesShared.entries.map { it.dataAboutColors }
                                }

                                AnimatedVisibility(
                                    currentContent != PaletteMenuCategories.ColorRoles,
                                    enter = EnterTransition.None,
                                    exit = ExitTransition.None,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    ColorRolesCategoryButton(
                                        modifier = Modifier
                                            .weight(1f)
                                            .sharedBounds(
                                                rememberSharedContentState(PaletteMenuCategories.ColorRoles),
                                                animatedVisibilityScope = this,
                                                boundsTransform = customBoundaTransform
                                            ),
                                        expand = {
                                            currentContent = PaletteMenuCategories.ColorRoles
                                        },
                                        isOnHomePage = isOnHomePage,
                                        label = "Color Roles",
                                        enabled =
                                        currentContent != PaletteMenuCategories.ColorRoles,
                                        listOfColors = allColors,
                                        changeValue = changeValue,
                                        key = uiElementColorData.name
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                AnimatedVisibility(
                                    currentContent != PaletteMenuCategories.AdditionalColors,
                                    enter = EnterTransition.None,
                                    exit = ExitTransition.None
                                ) {
                                    AdditionalColorsCombinedContainer(
                                        modifier = Modifier
                                            .sharedBounds(
                                                rememberSharedContentState("additionalSection"),
                                                animatedVisibilityScope = this,
                                                boundsTransform = customBoundaTransform,
                                                enter = fadeIn() + scaleInSharedContentToBounds(ContentScale.FillBounds),
                                                exit = fadeOut() + scaleOutSharedContentToBounds(ContentScale.FillBounds),
                                            )
                                            .width(44.dp),
                                        expand = {
                                            currentContent = PaletteMenuCategories.AdditionalColors
                                        },
                                        isOnHomePage = isOnHomePage,
                                        changeValue = changeValue,
                                        chosen = false,
                                        uiElementName = uiElementColorData.name
                                    )
                                }

                            }
                        }

                        // content in expanded state
                        val palette = when (currentContent) {
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

                        palette?.let {
                            TonalPalette(
                                tones = it,
                                changeValue = changeValue,
                                touchActionEnabled = true,
                                uiElementColorData.name,
                            )
                        }

                        this@Column.AnimatedVisibility(
                            currentContent == PaletteMenuCategories.ColorRoles,
                            enter = EnterTransition.None,
                            exit = ExitTransition.None
                        ) {
                            ColorRolesContent(
                                Modifier.sharedBounds(
                                    rememberSharedContentState(PaletteMenuCategories.ColorRoles),
                                    animatedVisibilityScope = this,
                                    boundsTransform = customBoundaTransform,
                                    enter = fadeIn() + scaleInSharedContentToBounds(ContentScale.FillBounds),
                                    exit = fadeOut() + scaleOutSharedContentToBounds(ContentScale.FillBounds),
                                ),
                                changeValue,
                                uiElementName = uiElementColorData.name
                            )
                        }

                        this@Column.AnimatedVisibility(
                            currentContent == PaletteMenuCategories.AdditionalColors,
                            enter = EnterTransition.None,
                            exit = ExitTransition.None
                        ) {
                            AdditionalColorsCombinedContainer(
                                Modifier.sharedBounds(
                                    rememberSharedContentState("additionalSection"),
                                    this,
                                    boundsTransform = customBoundaTransform
                                ),
                                expand = {
                                    currentContent = PaletteMenuCategories.AdditionalColors
                                },
                                isOnHomePage = false,
                                changeValue = changeValue,
                                chosen = true,
                                uiElementName = uiElementColorData.name
                            )
                        }
                    }
                }
            }
        }
    }
}