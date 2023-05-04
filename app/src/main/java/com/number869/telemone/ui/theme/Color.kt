package com.number869.telemone.ui.theme

import android.R.color.system_accent1_0
import android.R.color.system_accent1_10
import android.R.color.system_accent1_100
import android.R.color.system_accent1_1000
import android.R.color.system_accent1_200
import android.R.color.system_accent1_300
import android.R.color.system_accent1_400
import android.R.color.system_accent1_50
import android.R.color.system_accent1_500
import android.R.color.system_accent1_600
import android.R.color.system_accent1_700
import android.R.color.system_accent1_800
import android.R.color.system_accent1_900
import android.R.color.system_accent2_0
import android.R.color.system_accent2_10
import android.R.color.system_accent2_100
import android.R.color.system_accent2_1000
import android.R.color.system_accent2_200
import android.R.color.system_accent2_300
import android.R.color.system_accent2_400
import android.R.color.system_accent2_50
import android.R.color.system_accent2_500
import android.R.color.system_accent2_600
import android.R.color.system_accent2_700
import android.R.color.system_accent2_800
import android.R.color.system_accent2_900
import android.R.color.system_accent3_0
import android.R.color.system_accent3_10
import android.R.color.system_accent3_100
import android.R.color.system_accent3_1000
import android.R.color.system_accent3_200
import android.R.color.system_accent3_300
import android.R.color.system_accent3_400
import android.R.color.system_accent3_50
import android.R.color.system_accent3_500
import android.R.color.system_accent3_600
import android.R.color.system_accent3_700
import android.R.color.system_accent3_800
import android.R.color.system_accent3_900
import android.R.color.system_neutral1_0
import android.R.color.system_neutral1_10
import android.R.color.system_neutral1_100
import android.R.color.system_neutral1_1000
import android.R.color.system_neutral1_200
import android.R.color.system_neutral1_300
import android.R.color.system_neutral1_400
import android.R.color.system_neutral1_50
import android.R.color.system_neutral1_500
import android.R.color.system_neutral1_600
import android.R.color.system_neutral1_700
import android.R.color.system_neutral1_800
import android.R.color.system_neutral1_900
import android.R.color.system_neutral2_0
import android.R.color.system_neutral2_10
import android.R.color.system_neutral2_100
import android.R.color.system_neutral2_1000
import android.R.color.system_neutral2_200
import android.R.color.system_neutral2_300
import android.R.color.system_neutral2_400
import android.R.color.system_neutral2_50
import android.R.color.system_neutral2_500
import android.R.color.system_neutral2_600
import android.R.color.system_neutral2_700
import android.R.color.system_neutral2_800
import android.R.color.system_neutral2_900
import androidx.annotation.FloatRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.google.android.material.color.MaterialColors.harmonize
import com.smarttoolfactory.extendedcolors.util.ColorUtil
import com.smarttoolfactory.extendedcolors.util.getColorTonesMap


