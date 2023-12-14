package com.ilya.sessions.screen

import com.ilya.core.TextReference

sealed interface AlertDialogState {
    data class Triggered(
        val title: TextReference = TextReference.Str(""),
        val text: TextReference = TextReference.Str(""),
        val dismissButtonText: TextReference = TextReference.Str(""),
        val confirmButtonText: TextReference = TextReference.Str(""),
    ) : AlertDialogState
    
    object Consumed : AlertDialogState
}