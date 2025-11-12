package com.number869.telemone.shared.utils

import android.content.Context
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import com.number869.telemone.App
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
    palette: Map<String, Color>
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

enum class ThemeColorPreviewDisplayType(val id: Int) {
    SavedColorValues(1),
    CurrentColorSchemeWithFallback(2),
    CurrentColorScheme(3)
}

@Composable
fun getColorDisplayType(): ThemeColorPreviewDisplayType {
    val colorDisplayType by AppSettings.savedThemeDisplayType.run {
        getAsFlow().collectAsState(defaultValue)
    }

    return when (colorDisplayType) {
        1 -> ThemeColorPreviewDisplayType.SavedColorValues
        2 -> ThemeColorPreviewDisplayType.CurrentColorSchemeWithFallback
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
    palette: Map<String, Color>
) = source.stringify(using, palette)

fun List<UiElementColorData>.stringify(
    using: ThemeColorDataType,
    palette: Map<String, Color>
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


// pretend it does actually calculate a hash. i dont want to import a library just to
// calculate the hash
fun assetFoldersThemeHash(
    light: Boolean,
    context: Context
) = context.assets
    .open("default${if (light) "Light" else "Dark"}File.attheme")
    .bufferedReader()
    .readText()