package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import com.number869.telemone.ui.theme.FullPaletteList
import com.number869.telemone.ui.theme.Neutral
import com.number869.telemone.ui.theme.NeutralVariant
import com.number869.telemone.ui.theme.Primary
import com.number869.telemone.ui.theme.Secondary
import com.number869.telemone.ui.theme.SolarSet
import com.number869.telemone.ui.theme.Tertiary
import com.number869.telemone.ui.theme.fullPalette

// maybe i should use my overlay lib later
// animations are very much TODO
// TODO organize
@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun PalettePopup(
	currentUiElement: String,
	vm: MainViewModel,
	palette: FullPaletteList,
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
				hidePopup = { hidePopup() },
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
					ColorRolesMenu(vm, palette, currentUiElement)
				}
				
				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.Additional,
					enter = fadeIn(),
					exit = fadeOut()
				) {
					OtherMenu(vm, palette, currentUiElement)
				}

				this@OutlinedCard.AnimatedVisibility(
					visible = currentPopupContentType == PaletteMenuCategories.Primary,
					enter = fadeIn(),
					exit = fadeOut()
				) {
					Column {
						Row(horizontalArrangement = spacedBy(8.dp)) {
							Primary.values().toList().subList(0, 6).forEach { (color, name) ->
								TonalPaletteItem(Modifier.weight(1f), color, name, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							Primary.values().toList().subList(6, 13).forEach { (color, name) ->
								TonalPaletteItem(Modifier.weight(1f), color, name, currentUiElement, vm)
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
							Secondary.values().toList().subList(0, 6).forEach { (color, name) ->
								TonalPaletteItem(Modifier.weight(1f), color, name, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							Secondary.values().toList().subList(6, 13).forEach { (color, name) ->
								TonalPaletteItem(Modifier.weight(1f), color, name, currentUiElement, vm)
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
							Tertiary.values().toList().subList(0, 6).forEach { (color, name) ->
								TonalPaletteItem(Modifier.weight(1f), color, name, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							Tertiary.values().toList().subList(6, 13).forEach { (color, name) ->
								TonalPaletteItem(Modifier.weight(1f), color, name, currentUiElement, vm)
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
							Neutral.values().toList().subList(0, 6).forEach { (color, name) ->
								TonalPaletteItem(Modifier.weight(1f), color, name, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							Neutral.values().toList().subList(6, 13).forEach { (color, name) ->
								TonalPaletteItem(Modifier.weight(1f), color, name, currentUiElement, vm)
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
							NeutralVariant.values().toList().subList(0, 6).forEach { (color, name) ->
								TonalPaletteItem(Modifier.weight(1f), color, name, currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							NeutralVariant.values().toList().subList(6, 13).forEach { (color, name) ->
								TonalPaletteItem(Modifier.weight(1f), color, name, currentUiElement, vm)
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
							palette.blue.toList().subList(0, 6).forEach { (tone, value) ->
								TonalPaletteItem(Modifier.weight(1f), value, "blue_$tone", currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							palette.blue.toList().subList(6, 13).forEach { (tone, value) ->
								TonalPaletteItem(Modifier.weight(1f), value, "blue_$tone", currentUiElement, vm)
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
							palette.green.toList().subList(0, 6).forEach { (tone, value) ->
								TonalPaletteItem(Modifier.weight(1f), value, "green_$tone", currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							palette.green.toList().subList(6, 13).forEach { (tone, value) ->
								TonalPaletteItem(Modifier.weight(1f), value, "green_$tone", currentUiElement, vm)
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
							palette.orange.toList().subList(0, 6).forEach { (tone, value) ->
								TonalPaletteItem(Modifier.weight(1f), value, "orange_$tone", currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							palette.orange.toList().subList(6, 13).forEach { (tone, value) ->
								TonalPaletteItem(Modifier.weight(1f), value, "orange_$tone", currentUiElement, vm)
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
							palette.red.toList().subList(0, 6).forEach { (tone, value) ->
								TonalPaletteItem(Modifier.weight(1f), value, "red_$tone", currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							palette.red.toList().subList(6, 13).forEach { (tone, value) ->
								TonalPaletteItem(Modifier.weight(1f), value, "red_$tone", currentUiElement, vm)
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
							palette.violet.toList().subList(0, 6).forEach { (tone, value) ->
								TonalPaletteItem(Modifier.weight(1f), value, "violet_$tone", currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							palette.violet.toList().subList(6, 13).forEach { (tone, value) ->
								TonalPaletteItem(Modifier.weight(1f), value, "violet_$tone", currentUiElement, vm)
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
							palette.pink.toList().subList(0, 6).forEach { (tone, value) ->
								TonalPaletteItem(Modifier.weight(1f), value, "pink_$tone", currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							palette.pink.toList().subList(6, 13).forEach { (tone, value) ->
								TonalPaletteItem(Modifier.weight(1f), value, "pink_$tone", currentUiElement, vm)
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
							palette.cyan.toList().subList(0, 6).forEach { (tone, value) ->
								TonalPaletteItem(Modifier.weight(1f), value, "cyan_$tone", currentUiElement, vm)
							}
						}

						Spacer(modifier = Modifier.height(8.dp))

						Row(horizontalArrangement = spacedBy(8.dp)) {
							palette.cyan.toList().subList(6, 13).forEach { (tone, value) ->
								TonalPaletteItem(Modifier.weight(1f), value, "cyan_$tone", currentUiElement, vm)
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
	value: Color,
	tone: String,
	key: String,
	vm: MainViewModel
) {
	Box(
		modifier
			.height(80.dp)
			.width(30.dp)
			.clip(CircleShape)
			.background(value)
			.clickable { vm.changeValue(key, value, tone) }
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
private fun ColorRolesMenu(vm: MainViewModel, palette: FullPaletteList, key: String) {
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
			if (it == 0) LightColorRoles(vm, palette, key) else DarkColorRoles(vm, palette, key)
		}
	}
}

@Composable
fun OtherMenu(vm: MainViewModel, palette: FullPaletteList, key: String) {
	Column(verticalArrangement = spacedBy(8.dp)) {
		Row(horizontalArrangement = spacedBy(8.dp)) {
			ColorRoleItem(
				Modifier.weight(1f),
				value = palette.surfaceElevationLevel3Light,
				tone = "surface_elevation_level_3_light",
				key = key,
				vm = vm
			)

			ColorRoleItem(
				Modifier.weight(1f),
				value = palette.surfaceElevationLevel3Dark,
				tone = "surface_elevation_level_3_dark",
				key = key,
				vm = vm
			)
		}
		
		Box(contentAlignment = Alignment.Center) {
			ColorRoleItem(
				value = Color.Transparent,
				tone = "transparent",
				key = key,
				vm = vm
			)
			
			Text(text = "Transparent")
		}
	}
}

@Composable
fun LightColorRoles(vm: MainViewModel, palette: FullPaletteList, key: String) {
	Column(verticalArrangement = spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
		Icon(
			SolarSet.Sun,
			contentDescription = "Light theme color roles",
			Modifier.size(32.dp)
		)

		Spacer(modifier = Modifier.height(8.dp))

		PrimarySecondaryTertiaryErrorLight(vm, palette, key)
		SurfacesLight(vm, palette, key)
		SurfaceContainersLight(vm, palette, key)
		OnSurfacesAndOutlinesLight(vm, palette, key)
	}
}

@Composable
fun DarkColorRoles(vm: MainViewModel, palette: FullPaletteList, key: String) {
	Column(verticalArrangement = spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
		Icon(
			SolarSet.Moon,
			contentDescription = "Light theme color roles",
			Modifier.size(32.dp)
		)

		Spacer(modifier = Modifier.height(8.dp))

		PrimarySecondaryTertiaryErrorDark(vm, palette, key)
		SurfacesDark(vm, palette, key)
		SurfaceContainersDark(vm, palette, key)
		OnSurfacesAndOutlinesDark(vm, palette, key)
	}
}

@Composable
private fun ColorRoleItem(
	modifier: Modifier = Modifier,
	value: Color,
	tone: String,
	key: String,
	vm: MainViewModel
) {
	Box(
		modifier
			.height(40.dp)
			.fillMaxWidth()
			.clip(CircleShape)
			.border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
			.background(value)
			.clickable {
				vm.changeValue(
					key,
					value,
					tone
				)
			}
	)
}

@Composable
private fun PrimarySecondaryTertiaryErrorDark(
	vm: MainViewModel,
	palette: FullPaletteList,
	key: String
) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		Column(
			verticalArrangement = spacedBy(8.dp),
			modifier = Modifier.weight(1f)
		) {
			ColorRoleItem(
				Modifier,
				palette.colorRoles.primaryDark,
				"primary_dark",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onPrimaryDark,
				"on_primary_dark",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.primaryContainerDark,
				"primary_container_dark",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onPrimaryContainerDark,
				"on_primary_container_dark",
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
				palette.colorRoles.secondaryDark,
				"secondary_dark",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onSecondaryDark,
				"on_secondary_dark",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.secondaryContainerDark,
				"secondary_container_dark",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onSecondaryContainerDark,
				"on_secondary_container_dark",
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
				palette.colorRoles.tertiaryDark,
				"tertiary_dark",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onTertiaryDark,
				"on_tertiary_dark",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.tertiaryContainerDark,
				"tertiary_container_dark",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onTertiaryContainerDark,
				"on_tertiary_container_dark",
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
				palette.colorRoles.errorDark,
				"error_dark",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onErrorDark,
				"on_error_dark",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.errorContainerDark,
				"error_container_dark",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onErrorContainerDark,
				"on_error_container_dark",
				key,
				vm
			)
		}
	}
}

@Composable
private fun OnSurfacesAndOutlinesDark(vm: MainViewModel, palette: FullPaletteList, key: String) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.onSurfaceDark,
			tone = "on_surface_dark",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.onSurfaceVariantDark,
			tone = "on_surface_variant_dark",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.outlineDark,
			tone = "outline_dark",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.outlineVariantDark,
			tone = "outline_variant_dark",
			key,
			vm
		)
	}
}

@Composable
private fun SurfaceContainersDark(vm: MainViewModel, palette: FullPaletteList, key: String) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceContainerLowestDark,
			tone = "surface_container_lowest_dark",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceContainerLowDark,
			tone = "surface_container_low_dark",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceContainerDark,
			tone = "surface_container_dark",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceContainerHighDark,
			tone = "surface_container_high_dark",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceContainerHighestDark,
			tone = "surface_container_highest_dark",
			key,
			vm
		)
	}
}

@Composable
private fun SurfacesDark(vm: MainViewModel, palette: FullPaletteList, key: String) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceDimDark,
			tone = "surface_dim_dark",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceDark,
			tone = "surface_dark",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceBrightDark,
			tone = "surface_bright_dark",
			key,
			vm
		)
	}
}

@Composable
private fun PrimarySecondaryTertiaryErrorLight(
	vm: MainViewModel,
	palette: FullPaletteList,
	key: String
) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		Column(
			verticalArrangement = spacedBy(8.dp),
			modifier = Modifier.weight(1f)
		) {
			ColorRoleItem(
				Modifier,
				palette.colorRoles.primaryLight,
				"primary_light",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onPrimaryLight,
				"on_primary_light",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.primaryContainerLight,
				"primary_container_light",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onPrimaryContainerLight,
				"on_primary_container_light",
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
				palette.colorRoles.secondaryLight,
				"secondary_light",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onSecondaryLight,
				"on_secondary_light",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.secondaryContainerLight,
				"secondary_container_light",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onSecondaryContainerLight,
				"on_secondary_container_light",
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
				palette.colorRoles.tertiaryLight,
				"tertiary_light",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onTertiaryLight,
				"on_tertiary_light",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.tertiaryContainerLight,
				"tertiary_container_light",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onTertiaryContainerLight,
				"on_tertiary_container_light",
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
				palette.colorRoles.errorLight,
				"error_light",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onErrorLight,
				"on_error_light",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.errorContainerLight,
				"error_container_light",
				key,
				vm
			)

			ColorRoleItem(
				Modifier,
				palette.colorRoles.onErrorContainerLight,
				"on_error_container_light",
				key,
				vm
			)
		}
	}
}

@Composable
private fun OnSurfacesAndOutlinesLight(vm: MainViewModel, palette: FullPaletteList, key: String) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.onSurfaceLight,
			tone = "on_surface_light",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.onSurfaceVariantLight,
			tone = "on_surface_variant_light",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.outlineLight,
			tone = "outline_light",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.outlineVariantLight,
			tone = "outline_variant_light",
			key,
			vm
		)
	}
}

@Composable
private fun SurfaceContainersLight(vm: MainViewModel, palette: FullPaletteList, key: String) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceContainerLowestLight,
			tone = "surface_container_lowest_light",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceContainerLowLight,
			tone = "surface_container_low_light",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceContainerLight,
			tone = "surface_container_light",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceContainerHighLight,
			tone = "surface_container_high_light",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceContainerHighestLight,
			tone = "surface_container_highest_light",
			key,
			vm
		)
	}
}

@Composable
private fun SurfacesLight(vm: MainViewModel, palette: FullPaletteList, key: String) {
	Row(horizontalArrangement = spacedBy(8.dp)) {
		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceDimLight,
			tone = "surface_dim_light",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceLight,
			tone = "surface_light",
			key,
			vm
		)

		ColorRoleItem(
			Modifier.weight(1f),
			palette.colorRoles.surfaceBrightLight,
			tone = "surface_bright_light",
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
			Neutral -> if (isSystemInDarkTheme()) fullPalette().neutral_60 else fullPalette().neutral_40
			NeutralVariant -> if (isSystemInDarkTheme()) fullPalette().neutralVariant_60 else fullPalette().neutralVariant_40
			Blue -> if (isSystemInDarkTheme()) fullPalette().blue.getValue(60) else fullPalette().blue.getValue(
				40
			)

			Green -> if (isSystemInDarkTheme()) fullPalette().green.getValue(60) else fullPalette().green.getValue(
				40
			)

			Orange -> if (isSystemInDarkTheme()) fullPalette().orange.getValue(60) else fullPalette().orange.getValue(
				40
			)

			Red -> if (isSystemInDarkTheme()) fullPalette().red.getValue(60) else fullPalette().red.getValue(
				40
			)

			Violet -> if (isSystemInDarkTheme()) fullPalette().violet.getValue(60) else fullPalette().violet.getValue(
				40
			)

			Pink -> if (isSystemInDarkTheme()) fullPalette().pink.getValue(60) else fullPalette().pink.getValue(
				40
			)

			Cyan -> if (isSystemInDarkTheme()) fullPalette().cyan.getValue(60) else fullPalette().cyan.getValue(
				40
			)

			else -> Color.Transparent
		}
	}
}