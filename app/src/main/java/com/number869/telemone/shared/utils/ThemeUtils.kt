package com.number869.telemone.shared.utils

import android.content.Context
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

enum class ThemeColorPreviewDisplayType(val id: Int) {
    SavedColorValues(1),
    CurrentColorSchemeWithFallback(2),
    CurrentColorScheme(3)
}

@Composable
fun getColorDisplayType(): ThemeColorPreviewDisplayType {
    val colorDisplayType by AppSettings.savedThemeDisplayType.asState()

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

// composable because used in ui and needs reactivity via state
@Composable
fun shouldDisplayUpdateDialog(light: Boolean): Boolean {
    val lastDeclinedThemeHash by if (light)
        AppSettings.lastDeclinedStockThemeHashLight.asState()
    else
        AppSettings.lastDeclinedStockThemeHashDark.asState()

    return (lastDeclinedThemeHash != assetFoldersThemeHash(light))
}

@Composable
fun canThemeBeUpdated(light: Boolean): Boolean {
    val lastAcceptedHash by if (light)
        AppSettings.lastAcceptedStockThemeHashLight.asState()
    else
        AppSettings.lastAcceptedStockThemeHashDark.asState()

    return lastAcceptedHash == "" || assetFoldersThemeHash(light) != lastAcceptedHash
}

// pretend it does actually calculate a hash. i dont want to import a library just to
// calculate the hash
fun assetFoldersThemeHash(
    light: Boolean,
    context: Context = inject()
) = context.assets
    .open("default${if (light) "Light" else "Dark"}File.attheme")
    .bufferedReader()
    .readText()