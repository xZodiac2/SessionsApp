package com.ilya.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.sp

object Typography {
    val tinyFontSize = 14.sp
    val lowFontSize = 18.sp
    val defaultFontSize = 20.sp
    val mediumFontSize = 24.sp
    val largeFontSize = 30.sp
}

val LocalTypography = compositionLocalOf { Typography }