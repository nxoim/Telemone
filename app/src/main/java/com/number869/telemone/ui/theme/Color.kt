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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.google.android.material.color.MaterialColors
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
	"a1_0",
	"a1_10",
	"a1_50",
	"a1_100",
	"a1_200",
	"a1_300",
	"a1_400",
	"a1_500",
	"a1_600",
	"a1_700",
	"a1_800",
	"a1_900",
	"a1_1000",
	"a2_0",
	"a2_10",
	"a2_50",
	"a2_100",
	"a2_200",
	"a2_300",
	"a2_400",
	"a2_500",
	"a2_600",
	"a2_700",
	"a2_800",
	"a2_900",
	"a2_1000",
	"a3_0",
	"a3_10",
	"a3_50",
	"a3_100",
	"a3_200",
	"a3_300",
	"a3_400",
	"a3_500",
	"a3_600",
	"a3_700",
	"a3_800",
	"a3_900",
	"a3_1000",
	"n1_0",
	"n1_10",
	"n1_50",
	"n1_100",
	"n1_200",
	"n1_300",
	"n1_400",
	"n1_500",
	"n1_600",
	"n1_700",
	"n1_800",
	"n1_900",
	"n1_1000",
	"n2_0",
	"n2_10",
	"n2_50",
	"n2_100",
	"n2_200",
	"n2_300",
	"n2_400",
	"n2_500",
	"n2_600",
	"n2_700",
	"n2_800",
	"n2_900",
	"n2_1000"
)

data class FullPaletteList(
	val a1_0: Color,
	val a1_10: Color,
	val a1_50: Color,
	val a1_100: Color,
	val a1_200: Color,
	val a1_300: Color,
	val a1_400: Color,
	val a1_500: Color,
	val a1_600: Color,
	val a1_700: Color,
	val a1_800: Color,
	val a1_900: Color,
	val a1_1000: Color,
	val a2_0: Color,
	val a2_10: Color,
	val a2_50: Color,
	val a2_100: Color,
	val a2_200: Color,
	val a2_300: Color,
	val a2_400: Color,
	val a2_500: Color,
	val a2_600: Color,
	val a2_700: Color,
	val a2_800: Color,
	val a2_900: Color,
	val a2_1000: Color,
	val a3_0: Color,
	val a3_10: Color,
	val a3_50: Color,
	val a3_100: Color,
	val a3_200: Color,
	val a3_300: Color,
	val a3_400: Color,
	val a3_500: Color,
	val a3_600: Color,
	val a3_700: Color,
	val a3_800: Color,
	val a3_900: Color,
	val a3_1000: Color,
	val n1_0: Color,
	val n1_10: Color,
	val n1_50: Color,
	val n1_100: Color,
	val n1_200: Color,
	val n1_300: Color,
	val n1_400: Color,
	val n1_500: Color,
	val n1_600: Color,
	val n1_700: Color,
	val n1_800: Color,
	val n1_900: Color,
	val n1_1000: Color,
	val n2_0: Color,
	val n2_10: Color,
	val n2_50: Color,
	val n2_100: Color,
	val n2_200: Color,
	val n2_300: Color,
	val n2_400: Color,
	val n2_500: Color,
	val n2_600: Color,
	val n2_700: Color,
	val n2_800: Color,
	val n2_900: Color,
	val n2_1000: Color,
	val backgroundLight: Color,
	val surfaceLight: Color,
	val surfaceElevationLevel3Light: Color,
	val backgroundDark: Color,
	val surfaceDark: Color,
	val surfaceElevationLevel3Dark: Color,
	val blueTonalPalette: Map<Int, Color>,
	val redTonalPalette: Map<Int, Color>,
	val greenTonalPalette: Map<Int, Color>,
	val orangeTonalPalette: Map<Int, Color>,
	val violetTonalPalette: Map<Int, Color>,
	val cyanTonalPalette: Map<Int, Color>,
	val pinkTonalPalette: Map<Int, Color>
)

