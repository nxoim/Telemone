package com.number869.telemone.ui.screens.editor.components.new.popup

enum class PaletteMenuCategories() {
    Home,
    ColorRoles,
    Primary,
    Secondary,
    Tertiary,
    Neutral,
    NeutralVariant,
    Blue,
    Red,
    Green,
    Orange,
    Violet,
    Pink,
    Cyan,
    AdditionalColors;

    operator fun component1() = this

    operator fun component2() = when (this) {
        ColorRoles -> "Color Roles"
        NeutralVariant -> "Neutral Variant"
        else -> this.name
    }
}