val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// this is used for checking if the a token is present in a theme
// TODO ! UPDATE WHEN ADDING NEW COLOR TOKENS !
val allColorTokensAsList = listOf(
	"primary_0",
	"primary_10",
	"primary_20",
	"primary_30",
	"primary_40",
	"primary_50",
	"primary_60",
	"primary_70",
	"primary_80",
	"primary_90",
	"primary_95",
	"primary_99",
	"primary_100",
	"secondary_0",
	"secondary_10",
	"secondary_20",
	"secondary_30",
	"secondary_40",
	"secondary_50",
	"secondary_60",
	"secondary_70",
	"secondary_80",
	"secondary_90",
	"secondary_95",
	"secondary_99",
	"secondary_100",
	"tertiary_0",
	"tertiary_10",
	"tertiary_20",
	"tertiary_30",
	"tertiary_40",
	"tertiary_50",
	"tertiary_60",
	"tertiary_70",
	"tertiary_80",
	"tertiary_90",
	"tertiary_95",
	"tertiary_99",
	"tertiary_100",
	"neutral_0",
	"neutral_10",
	"neutral_20",
	"neutral_30",
	"neutral_40",
	"neutral_50",
	"neutral_60",
	"neutral_70",
	"neutral_80",
	"neutral_90",
	"neutral_95",
	"neutral_99",
	"neutral_100",
	"neutral_variant_0",
	"neutral_variant_10",
	"neutral_variant_20",
	"neutral_variant_30",
	"neutral_variant_40",
	"neutral_variant_50",
	"neutral_variant_60",
	"neutral_variant_70",
	"neutral_variant_80",
	"neutral_variant_90",
	"neutral_variant_95",
	"neutral_variant_99",
	"neutral_variant_100",
	"background_light",
	"surface_light",
	"surface_elevation_level_3_light",
	"background_dark",
	"surface_dark",
	"surface_elevation_level_3_dark",
	"blue_0",
	"blue_10",
	"blue_20",
	"blue_30",
	"blue_40",
	"blue_50",
	"blue_60",
	"blue_70",
	"blue_80",
	"blue_90",
	"blue_95",
	"blue_99",
	"blue_100",
	"red_0",
	"red_10",
	"red_20",
	"red_30",
	"red_40",
	"red_50",
	"red_60",
	"red_70",
	"red_80",
	"red_90",
	"red_95",
	"red_99",
	"red_100",
	"green_0",
	"green_10",
	"green_20",
	"green_30",
	"green_40",
	"green_50",
	"green_60",
	"green_70",
	"green_80",
	"green_90",
	"green_95",
	"green_99",
	"green_100",
	"orange_0",
	"orange_10",
	"orange_20",
	"orange_30",
	"orange_40",
	"orange_50",
	"orange_60",
	"orange_70",
	"orange_80",
	"orange_90",
	"orange_95",
	"orange_99",
	"orange_100",
	"violet_0",
	"violet_10",
	"violet_20",
	"violet_30",
	"violet_40",
	"violet_50",
	"violet_60",
	"violet_70",
	"violet_80",
	"violet_90",
	"violet_95",
	"violet_99",
	"violet_100",
	"cyan_0",
	"cyan_10",
	"cyan_20",
	"cyan_30",
	"cyan_40",
	"cyan_50",
	"cyan_60",
	"cyan_70",
	"cyan_80",
	"cyan_90",
	"cyan_95",
	"cyan_99",
	"cyan_100",
	"pink_0",
	"pink_10",
	"pink_20",
	"pink_30",
	"pink_40",
	"pink_50",
	"pink_60",
	"pink_70",
	"pink_80",
	"pink_90",
	"pink_95",
	"pink_99",
	"pink_100"
)

data class FullPaletteList(
	val primary_0: Color,
	val primary_10: Color,
	val primary_20: Color,
	val primary_30: Color,
	val primary_40: Color,
	val primary_50: Color,
	val primary_60: Color,
	val primary_70: Color,
	val primary_80: Color,
	val primary_90: Color,
	val primary_95: Color,
	val primary_99: Color,
	val primary_100: Color,
	val secondary_0: Color,
	val secondary_10: Color,
	val secondary_20: Color,
	val secondary_30: Color,
	val secondary_40: Color,
	val secondary_50: Color,
	val secondary_60: Color,
	val secondary_70: Color,
	val secondary_80: Color,
	val secondary_90: Color,
	val secondary_95: Color,
	val secondary_99: Color,
	val secondary_100: Color,
	val tertiary_0: Color,
	val tertiary_10: Color,
	val tertiary_20: Color,
	val tertiary_30: Color,
	val tertiary_40: Color,
	val tertiary_50: Color,
	val tertiary_60: Color,
	val tertiary_70: Color,
	val tertiary_80: Color,
	val tertiary_90: Color,
	val tertiary_95: Color,
	val tertiary_99: Color,
	val tertiary_100: Color,
	val neutral_0: Color,
	val neutral_10: Color,
	val neutral_20: Color,
	val neutral_30: Color,
	val neutral_40: Color,
	val neutral_50: Color,
	val neutral_60: Color,
	val neutral_70: Color,
	val neutral_80: Color,
	val neutral_90: Color,
	val neutral_95: Color,
	val neutral_99: Color,
	val neutral_100: Color,
	val neutralVariant_0: Color,
	val neutralVariant_10: Color,
	val neutralVariant_20: Color,
	val neutralVariant_30: Color,
	val neutralVariant_40: Color,
	val neutralVariant_50: Color,
	val neutralVariant_60: Color,
	val neutralVariant_70: Color,
	val neutralVariant_80: Color,
	val neutralVariant_90: Color,
	val neutralVariant_95: Color,
	val neutralVariant_99: Color,
	val neutralVariant_100: Color,
	val backgroundLight: Color,
	val surfaceLight: Color,
	val surfaceElevationLevel3Light: Color,
	val backgroundDark: Color,
	val surfaceDark: Color,
	val surfaceElevationLevel3Dark: Color,
	val blue: Map<Int, Color>,
	val red: Map<Int, Color>,
	val green: Map<Int, Color>,
	val orange: Map<Int, Color>,
	val violet: Map<Int, Color>,
	val cyan: Map<Int, Color>,
	val pink: Map<Int, Color>
)