@Composable
fun fullPalette(): FullPaletteList {
	var backgroundLight = Color.Red
	var surfaceLight = Color.Red
	var surfaceElevationLevel3Light = Color.Red
	var backgroundDark = Color.Red
	var surfaceDark = Color.Red
	var surfaceElevationLevel3Dark = Color.Red

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
		a1_0 = colorResource(system_accent1_0),
		a1_10 = colorResource(system_accent1_10),
		a1_50 = colorResource(system_accent1_50),
		a1_100 = colorResource(system_accent1_100),
		a1_200 = colorResource(system_accent1_200),
		a1_300 = colorResource(system_accent1_300),
		a1_400 = colorResource(system_accent1_400),
		a1_500 = colorResource(system_accent1_500),
		a1_600 = colorResource(system_accent1_600),
		a1_700 = colorResource(system_accent1_700),
		a1_800 = colorResource(system_accent1_800),
		a1_900 = colorResource(system_accent1_900),
		a1_1000 = colorResource(system_accent1_1000),
		a2_0 = colorResource(system_accent2_0),
		a2_10 = colorResource(system_accent2_10),
		a2_50 = colorResource(system_accent2_50),
		a2_100 = colorResource(system_accent2_100),
		a2_200 = colorResource(system_accent2_200),
		a2_300 = colorResource(system_accent2_300),
		a2_400 = colorResource(system_accent2_400),
		a2_500 = colorResource(system_accent2_500),
		a2_600 = colorResource(system_accent2_600),
		a2_700 = colorResource(system_accent2_700),
		a2_800 = colorResource(system_accent2_800),
		a2_900 = colorResource(system_accent2_900),
		a2_1000 = colorResource(system_accent2_1000),
		a3_0 = colorResource(system_accent3_0),
		a3_10 = colorResource(system_accent3_10),
		a3_50 = colorResource(system_accent3_50),
		a3_100 = colorResource(system_accent3_100),
		a3_200 = colorResource(system_accent3_200),
		a3_300 = colorResource(system_accent3_300),
		a3_400 = colorResource(system_accent3_400),
		a3_500 = colorResource(system_accent3_500),
		a3_600 = colorResource(system_accent3_600),
		a3_700 = colorResource(system_accent3_700),
		a3_800 = colorResource(system_accent3_800),
		a3_900 = colorResource(system_accent3_900),
		a3_1000 = colorResource(system_accent3_1000),
		n1_0 = colorResource(system_neutral1_0),
		n1_10 = colorResource(system_neutral1_10),
		n1_50 = colorResource(system_neutral1_50),
		n1_100 = colorResource(system_neutral1_100),
		n1_200 = colorResource(system_neutral1_200),
		n1_300 = colorResource(system_neutral1_300),
		n1_400 = colorResource(system_neutral1_400),
		n1_500 = colorResource(system_neutral1_500),
		n1_600 = colorResource(system_neutral1_600),
		n1_700 = colorResource(system_neutral1_700),
		n1_800 = colorResource(system_neutral1_800),
		n1_900 = colorResource(system_neutral1_900),
		n1_1000 = colorResource(system_neutral1_1000),
		n2_0 = colorResource(system_neutral2_0),
		n2_10 = colorResource(system_neutral2_10),
		n2_50 = colorResource(system_neutral2_50),
		n2_100 = colorResource(system_neutral2_100),
		n2_200 = colorResource(system_neutral2_200),
		n2_300 = colorResource(system_neutral2_300),
		n2_400 = colorResource(system_neutral2_400),
		n2_500 = colorResource(system_neutral2_500),
		n2_600 = colorResource(system_neutral2_600),
		n2_700 = colorResource(system_neutral2_700),
		n2_800 = colorResource(system_neutral2_800),
		n2_900 = colorResource(system_neutral2_900),
		n2_1000 = colorResource(system_neutral2_1000),
		backgroundLight = backgroundLight,
		// you cant get neutral 98 color... ask google about their decisions
		surfaceLight = surfaceLight,
		surfaceElevationLevel3Light = surfaceElevationLevel3Light,
		backgroundDark = backgroundDark,
		surfaceDark = surfaceDark,
		surfaceElevationLevel3Dark = surfaceElevationLevel3Dark,
		blueTonalPalette = getColorTonesMap(
			Color(
				MaterialColors.harmonize(Color.Blue.toArgb(), MaterialTheme.colorScheme.primary.toArgb())
			)
		),
		redTonalPalette = getColorTonesMap(
			Color(
				MaterialColors.harmonize(Color.Red.toArgb(), MaterialTheme.colorScheme.primary.toArgb())
			)
		),
		greenTonalPalette = getColorTonesMap(
			Color(
				MaterialColors.harmonize(Color.Green.toArgb(), MaterialTheme.colorScheme.primary.toArgb())
			)
		),
		orangeTonalPalette = getColorTonesMap(
			Color(
				MaterialColors.harmonize(Color(0xFFFFA500).toArgb(), MaterialTheme.colorScheme.primary.toArgb())
			)
		),
		violetTonalPalette = getColorTonesMap(
			Color(
				MaterialColors.harmonize(Color(0xFF9B26B6).toArgb(), MaterialTheme.colorScheme.primary.toArgb())
			)
		),
		cyanTonalPalette = getColorTonesMap(
			Color(
				MaterialColors.harmonize(Color.Cyan.toArgb(), MaterialTheme.colorScheme.primary.toArgb())
			)
		),
		pinkTonalPalette = getColorTonesMap(
			Color(
				MaterialColors.harmonize(Color(0xFFFF6499).toArgb(), MaterialTheme.colorScheme.primary.toArgb())
			)
		),
	)
}