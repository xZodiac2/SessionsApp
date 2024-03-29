package com.ilya.sessions.screen.alertDialog

import androidx.compose.runtime.Composable

@Composable
fun AlertDialogStateHandler(state: AlertDialogState) {
    when (state) {
        AlertDialogState.Consumed -> Unit
        is AlertDialogState.SubmitExitRequest -> ShowQuitAlertDialog(state)
    }
}