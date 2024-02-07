package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import com.number869.decomposite.core.common.viewModel.viewModel
import com.number869.telemone.MainViewModel
import com.number869.telemone.shared.ui.animateMovableContent
import com.number869.telemone.ui.theme.AdditionalColors
import com.number869.telemone.ui.theme.ColorRolesDark
import com.number869.telemone.ui.theme.ColorRolesLight
import com.number869.telemone.ui.theme.DataAboutColors
import com.number869.telemone.ui.theme.SolarSet
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
import kotlinx.coroutines.delay

// ⚠ if anyone knows how to make this pipipopo decently shorter - hmu
@Composable
fun PalettePopup(
	currentUiElement: String,
	currentColor: Color,
	currentColorName: String,
	isPopupVisible: Boolean,
	hidePopup: () -> Unit
) {
	LookaheadScope {
		var currentPopupContentType by remember { mutableStateOf(PaletteMenuCategories.Home) }
		var currentPopupContentTypeAnimated by remember { mutableStateOf(PaletteMenuCategories.Home) }
		val isOnHomePage by remember { derivedStateOf { currentPopupContentType == PaletteMenuCategories.Home } }
		val targetHeight by remember {
			derivedStateOf {
				when (currentPopupContentType) {
					PaletteMenuCategories.Home -> 516.dp
					PaletteMenuCategories.AdditionalColors -> 226.dp
					PaletteMenuCategories.ColorRoles -> 482.dp
					else -> 256.dp
				}
			}
		}
		val animatedPopupHeight by animateDpAsState(
			targetHeight,
			animationSpec = spring(0.9f, 200f),
			label = ""
		)

		@Composable
		fun Modifier.animateTonalItem(lookaheadScope: LookaheadScope) = this.then(
			Modifier.animateMovableContent(
				lookaheadScope,
				offsetAnimationSpec = spring(0.9f, 600f),
				sizeAnimationSpec = spring(0.9f, 200f)
			)
		)

		@Composable
		fun Modifier.animateCategoryButton(lookaheadScope: LookaheadScope) = this.then(
			Modifier.animateMovableContent(
				lookaheadScope,
				offsetAnimationSpec = spring(0.9f, 600f),
				sizeAnimationSpec = if (isOnHomePage) spring() else tween(easing = EaseOutCirc)
			)
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

		val primaryTonesAsListOfMovableContent = remember {
			primaryTones.map {
				movableContentOf {
					val inPreview = it.tone == 40 || it.tone == 50 || it.tone == 60

					TonalPaletteItem(
						Modifier
							.zIndex(if (inPreview) 1f else 0f)
							.animateTonalItem(this),
						toneInfo = it,
						key = currentUiElement,
						currentPopupContentType == PaletteMenuCategories.Primary
					)
				}
			}
		}

		val primaryAsMovableContent = remember {
			movableContentOf {
				SmallAnimatedExpandableCategoryContainer(
					Modifier.animateCategoryButton(this),
					label = "Primary",
					expand = {
						if (isOnHomePage) currentPopupContentType = PaletteMenuCategories.Primary
					},
					listOfColors = { primaryTonesAsListOfMovableContent },
					displayStaticPlaceholder = {
						currentPopupContentTypeAnimated = PaletteMenuCategories.Home
					},
					isOnHomePage = { isOnHomePage }
				) { currentPopupContentType == PaletteMenuCategories.Primary }
			}
		}

		val secondaryTonesAsListOfMovableContent = remember {
			secondaryTones.map {
				movableContentOf {
					val inPreview = it.tone == 40 || it.tone == 50 || it.tone == 60

					TonalPaletteItem(
						Modifier
							.zIndex(if (inPreview) 1f else 0f)
							.animateTonalItem(this),
						toneInfo = it,
						key = currentUiElement,
						currentPopupContentType == PaletteMenuCategories.Secondary
					)
				}
			}
		}

		val secondaryAsMovableContent = remember {
			movableContentOf {
				SmallAnimatedExpandableCategoryContainer(
					Modifier.animateCategoryButton(this),
					expand = {
						if (isOnHomePage) currentPopupContentType = PaletteMenuCategories.Secondary
						currentPopupContentTypeAnimated = PaletteMenuCategories.Secondary
					},
					{
						currentPopupContentTypeAnimated = PaletteMenuCategories.Home
					},
					label = "Secondary",
					listOfColors = { secondaryTonesAsListOfMovableContent },
					isOnHomePage = { isOnHomePage }
				) { currentPopupContentType == PaletteMenuCategories.Secondary }
			}
		}

		val tertiaryTonesAsListOfMovableContent = remember {
			tertiaryTones.map {
				movableContentOf {
					val inPreview = it.tone == 40 || it.tone == 50 || it.tone == 60

					TonalPaletteItem(
						Modifier
							.zIndex(if (inPreview) 1f else 0f)
							.animateTonalItem(this),
						toneInfo = it,
						key = currentUiElement,
						currentPopupContentType == PaletteMenuCategories.Tertiary
					)
				}
			}
		}

		val tertiaryAsMovableContent = remember {
			movableContentOf {
				SmallAnimatedExpandableCategoryContainer(
					Modifier.animateCategoryButton(this),
					expand = {
						if (isOnHomePage) currentPopupContentType = PaletteMenuCategories.Tertiary
						currentPopupContentTypeAnimated = PaletteMenuCategories.Tertiary
					},
					{
						currentPopupContentTypeAnimated = PaletteMenuCategories.Home
					},
					label = "Tertiary",
					listOfColors = { tertiaryTonesAsListOfMovableContent },
					isOnHomePage = { isOnHomePage }
				) { currentPopupContentType == PaletteMenuCategories.Tertiary }
			}
		}

		val neutralTonesAsListOfMovableContent = remember {
			neutralTones.map {
				movableContentOf {
					val inPreview = it.tone == 40 || it.tone == 50 || it.tone == 60

					TonalPaletteItem(
						Modifier
							.zIndex(if (inPreview) 1f else 0f)
							.animateTonalItem(this),
						toneInfo = it,
						key = currentUiElement,
						currentPopupContentType == PaletteMenuCategories.Neutral
					)
				}
			}
		}

		val neutralAsMovableContent = remember {
			movableContentOf {
				SmallAnimatedExpandableCategoryContainer(
					Modifier.animateCategoryButton(this),
					expand = {
						if (isOnHomePage) currentPopupContentType = PaletteMenuCategories.Neutral
						currentPopupContentTypeAnimated = PaletteMenuCategories.Neutral
					},
					{
						currentPopupContentTypeAnimated = PaletteMenuCategories.Home
					},
					label = "Neutral",
					listOfColors = { neutralTonesAsListOfMovableContent },
					isOnHomePage = { isOnHomePage }
				) { currentPopupContentType == PaletteMenuCategories.Neutral }
			}
		}

		val neutralVariantTonesAsListOfMovableContent = remember {
			neutralVariantTones.map {
				movableContentOf {
					val inPreview = it.tone == 40 || it.tone == 50 || it.tone == 60

					TonalPaletteItem(
						Modifier
							.zIndex(if (inPreview) 1f else 0f)
							.animateTonalItem(this),
						toneInfo = it,
						key = currentUiElement,
						currentPopupContentType == PaletteMenuCategories.NeutralVariant
					)
				}
			}
		}

		val neutralVariantAsMovableContent = remember {
			movableContentOf {
				SmallAnimatedExpandableCategoryContainer(
					Modifier.animateCategoryButton(this),
					expand = {
						if (isOnHomePage) currentPopupContentType = PaletteMenuCategories.NeutralVariant
						currentPopupContentTypeAnimated = PaletteMenuCategories.NeutralVariant

					},
					{
						currentPopupContentTypeAnimated = PaletteMenuCategories.Home
					},
					label = "Neutral Variant",
					listOfColors = { neutralVariantTonesAsListOfMovableContent },
					isOnHomePage = { isOnHomePage }
				) { currentPopupContentType == PaletteMenuCategories.NeutralVariant }
			}
		}

		val blueTonesAsListOfMovableContent = remember {
			blueTones.map {
				movableContentOf {
					val inPreview = it.tone == 40 || it.tone == 50 || it.tone == 60

					TonalPaletteItem(
						Modifier
							.zIndex(if (inPreview) 1f else 0f)
							.animateTonalItem(this),
						toneInfo = it,
						key = currentUiElement,
						currentPopupContentType == PaletteMenuCategories.Blue
					)
				}
			}
		}

		val blueAsMovableContent = remember {
			movableContentOf {
				SmallAnimatedExpandableCategoryContainer(
					Modifier.animateCategoryButton(this),
					expand = {
						if (isOnHomePage) currentPopupContentType = PaletteMenuCategories.Blue
						currentPopupContentTypeAnimated = PaletteMenuCategories.Blue
					},
					{
						currentPopupContentTypeAnimated = PaletteMenuCategories.Home
					},
					label = "Blue",
					listOfColors = { blueTonesAsListOfMovableContent },
					isOnHomePage = { isOnHomePage }
				) { currentPopupContentType == PaletteMenuCategories.Blue }
			}
		}

		val redTonesAsListOfMovableContent = remember {
			redTones.map {
				movableContentOf {
					val inPreview = it.tone == 40 || it.tone == 50 || it.tone == 60

					TonalPaletteItem(
						Modifier
							.zIndex(if (inPreview) 1f else 0f)
							.animateTonalItem(this),
						toneInfo = it,
						key = currentUiElement,
						currentPopupContentType == PaletteMenuCategories.Red
					)
				}
			}
		}

		val redAsMovableContent = remember {
			movableContentOf {
				SmallAnimatedExpandableCategoryContainer(
					Modifier.animateCategoryButton(this),
					expand = {
						if (isOnHomePage) currentPopupContentType = PaletteMenuCategories.Red
						currentPopupContentTypeAnimated = PaletteMenuCategories.Red
					},
					{
						currentPopupContentTypeAnimated = PaletteMenuCategories.Home
					},
					label = "Red",
					listOfColors = { redTonesAsListOfMovableContent },
					isOnHomePage = { isOnHomePage }
				) { currentPopupContentType == PaletteMenuCategories.Red }
			}
		}

		val greenTonesAsListOfMovableContent = remember {
			greenTones.map {
				movableContentOf {
					val inPreview = it.tone == 40 || it.tone == 50 || it.tone == 60

					TonalPaletteItem(
						Modifier
							.zIndex(if (inPreview) 1f else 0f)
							.animateTonalItem(this),
						toneInfo = it,
						key = currentUiElement,
						currentPopupContentType == PaletteMenuCategories.Green
					)
				}
			}
		}

		val greenAsMovableContent = remember {
			movableContentOf {
				SmallAnimatedExpandableCategoryContainer(
					Modifier.animateCategoryButton(this),
					expand = {
						if (isOnHomePage) currentPopupContentType = PaletteMenuCategories.Green
						currentPopupContentTypeAnimated = PaletteMenuCategories.Green
					},
					{
						currentPopupContentTypeAnimated = PaletteMenuCategories.Home
					},
					label = "Green",
					listOfColors = { greenTonesAsListOfMovableContent },
					isOnHomePage = { isOnHomePage }
				) { currentPopupContentType == PaletteMenuCategories.Green }
			}
		}

		val orangeTonesAsListOfMovableContent = remember {
			orangeTones.map {
				movableContentOf {
					val inPreview = it.tone == 40 || it.tone == 50 || it.tone == 60

					TonalPaletteItem(
						Modifier
							.zIndex(if (inPreview) 1f else 0f)
							.animateTonalItem(this),
						toneInfo = it,
						key = currentUiElement,
						currentPopupContentType == PaletteMenuCategories.Orange
					)
				}
			}
		}

		val orangeAsMovableContent = remember {
			movableContentOf {
				SmallAnimatedExpandableCategoryContainer(
					Modifier.animateCategoryButton(this),
					expand = {
						if (isOnHomePage) currentPopupContentType = PaletteMenuCategories.Orange
						currentPopupContentTypeAnimated = PaletteMenuCategories.Orange
					},
					{
						currentPopupContentTypeAnimated = PaletteMenuCategories.Home
					},
					label = "Orange",
					listOfColors = { orangeTonesAsListOfMovableContent },
					isOnHomePage = { isOnHomePage }
				) { currentPopupContentType == PaletteMenuCategories.Orange }
			}
		}

		val violetTonesAsListOfMovableContent = remember {
			violetTones.map {
				movableContentOf {
					val inPreview = it.tone == 40 || it.tone == 50 || it.tone == 60

					TonalPaletteItem(
						Modifier
							.zIndex(if (inPreview) 1f else 0f)
							.animateTonalItem(this),
						toneInfo = it,
						key = currentUiElement,
						currentPopupContentType == PaletteMenuCategories.Violet
					)
				}
			}
		}

		val violetAsMovableContent = remember {
			movableContentOf {
				SmallAnimatedExpandableCategoryContainer(
					Modifier.animateCategoryButton(this),
					expand = {
						if (isOnHomePage) currentPopupContentType = PaletteMenuCategories.Violet
						currentPopupContentTypeAnimated = PaletteMenuCategories.Violet
					},
					{
						currentPopupContentTypeAnimated = PaletteMenuCategories.Home
					},
					label = "Violet",
					listOfColors = { violetTonesAsListOfMovableContent },
					isOnHomePage = { isOnHomePage }
				) { currentPopupContentType == PaletteMenuCategories.Violet }
			}
		}

		val pinkTonesAsListOfMovableContent = remember {
			pinkTones.map {
				movableContentOf {
					val inPreview = it.tone == 40 || it.tone == 50 || it.tone == 60

					TonalPaletteItem(
						Modifier
							.zIndex(if (inPreview) 1f else 0f)
							.animateTonalItem(this),
						toneInfo = it,
						key = currentUiElement,
						currentPopupContentType == PaletteMenuCategories.Pink
					)
				}
			}
		}

		val pinkAsMovableContent = remember {
			movableContentOf {
				SmallAnimatedExpandableCategoryContainer(
					Modifier.animateCategoryButton(this),
					expand = {
						if (isOnHomePage) currentPopupContentType = PaletteMenuCategories.Pink
						currentPopupContentTypeAnimated = PaletteMenuCategories.Pink
					},
					{
						currentPopupContentTypeAnimated = PaletteMenuCategories.Home
					},
					label = "Pink",
					listOfColors = { pinkTonesAsListOfMovableContent },
					isOnHomePage = { isOnHomePage }
				) { currentPopupContentType == PaletteMenuCategories.Pink }
			}
		}

		val cyanTonesAsListOfMovableContent = remember {
			cyanTones.map {
				movableContentOf {
					val inPreview = it.tone == 40 || it.tone == 50 || it.tone == 60

					TonalPaletteItem(
						Modifier
							.zIndex(if (inPreview) 1f else 0f)
							.animateTonalItem(this),
						toneInfo = it,
						key = currentUiElement,
						currentPopupContentType == PaletteMenuCategories.Cyan
					)
				}
			}
		}

		val cyanAsMovableContent = remember {
			movableContentOf {
				SmallAnimatedExpandableCategoryContainer(
					Modifier.animateCategoryButton(this),
					expand = {
						if (isOnHomePage) currentPopupContentType = PaletteMenuCategories.Cyan
						currentPopupContentTypeAnimated = PaletteMenuCategories.Cyan
					},
					{
						currentPopupContentTypeAnimated = PaletteMenuCategories.Home
					},
					label = "Cyan",
					listOfColors = { cyanTonesAsListOfMovableContent },
					isOnHomePage = { isOnHomePage }
				) { currentPopupContentType == PaletteMenuCategories.Cyan }
			}
		}

		val firstColorRolesRowLight = remember {
			listOf<@Composable () -> Unit>(
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.PrimaryLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.SecondaryLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.TertiaryLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.ErrorLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				}
			)
		}

		val secondColorRolesRowLight = remember {
			listOf<@Composable () -> Unit>(
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.OnPrimaryLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.OnSecondaryLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.OnTertiaryLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.OnErrorLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				}
			)
		}

		val thirdColorRolesRowLight = remember {
			listOf<@Composable () -> Unit>(
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.PrimaryContainerLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.SecondaryContainerLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.TertiaryContainerLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.ErrorContainerLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				}
			)
		}

		val fourthColorRolesRowLight = remember {
			listOf<@Composable () -> Unit>(
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.OnPrimaryContainerLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.OnSecondaryContainerLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.OnTertiaryContainerLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.OnErrorContainerLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				}
			)
		}

		val fifthColorRolesRowLight = remember {
			listOf<@Composable () -> Unit>(
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.SurfaceDimLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.SurfaceLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.SurfaceBrightLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				}
			)
		}

		val sixthColorRolesRowLight = remember {
			listOf<@Composable () -> Unit>(
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.SurfaceContainerLowestLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.SurfaceContainerLowLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.SurfaceContainerLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.SurfaceContainerHighLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.SurfaceContainerHighestLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				}
			)
		}

		val seventhColorRolesRowLight = remember {
			listOf<@Composable () -> Unit>(
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.OnSurfaceLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.OnSurfaceVariantLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.OutlineLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesLight.OutlineVariantLight.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				}
			)
		}

		val firstColorRolesRowDark = remember {
			listOf<@Composable () -> Unit>(
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.PrimaryDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.SecondaryDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.TertiaryDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.ErrorDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				}
			)
		}

		val secondColorRolesRowDark = remember {
			listOf<@Composable () -> Unit>(
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.OnPrimaryDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.OnSecondaryDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.OnTertiaryDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.OnErrorDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				}
			)
		}

		val thirdColorRolesRowDark = remember {
			listOf<@Composable () -> Unit>(
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.PrimaryContainerDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.SecondaryContainerDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.TertiaryContainerDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.ErrorContainerDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				}
			)
		}

		val fourthColorRolesRowDark = remember {
			listOf<@Composable () -> Unit>(
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.OnPrimaryContainerDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.OnSecondaryContainerDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.OnTertiaryContainerDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.OnErrorContainerDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				}
			)
		}

		val fifthColorRolesRowDark = remember {
			listOf<@Composable () -> Unit>(
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.SurfaceDimDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.SurfaceDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.SurfaceBrightDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				}
			)
		}

		val sixthColorRolesRowDark = remember {
			listOf<@Composable () -> Unit>(
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.SurfaceContainerLowestDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.SurfaceContainerLowDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.SurfaceContainerDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.SurfaceContainerHighDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.SurfaceContainerHighestDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				}
			)
		}

		val seventhColorRolesRowDark = remember {
			listOf<@Composable () -> Unit>(
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.OnSurfaceDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.OnSurfaceVariantDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.OutlineDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				},
				movableContentOf {
					ColorRoleItem(
						Modifier.animateTonalItem(this),
						ColorRolesDark.OutlineVariantDark.dataAboutColors,
						currentUiElement
					) { currentPopupContentType == PaletteMenuCategories.ColorRoles }
				}
			)
		}

		val listOfColorRolesRowsLight = remember {
			listOf(
				firstColorRolesRowLight,
				secondColorRolesRowLight,
				thirdColorRolesRowLight,
				fourthColorRolesRowLight,
				fifthColorRolesRowLight,
				sixthColorRolesRowLight,
				seventhColorRolesRowLight
			)
		}

		val listOfColorRolesRowsDark = remember {
			listOf(
				firstColorRolesRowDark,
				secondColorRolesRowDark,
				thirdColorRolesRowDark,
				fourthColorRolesRowDark,
				fifthColorRolesRowDark,
				sixthColorRolesRowDark,
				seventhColorRolesRowDark
			)
		}

		val colorRolesAsMovableContent = remember {
			movableContentOf {
				ColorRolesAnimatedCategoryContainer(
					Modifier.animateCategoryButton(this),
					label = "Color Roles",
					expand = { if (isOnHomePage) currentPopupContentType = PaletteMenuCategories.ColorRoles },
					isOnHomePage = { isOnHomePage },
					expanded = { currentPopupContentType == PaletteMenuCategories.ColorRoles },
					displayStaticPlaceholder = { currentPopupContentTypeAnimated = PaletteMenuCategories.Home },
					listOfColorRolesRowsLight = listOfColorRolesRowsLight,
					listOfColorRolesRowsDark = listOfColorRolesRowsDark
				)
			}
		}

		val additionalColorsAsMovableContent = remember {
			movableContentOf {
				AdditionalColorsAnimatedCategoryContainer(
					Modifier.animateCategoryButton(this),
					currentUiElement,
					expand = { if (isOnHomePage) currentPopupContentType = PaletteMenuCategories.AdditionalColors },
					isOnHomePage = { isOnHomePage },
					expanded = { currentPopupContentType == PaletteMenuCategories.AdditionalColors },
					displayStaticPlaceholder = { currentPopupContentTypeAnimated = PaletteMenuCategories.Home }
				)
			}
		}

		AnimatedVisibility(
			visible = isPopupVisible,
			enter = expandVertically(),
			exit = shrinkVertically() + fadeOut(tween(150, 100))
		) {
			val animatedPlaceholderAlpha by animateFloatAsState(
				if (isOnHomePage) 1f else 0f,
				label = ""
			)

			Column(
				Modifier
					.clip(RoundedCornerShape(32.dp))
					.border(
						1.dp,
						MaterialTheme.colorScheme.outlineVariant,
						RoundedCornerShape(32.dp)
					)
					.clickable(false) {}
					.height(animatedPopupHeight)
					.widthIn(max = 364.dp)
					.background(MaterialTheme.colorScheme.background),
			) {
				PalettePopupAppBar(
					isOnHomePage,
					isPopupVisible,
					currentUiElement,
					currentColor,
					currentColorName,
					hidePopup = hidePopup,
					openHome = { currentPopupContentType = PaletteMenuCategories.Home }
				)

				Box(
					Modifier.padding(
						start = 16.dp,
						end = 16.dp,
						top = 8.dp,
						bottom = 16.dp
					)
				) {
					when (currentPopupContentType) {
						PaletteMenuCategories.Primary -> primaryAsMovableContent()
						PaletteMenuCategories.Secondary -> secondaryAsMovableContent()
						PaletteMenuCategories.Tertiary -> tertiaryAsMovableContent()
						PaletteMenuCategories.Neutral -> neutralAsMovableContent()
						PaletteMenuCategories.NeutralVariant -> neutralVariantAsMovableContent()
						PaletteMenuCategories.Blue -> blueAsMovableContent()
						PaletteMenuCategories.Red -> redAsMovableContent()
						PaletteMenuCategories.Green -> greenAsMovableContent()
						PaletteMenuCategories.Orange -> orangeAsMovableContent()
						PaletteMenuCategories.Violet -> violetAsMovableContent()
						PaletteMenuCategories.Pink -> pinkAsMovableContent()
						PaletteMenuCategories.Cyan -> cyanAsMovableContent()
						PaletteMenuCategories.ColorRoles -> colorRolesAsMovableContent()
						PaletteMenuCategories.AdditionalColors -> additionalColorsAsMovableContent()
						else -> {}
					}

					Column(
						horizontalAlignment = Alignment.CenterHorizontally,
						verticalArrangement = spacedBy(8.dp)
					) {
						Row(
							Modifier
								.weight(1f)
								.zIndex(
									if (
										currentPopupContentTypeAnimated == PaletteMenuCategories.Primary
										||
										currentPopupContentTypeAnimated == PaletteMenuCategories.Secondary
										||
										currentPopupContentTypeAnimated == PaletteMenuCategories.Tertiary
									)
										1f
									else
										0f
								),
							horizontalArrangement = spacedBy(8.dp)
						) {
							Box(Modifier.weight(1f)) {
								val visibilityAlpha by animateFloatAsState(
									if (currentPopupContentTypeAnimated == PaletteMenuCategories.Primary)
										0f
									else
										animatedPlaceholderAlpha,
									animationSpec = if (isOnHomePage) tween(0) else tween(10),
									label = ""
								)
								SmallStaticExpandableCategoryContainer(
									modifier = Modifier.graphicsLayer { alpha = if(currentPopupContentTypeAnimated == PaletteMenuCategories.Primary) visibilityAlpha else animatedPlaceholderAlpha },
									expand = { currentPopupContentTypeAnimated = PaletteMenuCategories.Primary },
									enabled =  { isOnHomePage },
									listOfColors = primaryTones,
									label = "Primary",
								)

								if (isOnHomePage && currentPopupContentTypeAnimated == PaletteMenuCategories.Primary) primaryAsMovableContent()
							}

							Box(Modifier.weight(1f)) {
								val visibilityAlpha by animateFloatAsState(
									if (currentPopupContentTypeAnimated == PaletteMenuCategories.Secondary)
										0f
									else
										animatedPlaceholderAlpha,
									animationSpec = if (isOnHomePage) tween(0) else tween(10),
									label = ""
								)
								SmallStaticExpandableCategoryContainer(
									modifier = Modifier.graphicsLayer { alpha = if(currentPopupContentTypeAnimated == PaletteMenuCategories.Secondary) visibilityAlpha else animatedPlaceholderAlpha },
									expand = { currentPopupContentTypeAnimated = PaletteMenuCategories.Secondary },
									enabled = { isOnHomePage },
									label = "Secondary",
									listOfColors = secondaryTones,
								)

								if (isOnHomePage && currentPopupContentTypeAnimated == PaletteMenuCategories.Secondary) secondaryAsMovableContent()
							}

							Box(Modifier.weight(1f)) {
								val visibilityAlpha by animateFloatAsState(
									if (currentPopupContentTypeAnimated == PaletteMenuCategories.Tertiary)
										0f
									else
										animatedPlaceholderAlpha,
									animationSpec = if (isOnHomePage) tween(0) else tween(10),
									label = ""
								)
								SmallStaticExpandableCategoryContainer(
									modifier = Modifier.graphicsLayer { alpha = if(currentPopupContentTypeAnimated == PaletteMenuCategories.Tertiary) visibilityAlpha else animatedPlaceholderAlpha },
									expand = { currentPopupContentTypeAnimated = PaletteMenuCategories.Tertiary },
									enabled = { isOnHomePage },
									label = "Tertiary",
									listOfColors = tertiaryTones,
								)

								if (isOnHomePage && currentPopupContentTypeAnimated == PaletteMenuCategories.Tertiary) tertiaryAsMovableContent()
							}
						}

						Row(
							Modifier
								.weight(1f)
								.zIndex(
									if (
										currentPopupContentTypeAnimated == PaletteMenuCategories.Neutral
										||
										currentPopupContentTypeAnimated == PaletteMenuCategories.NeutralVariant
									)
										1f
									else
										0f
								),
							horizontalArrangement = spacedBy(8.dp)
						) {
							Box(Modifier.weight(1f)) {
								val visibilityAlpha by animateFloatAsState(
									if (currentPopupContentTypeAnimated == PaletteMenuCategories.Neutral)
										0f
									else
										animatedPlaceholderAlpha,
									animationSpec = if (isOnHomePage) tween(0) else tween(10),
									label = ""
								)
								SmallStaticExpandableCategoryContainer(
									modifier = Modifier.graphicsLayer { alpha = if(currentPopupContentTypeAnimated == PaletteMenuCategories.Neutral) visibilityAlpha else animatedPlaceholderAlpha },
									expand = { currentPopupContentTypeAnimated = PaletteMenuCategories.Neutral },
									enabled = { isOnHomePage },
									label = "Neutral",
									listOfColors = neutralTones,
								)

								if (isOnHomePage && currentPopupContentTypeAnimated == PaletteMenuCategories.Neutral) neutralAsMovableContent()
							}

							Box(Modifier.weight(1f)) {
								val visibilityAlpha by animateFloatAsState(
									if (currentPopupContentTypeAnimated == PaletteMenuCategories.NeutralVariant)
										0f
									else
										animatedPlaceholderAlpha,
									animationSpec = if (isOnHomePage) tween(0) else tween(10),
									label = ""
								)
								SmallStaticExpandableCategoryContainer(
									modifier = Modifier.graphicsLayer { alpha = if(currentPopupContentTypeAnimated == PaletteMenuCategories.NeutralVariant) visibilityAlpha else animatedPlaceholderAlpha },
									expand = { currentPopupContentTypeAnimated = PaletteMenuCategories.NeutralVariant },
									enabled = { isOnHomePage },
									label = "Neutral Variant",
									listOfColors = neutralVariantTones,
								)

								if (isOnHomePage && currentPopupContentTypeAnimated == PaletteMenuCategories.NeutralVariant) neutralVariantAsMovableContent()
							}
						}

						Row(
							Modifier
								.weight(1f)
								.zIndex(
									if (
										currentPopupContentTypeAnimated == PaletteMenuCategories.Blue
										||
										currentPopupContentTypeAnimated == PaletteMenuCategories.Red
										||
										currentPopupContentTypeAnimated == PaletteMenuCategories.Green
									)
										1f
									else
										0f
								),
							horizontalArrangement = spacedBy(8.dp)
						) {
							Box(Modifier.weight(1f)) {
								val visibilityAlpha by animateFloatAsState(
									if (currentPopupContentTypeAnimated == PaletteMenuCategories.Blue)
										0f
									else
										animatedPlaceholderAlpha,
									animationSpec = if (isOnHomePage) tween(0) else tween(10),
									label = ""
								)
								SmallStaticExpandableCategoryContainer(
									modifier = Modifier.graphicsLayer { alpha = if(currentPopupContentTypeAnimated == PaletteMenuCategories.Blue) visibilityAlpha else animatedPlaceholderAlpha },
									expand = { currentPopupContentTypeAnimated = PaletteMenuCategories.Blue },
									enabled = { isOnHomePage },
									label = "Blue",
									listOfColors = blueTones,
								)

								if (isOnHomePage && currentPopupContentTypeAnimated == PaletteMenuCategories.Blue) blueAsMovableContent()
							}

							Box(Modifier.weight(1f)) {
								val visibilityAlpha by animateFloatAsState(
									if (currentPopupContentTypeAnimated == PaletteMenuCategories.Red)
										0f
									else
										animatedPlaceholderAlpha,
									animationSpec = if (isOnHomePage) tween(0) else tween(10),
									label = ""
								)
								SmallStaticExpandableCategoryContainer(
									modifier = Modifier.graphicsLayer { alpha = if(currentPopupContentTypeAnimated == PaletteMenuCategories.Red) visibilityAlpha else animatedPlaceholderAlpha },
									expand = { currentPopupContentTypeAnimated = PaletteMenuCategories.Red },
									enabled = { isOnHomePage },
									label = "Red",
									listOfColors = redTones,
								)

								if (isOnHomePage && currentPopupContentTypeAnimated == PaletteMenuCategories.Red) redAsMovableContent()
							}

							Box(Modifier.weight(1f)) {
								val visibilityAlpha by animateFloatAsState(
									if (currentPopupContentTypeAnimated == PaletteMenuCategories.Green)
										0f
									else
										animatedPlaceholderAlpha,
									animationSpec = if (isOnHomePage) tween(0) else tween(10),
									label = ""
								)
								SmallStaticExpandableCategoryContainer(
									modifier = Modifier.graphicsLayer { alpha = if(currentPopupContentTypeAnimated == PaletteMenuCategories.Green) visibilityAlpha else animatedPlaceholderAlpha },
									expand = { currentPopupContentTypeAnimated = PaletteMenuCategories.Green },
									enabled = { isOnHomePage },
									label = "Green",
									listOfColors = greenTones
								)

								if (isOnHomePage && currentPopupContentTypeAnimated == PaletteMenuCategories.Green) greenAsMovableContent()
							}
						}

						Row(
							Modifier.weight(1f),
							horizontalArrangement = spacedBy(8.dp)
						) {
							Box(Modifier.weight(1f)) {
								val visibilityAlpha by animateFloatAsState(
									if (currentPopupContentTypeAnimated == PaletteMenuCategories.Orange)
										0f
									else
										animatedPlaceholderAlpha,
									animationSpec = if (isOnHomePage) tween(0) else tween(10),
									label = ""
								)
								SmallStaticExpandableCategoryContainer(
									modifier = Modifier.graphicsLayer { alpha = if(currentPopupContentTypeAnimated == PaletteMenuCategories.Orange) visibilityAlpha else animatedPlaceholderAlpha },
									expand = { currentPopupContentTypeAnimated = PaletteMenuCategories.Orange },
									enabled = { isOnHomePage },
									label = "Orange",
									listOfColors = orangeTones,
								)

								if (isOnHomePage && currentPopupContentTypeAnimated == PaletteMenuCategories.Orange) orangeAsMovableContent()
							}

							Box(Modifier.weight(1f)) {
								val visibilityAlpha by animateFloatAsState(
									if (currentPopupContentTypeAnimated == PaletteMenuCategories.Violet)
										0f
									else
										animatedPlaceholderAlpha,
									animationSpec = if (isOnHomePage) tween(0) else tween(10),
									label = ""
								)
								SmallStaticExpandableCategoryContainer(
									modifier = Modifier.graphicsLayer { alpha = if(currentPopupContentTypeAnimated == PaletteMenuCategories.Violet) visibilityAlpha else animatedPlaceholderAlpha },
									expand = { currentPopupContentTypeAnimated = PaletteMenuCategories.Violet },
									enabled = { isOnHomePage },
									label = "Violet",
									listOfColors = violetTones,
								)

								if (isOnHomePage && currentPopupContentTypeAnimated == PaletteMenuCategories.Violet) violetAsMovableContent()
							}

							Box(Modifier.weight(1f)) {
								val visibilityAlpha by animateFloatAsState(
									if (currentPopupContentTypeAnimated == PaletteMenuCategories.Pink)
										0f
									else
										animatedPlaceholderAlpha,
									animationSpec = if (isOnHomePage) tween(0) else tween(10),
									label = ""
								)
								SmallStaticExpandableCategoryContainer(
									modifier = Modifier.graphicsLayer { alpha = if(currentPopupContentTypeAnimated == PaletteMenuCategories.Pink) visibilityAlpha else animatedPlaceholderAlpha },
									expand = { currentPopupContentTypeAnimated = PaletteMenuCategories.Pink },
									enabled = { isOnHomePage },
									label = "Pink",
									listOfColors = pinkTones,
								)

								if (isOnHomePage && currentPopupContentTypeAnimated == PaletteMenuCategories.Pink) pinkAsMovableContent()
							}

							Box(Modifier.weight(1f)) {
								val visibilityAlpha by animateFloatAsState(
									if (currentPopupContentTypeAnimated == PaletteMenuCategories.Cyan)
										0f
									else
										animatedPlaceholderAlpha,
									animationSpec = if (isOnHomePage) tween(0) else tween(10),
									label = ""
								)
								SmallStaticExpandableCategoryContainer(
									modifier = Modifier.graphicsLayer { alpha = if(currentPopupContentTypeAnimated == PaletteMenuCategories.Cyan) visibilityAlpha else animatedPlaceholderAlpha },
									expand = { currentPopupContentTypeAnimated = PaletteMenuCategories.Cyan },
									enabled = { isOnHomePage },
									label = "Cyan",
									listOfColors = cyanTones,
								)

								if (isOnHomePage && currentPopupContentTypeAnimated == PaletteMenuCategories.Cyan) cyanAsMovableContent()
							}
						}

						Spacer(modifier = Modifier.height(4.dp))

						Row(
							Modifier
								.fillMaxWidth()
								.weight(1f)) {
							Box(Modifier.weight(1f)) {
								val visibilityAlpha by animateFloatAsState(
									if (currentPopupContentTypeAnimated == PaletteMenuCategories.ColorRoles)
										0f
									else
										animatedPlaceholderAlpha,
									animationSpec = if (isOnHomePage) tween(0) else tween(10),
									label = ""
								)
								ColorRolesStaticCategoryContainer(
									modifier = Modifier.graphicsLayer { alpha = if(currentPopupContentTypeAnimated == PaletteMenuCategories.ColorRoles) visibilityAlpha else animatedPlaceholderAlpha },
									expand = { currentPopupContentTypeAnimated = PaletteMenuCategories.ColorRoles },
									isOnHomePage = { isOnHomePage },
									label = "Color Roles",
									listOfColors = listOf(
										Pair(ColorRolesDark.PrimaryDark.name, ColorRolesDark.PrimaryDark.dataAboutColors),
										Pair(ColorRolesDark.SecondaryDark.name, ColorRolesDark.SecondaryDark.dataAboutColors),
										Pair(ColorRolesDark.TertiaryDark.name, ColorRolesDark.TertiaryDark.dataAboutColors)
									),
									key = currentUiElement
								)

								if (isOnHomePage && currentPopupContentTypeAnimated == PaletteMenuCategories.ColorRoles) colorRolesAsMovableContent()
							}

							Spacer(modifier = Modifier.width(8.dp))

							Box(Modifier.width(44.dp)) {
								val visibilityAlpha by animateFloatAsState(
									if (currentPopupContentTypeAnimated == PaletteMenuCategories.AdditionalColors)
										0f
									else
										animatedPlaceholderAlpha,
									animationSpec = if (isOnHomePage) tween(0) else tween(10),
									label = ""
								)
								AdditionalColorsStaticCategoryContainer(
									modifier = Modifier.graphicsLayer { alpha = if(currentPopupContentTypeAnimated == PaletteMenuCategories.AdditionalColors) visibilityAlpha else animatedPlaceholderAlpha },
									expand = { currentPopupContentTypeAnimated = PaletteMenuCategories.AdditionalColors },
									isOnHomePage = { isOnHomePage }
								)

								if (isOnHomePage && currentPopupContentTypeAnimated == PaletteMenuCategories.AdditionalColors) additionalColorsAsMovableContent()
							}
						}
					}
				}
			}
		}
	}
}

