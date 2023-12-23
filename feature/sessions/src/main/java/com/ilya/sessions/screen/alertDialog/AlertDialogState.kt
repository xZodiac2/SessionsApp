package com.ilya.sessions.screen.alertDialog

sealed interface AlertDialogState {
    data class QuitAlertDialogRequest(
        val onConfirm: () -> Unit,
        val onDismiss: () -> Unit,
    ) : AlertDialogState
    
    object Consumed : AlertDialogState
}