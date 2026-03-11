package com.number869.telemone.ui.screens.common

import com.number869.telemone.common.ThemeFileSource

fun interface ThemeFilePicker {
    suspend fun pick(): ThemeFileSource?
}

fun interface ThemeExporter {
    suspend fun export(fileName: String, content: String)
}

fun interface LinkHandler {
    fun handle(uri: String)
}