@Composable
private fun TonalPaletteItem(
	modifier: Modifier = Modifier,
	toneInfo: ToneInfo,
	key: String,
	enabled: Boolean
) {
	val vm = viewModel<MainViewModel>()

	val size = if (!enabled)
		Size(32f, 24f)
	else
		Size(9999f, 80f)

	Box(
		modifier
			.width(size.width.dp)
			.height(size.height.dp)
			.clip(CircleShape)
			.background(toneInfo.colorValue)
			.let {
				// no, the enabled parameter in the clickable modifier
				// doesn't cut it
				return@let if (enabled) it.clickable {
					vm.changeValue(
						key,
						toneInfo.colorToken,
						toneInfo.colorValue
					)
				} else it
			}
	)
}

@Stable
@Composable
private fun ColorRolesAnimatedCategoryContainer(
	modifier: Modifier,
	expand: () -> Unit,
	label: String,
	isOnHomePage: () -> Boolean,
	displayStaticPlaceholder: () -> Unit,
	expanded: () -> Boolean,
	listOfColorRolesRowsLight: List<List<@Composable () -> Unit>>,
	listOfColorRolesRowsDark: List<List<@Composable () -> Unit>>
) {
	val density = LocalDensity.current

	var allowSwitchingToStatic by remember { mutableStateOf(false) }

	var maxSize by remember { mutableStateOf(Size.Unspecified) }
	val notPagerScrollState = rememberScrollState()

	val indicatorHorizontalAlignment by remember {
		derivedStateOf {
			-1f + ((1 + notPagerScrollState.value.toFloat()) / (1 + notPagerScrollState.maxValue.toFloat())) * 2
		}
	}

	val backgroundAndContentAlpha by animateFloatAsState(
		if (expanded()) 0f else 1f,
		label = ""
	)

	val wholeAlpha by animateFloatAsState(
		if (!isOnHomePage() && !expanded()) 0f else 1f,
		label = ""
	)

	Box(
		modifier
			.onGloballyPositioned { maxSize = it.size.toSize() / density.density }
			.clip(RoundedCornerShape(16.dp))
			.let {
				return@let if (isOnHomePage()) it.clickable { expand() } else it
			}
	) {
		Row(
			Modifier
				.fillMaxSize()
				.graphicsLayer { alpha = wholeAlpha }

				.border(
					1.dp,
					MaterialTheme.colorScheme.outlineVariant.copy(backgroundAndContentAlpha),
					RoundedCornerShape(16.dp)
				),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Center
		) {
			Box(
				Modifier
					.width(72.dp)
					.height(32.dp)) {
				if (!expanded()) {
					listOfColorRolesRowsLight.forEachIndexed { listIndex, rowItems ->
						rowItems.forEachIndexed { itemIndex, item ->
							item()
						}
					}

					listOfColorRolesRowsDark.forEachIndexed { listIndex, rowItems ->
						rowItems.forEachIndexed { itemIndex, item ->
							// if is the first row and the first 3 colors in it, i.e.
							// primary, secondary, and tertiary. error is 4th
							val zIndex = if (listIndex == 0 && itemIndex != 3) 1f else 0f
							val padding = when (itemIndex) {
								1 -> 12.dp
								2 -> 24.dp
								else -> 0.dp
							}

							Box(
								Modifier
									.zIndex(zIndex)
									.padding(start = padding)) {
								item()
							}
						}
					}

					LaunchedEffect(expanded()) {
						if (allowSwitchingToStatic && !expanded()) {
							delay(500)
							displayStaticPlaceholder()
						}
					}
				}
			}

			Spacer(modifier = Modifier.size(12.dp))

			Text(
				text = label,
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
				fontSize = 20.sp,
				fontWeight = FontWeight.W400,
				modifier = Modifier.graphicsLayer { alpha = backgroundAndContentAlpha }
			)
		}

		Column {
			Box(
				Modifier.graphicsLayer { alpha = 1f - backgroundAndContentAlpha },
				contentAlignment = BiasAlignment(indicatorHorizontalAlignment, 0f)
			) {
				HorizontalDivider()
				HorizontalDivider(
					modifier = Modifier.fillMaxWidth(0.5f),
					color = MaterialTheme.colorScheme.onSurface
				)
			}

			Spacer(modifier = Modifier.height(16.dp))

			Row(Modifier.horizontalScroll(notPagerScrollState)) {
				if (maxSize != Size.Unspecified) Box(Modifier.size(maxSize.width.dp, maxSize.height.dp)) {
					// light
					Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
						Icon(
							SolarSet.Sun,
							contentDescription = "Light theme colorValue roles",
							Modifier
								.size(32.dp)
								.graphicsLayer { alpha = 1f - backgroundAndContentAlpha }
						)

						Spacer(modifier = Modifier.height(16.dp))

						if (expanded()) Column(verticalArrangement = spacedBy(8.dp)) {
							listOfColorRolesRowsLight.forEach { rowItems ->
								ColorRolesPaletteRow(listWithMovableContent = rowItems)
							}
						}
					}
				}

				Spacer(modifier = Modifier.width(16.dp))

				if (maxSize != Size.Unspecified) Box(Modifier.size(maxSize.width.dp, maxSize.height.dp)) {
					Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
						Icon(
							SolarSet.Moon,
							contentDescription = "Dark theme colorValue roles",
							Modifier
								.size(32.dp)
								.graphicsLayer { alpha = 1f - backgroundAndContentAlpha }
						)

						Spacer(modifier = Modifier.height(16.dp))

						if (expanded()) Column(verticalArrangement = spacedBy(8.dp)) {
							listOfColorRolesRowsDark.forEachIndexed { index, rowItems ->
								ColorRolesPaletteRow(modifier = Modifier.zIndex(-index.toFloat()), rowItems)
							}
						}
					}
				}
			}
		}
	}

	// as soon as this is rendered - launch animation
	LaunchedEffect(Unit) {
		expand()
		allowSwitchingToStatic = true
	}
}

