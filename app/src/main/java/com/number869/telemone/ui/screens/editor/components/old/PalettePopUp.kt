package com.number869.telemone.ui.screens.editor.components.old

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.number869.telemone.MainViewModel
import com.number869.telemone.ui.theme.DarkTheme
import com.number869.telemone.ui.theme.FullPaletteList
import com.number869.telemone.ui.theme.LightTheme

@Composable
fun PalettePopup(key: String, vm: MainViewModel, palette: FullPaletteList) {
	Surface(modifier = Modifier) {
		Column {
			Text(text = key)

			Row {
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.primaryLight)
						.clickable { vm.changeValue(key, palette.colorRoles.primaryLight, "primary_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onPrimaryLight)
						.clickable { vm.changeValue(key, palette.colorRoles.onPrimaryLight, "on_primary_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.primaryContainerLight)
						.clickable { vm.changeValue(key, palette.colorRoles.primaryContainerLight, "primary_container_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onPrimaryContainerLight)
						.clickable { vm.changeValue(key, palette.colorRoles.onPrimaryContainerLight, "on_primary_container_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.inversePrimaryLight)
						.clickable { vm.changeValue(key, palette.colorRoles.inversePrimaryLight, "inverse_primary_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.secondaryLight)
						.clickable { vm.changeValue(key, palette.colorRoles.secondaryLight, "secondary_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onSecondaryLight)
						.clickable { vm.changeValue(key, palette.colorRoles.onSecondaryLight, "on_secondary_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.secondaryContainerLight)
						.clickable { vm.changeValue(key, palette.colorRoles.secondaryContainerLight, "secondary_container_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onSecondaryContainerLight)
						.clickable { vm.changeValue(key, palette.colorRoles.onPrimaryContainerLight, "on_secondary_container_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.tertiaryLight)
						.clickable { vm.changeValue(key, palette.colorRoles.tertiaryLight, "tertiary_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onTertiaryLight)
						.clickable { vm.changeValue(key, palette.colorRoles.onTertiaryLight, "on_tertiary_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.tertiaryContainerLight)
						.clickable { vm.changeValue(key, palette.colorRoles.tertiaryContainerLight, "tertiary_container_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onTertiaryContainerLight)
						.clickable { vm.changeValue(key, palette.colorRoles.onTertiaryContainerLight, "on_tertiary_container_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.backgroundLight)
						.clickable { vm.changeValue(key, palette.colorRoles.backgroundLight, "background_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onBackgroundLight)
						.clickable { vm.changeValue(key, palette.colorRoles.onBackgroundLight, "on_background_light") }
				)
			}
			Row {
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.surfaceLight)
						.clickable { vm.changeValue(key, palette.colorRoles.surfaceLight, "surface_light") }
				)

				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onSurfaceLight)
						.clickable { vm.changeValue(key, palette.colorRoles.onSurfaceLight, "on_surface_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.surfaceVariantLight)
						.clickable { vm.changeValue(key, palette.colorRoles.surfaceVariantLight, "surface_variant_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onSurfaceVariantLight)
						.clickable { vm.changeValue(key, palette.colorRoles.onSurfaceVariantLight, "on_surface_variant_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.surfaceTintLight)
						.clickable { vm.changeValue(key, palette.colorRoles.surfaceTintLight, "surface_tint_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.inverseSurfaceLight)
						.clickable { vm.changeValue(key, palette.colorRoles.inverseSurfaceLight, "inverse_surface_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.inverseOnSurfaceLight)
						.clickable { vm.changeValue(key, palette.colorRoles.inverseOnSurfaceLight, "inverse_on_surface_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.errorLight)
						.clickable { vm.changeValue(key, palette.colorRoles.errorLight, "error_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onErrorLight)
						.clickable { vm.changeValue(key, palette.colorRoles.onErrorLight, "on_error_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.errorContainerLight)
						.clickable { vm.changeValue(key, palette.colorRoles.errorContainerLight, "error_container_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onErrorContainerLight)
						.clickable { vm.changeValue(key, palette.colorRoles.onErrorContainerLight, "on_error_container_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.outlineLight)
						.clickable { vm.changeValue(key, palette.colorRoles.outlineLight, "outline_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.outlineVariantLight)
						.clickable { vm.changeValue(key, palette.colorRoles.outlineVariantLight, "outline_variant_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.scrimLight)
						.clickable { vm.changeValue(key, palette.colorRoles.scrimLight, "scrim_light") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.surfaceElevationLevel3Light)
						.clickable { vm.changeValue(key, palette.colorRoles.surfaceElevationLevel3Light, "surface_elevation_level_3_light") }
				)
			}


			Row {
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.primaryDark)
						.clickable { vm.changeValue(key, palette.colorRoles.primaryDark, "primary_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onPrimaryDark)
						.clickable { vm.changeValue(key, palette.colorRoles.onPrimaryDark, "on_primary_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.primaryContainerDark)
						.clickable { vm.changeValue(key, palette.colorRoles.primaryContainerDark, "primary_container_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onPrimaryContainerDark)
						.clickable { vm.changeValue(key, palette.colorRoles.onPrimaryContainerDark, "on_primary_container_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.inversePrimaryDark)
						.clickable { vm.changeValue(key, palette.colorRoles.inversePrimaryDark, "inverse_primary_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.secondaryDark)
						.clickable { vm.changeValue(key, palette.colorRoles.secondaryDark, "secondary_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onSecondaryDark)
						.clickable { vm.changeValue(key, palette.colorRoles.onSecondaryDark, "on_secondary_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.secondaryContainerDark)
						.clickable { vm.changeValue(key, palette.colorRoles.secondaryContainerDark, "secondary_container_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onSecondaryContainerDark)
						.clickable { vm.changeValue(key, palette.colorRoles.onPrimaryContainerDark, "on_secondary_container_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.tertiaryDark)
						.clickable { vm.changeValue(key, palette.colorRoles.tertiaryDark, "tertiary_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onTertiaryDark)
						.clickable { vm.changeValue(key, palette.colorRoles.onTertiaryDark, "on_tertiary_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.tertiaryContainerDark)
						.clickable { vm.changeValue(key, palette.colorRoles.tertiaryContainerDark, "tertiary_container_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onTertiaryContainerDark)
						.clickable { vm.changeValue(key, palette.colorRoles.onTertiaryContainerDark, "on_tertiary_container_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.backgroundDark)
						.clickable { vm.changeValue(key, palette.colorRoles.backgroundDark, "background_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onBackgroundDark)
						.clickable { vm.changeValue(key, palette.colorRoles.onBackgroundDark, "on_background_dark") }
				)
			}
			Row {
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.surfaceDark)
						.clickable { vm.changeValue(key, palette.colorRoles.surfaceDark, "surface_dark") }
				)

				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onSurfaceDark)
						.clickable { vm.changeValue(key, palette.colorRoles.onSurfaceDark, "on_surface_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.surfaceVariantDark)
						.clickable { vm.changeValue(key, palette.colorRoles.surfaceVariantDark, "surface_variant_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onSurfaceVariantDark)
						.clickable { vm.changeValue(key, palette.colorRoles.onSurfaceVariantDark, "on_surface_variant_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.surfaceTintDark)
						.clickable { vm.changeValue(key, palette.colorRoles.surfaceTintDark, "surface_tint_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.inverseSurfaceDark)
						.clickable { vm.changeValue(key, palette.colorRoles.inverseSurfaceDark, "inverse_surface_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.inverseOnSurfaceDark)
						.clickable { vm.changeValue(key, palette.colorRoles.inverseOnSurfaceDark, "inverse_on_surface_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.errorDark)
						.clickable { vm.changeValue(key, palette.colorRoles.errorDark, "error_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onErrorDark)
						.clickable { vm.changeValue(key, palette.colorRoles.onErrorDark, "on_error_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.errorContainerDark)
						.clickable { vm.changeValue(key, palette.colorRoles.errorContainerDark, "error_container_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.onErrorContainerDark)
						.clickable { vm.changeValue(key, palette.colorRoles.onErrorContainerDark, "on_error_container_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.outlineDark)
						.clickable { vm.changeValue(key, palette.colorRoles.outlineDark, "outline_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.outlineVariantDark)
						.clickable { vm.changeValue(key, palette.colorRoles.outlineVariantDark, "outline_variant_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.scrimDark)
						.clickable { vm.changeValue(key, palette.colorRoles.scrimDark, "scrim_dark") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.colorRoles.surfaceElevationLevel3Dark)
						.clickable { vm.changeValue(key, palette.colorRoles.surfaceElevationLevel3Dark, "surface_elevation_level_3_dark") }
				)
			}
			

			// primary
			Row {
				Box(
					Modifier
						.size(26.dp)
						.background(palette.primary_0)
						.clickable { vm.changeValue(key, palette.primary_0, "primary_0") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.primary_10)
						.clickable { vm.changeValue(key, palette.primary_10, "primary_10") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.primary_20)
						.clickable { vm.changeValue(key, palette.primary_20, "primary_20") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.primary_30)
						.clickable { vm.changeValue(key, palette.primary_30, "primary_30") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.primary_40)
						.clickable { vm.changeValue(key, palette.primary_40, "primary_40") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.primary_50)
						.clickable { vm.changeValue(key, palette.primary_50, "primary_50") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.primary_60)
						.clickable { vm.changeValue(key, palette.primary_60, "primary_60") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.primary_70)
						.clickable { vm.changeValue(key, palette.primary_70, "primary_70") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.primary_80)
						.clickable { vm.changeValue(key, palette.primary_80, "primary_80") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.primary_90)
						.clickable { vm.changeValue(key, palette.primary_90, "primary_90") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.primary_95)
						.clickable { vm.changeValue(key, palette.primary_95, "primary_95") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.primary_99)
						.clickable { vm.changeValue(key, palette.primary_99, "primary_99") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.primary_100)
						.clickable { vm.changeValue(key, palette.primary_100, "primary_100") }
				)
			}

			// secondary
			Row {
				Box(
					Modifier
						.size(26.dp)
						.background(palette.secondary_0)
						.clickable { vm.changeValue(key, palette.secondary_0, "secondary_0") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.secondary_10)
						.clickable { vm.changeValue(key, palette.secondary_10, "secondary_10") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.secondary_20)
						.clickable { vm.changeValue(key, palette.secondary_20, "secondary_20") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.secondary_30)
						.clickable { vm.changeValue(key, palette.secondary_30, "secondary_30") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.secondary_40)
						.clickable { vm.changeValue(key, palette.secondary_40, "secondary_40") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.secondary_50)
						.clickable { vm.changeValue(key, palette.secondary_50, "secondary_50") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.secondary_60)
						.clickable { vm.changeValue(key, palette.secondary_60, "secondary_60") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.secondary_70)
						.clickable { vm.changeValue(key, palette.secondary_70, "secondary_70") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.secondary_80)
						.clickable { vm.changeValue(key, palette.secondary_80, "secondary_80") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.secondary_90)
						.clickable { vm.changeValue(key, palette.secondary_90, "secondary_90") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.secondary_95)
						.clickable { vm.changeValue(key, palette.secondary_95, "secondary_95") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.secondary_99)
						.clickable { vm.changeValue(key, palette.secondary_99, "secondary_99") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.secondary_100)
						.clickable { vm.changeValue(key, palette.secondary_100, "secondary_100") }
				)
			}

			// tertiary
			Row {
				Box(
					Modifier
						.size(26.dp)
						.background(palette.tertiary_0)
						.clickable { vm.changeValue(key, palette.tertiary_0, "tertiary_0") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.tertiary_10)
						.clickable { vm.changeValue(key, palette.tertiary_10, "tertiary_10") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.tertiary_20)
						.clickable { vm.changeValue(key, palette.tertiary_20, "tertiary_20") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.tertiary_30)
						.clickable { vm.changeValue(key, palette.tertiary_30, "tertiary_30") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.tertiary_40)
						.clickable { vm.changeValue(key, palette.tertiary_40, "tertiary_40") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.tertiary_50)
						.clickable { vm.changeValue(key, palette.tertiary_50, "tertiary_50") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.tertiary_60)
						.clickable { vm.changeValue(key, palette.tertiary_60, "tertiary_60") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.tertiary_70)
						.clickable { vm.changeValue(key, palette.tertiary_70, "tertiary_70") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.tertiary_80)
						.clickable { vm.changeValue(key, palette.tertiary_80, "tertiary_80") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.tertiary_90)
						.clickable { vm.changeValue(key, palette.tertiary_90, "tertiary_90") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.tertiary_95)
						.clickable { vm.changeValue(key, palette.tertiary_95, "tertiary_95") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.tertiary_99)
						.clickable { vm.changeValue(key, palette.tertiary_99, "tertiary_99") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.tertiary_100)
						.clickable { vm.changeValue(key, palette.tertiary_100, "tertiary_100") }
				)
			}

			// neutral
			Row {
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutral_0)
						.clickable { vm.changeValue(key, palette.neutral_0, "neutral_0") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutral_10)
						.clickable { vm.changeValue(key, palette.neutral_10, "neutral_10") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutral_20)
						.clickable { vm.changeValue(key, palette.neutral_20, "neutral_20") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutral_30)
						.clickable { vm.changeValue(key, palette.neutral_30, "neutral_30") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutral_40)
						.clickable { vm.changeValue(key, palette.neutral_40, "neutral_40") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutral_50)
						.clickable { vm.changeValue(key, palette.neutral_50, "neutral_50") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutral_60)
						.clickable { vm.changeValue(key, palette.neutral_60, "neutral_60") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutral_70)
						.clickable { vm.changeValue(key, palette.neutral_70, "neutral_70") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutral_80)
						.clickable { vm.changeValue(key, palette.neutral_80, "neutral_80") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutral_90)
						.clickable { vm.changeValue(key, palette.neutral_90, "neutral_90") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutral_95)
						.clickable { vm.changeValue(key, palette.neutral_95, "neutral_95") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutral_99)
						.clickable { vm.changeValue(key, palette.neutral_99, "neutral_99") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutral_100)
						.clickable { vm.changeValue(key, palette.neutral_100, "neutral_100") }
				)
			}

			// neutral variant
			Row {
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutralVariant_0)
						.clickable { vm.changeValue(key, palette.neutralVariant_0, "neutral_variant_0") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutralVariant_10)
						.clickable { vm.changeValue(key, palette.neutralVariant_10, "neutral_variant_10") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutralVariant_20)
						.clickable { vm.changeValue(key, palette.neutralVariant_20, "neutral_variant_20") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutralVariant_30)
						.clickable { vm.changeValue(key, palette.neutralVariant_30, "neutral_variant_30") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutralVariant_40)
						.clickable { vm.changeValue(key, palette.neutralVariant_40, "neutral_variant_40") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutralVariant_50)
						.clickable { vm.changeValue(key, palette.neutralVariant_50, "neutral_variant_50") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutralVariant_60)
						.clickable { vm.changeValue(key, palette.neutralVariant_60, "neutral_variant_60") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutralVariant_70)
						.clickable { vm.changeValue(key, palette.neutralVariant_70, "neutral_variant_70") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutralVariant_80)
						.clickable { vm.changeValue(key, palette.neutralVariant_80, "neutral_variant_80") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutralVariant_90)
						.clickable { vm.changeValue(key, palette.neutralVariant_90, "neutral_variant_90") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutralVariant_95)
						.clickable { vm.changeValue(key, palette.neutralVariant_95, "neutral_variant_95") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutralVariant_99)
						.clickable { vm.changeValue(key, palette.neutralVariant_99, "neutral_variant_99") }
				)
				Box(
					Modifier
						.size(26.dp)
						.background(palette.neutralVariant_100)
						.clickable { vm.changeValue(key, palette.neutralVariant_100, "neutral_variant_100") }
				)
			}

			// transparent, surface, etc
			Row {
				LightTheme {
					val backgroundLight = MaterialTheme.colorScheme.background
					Box(
						Modifier
							.size(26.dp)
							.background(backgroundLight)
							.clickable {
								vm.changeValue(
									key,
									backgroundLight,
									"background_light"
								)
							}
					)

					val surfaceLight = MaterialTheme.colorScheme.surface
					Box(
						Modifier
							.size(26.dp)
							.background(surfaceLight)
							.clickable {
								vm.changeValue(
									key,
									surfaceLight,
									"surface_light"
								)
							}
					)

					val surfaceElevationLevel3Light = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
					Box(
						Modifier
							.size(26.dp)
							.background(surfaceElevationLevel3Light)
							.clickable {
								vm.changeValue(
									key,
									surfaceElevationLevel3Light,
									"surface_elevation_level_3_light"
								)
							}
					)
				}

				DarkTheme {
					val backgroundDark = MaterialTheme.colorScheme.background
					Box(
						Modifier
							.size(26.dp)
							.background(backgroundDark)
							.clickable {
								vm.changeValue(
									key,
									backgroundDark,
									"background_dark"
								)
							}
					)

					val surfaceDark = MaterialTheme.colorScheme.surface
					Box(
						Modifier
							.size(26.dp)
							.background(surfaceDark)
							.clickable {
								vm.changeValue(
									key,
									surfaceDark,
									"surface_dark"
								)
							}
					)

					val surfaceElevationLevel3Dark = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
					Box(
						Modifier
							.size(26.dp)
							.background(surfaceElevationLevel3Dark)
							.clickable {
								vm.changeValue(
									key,
									surfaceElevationLevel3Dark,
									"surface_elevation_level_3_dark"
								)
							}
					)
				}

				Text(
					text = "Transparent",
					modifier = Modifier
						.height(26.dp)
						.background(Color.Transparent)
						.clickable { vm.changeValue(key, Color.Transparent, "TRANSPARENT") }
				)
			}

			LazyRow() {
				palette.blue.toList().forEach { (tone, value) ->
					item {
						Box(
							Modifier
								.size(26.dp)
								.background(value)
								.clickable {
									vm.changeValue(
										key,
										value,
										"blue_$tone"
									)
								}
						)
					}
				}
			}

			LazyRow() {
				palette.green.toList().forEach { (tone, value) ->
					item {
						Box(
							Modifier
								.size(26.dp)
								.background(value)
								.clickable {
									vm.changeValue(
										key,
										value,
										"green_$tone"
									)
								}
						)
					}
				}
			}

			LazyRow() {
				palette.orange.toList().forEach { (tone, value) ->
					item {
						Box(
							Modifier
								.size(26.dp)
								.background(value)
								.clickable {
									vm.changeValue(
										key,
										value,
										"orange_$tone"
									)
								}
						)
					}
				}
			}

			LazyRow() {
				palette.red.toList().forEach { (tone, value) ->
					item {
						Box(
							Modifier
								.size(26.dp)
								.background(value)
								.clickable {
									vm.changeValue(
										key,
										value,
										"red_$tone"
									)
								}
						)
					}
				}
			}

			LazyRow() {
				palette.violet.toList().forEach { (tone, value) ->
					item {
						Box(
							Modifier
								.size(26.dp)
								.background(value)
								.clickable {
									vm.changeValue(
										key,
										value,
										"violet_$tone"
									)
								}
						)
					}
				}
			}

			LazyRow() {
				palette.pink.toList().forEach { (tone, value) ->
					item {
						Box(
							Modifier
								.size(26.dp)
								.background(value)
								.clickable {
									vm.changeValue(
										key,
										value,
										"pink_$tone"
									)
								}
						)
					}
				}
			}

			LazyRow() {
				palette.cyan.toList().forEach { (tone, value) ->
					item {
						Box(
							Modifier
								.size(26.dp)
								.background(value)
								.clickable {
									vm.changeValue(
										key,
										value,
										"cyan_$tone"
									)
								}
						)
					}
				}
			}
		}
	}
}