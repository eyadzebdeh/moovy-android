package com.apptasya.moovy.presentation.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color


@Composable
fun MoovyTheme(
    content: @Composable () -> Unit
) {
    //TODO: add typography
    CompositionLocalProvider(
        LocalDimensions provides Dimensions(),
        LocalColors provides Colors()
    ) {
        val colors = darkColors(
            primary = LocalColors.current.colorPrimary,
            secondary = LocalColors.current.colorSecondary,
            background = LocalColors.current.colorSurface,
            surface = LocalColors.current.colorSurface,
            error = Color.Red,
            onPrimary = Color.White,
            onSecondary = Color.White,
            onBackground = Color.White,
            onSurface = Color.White,
            onError = Color.White
        )
        MaterialTheme(
            content = content,
            colors = colors,
        )
    }
}