@Stable
@Composable
private fun ColorRolesStaticCategoryContainer(
	modifier: Modifier,
	expand: () -> Unit,
	isOnHomePage: () -> Boolean,
	label: String,
	listOfColors: List<Pair<String, DataAboutColors>>,
	key: String
) {
	Row(
		modifier
			.fillMaxSize()
			.clip(RoundedCornerShape(16.dp))
			.let { return@let if (isOnHomePage()) it.clickable { expand() } else it }
			.border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center
	) {
		Box(
			Modifier
				.width(72.dp)
				.height(32.dp)) {
			listOfColors.forEachIndexed { index, it ->
				ColorRoleItem(
					Modifier.padding(start = (index * 12).dp),
					dataAboutColors = it.second,
					key = key,
					enabled = { false }
				)
			}
		}

		Spacer(modifier = Modifier.size(12.dp))

		Text(
			text = label,
			style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
			fontSize = 20.sp,
			fontWeight = FontWeight.W400,
		)
	}
}

@Stable
@Composable
private fun SmallAnimatedExpandableCategoryContainer(
	modifier: Modifier = Modifier,
	expand: () -> Unit,
	displayStaticPlaceholder: () -> Unit,
	label: String,
	listOfColors: () -> List<@Composable () -> Unit>,
	isOnHomePage: () -> Boolean,
	chosen: () -> Boolean,
) {
	var allowSwitchingToStatic by remember { mutableStateOf(false) }

	val backgroundAndTextAlpha by animateFloatAsState(
		if (chosen()) 0f else 1f,
		label = ""
	)

	val wholeAlpha by animateFloatAsState(
		if (!isOnHomePage() && !chosen()) 0f else 1f,
		label = ""
	)

	Box(
		modifier
			.graphicsLayer { alpha = wholeAlpha }
			.clip(RoundedCornerShape(16.dp))
			.let {
				return@let if (isOnHomePage()) it.clickable { expand() } else it
			}
			.background(MaterialTheme.colorScheme.surfaceContainerHigh.copy(backgroundAndTextAlpha))
	) {
		if (chosen()) TonalPaletteFromType(listOfColors())

		Column(
			Modifier
				.let { return@let if (chosen()) it else it.clickable { expand() } }
				.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			if (!chosen()) Box(
				Modifier
					.height(24.dp)
					.width(48.dp)
					// zIndex of 1f so the tones are displayed over the
					// label during the animation
					.zIndex(1f)
			) {
				CategoryButtonColorsFromList(listOfColors())
			}

			Spacer(modifier = Modifier.height(10.dp))

			LaunchedEffect(chosen()) {
				if (allowSwitchingToStatic && !chosen()) {
					delay(500)
					displayStaticPlaceholder()
				}
			}

			Text(
				text = label,
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(
					MaterialTheme.typography.labelMedium
				),
				textAlign = TextAlign.Center,
				modifier = Modifier.graphicsLayer { alpha = backgroundAndTextAlpha }
			)
		}
	}

	// as soon as this is rendered - launch animation
	LaunchedEffect(Unit) {
		expand()
		allowSwitchingToStatic = true
	}
}

