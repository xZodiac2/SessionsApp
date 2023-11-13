package com.ilya.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class ColorScheme(
    val primary: Color = LightColorScheme.primary,
    val secondary: Color = LightColorScheme.secondary,
    val cardContainerColor: Color = Color.White,
    val background: Color = LightColorScheme.background,
    val filledHeartIconTint: Color = LightColorScheme.filledHeartIconTint,
    val outlinedHeartIconTint: Color = LightColorScheme.outlinedHeartIconTint,
    val primaryTextColor: Color = LightColorScheme.primaryTextColor,
    val secondaryTextColor: Color = LightColorScheme.secondaryTextColor,
)

object LightColorScheme {
    val primary = Color.White
    val secondary = Color(224, 224, 224, 255)
    val cardContainerColor = Color.White
    val background = Color.White
    val filledHeartIconTint = Color.Red
    val outlinedHeartIconTint = Color.Black
    val primaryTextColor = Color.Black
    val secondaryTextColor = Color(126, 126, 126)
}

object DarkColorScheme {
    val primary = Color(48, 48, 48, 255)
    val secondary = Color(66, 66, 66, 255)
    val cardContainerColor = Color(58, 58, 58, 255)
    val background = Color(48, 48, 48, 255)
    val heartIconTint = Color(255, 91, 91, 255)
    val outlinedHeartIconTint = Color.White
    val primaryTextColor = Color.White
    val secondaryTextColor = Color(170, 170, 170, 255)
}


val LocalColorScheme = compositionLocalOf { ColorScheme() }