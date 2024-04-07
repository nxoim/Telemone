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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.materialkolor.hct.Hct
import com.materialkolor.ktx.from
import com.materialkolor.ktx.harmonize
import com.materialkolor.ktx.toHct
import com.materialkolor.palettes.TonalPalette

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

class PaletteState(
	val entirePaletteAsMap: LinkedHashMap<String, Color>,
	val isDarkMode: Boolean
) {
	val allPossibleColorTokensAsList = entirePaletteAsMap.keys
}

@Composable
fun rememberPaletteState(): PaletteState {
	val isDarkMode = isSystemInDarkTheme()
	val entirePaletteAsMap = remember { linkedMapOf<String, Color>() }

	AdditionalColors.entries.forEach {
		entirePaletteAsMap[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	ColorRolesLight.entries.forEach {
		entirePaletteAsMap[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	ColorRolesDark.entries.forEach {
		entirePaletteAsMap[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	listOf(
		primaryTones,
		secondaryTones,
		tertiaryTones,
		neutralTones,
		neutralVariantTones,
		blueTones,
		redTones,
		greenTones,
		orangeTones,
		violetTones,
		cyanTones,
		pinkTones
	).forEach { toneList ->
		toneList.forEach { entirePaletteAsMap[it.colorToken] = it.colorValue }
	}

	return remember { PaletteState(entirePaletteAsMap, isDarkMode) }
}

enum class ColorRolesLight(val dataAboutColors: DataAboutColors) {
	PrimaryLight(
		DataAboutColors("primary_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.primary }
			color
		}
	),
	OnPrimaryLight(
		DataAboutColors("on_primary_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.onPrimary }
			color
		}
	),
	PrimaryContainerLight(
		DataAboutColors("primary_container_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.primaryContainer }
			color
		}
	),
	OnPrimaryContainerLight(
		DataAboutColors("on_primary_container_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.onPrimaryContainer }
			color
		}
	),
	SecondaryLight(
		DataAboutColors("secondary_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.secondary }
			color
		}
	),
	OnSecondaryLight(
		DataAboutColors("on_secondary_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.onSecondary }
			color
		}
	),
	SecondaryContainerLight(
		DataAboutColors("secondary_container_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.secondaryContainer }
			color
		}
	),
	OnSecondaryContainerLight(
		DataAboutColors("on_secondary_container_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.onSecondaryContainer }
			color
		}
	),
	TertiaryLight(
		DataAboutColors("tertiary_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.tertiary }
			color
		}
	),
	OnTertiaryLight(
		DataAboutColors("on_tertiary_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.onTertiary }
			color
		}
	),
	TertiaryContainerLight(
		DataAboutColors("tertiary_container_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.tertiaryContainer }
			color
		}
	),
	OnTertiaryContainerLight(
		DataAboutColors("on_tertiary_container_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.onTertiaryContainer }
			color
		}
	),
	SurfaceLight(
		DataAboutColors("surface_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.surface }
			color
		}
	),
	SurfaceDimLight(
		DataAboutColors("surface_dim_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.surfaceDim }
			color
		}
	),
	SurfaceBrightLight(
		DataAboutColors("surface_bright_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.surfaceBright }
			color
		}
	),
	OnSurfaceLight(
		DataAboutColors("on_surface_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.onSurface }
			color
		}
	),
	SurfaceContainerLowestLight(
		DataAboutColors("surface_container_lowest_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.surfaceContainerLowest }
			color
		}
	),
	SurfaceContainerLowLight(
		DataAboutColors("surface_container_low_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.surfaceContainerLow }
			color
		}
	),
	SurfaceContainerLight(
		DataAboutColors("surface_container_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.surfaceContainer }
			color
		}
	),
	SurfaceContainerHighLight(
		DataAboutColors("surface_container_high_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.surfaceContainerHigh }
			color
		}
	),
	SurfaceContainerHighestLight(
		DataAboutColors("surface_container_highest_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.surfaceContainerHighest }
			color
		}
	),
	OnSurfaceVariantLight(
		DataAboutColors("on_surface_variant_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.onSurfaceVariant }
			color
		}
	),
	ErrorLight(
		DataAboutColors("error_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.error }
			color
		}
	),
	OnErrorLight(
		DataAboutColors("on_error_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.onError }
			color
		}
	),
	ErrorContainerLight(
		DataAboutColors("error_container_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.errorContainer }
			color
		}
	),
	OnErrorContainerLight(
		DataAboutColors("on_error_container_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.onErrorContainer }
			color
		}
	),
	OutlineLight(
		DataAboutColors("outline_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.outline }
			color
		}
	),
	OutlineVariantLight(
		DataAboutColors("outline_variant_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.outlineVariant }
			color
		}
	);

	operator fun component1() = this.dataAboutColors.colorToken

	@Composable
	operator fun component2() = this.dataAboutColors.colorValue()
}

enum class ColorRolesDark(val dataAboutColors: DataAboutColors) {
	PrimaryDark(
		DataAboutColors("primary_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.primary }
			color
		}
	),

	OnPrimaryDark(
		DataAboutColors("on_primary_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.onPrimary }
			color
		}
	),

	PrimaryContainerDark(
		DataAboutColors("primary_container_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.primaryContainer }
			color
		}
	),

	OnPrimaryContainerDark(
		DataAboutColors("on_primary_container_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.onPrimaryContainer }
			color
		}
	),

	SecondaryDark(
		DataAboutColors("secondary_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.secondary }
			color
		}
	),

	OnSecondaryDark(
		DataAboutColors("on_secondary_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.onSecondary }
			color
		}
	),

	SecondaryContainerDark(
		DataAboutColors("secondary_container_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.secondaryContainer }
			color
		}
	),

	OnSecondaryContainerDark(
		DataAboutColors("on_secondary_container_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.onSecondaryContainer }
			color
		}
	),

	TertiaryDark(
		DataAboutColors("tertiary_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.tertiary }
			color
		}
	),

	OnTertiaryDark(
		DataAboutColors("on_tertiary_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.onTertiary }
			color
		}
	),

	TertiaryContainerDark(
		DataAboutColors("tertiary_container_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.tertiaryContainer }
			color
		}
	),

	OnTertiaryContainerDark(
		DataAboutColors("on_tertiary_container_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.onTertiaryContainer }
			color
		}
	),

	SurfaceDark(
		DataAboutColors("surface_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.surface }
			color
		}
	),

	SurfaceDimDark(
		DataAboutColors("surface_dim_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.surfaceDim }
			color
		}
	),

	SurfaceBrightDark(
		DataAboutColors("surface_bright_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.surfaceBright }
			color
		}
	),

	OnSurfaceDark(
		DataAboutColors("on_surface_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.onSurface }
			color
		}
	),

	SurfaceContainerLowestDark(
		DataAboutColors("surface_container_lowest_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.surfaceContainerLowest }
			color
		}
	),

	SurfaceContainerLowDark(
		DataAboutColors("surface_container_low_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.surfaceContainerLow }
			color
		}
	),

	SurfaceContainerDark(
		DataAboutColors("surface_container_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.surfaceContainer }
			color
		}
	),

	SurfaceContainerHighDark(
		DataAboutColors("surface_container_high_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.surfaceContainerHigh }
			color
		}
	),

	SurfaceContainerHighestDark(
		DataAboutColors("surface_container_highest_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.surfaceContainerHighest }
			color
		}
	),

	OnSurfaceVariantDark(
		DataAboutColors("on_surface_variant_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.onSurfaceVariant }
			color
		}
	),

	ErrorDark(
		DataAboutColors("error_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.error }
			color
		}
	),

	OnErrorDark(
		DataAboutColors("on_error_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.onError }
			color
		}
	),

	ErrorContainerDark(
		DataAboutColors("error_container_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.errorContainer }
			color
		}
	),

	OnErrorContainerDark(
		DataAboutColors("on_error_container_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.onErrorContainer }
			color
		}
	),

	OutlineDark(
		DataAboutColors("outline_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.outline }
			color
		}
	),

	OutlineVariantDark(
		DataAboutColors("outline_variant_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.outlineVariant }
			color
		}
	);

	operator fun component1() = this.dataAboutColors.colorToken

	@Composable
	operator fun component2() = this.dataAboutColors.colorValue()
}

enum class AdditionalColors(val dataAboutColors: DataAboutColors) {
	White(
		DataAboutColors("white") { Color.White }
	),
	Black(
		DataAboutColors("black") { Color.Black }
	),
	SurfaceElevationLevel3Light(
		DataAboutColors("surface_elevation_level_3_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp) }
			color
		}
	),
	SurfaceElevationLevel3Dark(
		DataAboutColors("surface_elevation_level_3_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp) }
			color
		}
	),
	Transparent(
		DataAboutColors("transparent") { Color.Transparent }
	),
	ScrimLight(
		DataAboutColors("scrim_light") {
			var color = Color.Red
			TelemoneTheme(false) { color = MaterialTheme.colorScheme.scrim }
			color
		}
	),
	ScrimDark(
		DataAboutColors("scrim_dark") {
			var color = Color.Red
			TelemoneTheme(true) { color = MaterialTheme.colorScheme.scrim }
			color
		}
	);

	operator fun component1() = this.dataAboutColors.colorToken

	@Composable
	operator fun component2() = this.dataAboutColors.colorValue()
}

val primaryTones
	@Composable
	get() = possibleTones.map { toneNumber ->
		val color = when (toneNumber) {
			100 -> colorResource(system_accent1_0)
			99 -> colorResource(system_accent1_10)
			95 -> colorResource(system_accent1_50)
			90 -> colorResource(system_accent1_100)
			80 -> colorResource(system_accent1_200)
			70 -> colorResource(system_accent1_300)
			60 -> colorResource(system_accent1_400)
			50 -> colorResource(system_accent1_500)
			40 -> colorResource(system_accent1_600)
			30 -> colorResource(system_accent1_700)
			20 -> colorResource(system_accent1_800)
			10 -> colorResource(system_accent1_900)
			else -> colorResource(system_accent1_1000)
		}

		ToneInfo(
			toneNumber,
			colorToken ="primary_$toneNumber",
			colorValue = color
		)
	}

val secondaryTones
	@Composable
	get() = possibleTones.map { toneNumber ->
		val color = when (toneNumber) {
			100 -> colorResource(system_accent2_0)
			99 -> colorResource(system_accent2_10)
			95 -> colorResource(system_accent2_50)
			90 -> colorResource(system_accent2_100)
			80 -> colorResource(system_accent2_200)
			70 -> colorResource(system_accent2_300)
			60 -> colorResource(system_accent2_400)
			50 -> colorResource(system_accent2_500)
			40 -> colorResource(system_accent2_600)
			30 -> colorResource(system_accent2_700)
			20 -> colorResource(system_accent2_800)
			10 -> colorResource(system_accent2_900)
			else -> colorResource(system_accent2_1000)
		}

		ToneInfo(
			toneNumber,
			colorToken ="secondary_$toneNumber",
			colorValue = color
		)
	}

val tertiaryTones
	@Composable
	get() = possibleTones.map { toneNumber ->
		val color = when (toneNumber) {
			100 -> colorResource(system_accent3_0)
			99 -> colorResource(system_accent3_10)
			95 -> colorResource(system_accent3_50)
			90 -> colorResource(system_accent3_100)
			80 -> colorResource(system_accent3_200)
			70 -> colorResource(system_accent3_300)
			60 -> colorResource(system_accent3_400)
			50 -> colorResource(system_accent3_500)
			40 -> colorResource(system_accent3_600)
			30 -> colorResource(system_accent3_700)
			20 -> colorResource(system_accent3_800)
			10 -> colorResource(system_accent3_900)
			else -> colorResource(system_accent3_1000)
		}
		ToneInfo(
			toneNumber,
			colorToken ="tertiary_$toneNumber",
			colorValue = color
		)
	}

val neutralTones
	@Composable
	get() = possibleTones.map { toneNumber ->
		val color = when (toneNumber) {
			100 -> colorResource(system_neutral1_0)
			99 -> colorResource(system_neutral1_10)
			95 -> colorResource(system_neutral1_50)
			90 -> colorResource(system_neutral1_100)
			80 -> colorResource(system_neutral1_200)
			70 -> colorResource(system_neutral1_300)
			60 -> colorResource(system_neutral1_400)
			50 -> colorResource(system_neutral1_500)
			40 -> colorResource(system_neutral1_600)
			30 -> colorResource(system_neutral1_700)
			20 -> colorResource(system_neutral1_800)
			10 -> colorResource(system_neutral1_900)
			else -> colorResource(system_neutral1_1000)
		}
		ToneInfo(
			toneNumber,
			colorToken ="neutral_$toneNumber",
			colorValue = color
		)
	}

// if only google provided a way to get the m3 tonal palette🤡🤡🤡
val neutralVariantTones
	@Composable
	get () = possibleTones.map { toneNumber ->
		val color = when (toneNumber) {
			100 -> colorResource(system_neutral2_0)
			99 -> colorResource(system_neutral2_10)
			95 -> colorResource(system_neutral2_50)
			90 -> colorResource(system_neutral2_100)
			80 -> colorResource(system_neutral2_200)
			70 -> colorResource(system_neutral2_300)
			60 -> colorResource(system_neutral2_400)
			50 -> colorResource(system_neutral2_500)
			40 -> colorResource(system_neutral2_600)
			30 -> colorResource(system_neutral2_700)
			20 -> colorResource(system_neutral2_800)
			10 -> colorResource(system_neutral2_900)
			else -> colorResource(system_neutral2_1000)
		}

		ToneInfo(
			toneNumber,
			colorToken ="neutral_variant_$toneNumber",
			colorValue = color
		)
	}

val blueTones @Composable get() = blue.getCustomTones(name = "blue")
val redTones @Composable get() = red.getCustomTones(name = "red")
val greenTones @Composable get() = green.getCustomTones(name = "green")
val orangeTones @Composable get() = orange.getCustomTones(name = "orange")
val violetTones @Composable get() = violet.getCustomTones(name = "violet")
val pinkTones @Composable get() = pink.getCustomTones(name = "pink")
val cyanTones @Composable get() = cyan.getCustomTones(name = "cyan")

@Stable
@Composable
fun Color.getCustomTones(name: String): List<ToneInfo> {
	return possibleTones.map { toneNumber ->
		ToneInfo(
			toneNumber,
			colorToken = "${name}_$toneNumber",
			colorValue = this
				.harmonize(colorResource(system_accent1_500))
				.matchSaturation(toThatOf = colorResource(system_accent1_500))
				.getTone(toneNumber)
		)
	}
}

@Immutable
data class ToneInfo(val tone: Int, val colorToken: String, val colorValue: Color)

@Immutable
data class DataAboutColors(val colorToken: String, val colorValue: @Composable () -> Color)

fun Color.matchSaturation(toThatOf: Color, saturationMultiplier: Double = 0.9): Color {
	val source = this.toHct()
	val target = toThatOf.toHct()

	val result = Hct.from(
		hue = source.hue,
		chroma = target.chroma * saturationMultiplier,
		tone = source.tone
	)

	return Color(result.toInt())
}

val possibleTones = listOf(0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 100)

fun Color.getTone(tone: Int) = Color(TonalPalette.from(this).tone(tone))

fun Color.blendWith(color: Color, @FloatRange(from = 0.0, to = 1.0) ratio: Float): Color {
	val inv = 1f - ratio
	return copy(
		red = red * inv + color.red * ratio,
		blue = blue * inv + color.blue * ratio,
		green = green * inv + color.green * ratio,
	)
}

val blue = Color.Blue
val red = Color.Red
val green = Color.Green
val orange = Color(0xFFFFAA00)
val violet = Color(0xFFEB00FF)
val pink = Color(0xFFFF32AC)
val cyan = Color(0xFF14AAAC)