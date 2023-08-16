package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.number869.telemone.MainViewModel
import com.number869.telemone.ui.theme.BlueTones
import com.number869.telemone.ui.theme.ColorRolesDark
import com.number869.telemone.ui.theme.ColorRolesLight
import com.number869.telemone.ui.theme.CyanTones
import com.number869.telemone.ui.theme.DataAboutColors
import com.number869.telemone.ui.theme.GreenTones
import com.number869.telemone.ui.theme.NeutralTones
import com.number869.telemone.ui.theme.NeutralVariantTones
import com.number869.telemone.ui.theme.OrangeTones
import com.number869.telemone.ui.theme.OtherColors
import com.number869.telemone.ui.theme.PinkTones
import com.number869.telemone.ui.theme.PrimaryTones
import com.number869.telemone.ui.theme.RedTones
import com.number869.telemone.ui.theme.SecondaryTones
import com.number869.telemone.ui.theme.SolarSet
import com.number869.telemone.ui.theme.TertiaryTones
import com.number869.telemone.ui.theme.VioletTones

// maybe i should use my overlay lib later
// animations are very much TODO
// TODO organize
@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun PalettePopup(
	currentUiElement: String,
	vm: MainViewModel,
	currentColor: Color,
	currentColorName: String,
	isPopupVisible: Boolean,
	hidePopup: () -> Unit
) {
	AnimatedVisibility(
		visible = isPopupVisible,
		enter = expandVertically(),
		exit = shrinkVertically()
	) {
		var currentPopupContentType by remember { mutableStateOf(PaletteMenuCategories.Home) }
		val isOnHomePage by remember { derivedStateOf { currentPopupContentType == PaletteMenuCategories.Home } }
		val animatedPopupHeight by animateDpAsState(
			when (currentPopupContentType) {
				PaletteMenuCategories.Home -> 608.dp
				PaletteMenuCategories.Additional -> 184.dp
				PaletteMenuCategories.ColorRoles -> 497.dp
				else -> 264.dp
			},
			animationSpec = spring(0.9f, 200f),
			label = ""
		)

		OutlinedCard(
			Modifier
				.height(animatedPopupHeight)
				.widthIn(max = 364.dp),
			shape = RoundedCornerShape(32.dp)
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


			Box(Modifier.padding(16.dp)) {
				// literally why
				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.Home,
					enter = fadeIn(),
					exit = fadeOut()
				) {
					Column {
						LazyVerticalGrid(
							columns = GridCells.Fixed(2),
							verticalArrangement = spacedBy(24.dp),
						) {
							items(
								PaletteMenuCategories.values()
									// hide home, other, and color roles from the grid.
									// color roles button is shown separately, at
									// the bottom of the popup.
									.filter {
										it != PaletteMenuCategories.Home
										&&
										it != PaletteMenuCategories.ColorRoles
										&&
										it != PaletteMenuCategories.Additional
									}
							) { (categoryType, categoryName, categoryColor) ->
								CategoryButton(
									isLarge = false,
									text = categoryName,
									categoryColor = categoryColor,
									onClick = { currentPopupContentType = categoryType }
								)
							}
						}

						Spacer(modifier = Modifier.height(16.dp))

						Row(Modifier.fillMaxWidth()) {
							CategoryButton(
								Modifier.weight(1f, fill = false),
								isLarge = true,
								text = "Color Roles",
								onClick = { currentPopupContentType = PaletteMenuCategories.ColorRoles }
							)
							
							Spacer(modifier = Modifier.width(8.dp))

							Box(
								Modifier
									.size(44.dp, 88.dp)
									.clip(RoundedCornerShape(16.dp))
									.background(MaterialTheme.colorScheme.surfaceContainerHigh)
									.clickable {
										currentPopupContentType = PaletteMenuCategories.Additional
									},
								contentAlignment = Alignment.Center
							) {
								Icon(
									Icons.Filled.MoreVert,
									contentDescription = "Additional Colors"
								)
							}
						}
					}
				}

				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.ColorRoles,
					enter = fadeIn(),
					exit = fadeOut()
				) {
					ColorRolesMenu(vm, currentUiElement)
				}
				
				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.Additional,
					enter = fadeIn(),
					exit = fadeOut()
				) {
					OtherMenu(vm, currentUiElement)
				}

				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.Primary,
					enter = fadeIn(),
					exit = fadeOut()
				) {
					Column {
						Row(horizontalArrangement = spacedBy(8.dp)) {
							PrimaryTones.entries.subList(0, 6).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							PrimaryTones.entries.subList(6, 13).forEach {
								TonalPaletteItem(Modifier.weight(1f),it.dataAboutColors, currentUiElement, vm)
							}
						}
					}
				}

				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.Secondary,
					enter = fadeIn(),
					exit = fadeOut()
				) {
					Column {
						Row(horizontalArrangement = spacedBy(8.dp)) {
							SecondaryTones.entries.subList(0, 6).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							SecondaryTones.entries.subList(6, 13).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}
					}
				}

				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.Tertiary,
					enter = fadeIn(),
					exit = fadeOut()
				) {
					Column {
						Row(horizontalArrangement = spacedBy(8.dp)) {
							TertiaryTones.entries.subList(0, 6).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							TertiaryTones.entries.subList(6, 13).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}
					}
				}

				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.Neutral,
					enter = fadeIn(),
					exit = fadeOut()
				) {
					Column {
						Row(horizontalArrangement = spacedBy(8.dp)) {
							NeutralTones.entries.subList(0, 6).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							NeutralTones.entries.subList(6, 13).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}
					}
				}

				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.NeutralVariant,
					enter = fadeIn(),
					exit = fadeOut()
				) {
					Column {
						Row(horizontalArrangement = spacedBy(8.dp)) {
							NeutralVariantTones.entries.subList(0, 6).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							NeutralVariantTones.entries.subList(6, 13).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}
					}

				}

				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.Blue, enter = fadeIn() + expandVertically(),
					exit = fadeOut()
				) {
					Column {
						Row(horizontalArrangement = spacedBy(8.dp)) {
							BlueTones.entries.subList(0, 6).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							BlueTones.entries.subList(6, 13).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}
					}
				}

				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.Green, enter = fadeIn() + expandVertically(),
					exit = fadeOut()
				) {
					Column {
						Row(horizontalArrangement = spacedBy(8.dp)) {
							GreenTones.entries.toList().subList(0, 6).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							GreenTones.entries.toList().subList(6, 13).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}
					}
				}

				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.Orange, enter = fadeIn() + expandVertically(),
					exit = fadeOut()
				) {
					Column {
						Row(horizontalArrangement = spacedBy(8.dp)) {
							OrangeTones.entries.toList().subList(0, 6).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							OrangeTones.entries.toList().subList(6, 13).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}
					}
				}

				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.Red, enter = fadeIn() + expandVertically(),
					exit = fadeOut()
				) {
					Column {
						Row(horizontalArrangement = spacedBy(8.dp)) {
							RedTones.entries.toList().subList(0, 6).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							RedTones.entries.toList().subList(6, 13).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}
					}
				}

				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.Violet, enter = fadeIn() + expandVertically(),
					exit = fadeOut()
				) {
					Column {
						Row(horizontalArrangement = spacedBy(8.dp)) {
							VioletTones.entries.toList().subList(0, 6).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							VioletTones.entries.toList().subList(6, 13).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}
					}
				}

				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.Pink, enter = fadeIn() + expandVertically(),
					exit = fadeOut()
				) {
					Column {
						Row(horizontalArrangement = spacedBy(8.dp)) {
							PinkTones.entries.toList().subList(0, 6).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							PinkTones.entries.toList().subList(6, 13).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}
					}
				}

				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.Cyan, enter = fadeIn() + expandVertically(),
					exit = fadeOut()
				) {
					Column {
						Row(horizontalArrangement = spacedBy(8.dp)) {
							CyanTones.entries.toList().subList(0, 6).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							CyanTones.entries.toList().subList(6, 13).forEach {
								TonalPaletteItem(Modifier.weight(1f), it.dataAboutColors, currentUiElement, vm)
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
	dataAboutColors: DataAboutColors,
	key: String,
	vm: MainViewModel
) {
	val colorToken = dataAboutColors.colorToken
	val colorValue = dataAboutColors.colorValue()

	Box(
		modifier
			.height(80.dp)
			.width(30.dp)
			.clip(CircleShape)
			.background(colorValue)
			.clickable { vm.changeValue(key, colorToken, colorValue) }
	)
}

@Composable
private fun CategoryButton(
	modifier: Modifier = Modifier,
	isLarge: Boolean = false,
	text: String,
	categoryColor: Color = Color.Transparent,
	onClick: () -> Unit
) {
	if (isLarge) {
		Row(
			modifier
				.clickable { onClick.invoke() }
				.fillMaxWidth()
				.height(88.dp)
				.clip(RoundedCornerShape(18.dp))
				.background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.Center
		) {
			Box(Modifier.width(96.dp)) {
				Box(
					Modifier
						.size(48.dp)
						.clip(CircleShape)
						.background(MaterialTheme.colorScheme.primary)
				)
				Box(
					Modifier
						.size(48.dp)
						.clip(CircleShape)
						.background(MaterialTheme.colorScheme.secondary)
						.align(Alignment.Center)
				)
				Box(
					Modifier
						.size(48.dp)
						.clip(CircleShape)
						.background(MaterialTheme.colorScheme.tertiary)
						.align(Alignment.CenterEnd)
				)
			}

			Spacer(modifier = Modifier.size(12.dp))

			Text(
				text = text,
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(
					MaterialTheme.typography.headlineSmall
				)
			)
		}
	} else {
		Row(
			modifier.clickable { onClick.invoke() },
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(
				Modifier
					.size(48.dp)
					.clip(CircleShape)
					.background(categoryColor)
			)

			Spacer(modifier = Modifier.size(12.dp))

			Text(
				text = text,
				style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)).plus(
					MaterialTheme.typography.bodyLarge
				),
				modifier = Modifier.width(100.dp)
			)
		}
	}
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ColorRolesMenu(vm: MainViewModel, key: String) {
	val colorRolesCategoryPagerState = rememberPagerState(
		initialPage = 0,
		initialPageOffsetFraction = 0f,
		pageCount = { 2 }
	)

	val indicatorHorizontalAlignment by remember {
		derivedStateOf {
			-1f + (colorRolesCategoryPagerState.getOffsetFractionForPage(0) * 2)
		}
	}

	Column {
		Box(contentAlignment = BiasAlignment(indicatorHorizontalAlignment, 0f)) {
			Divider()
			Divider(
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier.fillMaxWidth(0.5f)
			)
		}

		Spacer(modifier = Modifier.height(16.dp))

		HorizontalPager(
			state = colorRolesCategoryPagerState,
			pageSpacing = 16.dp
		){
			if (it == 0) LightColorRoles(vm, key) else DarkColorRoles(vm, key)
		}
	}
}

@Composable
fun OtherMenu(vm: MainViewModel, key: String) {
	Column(verticalArrangement = spacedBy(8.dp)) {
		Row(horizontalArrangement = spacedBy(8.dp)) {
			ColorRoleItem(
				Modifier.weight(1f),
				OtherColors.SurfaceElevationLevel3Light.dataAboutColors,
				key = key,
				vm = vm
			)

			ColorRoleItem(
				Modifier.weight(1f),
				OtherColors.SurfaceElevationLevel3Dark.dataAboutColors,
				key = key,
				vm = vm
			)
		}
		
		Box(contentAlignment = Alignment.Center) {
			ColorRoleItem(
				Modifier,
				OtherColors.Transparent.dataAboutColors,
				key = key,
				vm = vm
			)
			
			Text(text = "Transparent")
		}
	}
}

@Composable
fun LightColorRoles(vm: MainViewModel, key: String) {
	Column(verticalArrangement = spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
		Icon(
			SolarSet.Sun,
			contentDescription = "Light theme color roles",
			Modifier.size(32.dp)
		)

		Spacer(modifier = Modifier.height(8.dp))

		PrimarySecondaryTertiaryErrorLight(vm, key)
		SurfacesLight(vm, key)
		SurfaceContainersLight(vm, key)
		OnSurfacesAndOutlinesLight(vm, key)
	}
}

@Composable
fun DarkColorRoles(vm: MainViewModel, key: String) {
	Column(verticalArrangement = spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
		Icon(
			SolarSet.Moon,
			contentDescription = "Light theme color roles",
			Modifier.size(32.dp)
		)

		Spacer(modifier = Modifier.height(8.dp))

		PrimarySecondaryTertiaryErrorDark(vm, key)
		SurfacesDark(vm, key)
		SurfaceContainersDark(vm, key)
		OnSurfacesAndOutlinesDark(vm, key)
	}
}

@Composable
private fun ColorRoleItem(
	modifier: Modifier = Modifier,
	dataAboutColors: DataAboutColors,
	key: String,
	vm: MainViewModel
) {
	val colorToken = dataAboutColors.colorToken
	val colorValue = dataAboutColors.colorValue()

	Box(
		modifier
			.height(40.dp)
			.fillMaxWidth()
			.clip(CircleShape)
			.border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
			.background(colorValue)
			.clickable {
				vm.changeValue(
					key,
					colorToken,
					colorValue
				)
			}
	)
}

@Composable
private fun PrimarySecondaryTertiaryErrorDark(
	vm: MainViewModel,
	key: String
) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		Column(
			verticalArrangement = spacedBy(8.dp),
			modifier = Modifier.weight(1f)
		) {
			ColorRoleItem(
				Modifier,
				ColorRolesDark.PrimaryDark.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesDark.OnPrimaryDark.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesDark.PrimaryContainerDark.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesDark.OnPrimaryContainerDark.dataAboutColors,
				key,
				vm
			)
		}

		Column(
			verticalArrangement = spacedBy(8.dp),
			modifier = Modifier.weight(1f)
		) {
			ColorRoleItem(
				Modifier,
				ColorRolesDark.SecondaryDark.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesDark.OnSecondaryDark.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesDark.SecondaryContainerDark.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesDark.OnSecondaryContainerDark.dataAboutColors,
				key,
				vm
			)
		}

		Column(
			verticalArrangement = spacedBy(8.dp),
			modifier = Modifier.weight(1f)
		) {
			ColorRoleItem(
				Modifier,
				ColorRolesDark.TertiaryDark.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesDark.OnTertiaryDark.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesDark.TertiaryContainerDark.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesDark.OnTertiaryContainerDark.dataAboutColors,
				key,
				vm
			)
		}

		Column(
			verticalArrangement = spacedBy(8.dp),
			modifier = Modifier.weight(1f)
		) {
			ColorRoleItem(
				Modifier,
				ColorRolesDark.ErrorDark.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesDark.OnErrorDark.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesDark.ErrorContainerDark.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesDark.OnErrorContainerDark.dataAboutColors,
				key,
				vm
			)
		}
	}
}

