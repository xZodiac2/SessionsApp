package com.ilya.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun SessionsAppTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkTheme()) {
        ColorScheme(
            primary = DarkColorScheme.primary,
            secondary = DarkColorScheme.secondary,
            cardContainerColor = DarkColorScheme.cardContainerColor,
            background = DarkColorScheme.background,
            filledHeartIconTint = DarkColorScheme.heartIconTint,
            outlinedHeartIconTint = DarkColorScheme.outlinedHeartIconTint,
            primaryTextColor = DarkColorScheme.primaryTextColor,
            secondaryTextColor = DarkColorScheme.secondaryTextColor
        )
    } else {
        ColorScheme()
    }
    
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(colorScheme.secondary)
    
    CompositionLocalProvider(LocalColorScheme provides colorScheme) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalColorScheme.current.background)
        ) { content() }
    }
}