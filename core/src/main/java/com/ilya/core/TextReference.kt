package com.ilya.core

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface TextReference {
    class Str(val value: String) : TextReference
    class StringRef(@StringRes val stringId: Int, vararg val formatArgs: Any) : TextReference
    
    @Composable
    fun resolve(): String {
        return when (this) {
            is Str -> value
            is StringRef -> stringResource(id = stringId, *formatArgs)
        }
    }
}