@Composable
private fun OnSurfacesAndOutlinesDark(vm: MainViewModel, key: String) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesDark.OnSurfaceDark.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesDark.OnSurfaceVariantDark.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesDark.OutlineDark.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesDark.OutlineVariantDark.dataAboutColors,
			key,
			vm
		)
	}
}

@Composable
private fun SurfaceContainersDark(vm: MainViewModel, key: String) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesDark.SurfaceContainerLowestDark.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesDark.SurfaceContainerLowDark.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesDark.SurfaceContainerDark.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesDark.SurfaceContainerHighDark.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesDark.SurfaceContainerHighestDark.dataAboutColors,
			key,
			vm
		)
	}
}

@Composable
private fun SurfacesDark(vm: MainViewModel, key: String) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesDark.SurfaceDimDark.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesDark.SurfaceDark.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesDark.SurfaceBrightDark.dataAboutColors,
			key,
			vm
		)
	}
}

@Composable
private fun PrimarySecondaryTertiaryErrorLight(
	vm: MainViewModel,
	key: String
) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		Column(
			verticalArrangement = spacedBy(8.dp),
			modifier = Modifier.weight(1f)
		) {
			ColorRoleItem(
				Modifier,
				ColorRolesLight.PrimaryLight.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesLight.OnPrimaryLight.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesLight.PrimaryContainerLight.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesLight.OnPrimaryContainerLight.dataAboutColors,
				key,
				vm
			)
		}

		Column(
			verticalArrangement = spacedBy(8.dp),
			modifier = Modifier.weight(1f)
		) {
			ColorRoleItem(
				Modifier,
				ColorRolesLight.SecondaryLight.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesLight.OnSecondaryLight.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesLight.SecondaryContainerLight.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesLight.OnSecondaryContainerLight.dataAboutColors,
				key,
				vm
			)
		}

		Column(
			verticalArrangement = spacedBy(8.dp),
			modifier = Modifier.weight(1f)
		) {
			ColorRoleItem(
				Modifier,
				ColorRolesLight.TertiaryLight.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesLight.OnTertiaryLight.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesLight.TertiaryContainerLight.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesLight.OnTertiaryContainerLight.dataAboutColors,
				key,
				vm
			)
		}

		Column(
			verticalArrangement = spacedBy(8.dp),
			modifier = Modifier.weight(1f)
		) {
			ColorRoleItem(
				Modifier,
				ColorRolesLight.ErrorLight.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesLight.OnErrorLight.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesLight.ErrorContainerLight.dataAboutColors,
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				ColorRolesLight.OnErrorContainerLight.dataAboutColors,
				key,
				vm
			)
		}
	}
}