@Composable
fun fullPalette(): FullPaletteList {
	var backgroundLight = Color.Red
	var surfaceLight = Color.Red
	var surfaceElevationLevel3Light = Color.Red
	var backgroundDark = Color.Red
	var surfaceDark = Color.Red
	var surfaceElevationLevel3Dark = Color.Red

	val saturationOfPrimary = ColorUtil.colorToHSL(colorResource(system_accent1_600))[1]

	val harmonizedBlue = Color(
		harmonize(Color.Blue.toArgb(), MaterialTheme.colorScheme.primary.toArgb())
	).blendWith(Color.White, 1f - saturationOfPrimary)

	val harmonizedRed = Color(
		harmonize(Color.Red.toArgb(), MaterialTheme.colorScheme.primary.toArgb())
	).blendWith(Color.White, 1f - saturationOfPrimary)

	val harmonizedGreen = Color(
		harmonize(Color.Green.toArgb(), MaterialTheme.colorScheme.primary.toArgb())
	).blendWith(Color.White, 1f - saturationOfPrimary)

	val harmonizedOrange = Color(
		harmonize(Color(0xFFFFAA00).toArgb(), MaterialTheme.colorScheme.primary.toArgb())
	).blendWith(Color.White, 1f - saturationOfPrimary)

	val harmonizedViolet = Color(
		harmonize(Color(0xFFEB00FF).toArgb(), MaterialTheme.colorScheme.primary.toArgb())
	).blendWith(Color.White, 1f - saturationOfPrimary)

	val harmonizedCyan = Color(
		harmonize(Color.Cyan.toArgb(), MaterialTheme.colorScheme.primary.toArgb())
	).blendWith(Color.White, 1f - saturationOfPrimary)

	val harmonizedPink = Color(
		harmonize(Color(0xFFFF32AC).toArgb(), MaterialTheme.colorScheme.primary.toArgb())
	).blendWith(Color.White, 1f - saturationOfPrimary)

	LightTheme {
		backgroundLight = MaterialTheme.colorScheme.background
		surfaceLight = MaterialTheme.colorScheme.surface
		surfaceElevationLevel3Light = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
	}

	DarkTheme {
		backgroundDark = MaterialTheme.colorScheme.background
		surfaceDark = MaterialTheme.colorScheme.surface
		surfaceElevationLevel3Dark = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
	}

	return FullPaletteList(
		primary_0 = colorResource(system_accent1_1000),
		primary_10 = colorResource(system_accent1_900),
		primary_20 = colorResource(system_accent1_800),
		primary_30 = colorResource(system_accent1_700),
		primary_40 = colorResource(system_accent1_600),
		primary_50 = colorResource(system_accent1_500),
		primary_60 = colorResource(system_accent1_400),
		primary_70 = colorResource(system_accent1_300),
		primary_80 = colorResource(system_accent1_200),
		primary_90 = colorResource(system_accent1_100),
		primary_95 = colorResource(system_accent1_50),
		primary_99 = colorResource(system_accent1_10),
		primary_100 = colorResource(system_accent1_0),
		secondary_0 = colorResource(system_accent2_1000),
		secondary_10 = colorResource(system_accent2_900),
		secondary_20 = colorResource(system_accent2_800),
		secondary_30 = colorResource(system_accent2_700),
		secondary_40 = colorResource(system_accent2_600),
		secondary_50 = colorResource(system_accent2_500),
		secondary_60 = colorResource(system_accent2_400),
		secondary_70 = colorResource(system_accent2_300),
		secondary_80 = colorResource(system_accent2_200),
		secondary_90 = colorResource(system_accent2_100),
		secondary_95 = colorResource(system_accent2_50),
		secondary_99 = colorResource(system_accent2_10),
		secondary_100 = colorResource(system_accent2_0),
		tertiary_0 = colorResource(system_accent3_1000),
		tertiary_10 = colorResource(system_accent3_900),
		tertiary_20 = colorResource(system_accent3_800),
		tertiary_30 = colorResource(system_accent3_700),
		tertiary_40 = colorResource(system_accent3_600),
		tertiary_50 = colorResource(system_accent3_500),
		tertiary_60 = colorResource(system_accent3_400),
		tertiary_70 = colorResource(system_accent3_300),
		tertiary_80 = colorResource(system_accent3_200),
		tertiary_90 = colorResource(system_accent3_100),
		tertiary_95 = colorResource(system_accent3_50),
		tertiary_99 = colorResource(system_accent3_10),
		tertiary_100 = colorResource(system_accent3_0),
		neutral_0 = colorResource(system_neutral1_1000),
		neutral_10 = colorResource(system_neutral1_900),
		neutral_20 = colorResource(system_neutral1_800),
		neutral_30 = colorResource(system_neutral1_700),
		neutral_40 = colorResource(system_neutral1_600),
		neutral_50 = colorResource(system_neutral1_500),
		neutral_60 = colorResource(system_neutral1_400),
		neutral_70 = colorResource(system_neutral1_300),
		neutral_80 = colorResource(system_neutral1_200),
		neutral_90 = colorResource(system_neutral1_100),
		neutral_95 = colorResource(system_neutral1_50),
		neutral_99 = colorResource(system_neutral1_10),
		neutral_100 = colorResource(system_neutral1_0),
		neutralVariant_0 = colorResource(system_neutral2_1000),
		neutralVariant_10 = colorResource(system_neutral2_900),
		neutralVariant_20 = colorResource(system_neutral2_800),
		neutralVariant_30 = colorResource(system_neutral2_700),
		neutralVariant_40 = colorResource(system_neutral2_600),
		neutralVariant_50 = colorResource(system_neutral2_500),
		neutralVariant_60 = colorResource(system_neutral2_400),
		neutralVariant_70 = colorResource(system_neutral2_300),
		neutralVariant_80 = colorResource(system_neutral2_200),
		neutralVariant_90 = colorResource(system_neutral2_100),
		neutralVariant_95 = colorResource(system_neutral2_50),
		neutralVariant_99 = colorResource(system_neutral2_10),
		neutralVariant_100 = colorResource(system_neutral2_0),
		backgroundLight = backgroundLight,
		// you cant get neutral 98 color... ask google about their decisions
		surfaceLight = surfaceLight,
		surfaceElevationLevel3Light = surfaceElevationLevel3Light,
		backgroundDark = backgroundDark,
		surfaceDark = surfaceDark,
		surfaceElevationLevel3Dark = surfaceElevationLevel3Dark,
		blue = getColorTonesMap(harmonizedBlue),
		red = getColorTonesMap(harmonizedRed),
		green = getColorTonesMap(harmonizedGreen),
		orange = getColorTonesMap(harmonizedOrange),
		violet = getColorTonesMap(harmonizedViolet),
		cyan = getColorTonesMap(harmonizedCyan),
		pink = getColorTonesMap(harmonizedPink),
	)
}

fun Color.blendWith(color: Color, @FloatRange(from = 0.0, to = 1.0) ratio: Float): Color {
	val inv = 1f - ratio
	return copy(
		red = red * inv + color.red * ratio,
		blue = blue * inv + color.blue * ratio,
		green = green * inv + color.green * ratio,
	)
}