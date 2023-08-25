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
import android.annotation.SuppressLint
import androidx.annotation.FloatRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

data class DataAboutColors(
	val colorToken: String,
	val colorValue: @Composable () -> Color
)

class PaletteState(val entirePaletteAsMap: MutableState<LinkedHashMap<String, Color>>) {
	val allPossibleColorTokensAsList = entirePaletteAsMap.value.keys
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun rememberPaletteState(): PaletteState {
	val entirePaletteAsMap = remember { mutableStateOf(linkedMapOf<String, Color>()) }

	AdditionalColors.entries.forEach {
		entirePaletteAsMap.value[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	ColorRolesLight.entries.forEach {
		entirePaletteAsMap.value[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	ColorRolesDark.entries.forEach {
		entirePaletteAsMap.value[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	PrimaryTones.entries.forEach {
		entirePaletteAsMap.value[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	SecondaryTones.entries.forEach {
		entirePaletteAsMap.value[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	TertiaryTones.entries.forEach {
		entirePaletteAsMap.value[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	NeutralTones.entries.forEach {
		entirePaletteAsMap.value[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	NeutralVariantTones.entries.forEach {
		entirePaletteAsMap.value[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	BlueTones.entries.forEach {
		entirePaletteAsMap.value[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	RedTones.entries.forEach {
		entirePaletteAsMap.value[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	GreenTones.entries.forEach {
		entirePaletteAsMap.value[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	OrangeTones.entries.forEach {
		entirePaletteAsMap.value[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	VioletTones.entries.forEach {
		entirePaletteAsMap.value[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	CyanTones.entries.forEach {
		entirePaletteAsMap.value[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}

	PinkTones.entries.forEach {
		entirePaletteAsMap.value[it.dataAboutColors.colorToken] = it.dataAboutColors.colorValue()
	}


	return remember { PaletteState(entirePaletteAsMap) }
}

enum class ColorRolesLight(val dataAboutColors: DataAboutColors) {
	PrimaryLight(
		DataAboutColors("primary_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.primary }
			color
		}
	),
	OnPrimaryLight(
		DataAboutColors("on_primary_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.onPrimary }
			color
		}
	),
	PrimaryContainerLight(
		DataAboutColors("primary_container_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.primaryContainer }
			color
		}
	),
	OnPrimaryContainerLight(
		DataAboutColors("on_primary_container_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.onPrimaryContainer }
			color
		}
	),
	SecondaryLight(
		DataAboutColors("secondary_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.secondary }
			color
		}
	),
	OnSecondaryLight(
		DataAboutColors("on_secondary_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.onSecondary }
			color
		}
	),
	SecondaryContainerLight(
		DataAboutColors("secondary_container_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.secondaryContainer }
			color
		}
	),
	OnSecondaryContainerLight(
		DataAboutColors("on_secondary_container_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.onSecondaryContainer }
			color
		}
	),
	TertiaryLight(
		DataAboutColors("tertiary_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.tertiary }
			color
		}
	),
	OnTertiaryLight(
		DataAboutColors("on_tertiary_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.onTertiary }
			color
		}
	),
	TertiaryContainerLight(
		DataAboutColors("tertiary_container_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.tertiaryContainer }
			color
		}
	),
	OnTertiaryContainerLight(
		DataAboutColors("on_tertiary_container_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.onTertiaryContainer }
			color
		}
	),
	SurfaceLight(
		DataAboutColors("surface_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.surface }
			color
		}
	),
	SurfaceDimLight(
		DataAboutColors("surface_dim_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.surfaceDim }
			color
		}
	),
	SurfaceBrightLight(
		DataAboutColors("surface_bright_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.surfaceBright }
			color
		}
	),
	OnSurfaceLight(
		DataAboutColors("on_surface_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.onSurface }
			color
		}
	),
	SurfaceContainerLowestLight(
		DataAboutColors("surface_container_lowest_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.surfaceContainerLowest }
			color
		}
	),
	SurfaceContainerLowLight(
		DataAboutColors("surface_container_low_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.surfaceContainerLow }
			color
		}
	),
	SurfaceContainerLight(
		DataAboutColors("surface_container_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.surfaceContainer }
			color
		}
	),
	SurfaceContainerHighLight(
		DataAboutColors("surface_container_high_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.surfaceContainerHigh }
			color
		}
	),
	SurfaceContainerHighestLight(
		DataAboutColors("surface_container_highest_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.surfaceContainerHighest }
			color
		}
	),
	OnSurfaceVariantLight(
		DataAboutColors("on_surface_variant_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.onSurfaceVariant }
			color
		}
	),
	ErrorLight(
		DataAboutColors("error_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.error }
			color
		}
	),
	OnErrorLight(
		DataAboutColors("on_error_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.onError }
			color
		}
	),
	ErrorContainerLight(
		DataAboutColors("error_container_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.errorContainer }
			color
		}
	),
	OnErrorContainerLight(
		DataAboutColors("on_error_container_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.onErrorContainer }
			color
		}
	),
	OutlineLight(
		DataAboutColors("outline_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.outline }
			color
		}
	),
	OutlineVariantLight(
		DataAboutColors("outline_variant_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.outlineVariant }
			color
		}
	);

	operator fun component1(): String {
		return this.dataAboutColors.colorToken
	}


	@Composable
	operator fun component2(): Color {
		return this.dataAboutColors.colorValue()
	}
}

enum class ColorRolesDark(val dataAboutColors: DataAboutColors) {
	PrimaryDark(
		DataAboutColors("primary_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.primary }
			color
		}
	),

	OnPrimaryDark(
		DataAboutColors("on_primary_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.onPrimary }
			color
		}
	),

	PrimaryContainerDark(
		DataAboutColors("primary_container_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.primaryContainer }
			color
		}
	),

	OnPrimaryContainerDark(
		DataAboutColors("on_primary_container_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.onPrimaryContainer }
			color
		}
	),

	SecondaryDark(
		DataAboutColors("secondary_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.secondary }
			color
		}
	),

	OnSecondaryDark(
		DataAboutColors("on_secondary_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.onSecondary }
			color
		}
	),

	SecondaryContainerDark(
		DataAboutColors("secondary_container_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.secondaryContainer }
			color
		}
	),

	OnSecondaryContainerDark(
		DataAboutColors("on_secondary_container_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.onSecondaryContainer }
			color
		}
	),

	TertiaryDark(
		DataAboutColors("tertiary_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.tertiary }
			color
		}
	),

	OnTertiaryDark(
		DataAboutColors("on_tertiary_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.onTertiary }
			color
		}
	),

	TertiaryContainerDark(
		DataAboutColors("tertiary_container_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.tertiaryContainer }
			color
		}
	),

	OnTertiaryContainerDark(
		DataAboutColors("on_tertiary_container_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.onTertiaryContainer }
			color
		}
	),

	SurfaceDark(
		DataAboutColors("surface_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.surface }
			color
		}
	),

	SurfaceDimDark(
		DataAboutColors("surface_dim_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.surfaceDim }
			color
		}
	),

	SurfaceBrightDark(
		DataAboutColors("surface_bright_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.surfaceBright }
			color
		}
	),

	OnSurfaceDark(
		DataAboutColors("on_surface_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.onSurface }
			color
		}
	),

	SurfaceContainerLowestDark(
		DataAboutColors("surface_container_lowest_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.surfaceContainerLowest }
			color
		}
	),

	SurfaceContainerLowDark(
		DataAboutColors("surface_container_low_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.surfaceContainerLow }
			color
		}
	),

	SurfaceContainerDark(
		DataAboutColors("surface_container_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.surfaceContainer }
			color
		}
	),

	SurfaceContainerHighDark(
		DataAboutColors("surface_container_high_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.surfaceContainerHigh }
			color
		}
	),

	SurfaceContainerHighestDark(
		DataAboutColors("surface_container_highest_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.surfaceContainerHighest }
			color
		}
	),

	OnSurfaceVariantDark(
		DataAboutColors("on_surface_variant_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.onSurfaceVariant }
			color
		}
	),

	ErrorDark(
		DataAboutColors("error_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.error }
			color
		}
	),

	OnErrorDark(
		DataAboutColors("on_error_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.onError }
			color
		}
	),

	ErrorContainerDark(
		DataAboutColors("error_container_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.errorContainer }
			color
		}
	),

	OnErrorContainerDark(
		DataAboutColors("on_error_container_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.onErrorContainer }
			color
		}
	),

	OutlineDark(
		DataAboutColors("outline_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.outline }
			color
		}
	),

	OutlineVariantDark(
		DataAboutColors("outline_variant_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.outlineVariant }
			color
		}
	);


	operator fun component1(): String {
		return this.dataAboutColors.colorToken
	}

	@Composable
	operator fun component2(): Color {
		return this.dataAboutColors.colorValue()
	}
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
			LightTheme { color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp) }
			color
		}
	),
	SurfaceElevationLevel3Dark(
		DataAboutColors("surface_elevation_level_3_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp) }
			color
		}
	),
	Transparent(
		DataAboutColors("transparent") { Color.Transparent }
	),
	ScrimLight(
		DataAboutColors("scrim_light") {
			var color = Color.Red
			LightTheme { color = MaterialTheme.colorScheme.scrim }
			color
		}
	),
	ScrimDark(
		DataAboutColors("scrim_dark") {
			var color = Color.Red
			DarkTheme { color = MaterialTheme.colorScheme.scrim }
			color
		}
	);

	operator fun component1(): String {
		return this.dataAboutColors.colorToken
	}

	@Composable
	operator fun component2(): Color {
		return this.dataAboutColors.colorValue()
	}
}

enum class PrimaryTones(val dataAboutColors: DataAboutColors){
	T0(DataAboutColors("primary_0") { colorResource(system_accent1_1000) }),
	T10(DataAboutColors("primary_10") { colorResource(system_accent1_900) }),
	T20(DataAboutColors("primary_20") { colorResource(system_accent1_800) }),
	T30(DataAboutColors("primary_30") { colorResource(system_accent1_700) }),
	T40(DataAboutColors("primary_40") { colorResource(system_accent1_600) }),
	T50(DataAboutColors("primary_50") { colorResource(system_accent1_500) }),
	T60(DataAboutColors("primary_60") { colorResource(system_accent1_400) }),
	T70(DataAboutColors("primary_70") { colorResource(system_accent1_300) }),
	T80(DataAboutColors("primary_80") { colorResource(system_accent1_200) }),
	T90(DataAboutColors("primary_90") { colorResource(system_accent1_100) }),
	T95(DataAboutColors("primary_95") { colorResource(system_accent1_50) }),
	T99(DataAboutColors("primary_99") { colorResource(system_accent1_10) }),
	T100(DataAboutColors("primary_100") { colorResource(system_accent1_0) });

	operator fun component1(): String {
		return this.dataAboutColors.colorToken
	}

	@Composable
	operator fun component2(): Color {
		return this.dataAboutColors.colorValue()
	}
}

enum class SecondaryTones(val dataAboutColors: DataAboutColors) {
	T0(DataAboutColors("secondary_0") { colorResource(system_accent2_1000) }),
	T10(DataAboutColors("secondary_10") { colorResource(system_accent2_900) }),
	T20(DataAboutColors("secondary_20") { colorResource(system_accent2_800) }),
	T30(DataAboutColors("secondary_30") { colorResource(system_accent2_700) }),
	T40(DataAboutColors("secondary_40") { colorResource(system_accent2_600) }),
	T50(DataAboutColors("secondary_50") { colorResource(system_accent2_500) }),
	T60(DataAboutColors("secondary_60") { colorResource(system_accent2_400) }),
	T70(DataAboutColors("secondary_70") { colorResource(system_accent2_300) }),
	T80(DataAboutColors("secondary_80") { colorResource(system_accent2_200) }),
	T90(DataAboutColors("secondary_90") { colorResource(system_accent2_100) }),
	T95(DataAboutColors("secondary_95") { colorResource(system_accent2_50) }),
	T99(DataAboutColors("secondary_99") { colorResource(system_accent2_10) }),
	T100(DataAboutColors("secondary_100") { colorResource(system_accent2_0) });

	operator fun component1(): String {
		return this.dataAboutColors.colorToken
	}

	@Composable
	operator fun component2(): Color {
		return this.dataAboutColors.colorValue()
	}
}

enum class TertiaryTones(val dataAboutColors: DataAboutColors) {
	T0(DataAboutColors("tertiary_0") { colorResource(system_accent3_1000) }),
	T10(DataAboutColors("tertiary_10") { colorResource(system_accent3_900) }),
	T20(DataAboutColors("tertiary_20") { colorResource(system_accent3_800) }),
	T30(DataAboutColors("tertiary_30") { colorResource(system_accent3_700) }),
	T40(DataAboutColors("tertiary_40") { colorResource(system_accent3_600) }),
	T50(DataAboutColors("tertiary_50") { colorResource(system_accent3_500) }),
	T60(DataAboutColors("tertiary_60") { colorResource(system_accent3_400) }),
	T70(DataAboutColors("tertiary_70") { colorResource(system_accent3_300) }),
	T80(DataAboutColors("tertiary_80") { colorResource(system_accent3_200) }),
	T90(DataAboutColors("tertiary_90") { colorResource(system_accent3_100) }),
	T95(DataAboutColors("tertiary_95") { colorResource(system_accent3_50) }),
	T99(DataAboutColors("tertiary_99") { colorResource(system_accent3_10) }),
	T100(DataAboutColors("tertiary_100") { colorResource(system_accent3_0) });


	operator fun component1(): String {
		return this.dataAboutColors.colorToken
	}

	@Composable
	operator fun component2(): Color {
		return this.dataAboutColors.colorValue()
	}
}

enum class NeutralTones(val dataAboutColors: DataAboutColors) {
	T0(DataAboutColors("neutral_0") { colorResource(system_neutral1_1000) }),
	T10(DataAboutColors("neutral_10") { colorResource(system_neutral1_900) }),
	T20(DataAboutColors("neutral_20") { colorResource(system_neutral1_800) }),
	T30(DataAboutColors("neutral_30") { colorResource(system_neutral1_700) }),
	T40(DataAboutColors("neutral_40") { colorResource(system_neutral1_600) }),
	T50(DataAboutColors("neutral_50") { colorResource(system_neutral1_500) }),
	T60(DataAboutColors("neutral_60") { colorResource(system_neutral1_400) }),
	T70(DataAboutColors("neutral_70") { colorResource(system_neutral1_300) }),
	T80(DataAboutColors("neutral_80") { colorResource(system_neutral1_200) }),
	T90(DataAboutColors("neutral_90") { colorResource(system_neutral1_100) }),
	T95(DataAboutColors("neutral_95") { colorResource(system_neutral1_50) }),
	T99(DataAboutColors("neutral_99") { colorResource(system_neutral1_10) }),
	T100(DataAboutColors("neutral_100") { colorResource(system_neutral1_0) });

	operator fun component1(): String {
		return this.dataAboutColors.colorToken
	}

	@Composable
	operator fun component2(): Color {
		return this.dataAboutColors.colorValue()
	}
}
enum class NeutralVariantTones(val dataAboutColors: DataAboutColors) {
	T0(DataAboutColors("neutral_variant_0") { colorResource(system_neutral2_1000) }),
	T10(DataAboutColors("neutral_variant_10") { colorResource(system_neutral2_900) }),
	T20(DataAboutColors("neutral_variant_20") { colorResource(system_neutral2_800) }),
	T30(DataAboutColors("neutral_variant_30") { colorResource(system_neutral2_700) }),
	T40(DataAboutColors("neutral_variant_40") { colorResource(system_neutral2_600) }),
	T50(DataAboutColors("neutral_variant_50") { colorResource(system_neutral2_500) }),
	T60(DataAboutColors("neutral_variant_60") { colorResource(system_neutral2_400) }),
	T70(DataAboutColors("neutral_variant_70") { colorResource(system_neutral2_300) }),
	T80(DataAboutColors("neutral_variant_80") { colorResource(system_neutral2_200) }),
	T90(DataAboutColors("neutral_variant_90") { colorResource(system_neutral2_100) }),
	T95(DataAboutColors("neutral_variant_95") { colorResource(system_neutral2_50) }),
	T99(DataAboutColors("neutral_variant_99") { colorResource(system_neutral2_10) }),
	T100(DataAboutColors("neutral_variant_100") { colorResource(system_neutral2_0) });

	operator fun component1(): String {
		return this.dataAboutColors.colorToken
	}

	@Composable
	operator fun component2(): Color {
		return this.dataAboutColors.colorValue()
	}
}

// i mean yes there has to be a better way to do this without
// instancing everything for every color
enum class BlueTones(val dataAboutColors: DataAboutColors) {
	T0(DataAboutColors("blue_0") {
		val harmonizedBlue = Color(
			harmonize(
				blue.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedBlue)[0]!!
	}),
	T10(DataAboutColors("blue_10") {
		val harmonizedBlue = Color(
			harmonize(
				blue.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedBlue)[10]!!
	}),

	T20(DataAboutColors("blue_20") {
		val harmonizedBlue = Color(
			harmonize(
				blue.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedBlue)[20]!!
	}),

	T30(DataAboutColors("blue_30") {
		val harmonizedBlue = Color(
			harmonize(
				blue.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedBlue)[30]!!
	}),

	T40(DataAboutColors("blue_40") {
		val harmonizedBlue = Color(
			harmonize(
				blue.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedBlue)[40]!!
	}),

	T50(DataAboutColors("blue_50") {
		val harmonizedBlue = Color(
			harmonize(
				blue.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedBlue)[50]!!
	}),

	T60(DataAboutColors("blue_60") {
		val harmonizedBlue = Color(
			harmonize(
				blue.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedBlue)[60]!!
	}),

	T70(DataAboutColors("blue_70") {
		val harmonizedBlue = Color(
			harmonize(
				blue.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedBlue)[70]!!
	}),

	T80(DataAboutColors("blue_80") {
		val harmonizedBlue = Color(
			harmonize(
				blue.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedBlue)[80]!!
	}),

	T90(DataAboutColors("blue_90") {
		val harmonizedBlue = Color(
			harmonize(
				blue.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedBlue)[90]!!
	}),

	T95(DataAboutColors("blue_95") {
		val harmonizedBlue = Color(
			harmonize(
				blue.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedBlue)[95]!!
	}),

	T99(DataAboutColors("blue_99") {
		val harmonizedBlue = Color(
			harmonize(
				blue.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedBlue)[99]!!
	}),

	T100(DataAboutColors("blue_100") {
		val harmonizedBlue = Color(
			harmonize(
				blue.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedBlue)[100]!!
	});

	operator fun component1(): String {
		return this.dataAboutColors.colorToken
	}

	@Composable
	operator fun component2(): Color {
		return this.dataAboutColors.colorValue()
	}
}

enum class RedTones(val dataAboutColors: DataAboutColors) {
	T0(DataAboutColors("red_0") {
		val harmonizedRed = Color(
			harmonize(
				red.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedRed)[0]!!
	}),

	T10(DataAboutColors("red_10") {
		val harmonizedRed = Color(
			harmonize(
				red.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedRed)[10]!!
	}),

	T20(DataAboutColors("red_20") {
		val harmonizedRed = Color(
			harmonize(
				red.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedRed)[20]!!
	}),

	T30(DataAboutColors("red_30") {
		val harmonizedRed = Color(
			harmonize(
				red.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedRed)[30]!!
	}),

	T40(DataAboutColors("red_40") {
		val harmonizedRed = Color(
			harmonize(
				red.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedRed)[40]!!
	}),

	T50(DataAboutColors("red_50") {
		val harmonizedRed = Color(
			harmonize(
				red.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedRed)[50]!!
	}),

	T60(DataAboutColors("red_60") {
		val harmonizedRed = Color(
			harmonize(
				red.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedRed)[60]!!
	}),

	T70(DataAboutColors("red_70") {
		val harmonizedRed = Color(
			harmonize(
				red.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedRed)[70]!!
	}),

	T80(DataAboutColors("red_80") {
		val harmonizedRed = Color(
			harmonize(
				red.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedRed)[80]!!
	}),

	T90(DataAboutColors("red_90") {
		val harmonizedRed = Color(
			harmonize(
				red.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedRed)[90]!!
	}),

	T95(DataAboutColors("red_95") {
		val harmonizedRed = Color(
			harmonize(
				red.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedRed)[95]!!
	}),

	T99(DataAboutColors("red_99") {
		val harmonizedRed = Color(
			harmonize(
				red.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedRed)[99]!!
	}),

	T100(DataAboutColors("red_100") {
		val harmonizedRed = Color(
			harmonize(
				red.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedRed)[100]!!
	});

	operator fun component1(): String {
		return this.dataAboutColors.colorToken
	}

	@Composable
	operator fun component2(): Color {
		return this.dataAboutColors.colorValue()
	}
}

enum class GreenTones(val dataAboutColors: DataAboutColors) {
	T0(DataAboutColors("green_0") {
		val harmonizedGreen = Color(
			harmonize(
				green.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedGreen)[0]!!
	}),

	T10(DataAboutColors("green_10") {
		val harmonizedGreen = Color(
			harmonize(
				green.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedGreen)[10]!!
	}),

	T20(DataAboutColors("green_20") {
		val harmonizedGreen = Color(
			harmonize(
				green.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedGreen)[20]!!
	}),

	T30(DataAboutColors("green_30") {
		val harmonizedGreen = Color(
			harmonize(
				green.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedGreen)[30]!!
	}),

	T40(DataAboutColors("green_40") {
		val harmonizedGreen = Color(
			harmonize(
				green.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedGreen)[40]!!
	}),

	T50(DataAboutColors("green_50") {
		val harmonizedGreen = Color(
			harmonize(
				green.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedGreen)[50]!!
	}),

	T60(DataAboutColors("green_60") {
		val harmonizedGreen = Color(
			harmonize(
				green.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedGreen)[60]!!
	}),

	T70(DataAboutColors("green_70") {
		val harmonizedGreen = Color(
			harmonize(
				green.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedGreen)[70]!!
	}),

	T80(DataAboutColors("green_80") {
		val harmonizedGreen = Color(
			harmonize(
				green.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedGreen)[80]!!
	}),

	T90(DataAboutColors("green_90") {
		val harmonizedGreen = Color(
			harmonize(
				green.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedGreen)[90]!!
	}),

	T95(DataAboutColors("green_95") {
		val harmonizedGreen = Color(
			harmonize(
				green.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedGreen)[95]!!
	}),

	T99(DataAboutColors("green_99") {
		val harmonizedGreen = Color(
			harmonize(
				green.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedGreen)[99]!!
	}),

	T100(DataAboutColors("green_100") {
		val harmonizedGreen = Color(
			harmonize(
				green.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedGreen)[100]!!
	});

	operator fun component1(): String {
		return this.dataAboutColors.colorToken
	}

	@Composable
	operator fun component2(): Color {
		return this.dataAboutColors.colorValue()
	}
}

enum class OrangeTones(val dataAboutColors: DataAboutColors) {
	T0(DataAboutColors("orange_0") {
		val harmonizedOrange = Color(
			harmonize(
				orange.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedOrange)[0]!!
	}),

	T10(DataAboutColors("orange_10") {
		val harmonizedOrange = Color(
			harmonize(
				orange.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedOrange)[10]!!
	}),

	T20(DataAboutColors("orange_20") {
		val harmonizedOrange = Color(
			harmonize(
				orange.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedOrange)[20]!!
	}),

	T30(DataAboutColors("orange_30") {
		val harmonizedOrange = Color(
			harmonize(
				orange.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedOrange)[30]!!
	}),

	T40(DataAboutColors("orange_40") {
		val harmonizedOrange = Color(
			harmonize(
				orange.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedOrange)[40]!!
	}),

	T50(DataAboutColors("orange_50") {
		val harmonizedOrange = Color(
			harmonize(
				orange.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedOrange)[50]!!
	}),

	T60(DataAboutColors("orange_60") {
		val harmonizedOrange = Color(
			harmonize(
				orange.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedOrange)[60]!!
	}),

	T70(DataAboutColors("orange_70") {
		val harmonizedOrange = Color(
			harmonize(
				orange.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedOrange)[70]!!
	}),

	T80(DataAboutColors("orange_80") {
		val harmonizedOrange = Color(
			harmonize(
				orange.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedOrange)[80]!!
	}),

	T90(DataAboutColors("orange_90") {
		val harmonizedOrange = Color(
			harmonize(
				orange.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedOrange)[90]!!
	}),

	T95(DataAboutColors("orange_95") {
		val harmonizedOrange = Color(
			harmonize(
				orange.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedOrange)[95]!!
	}),

	T99(DataAboutColors("orange_99") {
		val harmonizedOrange = Color(
			harmonize(
				orange.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedOrange)[99]!!
	}),

	T100(DataAboutColors("orange_100") {
		val harmonizedOrange = Color(
			harmonize(
				orange.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedOrange)[100]!!
	});


	operator fun component1(): String {
		return this.dataAboutColors.colorToken
	}

	@Composable
	operator fun component2(): Color {
		return this.dataAboutColors.colorValue()
	}
}

enum class VioletTones(val dataAboutColors: DataAboutColors) {
	T0(DataAboutColors("violet_0") {
		val harmonizedViolet = Color(
			harmonize(
				violet.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedViolet)[0]!!
	}),

	T10(DataAboutColors("violet_10") {
		val harmonizedViolet = Color(
			harmonize(
				violet.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedViolet)[10]!!
	}),

	T20(DataAboutColors("violet_20") {
		val harmonizedViolet = Color(
			harmonize(
				violet.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedViolet)[20]!!
	}),

	T30(DataAboutColors("violet_30") {
		val harmonizedViolet = Color(
			harmonize(
				violet.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedViolet)[30]!!
	}),

	T40(DataAboutColors("violet_40") {
		val harmonizedViolet = Color(
			harmonize(
				violet.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedViolet)[40]!!
	}),

	T50(DataAboutColors("violet_50") {
		val harmonizedViolet = Color(
			harmonize(
				violet.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedViolet)[50]!!
	}),

	T60(DataAboutColors("violet_60") {
		val harmonizedViolet = Color(
			harmonize(
				violet.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedViolet)[60]!!
	}),

	T70(DataAboutColors("violet_70") {
		val harmonizedViolet = Color(
			harmonize(
				violet.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedViolet)[70]!!
	}),

	T80(DataAboutColors("violet_80") {
		val harmonizedViolet = Color(
			harmonize(
				violet.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedViolet)[80]!!
	}),

	T90(DataAboutColors("violet_90") {
		val harmonizedViolet = Color(
			harmonize(
				violet.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedViolet)[90]!!
	}),

	T95(DataAboutColors("violet_95") {
		val harmonizedViolet = Color(
			harmonize(
				violet.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedViolet)[95]!!
	}),

	T99(DataAboutColors("violet_99") {
		val harmonizedViolet = Color(
			harmonize(
				violet.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedViolet)[99]!!
	}),

	T100(DataAboutColors("violet_100") {
		val harmonizedViolet = Color(
			harmonize(
				violet.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedViolet)[100]!!
	});

	operator fun component1(): String {
		return this.dataAboutColors.colorToken
	}

	@Composable
	operator fun component2(): Color {
		return this.dataAboutColors.colorValue()
	}
}

enum class CyanTones(val dataAboutColors: DataAboutColors) {
	T0(DataAboutColors("cyan_0") {
		val harmonizedCyan = Color(
			harmonize(
				cyan.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedCyan)[0]!!
	}),

	T10(DataAboutColors("cyan_10") {
		val harmonizedCyan = Color(
			harmonize(
				cyan.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedCyan)[10]!!
	}),

	T20(DataAboutColors("cyan_20") {
		val harmonizedCyan = Color(
			harmonize(
				cyan.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedCyan)[20]!!
	}),

	T30(DataAboutColors("cyan_30") {
		val harmonizedCyan = Color(
			harmonize(
				cyan.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedCyan)[30]!!
	}),

	T40(DataAboutColors("cyan_40") {
		val harmonizedCyan = Color(
			harmonize(
				cyan.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedCyan)[40]!!
	}),

	T50(DataAboutColors("cyan_50") {
		val harmonizedCyan = Color(
			harmonize(
				cyan.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedCyan)[50]!!
	}),

	T60(DataAboutColors("cyan_60") {
		val harmonizedCyan = Color(
			harmonize(
				cyan.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedCyan)[60]!!
	}),

	T70(DataAboutColors("cyan_70") {
		val harmonizedCyan = Color(
			harmonize(
				cyan.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedCyan)[70]!!
	}),

	T80(DataAboutColors("cyan_80") {
		val harmonizedCyan = Color(
			harmonize(
				cyan.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedCyan)[80]!!
	}),

	T90(DataAboutColors("cyan_90") {
		val harmonizedCyan = Color(
			harmonize(
				cyan.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedCyan)[90]!!
	}),

	T95(DataAboutColors("cyan_95") {
		val harmonizedCyan = Color(
			harmonize(
				cyan.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedCyan)[95]!!
	}),

	T99(DataAboutColors("cyan_99") {
		val harmonizedCyan = Color(
			harmonize(
				cyan.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedCyan)[99]!!
	}),

	T100(DataAboutColors("cyan_100") {
		val harmonizedCyan = Color(
			harmonize(
				cyan.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedCyan)[100]!!
	});

	operator fun component1(): String {
		return this.dataAboutColors.colorToken
	}

	@Composable
	operator fun component2(): Color {
		return this.dataAboutColors.colorValue()
	}
}

enum class PinkTones(val dataAboutColors: DataAboutColors) {
	T0(DataAboutColors("pink_0") {
		val harmonizedPink = Color(
			harmonize(
				pink.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedPink)[0]!!
	}),

	T10(DataAboutColors("pink_10") {
		val harmonizedPink = Color(
			harmonize(
				pink.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedPink)[10]!!
	}),

	T20(DataAboutColors("pink_20") {
		val harmonizedPink = Color(
			harmonize(
				pink.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedPink)[20]!!
	}),

	T30(DataAboutColors("pink_30") {
		val harmonizedPink = Color(
			harmonize(
				pink.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedPink)[30]!!
	}),

	T40(DataAboutColors("pink_40") {
		val harmonizedPink = Color(
			harmonize(
				pink.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedPink)[40]!!
	}),

	T50(DataAboutColors("pink_50") {
		val harmonizedPink = Color(
			harmonize(
				pink.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedPink)[50]!!
	}),

	T60(DataAboutColors("pink_60") {
		val harmonizedPink = Color(
			harmonize(
				pink.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedPink)[60]!!
	}),

	T70(DataAboutColors("pink_70") {
		val harmonizedPink = Color(
			harmonize(
				pink.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedPink)[70]!!
	}),

	T80(DataAboutColors("pink_80") {
		val harmonizedPink = Color(
			harmonize(
				pink.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedPink)[80]!!
	}),

	T90(DataAboutColors("pink_90") {
		val harmonizedPink = Color(
			harmonize(
				pink.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedPink)[90]!!
	}),

	T95(DataAboutColors("pink_95") {
		val harmonizedPink = Color(
			harmonize(
				pink.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedPink)[95]!!
	}),

	T99(DataAboutColors("pink_99") {
		val harmonizedPink = Color(
			harmonize(
				pink.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedPink)[99]!!
	}),

	T100(DataAboutColors("pink_100") {
		val harmonizedPink = Color(
			harmonize(
				pink.toArgb(),
				colorResource(system_accent1_500).toArgb()
			)
		).blendWith(Color.White, 1f - getPrimaryColorSaturation())

		getColorTonesMap(harmonizedPink)[100]!!
	});


	operator fun component1(): String {
		return this.dataAboutColors.colorToken
	}

	@Composable
	operator fun component2(): Color {
		return this.dataAboutColors.colorValue()
	}
}

fun Color.blendWith(color: Color, @FloatRange(from = 0.0, to = 1.0) ratio: Float): Color {
	val inv = 1f - ratio
	return copy(
		red = red * inv + color.red * ratio,
		blue = blue * inv + color.blue * ratio,
		green = green * inv + color.green * ratio,
	)
}

@Composable
fun getPrimaryColorSaturation(): Float {
	return ColorUtil.colorToHSL(colorResource(system_accent1_600))[1]
}

private val blue = Color.Blue
private val red = Color.Red
private val green = Color.Green
private val orange = Color(0xFFFFAA00)
private val violet = Color(0xFFEB00FF)
private val pink = Color(0xFFFF32AC)
private val cyan = Color.Cyan

