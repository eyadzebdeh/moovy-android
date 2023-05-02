package com.apptasya.moovy.presentation.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class Colors(
    val colorPrimary: Color = Color(0xFFC21E24),
    val colorSecondary: Color = Color(0xFF736F6F),
    val colorSurface: Color = Color.Black
)

val LocalColors = compositionLocalOf { Colors() }