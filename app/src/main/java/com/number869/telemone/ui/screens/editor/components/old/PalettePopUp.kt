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
										"blue_$tone${if (tone != 0) "0" else ""}"
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
										"green_$tone${if (tone != 0) "0" else ""}"
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
										"orange_$tone${if (tone != 0) "0" else ""}"
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
										"red_$tone${if (tone != 0) "0" else ""}"
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
										"violet_$tone${if (tone != 0) "0" else ""}"
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
										"pink_$tone${if (tone != 0) "0" else ""}"
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
										"cyan_$tone${if (tone != 0) "0" else ""}"
									)
								}
						)
					}
				}
			}
		}
	}
}