@Composable
private fun OnSurfacesAndOutlinesLight(vm: MainViewModel, key: String) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesLight.OnSurfaceLight.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesLight.OnSurfaceVariantLight.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesLight.OutlineLight.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesLight.OutlineVariantLight.dataAboutColors,
			key,
			vm
		)
	}
}

@Composable
private fun SurfaceContainersLight(vm: MainViewModel, key: String) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesLight.SurfaceContainerLowestLight.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesLight.SurfaceContainerLowLight.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesLight.SurfaceContainerLight.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesLight.SurfaceContainerHighLight.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesLight.SurfaceContainerHighestLight.dataAboutColors,
			key,
			vm
		)
	}
}

@Composable
private fun SurfacesLight(vm: MainViewModel, key: String) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesLight.SurfaceDimLight.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesLight.SurfaceLight.dataAboutColors,
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			ColorRolesLight.SurfaceBrightLight.dataAboutColors,
			key,
			vm
		)
	}
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
	Green,
	Orange,
	Red,
	Violet,
	Pink,
	Cyan,
	Additional;

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

	@Composable
	operator fun component3(): Color {
		return when (this) {
			Primary -> MaterialTheme.colorScheme.primary
			Secondary -> MaterialTheme.colorScheme.secondary
			Tertiary -> MaterialTheme.colorScheme.tertiary
			Neutral -> if (isSystemInDarkTheme()) NeutralTones.T60.dataAboutColors.colorValue() else NeutralTones.T40.dataAboutColors.colorValue()
			NeutralVariant -> if (isSystemInDarkTheme()) NeutralVariantTones.T60.dataAboutColors.colorValue() else NeutralVariantTones.T40.dataAboutColors.colorValue()
			Blue -> if (isSystemInDarkTheme()) BlueTones.T60.dataAboutColors.colorValue() else BlueTones.T40.dataAboutColors.colorValue()

			Green -> if (isSystemInDarkTheme()) GreenTones.T60.dataAboutColors.colorValue() else GreenTones.T40.dataAboutColors.colorValue()

			Orange -> if (isSystemInDarkTheme()) OrangeTones.T60.dataAboutColors.colorValue() else OrangeTones.T40.dataAboutColors.colorValue()

			Red -> if (isSystemInDarkTheme()) RedTones.T60.dataAboutColors.colorValue() else RedTones.T40.dataAboutColors.colorValue()

			Violet -> if (isSystemInDarkTheme()) VioletTones.T60.dataAboutColors.colorValue() else VioletTones.T40.dataAboutColors.colorValue()

			Pink -> if (isSystemInDarkTheme()) PinkTones.T60.dataAboutColors.colorValue() else PinkTones.T40.dataAboutColors.colorValue()

			Cyan -> if (isSystemInDarkTheme()) CyanTones.T60.dataAboutColors.colorValue() else CyanTones.T40.dataAboutColors.colorValue()

			else -> Color.Transparent
		}
	}
}