@Stable
@Composable
private fun SmallStaticExpandableCategoryContainer(
	modifier: Modifier = Modifier,
	expand: () -> Unit,
	enabled: () -> Boolean,
	label: String,
	listOfColors: List<ToneInfo>
) {
	Box(
		modifier
			.clip(RoundedCornerShape(16.dp))
			.background(MaterialTheme.colorScheme.surfaceContainerHigh)
	) {
		Column(
			Modifier
				.let { return@let if (enabled()) it.clickable { expand() } else it }
				.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Box(
				Modifier
					.height(24.dp)
					.width(48.dp)) {
				listOfColors.subList(4, 7).forEachIndexed { index, it ->
					Box(
						modifier
							.padding(start = (index * 8).dp)
							.width(32.dp)
							.height(24.dp)
							.clip(CircleShape)
							.background(it.colorValue)
					)
				}
			}

			Spacer(modifier = Modifier.height(10.dp))

			Text(
				text = label,
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(
					MaterialTheme.typography.labelMedium
				),
				textAlign = TextAlign.Center
			)
		}
	}
}

@Composable
fun AdditionalColorsAnimatedCategoryContainer(
	modifier: Modifier = Modifier,
	key: String,
	expand: () -> Unit,
	isOnHomePage: () -> Boolean,
	expanded: () -> Boolean,
	displayStaticPlaceholder: () -> Unit
) {
	var allowSwitchingToStatic by remember { mutableStateOf(false) }

	val backgroundAndTextAlpha by animateFloatAsState(
		if (expanded()) 0f else 1f,
		label = ""
	)

	val wholeAlpha by animateFloatAsState(
		if (!isOnHomePage() && !expanded()) 0f else 1f,
		label = ""
	)

	Box(
		modifier
			.fillMaxSize()
			.graphicsLayer { alpha = wholeAlpha }
			.border(
				1.dp,
				MaterialTheme.colorScheme.outlineVariant.copy(backgroundAndTextAlpha),
				RoundedCornerShape(16.dp)
			)
			.let {
				return@let if (isOnHomePage())
					it.clickable { expand() }
				else
					it
			},
		contentAlignment = Alignment.Center
	) {
		Icon(
			Icons.Filled.MoreVert,
			contentDescription = "Additional Colors",
			modifier = Modifier.graphicsLayer { alpha  = backgroundAndTextAlpha }
		)

		AnimatedVisibility(
			visible = expanded(),
			enter = fadeIn(),
			exit = fadeOut()
		) {
			Column(verticalArrangement = spacedBy(8.dp)) {
				Row(horizontalArrangement = spacedBy(8.dp)) {
					ColorRoleItem(
						Modifier.weight(1f),
						AdditionalColors.White.dataAboutColors,
						key = key
					) { true }

					ColorRoleItem(
						Modifier.weight(1f),
						AdditionalColors.Black.dataAboutColors,
						key = key
					) { true }
				}
				Row(horizontalArrangement = spacedBy(8.dp)) {
					ColorRoleItem(
						Modifier.weight(1f),
						AdditionalColors.SurfaceElevationLevel3Light.dataAboutColors,
						key = key
					) { true }

					ColorRoleItem(
						Modifier.weight(1f),
						AdditionalColors.SurfaceElevationLevel3Dark.dataAboutColors,
						key = key
					) { true }
				}

				Box(contentAlignment = Alignment.Center) {
					ColorRoleItem(
						Modifier,
						AdditionalColors.Transparent.dataAboutColors,
						key = key
					) { true }

					Text(text = "Transparent")
				}
			}
		}

		LaunchedEffect(expanded()) {
			if (allowSwitchingToStatic && !expanded()) {
				delay(500)
				displayStaticPlaceholder()
			}
		}
	}

	// as soon as this is rendered - launch animation
	LaunchedEffect(Unit) {
		expand()
		allowSwitchingToStatic = true
	}
}

