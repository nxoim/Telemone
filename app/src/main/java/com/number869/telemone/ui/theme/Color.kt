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
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberUpdatedState
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

@Immutable
data class PaletteState(
    val primaryTones: List<ToneInfo>,
    val secondaryTones: List<ToneInfo>,
    val tertiaryTones: List<ToneInfo>,
    val neutralTones: List<ToneInfo>,
    val neutralVariantTones: List<ToneInfo>,
    val blueTones: List<ToneInfo>,
    val redTones: List<ToneInfo>,
    val greenTones: List<ToneInfo>,
    val orangeTones: List<ToneInfo>,
    val violetTones: List<ToneInfo>,
    val cyanTones: List<ToneInfo>,
    val pinkTones: List<ToneInfo>,
    val colorRolesLight: ColorRoles,
    val colorRolesDark: ColorRoles,
    val colorRolesShared: ColorRolesShared,
    val additionalColors: AdditionalColors,
    val isDarkMode: Boolean
) {
    val entirePaletteAsMap: LinkedHashMap<String, Color> = linkedMapOf<String, Color>().apply {
        additionalColors.toListOfDataAboutColors().forEach { put(it.colorToken, it.colorValue) }
        if (isDarkMode) {
            colorRolesDark.toListOfDataAboutColors().forEach { put(it.colorToken, it.colorValue) }
            colorRolesLight.toListOfDataAboutColors().forEach { put(it.colorToken, it.colorValue) }
        } else {
            colorRolesLight.toListOfDataAboutColors().forEach { put(it.colorToken, it.colorValue) }
            colorRolesDark.toListOfDataAboutColors().forEach { put(it.colorToken, it.colorValue) }
        }
        colorRolesShared.toListOfDataAboutColors().forEach { put(it.colorToken, it.colorValue) }

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
            toneList.forEach { put(it.colorToken, it.colorValue) }
        }
    }
    val allPossibleColorTokensAsList = entirePaletteAsMap.keys
}

@Composable
fun rememberPaletteState(): PaletteState {
    val isDarkMode = isSystemInDarkTheme()
    val lightColorScheme = getColorScheme(isDark = false)
    val darkColorScheme = getColorScheme(isDark = true)

    return rememberUpdatedState(
        PaletteState(
            primaryTones = primaryTones,
            secondaryTones = secondaryTones,
            tertiaryTones = tertiaryTones,
            neutralTones = neutralTones,
            neutralVariantTones = neutralVariantTones,
            blueTones = blueTones,
            redTones = redTones,
            greenTones = greenTones,
            orangeTones = orangeTones,
            violetTones = violetTones,
            cyanTones = cyanTones,
            pinkTones = pinkTones,
            colorRolesLight = lightColorScheme.toColorRoles(light = true),
            colorRolesDark = darkColorScheme.toColorRoles(light = false),
            colorRolesShared = getSharedColorRoles(primaryTones, secondaryTones, tertiaryTones),
            additionalColors = getAdditionalColors(
                surfaceElevationLevel3Light = lightColorScheme.surfaceColorAtElevation(3.dp),
                surfaceElevationLevel3Dark = darkColorScheme.surfaceColorAtElevation(3.dp)
            ),
            isDarkMode = isDarkMode
        )
    ).value
}

private fun ColorScheme.toColorRoles(light: Boolean): ColorRoles {
    val suffix = if (light) "light" else "dark"

    return ColorRoles(
        primary = DataAboutColors("primary_$suffix", this.primary),
        onPrimary = DataAboutColors("on_primary_$suffix", this.onPrimary),
        primaryContainer = DataAboutColors("primary_container_$suffix", this.primaryContainer),
        onPrimaryContainer = DataAboutColors(
            "on_primary_container_$suffix",
            this.onPrimaryContainer
        ),
        secondary = DataAboutColors("secondary_$suffix", this.secondary),
        onSecondary = DataAboutColors("on_secondary_$suffix", this.onSecondary),
        secondaryContainer = DataAboutColors(
            "secondary_container_$suffix",
            this.secondaryContainer
        ),
        onSecondaryContainer = DataAboutColors(
            "on_secondary_container_$suffix",
            this.onSecondaryContainer
        ),
        tertiary = DataAboutColors("tertiary_$suffix", this.tertiary),
        onTertiary = DataAboutColors("on_tertiary_$suffix", this.onTertiary),
        tertiaryContainer = DataAboutColors("tertiary_container_$suffix", this.tertiaryContainer),
        onTertiaryContainer = DataAboutColors(
            "on_tertiary_container_$suffix",
            this.onTertiaryContainer
        ),
        surface = DataAboutColors("surface_$suffix", this.surface),
        surfaceDim = DataAboutColors("surface_dim_$suffix", this.surfaceDim),
        surfaceBright = DataAboutColors("surface_bright_$suffix", this.surfaceBright),
        onSurface = DataAboutColors("on_surface_$suffix", this.onSurface),
        surfaceContainerLowest = DataAboutColors(
            "surface_container_lowest_$suffix",
            this.surfaceContainerLowest
        ),
        surfaceContainerLow = DataAboutColors(
            "surface_container_low_$suffix",
            this.surfaceContainerLow
        ),
        surfaceContainer = DataAboutColors("surface_container_$suffix", this.surfaceContainer),
        surfaceContainerHigh = DataAboutColors(
            "surface_container_high_$suffix",
            this.surfaceContainerHigh
        ),
        surfaceContainerHighest = DataAboutColors(
            "surface_container_highest_$suffix",
            this.surfaceContainerHighest
        ),
        onSurfaceVariant = DataAboutColors("on_surface_variant_$suffix", this.onSurfaceVariant),
        error = DataAboutColors("error_$suffix", this.error),
        onError = DataAboutColors("on_error_$suffix", this.onError),
        errorContainer = DataAboutColors("error_container_$suffix", this.errorContainer),
        onErrorContainer = DataAboutColors("on_error_container_$suffix", this.onErrorContainer),
        outline = DataAboutColors("outline_$suffix", this.outline),
        outlineVariant = DataAboutColors("outline_variant_$suffix", this.outlineVariant),
        inverseSurface = DataAboutColors("inverse_surface_$suffix", this.inverseSurface),
        inverseOnSurface = DataAboutColors("inverse_on_surface_$suffix", this.inverseOnSurface),
        inversePrimary = DataAboutColors("inverse_primary_$suffix", this.inversePrimary),
        scrim = DataAboutColors("scrim_$suffix", this.scrim),
        shadow = DataAboutColors("shadow_$suffix", Color.Black)
    )
}

