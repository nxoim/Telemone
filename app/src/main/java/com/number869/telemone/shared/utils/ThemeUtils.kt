package com.number869.telemone.shared.utils

import android.content.SharedPreferences
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.number869.telemone.data.AppSettings
import com.number869.telemone.data.UiElementColorData
import com.number869.telemone.ui.theme.PaletteState

val UiElementColorData.color get() = Color(colorValue)
fun incompatibleUiElementColorData(ofUiElement: String) = UiElementColorData(
    ofUiElement,
    "INCOMPATIBLE VALUE",
    Color.Red.toArgb()
)

@Composable
fun colorOf(
    data: UiElementColorData,
    colorDisplayType: ThemeColorPreviewDisplayType,
    palette: Map<String, Color> = remember {
        inject<PaletteState>().entirePaletteAsMap
    }
) = animateColorAsState(
    when (colorDisplayType) {
        ThemeColorPreviewDisplayType.SavedColorValues -> {
            data.color
        }
        // in case theres a need to show monet colors only when available
        ThemeColorPreviewDisplayType.CurrentColorSchemeWithFallback -> {
            val colorFromToken = getColorValueFromColorTokenOrNull(data.colorToken, palette)
            val colorAsSaved = data.color
            colorFromToken ?: colorAsSaved
        }

        ThemeColorPreviewDisplayType.CurrentColorScheme -> {
            getColorValueFromColorToken(data.colorToken, palette)
        }
    },
    label = "i hate these labels"
).value

fun getColorValueFromColorToken(
    tokenToLookFor: String,
    palette: Map<String, Color>
) = palette[tokenToLookFor] ?: Color.Red

fun getColorValueFromColorTokenOrNull(
    tokenToLookFor: String,
    palette: Map<String, Color>
) = palette[tokenToLookFor]

fun getColorTokenFromColorValue(
    valueToLookFor: Color,
    palette: Map<String, Color>
) = palette.entries.find { it.value == valueToLookFor }?.key

enum class ThemeColorDataType {
    ColorValues,
    ColorTokens,
    ColorValuesFromDevicesColorScheme
}

enum class ThemeColorPreviewDisplayType(val id: String) {
    SavedColorValues("1"),
    CurrentColorSchemeWithFallback("2"),
    CurrentColorScheme("3")
}

fun getColorDisplayType(preferences: SharedPreferences): ThemeColorPreviewDisplayType {
    val colorDisplayTypePref = preferences.getString(
        AppSettings.SavedThemeItemDisplayType.id,
        "1"
    )
    return when (colorDisplayTypePref) {
        "1" -> ThemeColorPreviewDisplayType.SavedColorValues
        "2" -> ThemeColorPreviewDisplayType.CurrentColorSchemeWithFallback
        else -> ThemeColorPreviewDisplayType.CurrentColorScheme
    }
}

sealed interface ThemeStorageType {
    data class Default(val isLight: Boolean) : ThemeStorageType
    data class Stock(val isLight: Boolean) : ThemeStorageType
    data class ByUuid(
        val uuid: String,
        val withTokens: Boolean,
        val clearCurrentTheme: Boolean
    ) : ThemeStorageType
    data class ExternalFile(val uri: Uri, val clearCurrentTheme: Boolean) : ThemeStorageType
}

@JvmName("stringify2") // cuz compile issue "declaration clash"
fun stringify(
    source: List<UiElementColorData>,
    using: ThemeColorDataType,
    palette: Map<String, Color> = inject<PaletteState>().entirePaletteAsMap
) = source.stringify(using, palette)

fun List<UiElementColorData>.stringify(
    using: ThemeColorDataType,
    palette: Map<String, Color> = inject<PaletteState>().entirePaletteAsMap
): String {
    val theme = when(using) {
        ThemeColorDataType.ColorValues -> {
            this
                .asSequence()
                .sortedBy { it.name }
                .associate { it.name to it.colorValue.toString() }
        }
        ThemeColorDataType.ColorTokens -> {
            this
                .asSequence()
                .sortedBy { it.name }
                .associate { it.name to it.colorToken }
        }
        ThemeColorDataType.ColorValuesFromDevicesColorScheme -> {
            this
                .asSequence()
                .sortedBy { it.name }
                .associate {
                    it.name to getColorValueFromColorToken(it.colorToken, palette).toArgb().toString()
                }
        }
    }


    val themeAsString = theme.entries.joinToString("\n")
        .replace(")", "")
        .replace("(", "")
        .replace(", ", "=")

    return "${
        themeAsString
    }\n"
}