@Composable
fun AdditionalColorsStaticCategoryContainer(
	modifier: Modifier = Modifier,
	expand: () -> Unit,
	isOnHomePage: () -> Boolean
) {
	Box(
		modifier
			.fillMaxSize()
			.border(
				1.dp,
				MaterialTheme.colorScheme.outlineVariant,
				RoundedCornerShape(16.dp)
			)
			.let {
				return@let if (isOnHomePage())
					it.clickable { expand() }
				else
					it
			},
		contentAlignment = Alignment.Center
	) {
		Icon(
			Icons.Filled.MoreVert,
			contentDescription = "Additional Colors"
		)
	}
}

@Composable
private fun ColorRoleItem(
	modifier: Modifier = Modifier,
	dataAboutColors: DataAboutColors,
	key: String,
	enabled: () -> Boolean,
) {
	val vm = viewModel<MainViewModel>()

	val colorToken = dataAboutColors.colorToken
	val colorValue = dataAboutColors.colorValue()

	val outlineAlpha by animateFloatAsState(
		if (enabled()) 1f else 0f, label = ""
	)

	Box(
		modifier
			.height(40.dp)
			.fillMaxWidth()
			.clip(CircleShape)
			.border(1.dp, MaterialTheme.colorScheme.outline.copy(outlineAlpha), CircleShape)
			.background(colorValue)
			.let {
				return@let if (enabled())
					it.clickable { vm.changeValue(key, colorToken, colorValue) }
				else
					it
			}
	)
}

