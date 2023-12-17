package com.ilya.sessions.screen.alertDialog

sealed interface AlertDialogState {
    data class Triggered(
        val onConfirm: () -> Unit,
        val onDismiss: () -> Unit,
    ) : AlertDialogState
    
    object Consumed : AlertDialogState
}