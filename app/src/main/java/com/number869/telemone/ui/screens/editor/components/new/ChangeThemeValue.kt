package com.number869.telemone.ui.screens.editor.components.new

import androidx.compose.ui.graphics.Color

fun interface ChangeThemeValue {
    operator fun invoke(uiElementName: String, colorToken: String, colorValue: Color)
}