data class ColorRoles(
    val primary: DataAboutColors,
    val onPrimary: DataAboutColors,
    val primaryContainer: DataAboutColors,
    val onPrimaryContainer: DataAboutColors,
    val secondary: DataAboutColors,
    val onSecondary: DataAboutColors,
    val secondaryContainer: DataAboutColors,
    val onSecondaryContainer: DataAboutColors,
    val tertiary: DataAboutColors,
    val onTertiary: DataAboutColors,
    val tertiaryContainer: DataAboutColors,
    val onTertiaryContainer: DataAboutColors,
    val surface: DataAboutColors,
    val surfaceDim: DataAboutColors,
    val surfaceBright: DataAboutColors,
    val onSurface: DataAboutColors,
    val surfaceContainerLowest: DataAboutColors,
    val surfaceContainerLow: DataAboutColors,
    val surfaceContainer: DataAboutColors,
    val surfaceContainerHigh: DataAboutColors,
    val surfaceContainerHighest: DataAboutColors,
    val onSurfaceVariant: DataAboutColors,
    val error: DataAboutColors,
    val onError: DataAboutColors,
    val errorContainer: DataAboutColors,
    val onErrorContainer: DataAboutColors,
    val outline: DataAboutColors,
    val outlineVariant: DataAboutColors,
    val inverseSurface: DataAboutColors,
    val inverseOnSurface: DataAboutColors,
    val inversePrimary: DataAboutColors,
    val scrim: DataAboutColors,
    val shadow: DataAboutColors
) {
    fun toListOfDataAboutColors() = listOf(
        primary,
        onPrimary,
        primaryContainer,
        onPrimaryContainer,
        secondary,
        onSecondary,
        secondaryContainer,
        onSecondaryContainer,
        tertiary,
        onTertiary,
        tertiaryContainer,
        onTertiaryContainer,
        surface,
        surfaceDim,
        surfaceBright,
        onSurface,
        surfaceContainerLowest,
        surfaceContainerLow,
        surfaceContainer,
        surfaceContainerHigh,
        surfaceContainerHighest,
        onSurfaceVariant,
        error,
        onError,
        errorContainer,
        onErrorContainer,
        outline,
        outlineVariant,
        inverseSurface,
        inverseOnSurface,
        inversePrimary,
        scrim,
        shadow
    )
}

data class ColorRolesShared(
    val primaryFixed: DataAboutColors,
    val onPrimaryFixed: DataAboutColors,
    val primaryFixedDim: DataAboutColors,
    val onPrimaryFixedVariant: DataAboutColors,
    val secondaryFixed: DataAboutColors,
    val onSecondaryFixed: DataAboutColors,
    val secondaryFixedDim: DataAboutColors,
    val onSecondaryFixedVariant: DataAboutColors,
    val tertiaryFixed: DataAboutColors,
    val onTertiaryFixed: DataAboutColors,
    val tertiaryFixedDim: DataAboutColors,
    val onTertiaryFixedVariant: DataAboutColors
) {
    fun toListOfDataAboutColors() = listOf(
        primaryFixed,
        onPrimaryFixed,
        primaryFixedDim,
        onPrimaryFixedVariant,
        secondaryFixed,
        onSecondaryFixed,
        secondaryFixedDim,
        onSecondaryFixedVariant,
        tertiaryFixed,
        onTertiaryFixed,
        tertiaryFixedDim,
        onTertiaryFixedVariant
    )
}