enum class PaletteMenuCategories() {
	Home,
	ColorRoles,
	Primary,
	Secondary,
	Tertiary,
	Neutral,
	NeutralVariant,
	Blue,
	Red,
	Green,
	Orange,
	Violet,
	Pink,
	Cyan,
	AdditionalColors;

	operator fun component1(): PaletteMenuCategories {
		return this
	}

	operator fun component2(): String {
		return when (this) {
			ColorRoles -> "Color Roles"
			NeutralVariant -> "Neutral Variant"
			else -> this.name
		}
	}
}

@Stable
@Composable
fun CategoryButtonColorsFromList(listWithMovableContent: List<@Composable () -> Unit>) {
	listWithMovableContent.subList(1, listWithMovableContent.lastIndex).forEachIndexed { index, content ->
		val padding = if (listWithMovableContent.lastIndex <= 2) when (index) {
			0 -> 0.dp
			1 -> 8.dp
			2 -> 16.dp
			else -> 8.dp
		} else when (index) {
			3 -> 0.dp
			4 -> 8.dp
			5 -> 16.dp
			else -> 8.dp
		}

		val zIndex = if (index == 5 || index == 4 || index == 3) 1f else 0f

		Box(
			Modifier
				.zIndex(zIndex) // putting nice tones on top so it looks nice
				.padding(start = padding)
		) {
			content()
		}
	}
}

@Stable
@Composable
fun TonalPaletteFromType(listWithMovableContent: List<@Composable () -> Unit>) {
	Column {
		Row(horizontalArrangement = spacedBy(8.dp)) {
			listWithMovableContent.subList(1, 6).forEachIndexed { index, content ->
				val zIndex = if (index == 6 || index == 5 || index == 4) 1f else 0f

				Box(
					Modifier
						.weight(1f)
						.zIndex(zIndex)) { content() }
			}
		}

		Spacer(modifier = Modifier.height(8.dp))

		// giving it zIndex of -1f because otherwise the white
		// tonal item shows up in the middle  when the animation starts
		// and it just looks bad, but TODO: the first element of this row is
		// displayed under the last one of the top row at the
		// beginning of the expansion animation, and so it doesnt match
		// the preview
		Row(horizontalArrangement = spacedBy(8.dp), modifier = Modifier.zIndex(-1f)) {
			listWithMovableContent.subList(6, listWithMovableContent.lastIndex).forEachIndexed { index, content ->
				val zIndex = if (index == 0 ) 1f else 0f

				Box(Modifier.weight(1f).zIndex(zIndex)) { content() }
			}
		}
	}
}

@Stable
@Composable
fun ColorRolesPaletteRow(modifier: Modifier = Modifier, listWithMovableContent: List<@Composable () -> Unit>) {
	Row(modifier, horizontalArrangement = spacedBy(8.dp)) {
		listWithMovableContent.forEachIndexed { index, item ->
			val zIndex = when(index) {
				0 -> 0f
				1 -> 1f
				2 -> 2f
				3 -> -1f
				else -> 0f
			}

			Box(
				Modifier
					.weight(1f)
					.zIndex(zIndex)) { item() }
		}
	}
}