private fun getSharedColorRoles(
    primaryTones: List<ToneInfo>,
    secondaryTones: List<ToneInfo>,
    tertiaryTones: List<ToneInfo>
) = ColorRolesShared(
    primaryFixed = DataAboutColors(
        "primary_fixed",
        primaryTones.find { it.tone == 90 }!!.colorValue
    ),
    onPrimaryFixed = DataAboutColors(
        "on_primary_fixed",
        primaryTones.find { it.tone == 10 }!!.colorValue
    ),
    primaryFixedDim = DataAboutColors(
        "on_primary_fixed_dim", // oops TODO migration (maybe. this app does not have much time to live)
        primaryTones.find { it.tone == 80 }!!.colorValue
    ),
    onPrimaryFixedVariant = DataAboutColors(
        "on_primary_fixed_variant",
        primaryTones.find { it.tone == 30 }!!.colorValue
    ),
    secondaryFixed = DataAboutColors(
        "secondary_fixed",
        secondaryTones.find { it.tone == 90 }!!.colorValue
    ),
    onSecondaryFixed = DataAboutColors(
        "on_secondary_fixed",
        secondaryTones.find { it.tone == 10 }!!.colorValue
    ),
    secondaryFixedDim = DataAboutColors(
        "on_secondary_fixed_dim",
        secondaryTones.find { it.tone == 80 }!!.colorValue
    ),
    onSecondaryFixedVariant = DataAboutColors(
        "on_secondary_fixed_variant",
        secondaryTones.find { it.tone == 30 }!!.colorValue
    ),
    tertiaryFixed = DataAboutColors(
        "tertiary_fixed",
        tertiaryTones.find { it.tone == 90 }!!.colorValue
    ),
    onTertiaryFixed = DataAboutColors(
        "on_tertiary_fixed",
        tertiaryTones.find { it.tone == 10 }!!.colorValue
    ),
    tertiaryFixedDim = DataAboutColors(
        "on_tertiary_fixed_dim",
        tertiaryTones.find { it.tone == 80 }!!.colorValue
    ),
    onTertiaryFixedVariant = DataAboutColors(
        "on_tertiary_fixed_variant",
        tertiaryTones.find { it.tone == 30 }!!.colorValue
    )
)

data class AdditionalColors(
    val white: DataAboutColors,
    val black: DataAboutColors,
    val surfaceElevationLevel3Light: DataAboutColors,
    val surfaceElevationLevel3Dark: DataAboutColors,
    val transparent: DataAboutColors
) {
    fun toListOfDataAboutColors() = listOf(
        white,
        black,
        surfaceElevationLevel3Light,
        surfaceElevationLevel3Dark,
        transparent
    )
}

private fun getAdditionalColors(
    surfaceElevationLevel3Light: Color,
    surfaceElevationLevel3Dark: Color
) = AdditionalColors(
    white = DataAboutColors("white", Color.White),
    black = DataAboutColors("black", Color.Black),
    surfaceElevationLevel3Light = DataAboutColors(
        "surface_elevation_level_3_light",
        surfaceElevationLevel3Light
    ),
    surfaceElevationLevel3Dark = DataAboutColors(
        "surface_elevation_level_3_dark",
        surfaceElevationLevel3Dark
    ),
    transparent = DataAboutColors("transparent", Color.Transparent)
)

private val primaryTones
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
            colorToken = "primary_$toneNumber",
            colorValue = color
        )
    }

private val secondaryTones
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
            colorToken = "secondary_$toneNumber",
            colorValue = color
        )
    }

private val tertiaryTones
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
            colorToken = "tertiary_$toneNumber",
            colorValue = color
        )
    }

private val neutralTones
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
            colorToken = "neutral_$toneNumber",
            colorValue = color
        )
    }

// if only google provided a way to get the m3 tonal palette🤡🤡🤡
private val neutralVariantTones
    @Composable
    get() = possibleTones.map { toneNumber ->
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
            colorToken = "neutral_variant_$toneNumber",
            colorValue = color
        )
    }

private val blueTones @Composable get() = blue.getCustomTones(name = "blue")
private val redTones @Composable get() = red.getCustomTones(name = "red")
private val greenTones @Composable get() = green.getCustomTones(name = "green")
private val orangeTones @Composable get() = orange.getCustomTones(name = "orange")
private val violetTones @Composable get() = violet.getCustomTones(name = "violet")
private val pinkTones @Composable get() = pink.getCustomTones(name = "pink")
private val cyanTones @Composable get() = cyan.getCustomTones(name = "cyan")

@Stable
@Composable
private fun Color.getCustomTones(name: String): List<ToneInfo> {
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
data class DataAboutColors(val colorToken: String, val colorValue: Color)

private fun Color.matchSaturation(toThatOf: Color, saturationMultiplier: Double = 0.9): Color {
    val source = this.toHct()
    val target = toThatOf.toHct()

    val result = Hct.from(
        hue = source.hue,
        chroma = target.chroma * saturationMultiplier,
        tone = source.tone
    )

    return Color(result.toInt())
}

private val possibleTones = listOf(0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 95, 99, 100)

private fun Color.getTone(tone: Int) = Color(TonalPalette.from(this).tone(tone))

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